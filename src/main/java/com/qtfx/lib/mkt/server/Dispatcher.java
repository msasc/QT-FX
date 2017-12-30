/*
 * Copyright (C) 2017 Miquel Sas
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 */

package com.qtfx.lib.mkt.server;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qtfx.lib.mkt.data.Data;
import com.qtfx.lib.mkt.data.Instrument;
import com.qtfx.lib.mkt.data.OfferSide;
import com.qtfx.lib.mkt.data.Period;
import com.qtfx.lib.mkt.data.Tick;

/**
 * Dispatches account, order, data and tick events.
 *
 * @author Miquel Sas
 */
public class Dispatcher {

	/** Logger instance. */
	private static final Logger LOGGER = LogManager.getLogger();

	///////////////////////
	// Account dispatching.

	/**
	 * Account dispatcher.
	 */
	class AccountDispatcher implements Runnable {
		boolean execute = false;

		@Override
		public void run() {
			while (execute) {
				dispatchAccountUpdates();
				try {
					Thread.sleep(sleep);
				} catch (InterruptedException e) {
					LOGGER.catching(e);
					break;
				}
			}
		}
	}

	/** List of account listeners. */
	private List<AccountListener> accountListeners = new ArrayList<>();
	/** List of account updates. */
	private List<Account> accountUpdates = new ArrayList<>();
	/** Account lock. */
	private ReentrantLock accountLock = new ReentrantLock();
	/** Account dispatcher. */
	private AccountDispatcher accountDispatcher;

	/////////////////////
	// Order dispatching.

	/**
	 * Order dispatcher.
	 */
	class OrderDispatcher implements Runnable {
		boolean execute = false;

		@Override
		public void run() {
			while (execute) {
				dispatchOrderUpdates();
				try {
					Thread.sleep(sleep);
				} catch (InterruptedException e) {
					LOGGER.catching(e);
					break;
				}
			}
		}
	}

	/** List of order listeners. */
	private List<OrderListener> orderListeners = new ArrayList<>();
	/** List of received order messages. */
	private List<OrderMessage> orderMessages = new ArrayList<>();
	/** Order lock. */
	private ReentrantLock orderLock = new ReentrantLock();
	/** Order dispatcher. */
	private OrderDispatcher orderDispatcher;

	//////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Instrument dispatching. Since data and specially ticks can come in a huge pace and listeners can take a
	// considerable time to process them, a thread per instrument is created to dispatch data and tick events.

	/**
	 * Instrument dispatcher.
	 */
	class InstrumentDispatcher implements Runnable {
		Instrument instrument;
		boolean execute = true;

		@Override
		public void run() {
			while (execute) {
				dispatchTicks(instrument);
				try {
					Thread.sleep(sleep);
				} catch (InterruptedException e) {
					LOGGER.catching(e);
					break;
				}
			}
		}
	}

	/** List of instrument dispatchers. */
	private List<InstrumentDispatcher> instrumentDispatchers = new ArrayList<>();

	////////////////////
	// Tick dispatching.

	/**
	 * Tick event structure.
	 */
	static class TickEvent {
		Instrument instrument;
		Tick tick;
	}

	/** List of tick listeners. */
	private List<TickListener> tickListeners = new ArrayList<>();
	/** List of input ticks. */
	private List<TickEvent> tickEvents = new ArrayList<>();
	/** Tick lock. */
	private ReentrantLock tickLock = new ReentrantLock();

	//////////////////////////
	// Data (bar) dispatching.

	/**
	 * Data event structure.
	 */
	static class DataEvent {
		Instrument instrument;
		Period period;
		OfferSide offerSide;
		Data data;
	}

	/** List of data listeners. */
	private List<DataListener> dataListeners = new ArrayList<>();
	/** List of start input data events. */
	private List<DataEvent> dataStartEvents = new ArrayList<>();
	/** List of end input data events. */
	private List<DataEvent> dataEndEvents = new ArrayList<>();

	//////////////////////////////////////
	// Server and dispatcher thread group.

	/** Server. */
	private Server server;
	/** Dispatcher thread group. */
	private ThreadGroup dispatcherThreadGroup = new ThreadGroup("Server dispatcher");
	/** Thread sleep. No need to stress. */
	private int sleep = 5;

	/**
	 * Constructor.
	 * 
	 * @param server The server.
	 */
	public Dispatcher(Server server) {
		super();
		this.server = server;
	}

	///////////////////////
	// Account dispatching.

	/**
	 * Add an account listener to receive account updates.
	 * 
	 * @param listener The listener.
	 */
	public void addAccountListener(AccountListener listener) {
		accountListeners.add(listener);

		// Initialize and start the account dispatcher if necessary.
		if (accountDispatcher == null) {
			accountDispatcher = new AccountDispatcher();
			Thread accountThread = new Thread(dispatcherThreadGroup, accountDispatcher, "Account");
			accountThread.start();
		}
	}

	/**
	 * Add an account update.
	 * 
	 * @param account The account state.
	 */
	public void addAccountUpdate(Account account) {
		// Only add it if there are listeners whiling to listen updates.
		if (!accountListeners.isEmpty()) {
			try {
				accountLock.lock();
				accountUpdates.add(account);
			} finally {
				accountLock.unlock();
			}
		}
	}

	/**
	 * Dispatch account updates.
	 */
	private void dispatchAccountUpdates() {

		// ActionCreate a private list of account updates.
		List<Account> accountUpdates = new ArrayList<>();
		accountLock.lock();
		accountUpdates.addAll(this.accountUpdates);
		this.accountUpdates.clear();
		accountLock.unlock();

		// Do dispatch.
		if (!accountUpdates.isEmpty()) {
			for (Account account : accountUpdates) {
				for (AccountListener listener : accountListeners) {
					listener.changed(account);
				}
			}
		}
	}

	/////////////////////
	// Order dispatching.

	/**
	 * Add an order listener.
	 * 
	 * @param listener The listener.
	 */
	public void addOrderListener(OrderListener listener) {
		orderListeners.add(listener);

		// Initialize and start the account dispatcher if necessary.
		if (orderDispatcher == null) {
			orderDispatcher = new OrderDispatcher();
			Thread orderThread = new Thread(dispatcherThreadGroup, orderDispatcher, "Order");
			orderThread.start();
		}
	}

	/**
	 * Add an order message.
	 * 
	 * @param orderMessage The order message.
	 */
	public void addOrderMessage(OrderMessage orderMessage) {
		// Only add it if there are listeners whiling to listen updates.
		if (!orderListeners.isEmpty()) {
			try {
				orderLock.lock();
				orderMessages.add(orderMessage);
			} finally {
				orderLock.unlock();
			}
		}
	}

	/**
	 * Dispatch order updates.
	 */
	private void dispatchOrderUpdates() {

		// ActionCreate a private list of order updates.
		List<OrderMessage> orderMessages = new ArrayList<>();
		orderLock.lock();
		orderMessages.addAll(this.orderMessages);
		this.orderMessages.clear();
		orderLock.unlock();

		// Do dispatch.
		if (!orderMessages.isEmpty()) {
			for (OrderMessage orderMessage : orderMessages) {
				for (OrderListener listener : orderListeners) {
					listener.changed(orderMessage);
				}
			}
		}
	}

	//////////////////////////////////
	// Instrument dispatching helpers.

	/**
	 * Check if there is an instrument dispatcher for the argument instrument.
	 * 
	 * @param instrument The instrument.
	 * @return A boolean.
	 */
	private boolean isInstrumentDispatcher(Instrument instrument) {
		for (InstrumentDispatcher dispatcher : instrumentDispatchers) {
			if (dispatcher.instrument.equals(instrument)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Add the instrument dispatcher and start the thread.
	 * 
	 * @param instrument The instrument.
	 */
	private void addInstrumentDispatcher(Instrument instrument) {
		InstrumentDispatcher dispatcher = new InstrumentDispatcher();
		dispatcher.instrument = instrument;
		instrumentDispatchers.add(dispatcher);
		Thread instrumentThread = new Thread(dispatcherThreadGroup, dispatcher, "Instrument " + instrument.getId());
		instrumentThread.start();
	}

	/////////////////////
	// Ticks dispatching.

	/**
	 * Add a tick listener.
	 * 
	 * @param listener The listener.
	 */
	public void addTickListener(TickListener listener) {
		try {
			// Ensure that the instrument is subscribed.
			server.ensureSubscribed(listener.getInstrument());
			tickListeners.add(listener);

			// Check if there is an instrument dispatcher for the listener instrument and if not create it.
			if (!isInstrumentDispatcher(listener.getInstrument())) {
				addInstrumentDispatcher(listener.getInstrument());
			}

		} catch (ServerException exc) {
			LOGGER.catching(exc);
		}
	}

	/**
	 * Add a tick event.
	 * 
	 * @param instrument The instrument.
	 * @param tick The tick.
	 */
	public void addTickEvent(Instrument instrument, Tick tick) {
		try {
			tickLock.lock();
			TickEvent event = new TickEvent();
			event.instrument = instrument;
			event.tick = tick;
			tickEvents.add(event);
		} finally {
			tickLock.unlock();
		}
	}

	/**
	 * Dispatch ticks of the instrument.
	 * 
	 * @param instrument The instrument.
	 */
	private void dispatchTicks(Instrument instrument) {

		// ActionCreate a private list of ticks of the instrument.
		List<TickEvent> instrumentTicks = new ArrayList<>();
		tickLock.lock();
		Iterator<TickEvent> i = tickEvents.iterator();
		while (i.hasNext()) {
			TickEvent e = i.next();
			if (e.instrument.equals(instrument)) {
				instrumentTicks.add(e);
				i.remove();
			}
		}
		tickLock.unlock();

		// Do dispatch.
		if (!instrumentTicks.isEmpty()) {
			for (TickListener listener : tickListeners) {
				if (listener.getInstrument().equals(instrument)) {
					for (TickEvent tickEvent : instrumentTicks) {
						listener.tick(tickEvent.tick);
					}
				}
			}
		}
	}

	//////////////////////////
	// Data (bar) dispatching.

	/**
	 * Add a data listener.
	 * 
	 * @param listener The listener.
	 */
	public void addDataListener(DataListener listener) {
		dataListeners.add(listener);
	}

	/**
	 * Add a start data event.
	 * 
	 * @param instrument Instrument.
	 * @param period Period.
	 * @param offerSide Offer side.
	 * @param data Data (bar).
	 */
	public void addDataStartEvent(Instrument instrument, Period period, OfferSide offerSide, Data data) {
		DataEvent event = new DataEvent();
		event.instrument = instrument;
		event.period = period;
		event.offerSide = offerSide;
		event.data = data;
		dataStartEvents.add(event);
	}

	/**
	 * Add an end data event.
	 * 
	 * @param instrument Instrument.
	 * @param period Period.
	 * @param offerSide Offer side.
	 * @param data Data (bar).
	 */
	public void addDataEndEvent(Instrument instrument, Period period, OfferSide offerSide, Data data) {
		DataEvent event = new DataEvent();
		event.instrument = instrument;
		event.period = period;
		event.offerSide = offerSide;
		event.data = data;
		dataEndEvents.add(event);
	}

	////////////////////
	// Stop dispatchers.

	/**
	 * Dispatch events to listeners.
	 */
	public void dispatch() {

	}
}
