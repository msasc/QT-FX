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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.qtfx.lib.mkt.data.Instrument;
import com.qtfx.lib.mkt.data.Period;
import com.qtfx.lib.mkt.server.ServerException;

/**
 * The server class is responsible to provide access to server services.
 *
 * @author Miquel Sas
 */
public abstract class Server {

	/////////////////////////////////////////////
	// Description and identification properties.

	/** Server id. */
	private String id;
	/** Server name. */
	private String name;
	/** Server title. */
	private String title;

	//////////////////////////////////////
	// Instrument subscription management.

	/** List of subscribed instruments. */
	private Set<Instrument> subscribedInstruments = new HashSet<>();

	/////////////////////////
	// Dispatching of events:
	// - Bar completions
	// - Ticks
	// - Account changes
	// - Messages

	/** Bar, Tick, Account and Messages dispatcher. */
	private Dispatcher dispatcher;

	/**
	 * Default constructor.
	 */
	public Server() {
		super();
	}

	/////////////////////////////////////////////
	// Description and identification properties.

	/**
	 * Returns an unique and short id.
	 * 
	 * @return The server id.
	 */
	public String getId() {
		return id;
	}

	/**
	 * Set the server id.
	 * 
	 * @param id The id.
	 */
	protected void setId(String id) {
		this.id = id;
	}

	/**
	 * Returns the name of the server.
	 * 
	 * @return The name of the server.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the server name.
	 * 
	 * @param name The name.
	 */
	protected void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the server title or long name.
	 * 
	 * @return The server title or long name.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Set the server title.
	 * 
	 * @param title The title.
	 */
	protected void setTitle(String title) {
		this.title = title;
	}

	/////////////////////////
	// Connection management.

	/**
	 * Return the connection manager.
	 * 
	 * @return The connection manager.
	 * @throws ServerException
	 */
	public abstract ConnectionManager getConnectionManager() throws ServerException;

	//////////////////
	// Account access.

	/**
	 * Returns last known state of the account.
	 * 
	 * @return The account.
	 */
	public abstract Account getAccount();

	//////////////////
	// History access.

	/**
	 * Return an instance of a suitable history manager.
	 * 
	 * @return The history manager.
	 */
	public abstract HistoryManager getHistoryManager();

	//////////////////////////////////////
	// Instrument subscription management.

	/**
	 * Returns a list with all available instruments.
	 * 
	 * @return A list with all available instruments.
	 * @throws ServerException If a server error occurs.
	 */
	public abstract List<Instrument> getAvailableInstruments() throws ServerException;

	/**
	 * Subscribe the list of instruments to receive feed data.
	 * 
	 * @param instruments The list of instruments.
	 */
	public abstract void subscribeInstruments(Set<Instrument> instruments) throws ServerException;

	/**
	 * Unsubscribe the list of instruments to stop receiving feed data.
	 * 
	 * @param instruments The list of instruments.
	 */
	public abstract void unsubscribeInstruments(Set<Instrument> instruments) throws ServerException;

	/**
	 * Ensure that the argument instrument is subscribed.
	 * 
	 * @param instrument The instrument.
	 * @throws ServerException
	 */
	public void ensureSubscribed(Instrument instrument) throws ServerException {
		if (!getConnectionManager().isConnected()) {
			throw new ServerException("Not connected.");
		}
		if (!subscribedInstruments.contains(instrument)) {
			subscribedInstruments.add(instrument);
			subscribeInstruments(subscribedInstruments);
		}
	}

	/**
	 * Subscribe the instrument and period to receive feed data (both offer sides).
	 * 
	 * @param instrument The instrument.
	 * @param period The offer side.
	 */
	public abstract void subscribeToFeed(Instrument instrument, Period period);

	/////////////////////////
	// Dispatching of events:

	/**
	 * Return the dispatcher.
	 * 
	 * @return The dispatcher.
	 */
	public Dispatcher getDispatcher() {
		if (dispatcher == null) {
			dispatcher = new Dispatcher(this);
		}
		return dispatcher;
	}
}
