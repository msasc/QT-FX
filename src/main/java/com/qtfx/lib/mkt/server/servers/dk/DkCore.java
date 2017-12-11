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

package com.qtfx.lib.mkt.server.servers.dk;

import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dukascopy.api.IAccount;
import com.dukascopy.api.IBar;
import com.dukascopy.api.IContext;
import com.dukascopy.api.ICurrency;
import com.dukascopy.api.IMessage;
import com.dukascopy.api.IStrategy;
import com.dukascopy.api.ITick;
import com.dukascopy.api.JFException;
import com.dukascopy.api.OfferSide;
import com.dukascopy.api.system.ClientFactory;
import com.dukascopy.api.system.IClient;
import com.dukascopy.api.system.ISystemListener;
import com.qtfx.lib.mkt.data.Data;
import com.qtfx.lib.mkt.data.Filter;
import com.qtfx.lib.mkt.data.Instrument;
import com.qtfx.lib.mkt.data.Period;
import com.qtfx.lib.mkt.data.Tick;
import com.qtfx.lib.mkt.data.Unit;
import com.qtfx.lib.mkt.server.ConnectionEvent;
import com.qtfx.lib.mkt.server.OrderCommand;
import com.qtfx.lib.mkt.server.OrderState;
import com.qtfx.lib.mkt.server.ServerException;
import com.qtfx.lib.mkt.server_bk.servers.dkcp.DkConverter;

/**
 * Dukascopy core. This is the only class that contains Dukascopy objects as well as conversions to/from
 * Dukascopy/System classes.
 * 
 * @author Miquel Sas
 */
public class DkCore {

	/** Logger instance. */
	private static final Logger LOGGER = LogManager.getLogger();
	////////////////////////////////////////
	// From/To Dukascopy/System conversions.

	/** A map with Dukascopy instruments by id (name). */
	private static Map<String, com.dukascopy.api.Instrument> mapInstruments = null;

	/**
	 * Returns this system data item given the Dukascopy bar.
	 * 
	 * @param dkBar The Dukascopy bar.
	 * @return This system price data item.
	 */
	public static Data fromDkBar(IBar dkBar) {
		Data data = new Data(Data.DATA_PRICE_SIZE);
		Data.setOpen(data, dkBar.getOpen());
		Data.setHigh(data, dkBar.getHigh());
		Data.setLow(data, dkBar.getLow());
		Data.setClose(data, dkBar.getClose());
		Data.setVolume(data, dkBar.getVolume());
		data.setTime(dkBar.getTime());
		return data;
	}

	/**
	 * Convert from a Dukascopy instrument to a system instrument.
	 * 
	 * @param dkInstrument The Dukascopy instrument.
	 * @return This system corresponding instrument.
	 */
	public static Instrument fromDkInstrument(com.dukascopy.api.Instrument dkInstrument) {
		Instrument instrument = new Instrument();
		
		// Identifier.
		instrument.setId(dkInstrument.name());

		// Dukascopy instruments use the JFCurrency as currency. For indexes and raw materials the primary currency is a
		// pseudo-currency like DEU.IDX and its java currency is null.
		ICurrency jfPrimaryCurrency = dkInstrument.getPrimaryJFCurrency();
		ICurrency jfSecondaryCurrency = dkInstrument.getSecondaryJFCurrency();
		Currency primaryCurrency = jfPrimaryCurrency.getJavaCurrency();
		Currency secondaryCurrency = jfSecondaryCurrency.getJavaCurrency();

		boolean pair = (primaryCurrency != null);

		// Description:
		instrument.setDescription(dkInstrument.toString());

		// Primary currency:
		// - If it is a pair, use the Dukascopy primary currency.
		// - If not, use the secondary currency.
		// Correct Dukascopy bug in secondary currency with CNY.
		if (dkInstrument.getSecondaryJFCurrency().toString().equals("CNH")) {
			secondaryCurrency = Currency.getInstance("CNY");
		}
		instrument.setPrimaryCurrency(pair ? primaryCurrency : secondaryCurrency);

		// Secondary currency.
		instrument.setSecondaryCurrency(secondaryCurrency);

		// Pip value.
		instrument.setPipValue(dkInstrument.getPipValue());

		// Pip scale.
		instrument.setPipScale(dkInstrument.getPipScale());

		// Tick scale, by default one order higher than pip scale.
		int tickScale = instrument.getPipScale() + 1;
		instrument.setTickScale(tickScale);

		// Tick value:
		// - For pairs tick scale unit.
		// - For the rest, half pip value.
		double tickValue = (1 / Math.pow(10, tickScale)) * (pair ? 1 : 5);
		instrument.setTickValue(tickValue);

		// Volume scale.
		instrument.setVolumeScale(0);

		return instrument;
	}

	/**
	 * Returns this system offer side given the Dukascopy offer side.
	 * 
	 * @param dkOfferSide The Dukascopy offer side.
	 * @return This system offer side.
	 */
	public static OfferSide fromDkOfferSide(com.dukascopy.api.OfferSide dkOfferSide) {
		switch (dkOfferSide) {
		case ASK:
			return OfferSide.ASK;
		case BID:
			return OfferSide.BID;
		default:
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Returns this system <i>OrderCommand</i> given the Dukascopy order command.
	 * 
	 * @param orderCommand The Dukascopy order command.
	 * @param closed A boolean that indicates if the order is closed.
	 * @return This system <i>OrderCommand</i>.
	 */
	public static OrderCommand fromDkOrderCommand(
		com.dukascopy.api.IEngine.OrderCommand orderCommand,
		boolean closed) {
		switch (orderCommand) {
		case BUY:
			return (closed ? OrderCommand.BUY : OrderCommand.BUY_MARKET);
		case BUYLIMIT:
			return OrderCommand.BUY_LIMIT_ASK;
		case BUYLIMIT_BYBID:
			return OrderCommand.BUY_LIMIT_BID;
		case BUYSTOP:
			return OrderCommand.BUY_STOP_ASK;
		case BUYSTOP_BYBID:
			return OrderCommand.BUY_STOP_BID;
		case SELL:
			return (closed ? OrderCommand.SELL : OrderCommand.SELL_MARKET);
		case SELLLIMIT:
			return OrderCommand.SELL_LIMIT_BID;
		case SELLLIMIT_BYASK:
			return OrderCommand.SELL_LIMIT_ASK;
		case SELLSTOP:
			return OrderCommand.SELL_STOP_BID;
		case SELLSTOP_BYASK:
			return OrderCommand.SELL_STOP_ASK;
		case PLACE_BID:
			return OrderCommand.PLACE_BID;
		case PLACE_OFFER:
			return OrderCommand.PLACE_ASK;
		default:
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Returns this system order state given the Dukascopy order state.
	 * 
	 * @param dkOrderState The Dukascopy order state.
	 * @return This system order state.
	 */
	public static OrderState fromDkOrderState(com.dukascopy.api.IOrder.State dkOrderState) {
		switch (dkOrderState) {
		case CANCELED:
			return OrderState.CANCELLED;
		case CLOSED:
			return OrderState.CLOSED;
		case CREATED:
			return OrderState.CREATED;
		case FILLED:
			return OrderState.FILLED;
		case OPENED:
			return OrderState.OPENED;
		default:
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Returns this system period given the Dukascopy period.
	 * 
	 * @param dkPeriod The Dukascopy period.
	 * @return This system period.
	 */
	public static Period fromDkPeriod(com.dukascopy.api.Period dkPeriod) {
		Unit unit = fromDkUnit(dkPeriod.getUnit());
		int size = dkPeriod.getNumOfUnits();
		return new Period(unit, size);
	}

	/**
	 * Returns this system tick given the Dukascopy tick.
	 * 
	 * @param dkTick The Dukascopy tick.
	 * @return This system tick.
	 */
	public static Tick fromDkTick(ITick dkTick) {
		Tick tick = new Tick();
		double[] askValues = dkTick.getAsks();
		double[] askVolumes = dkTick.getAskVolumes();
		int askSize = askValues.length;
		for (int i = 0; i < askSize; i++) {
			tick.addAsk(askValues[i], askVolumes[i]);
		}
		double[] bidValues = dkTick.getBids();
		double[] bidVolumes = dkTick.getBidVolumes();
		int bidSize = bidValues.length;
		for (int i = 0; i < bidSize; i++) {
			tick.addBid(bidValues[i], bidVolumes[i]);
		}
		tick.setTime(dkTick.getTime());
		return tick;
	}

	/**
	 * Returns this system unit given the Dukaascopy unit.
	 * 
	 * @param dkUnit The Dukascopy unit.
	 * @return The system unit.
	 */
	public static Unit fromDkUnit(com.dukascopy.api.Unit dkUnit) {
		switch (dkUnit) {
		case Millisecond:
			return Unit.MILLISECOND;
		case Second:
			return Unit.SECOND;
		case Minute:
			return Unit.MINUTE;
		case Hour:
			return Unit.HOUR;
		case Day:
			return Unit.DAY;
		case Week:
			return Unit.WEEK;
		case Month:
			return Unit.MONTH;
		case Year:
			return Unit.YEAR;
		default:
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Returns a suitable Dukascopy filter given this system filter.
	 * 
	 * @param filter The filter.
	 * @return The Dukascopy filter.
	 */
	public static com.dukascopy.api.Filter toDkFilter(Filter filter) {
		switch (filter) {
		case ALL_FLATS:
			return com.dukascopy.api.Filter.ALL_FLATS;
		case NO_FILTER:
			return com.dukascopy.api.Filter.NO_FILTER;
		case WEEKENDS:
			return com.dukascopy.api.Filter.WEEKENDS;
		default:
			// Should never come here.
			throw new IllegalArgumentException("Filter not supported: " + filter.name());
		}
	}

	/**
	 * Returns the Dukascopy instrument given the system instrument.
	 * 
	 * @param instrument The system instrument.
	 * @return The Dukascopy instrument.
	 */
	public static com.dukascopy.api.Instrument toDkInstrument(Instrument instrument) {
		if (mapInstruments == null) {
			mapInstruments = new HashMap<>();
			for (com.dukascopy.api.Instrument dkInstrument : com.dukascopy.api.Instrument.values()) {
				mapInstruments.put(dkInstrument.name(), dkInstrument);
			}
		}
		com.dukascopy.api.Instrument dkInstrument = mapInstruments.get(instrument.getId());
		if (dkInstrument != null) {
			return dkInstrument;
		}
		// Should never come here.
		throw new IllegalArgumentException("Instrument not supported: " + instrument.getId());
	}

	/**
	 * Returns the Dukascopy offer side given this system offer side.
	 * 
	 * @param offerSide The offer side.
	 * @return The Dukascopy offer side.
	 */
	public static com.dukascopy.api.OfferSide toDkOfferSide(OfferSide offerSide) {
		switch (offerSide) {
		case ASK:
			return com.dukascopy.api.OfferSide.ASK;
		case BID:
			return com.dukascopy.api.OfferSide.BID;
		default:
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Returns the Dukascopy order command given this system order command.
	 * 
	 * @param orderCommand This system order command.
	 * @return The Dukascopy order command.
	 */
	public static com.dukascopy.api.IEngine.OrderCommand toDkOrderCommand(OrderCommand orderCommand) {
		switch (orderCommand) {
		case BUY:
		case BUY_MARKET:
			return com.dukascopy.api.IEngine.OrderCommand.BUY;
		case BUY_LIMIT_ASK:
			return com.dukascopy.api.IEngine.OrderCommand.BUYLIMIT;
		case BUY_LIMIT_BID:
			return com.dukascopy.api.IEngine.OrderCommand.BUYLIMIT_BYBID;
		case BUY_STOP_ASK:
			return com.dukascopy.api.IEngine.OrderCommand.BUYSTOP;
		case BUY_STOP_BID:
			return com.dukascopy.api.IEngine.OrderCommand.BUYSTOP_BYBID;
		case SELL:
		case SELL_MARKET:
			return com.dukascopy.api.IEngine.OrderCommand.SELL;
		case SELL_LIMIT_BID:
			return com.dukascopy.api.IEngine.OrderCommand.SELLLIMIT;
		case SELL_LIMIT_ASK:
			return com.dukascopy.api.IEngine.OrderCommand.SELLLIMIT_BYASK;
		case SELL_STOP_BID:
			return com.dukascopy.api.IEngine.OrderCommand.SELLSTOP;
		case SELL_STOP_ASK:
			return com.dukascopy.api.IEngine.OrderCommand.SELLSTOP_BYASK;
		case PLACE_BID:
			return com.dukascopy.api.IEngine.OrderCommand.PLACE_BID;
		case PLACE_ASK:
			return com.dukascopy.api.IEngine.OrderCommand.PLACE_OFFER;
		default:
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Returns a suitable Dukascopy period give this system period.
	 * 
	 * @param period The period.
	 * @return The Dukascopy period.
	 */
	public static com.dukascopy.api.Period toDkPeriod(Period period) {
		com.dukascopy.api.Unit unit = toDkUnit(period.getUnit());
		int size = period.getSize();
		return com.dukascopy.api.Period.createCustomPeriod(unit, size);
	}

	/**
	 * Returns the corresponding unit given the system unit.
	 * 
	 * @param unit The system unit.
	 * @return The Dukascopy unit.
	 */
	public static com.dukascopy.api.Unit toDkUnit(Unit unit) {
		switch (unit) {
		case MILLISECOND:
			return com.dukascopy.api.Unit.Millisecond;
		case SECOND:
			return com.dukascopy.api.Unit.Second;
		case MINUTE:
			return com.dukascopy.api.Unit.Minute;
		case HOUR:
			return com.dukascopy.api.Unit.Hour;
		case DAY:
			return com.dukascopy.api.Unit.Day;
		case WEEK:
			return com.dukascopy.api.Unit.Week;
		case MONTH:
			return com.dukascopy.api.Unit.Month;
		case YEAR:
			return com.dukascopy.api.Unit.Year;
		default:
			throw new IllegalArgumentException();
		}
	}

	//////////////////////////////
	// Dukascopy system listener.

	/**
	 * Dukascopy system listener to forward connection and strategy events.
	 */
	class SystemListener implements ISystemListener {
		@Override
		public String toString() {
			return "SystemListener [server=" + server + "]";
		}

		@Override
		public void onStart(long processId) {
		}

		@Override
		public void onStop(long processId) {
		}

		@Override
		public void onConnect() {
			try {
				ConnectionEvent e = new ConnectionEvent(client);
				StringBuilder b = new StringBuilder();
				b.append("Connected to Dukascopy server: ");
				b.append(server.getConnectionManager().getConnectionType().name());
				e.setMessage(b.toString());
				server.getConnectionManager().notifyConnectionEvent(e);
			} catch (ServerException exc) {
				LOGGER.catching(null);
			}
		}

		public void onDisconnect() {
			try {
				ConnectionEvent e = new ConnectionEvent(client);
				StringBuilder b = new StringBuilder();
				b.append("Disconnected from Dukascopy server: ");
				b.append(server.getConnectionManager().getConnectionType().name());
				e.setMessage(b.toString());
				server.getConnectionManager().notifyConnectionEvent(e);
			} catch (ServerException exc) {
				LOGGER.catching(null);
			}
		}
	}
	
	//////////////////////////////
	// Dukscopy strategy listener.

	class StrategyListener implements IStrategy {
		@Override
		public void onStart(IContext context) throws JFException {
			DKCore.this.context = context;
		}
		@Override
		public void onTick(Instrument instrument, ITick tick) throws JFException {
			server.getFeedDispatcher().addTick(fromDkInstrument(instrument),.fromDkTick(tick));
		}

		@Override
		public void onBar(Instrument instrument, Period period, IBar askBar, IBar bidBar) throws JFException {
			server.getFeedDispatcher().addData(
				DkConverter.fromDkInstrument(instrument),
				DkConverter.fromDkPeriod(period), 
				DkConverter.fromDkOfferSide(OfferSide.ASK),
				DkConverter.fromDkBar(askBar));
			server.getFeedDispatcher().addData(
				DkConverter.fromDkInstrument(instrument),
				DkConverter.fromDkPeriod(period), 
				DkConverter.fromDkOfferSide(OfferSide.BID),
				DkConverter.fromDkBar(bidBar));
		}

		/**
		 * Called when new message is received.
		 */
		@Override
		public void onMessage(IMessage message) throws JFException {
		}

		/**
		 * Called when account information update is received.
		 */
		@Override
		public void onAccount(IAccount account) throws JFException {
		}

		/**
		 * Called before strategy is stopped.
		 */
		@Override
		public void onStop() throws JFException {
		}
		@Override
		public void onTick(com.dukascopy.api.Instrument instrument, ITick tick) throws JFException {
			// TODO Auto-generated method stub
		}
		@Override
		public void onBar(
			com.dukascopy.api.Instrument instrument,
			com.dukascopy.api.Period period,
			IBar askBar,
			IBar bidBar)
			throws JFException {
			// TODO Auto-generated method stub
			
		}
		
	}

	//////////////////////////////
	// Dukascopy internal objects.

	/** Client interface. */
	IClient client;
	/** The saved context. */
	IContext context;
	/** System listener. */
	SystemListener systemListener;
	/** Strategy listener to forward tick and data feed. */
	StrategyListener stateryListener;

	/////////////////////////
	// Server implementation.

	/** Dukascopy server. */
	DkServer server;

	/**
	 * Constructor.
	 * 
	 * @param server The Dukascopy server.
	 */
	public (DkServer server) throws ServerException {
		super();
		
		this.server = server;
		try {
			// Initialize the Dukascopy client.
			client = ClientFactory.getDefaultInstance();

			// Initialize and set the system listener.
			systemListener = new SystemListener();
			client.setSystemListener(systemListener);

			// Initialize the context strategy.
			// strategyListener = new DkStrategyListener(this);

		} catch (ClassNotFoundException | IllegalAccessException | InstantiationException cause) {
			throw new ServerException(cause);
		}
	}

	@Override
	public void onTick(com.dukascopy.api.Instrument instrument, ITick tick) throws JFException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBar(
		com.dukascopy.api.Instrument instrument,
		com.dukascopy.api.Period period,
		IBar askBar,
		IBar bidBar)
		throws JFException {
		// TODO Auto-generated method stub
		
	}
}
