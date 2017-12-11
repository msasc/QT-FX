/*
 * Copyright (C) 2015 Miquel Sas
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
package com.qtfx.lib.mkt.server_bk.servers.dkcp;

import java.util.ArrayList;
import java.util.List;

import com.qtfx.lib.mkt.data.Data;
import com.qtfx.lib.mkt.data.Filter;
import com.qtfx.lib.mkt.data.Instrument;
import com.qtfx.lib.mkt.data.Period;
import com.qtfx.lib.mkt.data.Tick;
import com.qtfx.lib.mkt.server_bk.DataIterator;
import com.qtfx.lib.mkt.server_bk.HistoryManager;
import com.qtfx.lib.mkt.server_bk.ok.OfferSide;
import com.qtfx.lib.mkt.server_bk.ok.Order;
import com.qtfx.lib.mkt.server_bk.ok.ServerException;

/**
 * Dukascopy history manager implementation.
 * 
 * @author Miquel Sas
 */
public class DkHistoryManagerOld implements HistoryManager {

	/**
	 * KeyServer reference.
	 */
	private DkServer server;

	/**
	 * Constructor assigning the reference server.
	 * 
	 * @param server The Dukascopy server.
	 */
	public DkHistoryManagerOld(DkServer server) {
		super();
		this.server = server;
	}

	/**
	 * Returns the last tick.
	 * 
	 * @param instrument The instrument.
	 * @return The last tick.
	 * @throws ServerException If a server error occurs.
	 */
	@Override
	public Tick getLastTick(Instrument instrument) throws ServerException {
		server.checkSubscribed(instrument);
		return DkConverter.getLastTick(server.getContext(), instrument);
	}

	/**
	 * Returns the tick data.
	 * 
	 * @param instrument The instrument.
	 * @param from From time.
	 * @param to To time.
	 * @return The tick data.
	 * @throws ServerException If a server error occurs.
	 */
	@Override
	public List<Tick> getTickData(Instrument instrument, long from, long to) throws ServerException {
		server.checkSubscribed(instrument);
		com.dukascopy.api.Instrument dkInstrument = DkConverter.toDkInstrument(instrument);
		server.checkSubscribed(instrument);
		try {
			com.dukascopy.api.IHistory history = server.getContext().getHistory();
			List<com.dukascopy.api.ITick> dkTicks = history.getTicks(dkInstrument, from, to);
			List<Tick> ticks = new ArrayList<>();
			for (com.dukascopy.api.ITick dkTick : dkTicks) {
				Tick tick = DkConverter.fromDkTick(dkTick);
				ticks.add(tick);
			}
			return ticks;
		} catch (Exception cause) {
			throw new ServerException(cause);
		}
	}

	/**
	 * Returns ticks for the instrument.
	 * 
	 * @param instrument The instrument.
	 * @param time Time of the last one second tick interval in period specified in <i>oneSecondIntervalsBefore</i>
	 *        parameter or/and time of the one second tick interval prior first one second tick interval in period
	 *        specified with <i>oneSecondIntervalsAfter</i> parameter.
	 * @param oneSecondIntervalsBefore Number of one second ticks intervals to include before and including the time
	 *        parameter.
	 * @param oneSecondIntervalsAfter Number of one second ticks intervals to include after, not including the time
	 *        parameter.
	 * @return The list of ticks.
	 * @throws ServerException If a server error occurs.
	 */
	@Override
	public List<Tick> getTickData(
		Instrument instrument,
		long time,
		int oneSecondIntervalsBefore,
		int oneSecondIntervalsAfter)
		throws ServerException {
		com.dukascopy.api.Instrument dkInstrument = DkConverter.toDkInstrument(instrument);
		server.checkSubscribed(instrument);
		try {
			com.dukascopy.api.IHistory history = server.getContext().getHistory();
			List<com.dukascopy.api.ITick> dkTicks =
				history.getTicks(dkInstrument, oneSecondIntervalsBefore, time, oneSecondIntervalsAfter);
			List<Tick> ticks = new ArrayList<>();
			for (com.dukascopy.api.ITick dkTick : dkTicks) {
				Tick tick = DkConverter.fromDkTick(dkTick);
				ticks.add(tick);
			}
			return ticks;
		} catch (Exception cause) {
			throw new ServerException(cause);
		}
	}

	/**
	 * Returns a long indicating the first of data available for the given instrument and period.
	 * 
	 * @param instrument The instrument.
	 * @param period The period.
	 * @return A long indicating the first of data available for the given instrument and period.
	 * @throws ServerException If a server error occurs.
	 */
	@Override
	public long getTimeOfFirstData(Instrument instrument, Period period) throws ServerException {
		com.dukascopy.api.Instrument dkInstrument = DkConverter.toDkInstrument(instrument);
		com.dukascopy.api.Period dkPeriod = DkConverter.toDkPeriod(period);
		server.checkSubscribed(instrument);
		return server.getContext().getDataService().getTimeOfFirstCandle(dkInstrument, dkPeriod);
	}

	/**
	 * Returns a long indicating the time of the first order for the given instrument. This is a not efficient method
	 * since Dukascopy does not provide a specific method for this functionality, and the system has to retrieve all
	 * orders to access the first one.
	 * 
	 * @param instrument The instrument.
	 * @return A long indicating the time of the first order for the given instrument.
	 * @throws ServerException If a server error occurs.
	 */
	@Override
	public long getTimeOfFirstOrder(Instrument instrument) throws ServerException {
		List<Order> orders = getOrders(instrument, 0, Long.MAX_VALUE);
		if (!orders.isEmpty()) {
			Order firstOrder = orders.get(orders.size() - 1);
			return firstOrder.getCreationTime();
		}
		return 0;
	}

	/**
	 * Returns a long indicating the time of the first tick for the given instrument.
	 * 
	 * @param instrument The instrument.
	 * @return A long indicating the time of the first tick for the given instrument.
	 * @throws ServerException If a server error occurs.
	 */
	@Override
	public long getTimeOfFirstTick(Instrument instrument) throws ServerException {
		com.dukascopy.api.Instrument dkInstrument = DkConverter.toDkInstrument(instrument);
		server.checkSubscribed(instrument);
		return server.getContext().getDataService().getTimeOfFirstTick(dkInstrument);
	}

	/**
	 * Returns the last price data element.
	 * 
	 * @param instrument The instrument.
	 * @param period The period.
	 * @return The price data element.
	 * @throws ServerException If a server error occurs.
	 */
	@Override
	public Data getLastData(Instrument instrument, Period period) throws ServerException {
		return getData(instrument, period, 0);
	}

	/**
	 * Returns the price data element.
	 * 
	 * @param instrument The instrument.
	 * @param period The period.
	 * @param shift Shift, 0 for current, 1 for previous, and so n.
	 * @return The price data element.
	 * @throws ServerException If a server error occurs.
	 */
	@Override
	public Data getData(Instrument instrument, Period period, int shift) throws ServerException {
		return getData(instrument, period, OfferSide.ASK, shift);
	}

	/**
	 * Returns the data element.
	 * 
	 * @param instrument The instrument.
	 * @param period The period.
	 * @param offerSide The offer side.
	 * @param shift Shift, 0 for current, 1 for previous, and so n.
	 * @return The price data element.
	 * @throws ServerException If a server error occurs.
	 */
	@Override
	public Data getData(Instrument instrument, Period period, OfferSide offerSide, int shift) throws ServerException {
		
		com.dukascopy.api.Instrument dkInstrument = null;
		com.dukascopy.api.Period dkPeriod = null;
		com.dukascopy.api.OfferSide dkOfferSide = null;
		com.dukascopy.api.IHistory history = null;
		com.dukascopy.api.IBar bar = null;
		
		try {
			
			dkInstrument = DkConverter.toDkInstrument(instrument);
			dkPeriod = DkConverter.toDkPeriod(period);
			dkOfferSide = DkConverter.toDkOfferSide(offerSide);
			server.checkSubscribed(instrument);
			
			history = server.getContext().getHistory();
			int retry = 100;
			while (retry-- > 0) {
				bar = history.getBar(dkInstrument, dkPeriod, dkOfferSide, shift);
				if (bar != null) {
					break;
				}
				Thread.sleep(100);
			}
			
			Data data = new Data(Data.DATA_PRICE_SIZE);
			Data.setOpen(data, bar.getOpen());
			Data.setHigh(data, bar.getHigh());
			Data.setLow(data, bar.getLow());
			Data.setClose(data, bar.getClose());
			Data.setVolume(data, bar.getVolume());
			data.setTime(bar.getTime());
			return data;
		} catch (Exception cause) {
			throw new ServerException(cause);
		}
	}

	/**
	 * Returns the list of data.
	 * 
	 * @param instrument The instrument.
	 * @param period The period.
	 * @param filter The filter to apply.
	 * @param from From time.
	 * @param to To time.
	 * @return The list of data.
	 * @throws ServerException If a server error occurs.
	 */
	@Override
	public List<Data> getDataList(Instrument instrument, Period period, Filter filter, long from, long to)
		throws ServerException {
		return getDataList(instrument, period, OfferSide.ASK, filter, from, to);
	}

	/**
	 * Returns the list of data.
	 * 
	 * @param instrument The instrument.
	 * @param period The period.
	 * @param offerSide The offer side.
	 * @param filter The filter to apply.
	 * @param from From time.
	 * @param to To time.
	 * @return The list of data.
	 * @throws ServerException If a server error occurs.
	 */
	@Override
	public List<Data> getDataList(
		Instrument instrument,
		Period period,
		OfferSide offerSide,
		Filter filter,
		long from,
		long to)
		throws ServerException {
		com.dukascopy.api.Instrument dkInstrument = DkConverter.toDkInstrument(instrument);
		com.dukascopy.api.Period dkPeriod = DkConverter.toDkPeriod(period);
		com.dukascopy.api.Filter dkFilter = DkConverter.toDkFilter(filter);
		com.dukascopy.api.OfferSide dkOfferSide = DkConverter.toDkOfferSide(offerSide);
		server.checkSubscribed(instrument);
		try {
			com.dukascopy.api.IHistory history = server.getContext().getHistory();
			List<com.dukascopy.api.IBar> bars =
				history.getBars(dkInstrument, dkPeriod, dkOfferSide, dkFilter, from, to);
			List<Data> dataList = new ArrayList<>();
			for (int i = 0; i < bars.size(); i++) {
				com.dukascopy.api.IBar bar = bars.get(i);
				Data data = new Data(Data.DATA_PRICE_SIZE);
				Data.setOpen(data, bar.getOpen());
				Data.setHigh(data, bar.getHigh());
				Data.setLow(data, bar.getLow());
				Data.setClose(data, bar.getClose());
				Data.setVolume(data, bar.getVolume());
				data.setTime(bar.getTime());
				dataList.add(data);
			}
			return dataList;
		} catch (Exception cause) {
			throw new ServerException(cause);
		}
	}

	/**
	 * Returns data items for the sprecified parameters.
	 * 
	 * @param instrument The instrument.
	 * @param period The period.
	 * @param filter The filter to apply.
	 * @param time Reference time.
	 * @param periodsBefore Number of periods before including the period starting at the reference time.
	 * @param periodsAfter Number of periods after not including the period starting at the reference time.
	 * @return The list of data items.
	 * @throws ServerException If a server error occurs.
	 */
	@Override
	public List<Data> getDataList(
		Instrument instrument,
		Period period,
		Filter filter,
		long time,
		int periodsBefore,
		int periodsAfter) throws ServerException {
		return getDataList(instrument, period, OfferSide.ASK, filter, time, periodsBefore, periodsAfter);
	}

	/**
	 * Returns data items for the specified parameters.
	 * 
	 * @param instrument The instrument.
	 * @param period The period.
	 * @param offerSide The offer side.
	 * @param filter The filter to apply.
	 * @param time Reference time.
	 * @param periodsBefore Number of periods before including the period starting at the reference time.
	 * @param periodsAfter Number of periods after not including the period starting at the reference time.
	 * @return The list of data items.
	 * @throws ServerException If a server error occurs.
	 */
	@Override
	public List<Data> getDataList(
		Instrument instrument,
		Period period,
		OfferSide offerSide,
		Filter filter,
		long time,
		int periodsBefore,
		int periodsAfter) throws ServerException {

		com.dukascopy.api.Instrument dkInstrument = DkConverter.toDkInstrument(instrument);
		com.dukascopy.api.Period dkPeriod = DkConverter.toDkPeriod(period);
		com.dukascopy.api.Filter dkFilter = DkConverter.toDkFilter(filter);
		com.dukascopy.api.OfferSide dkOfferSide = DkConverter.toDkOfferSide(offerSide);
		server.checkSubscribed(instrument);
		try {
			com.dukascopy.api.IHistory history = server.getContext().getHistory();
			List<com.dukascopy.api.IBar> bars = history.getBars(
				dkInstrument,
				dkPeriod,
				dkOfferSide,
				dkFilter,
				periodsBefore,
				time,
				periodsAfter);
			List<Data> dataList = new ArrayList<>();
			for (int i = 0; i < bars.size(); i++) {
				com.dukascopy.api.IBar bar = bars.get(i);
				Data data = new Data(Data.DATA_PRICE_SIZE);
				Data.setOpen(data, bar.getOpen());
				Data.setHigh(data, bar.getHigh());
				Data.setLow(data, bar.getLow());
				Data.setClose(data, bar.getClose());
				Data.setVolume(data, bar.getVolume());
				data.setTime(bar.getTime());
				dataList.add(data);
			}
			return dataList;
		} catch (Exception cause) {
			throw new ServerException(cause);
		}
	}

	/**
	 * Returns an iterator aimed to download huge amounts of data.
	 * 
	 * @param instrument The instrument.
	 * @param period The period.
	 * @param filter The filter to apply.
	 * @param from From time.
	 * @param to To time.
	 * @return The iterator.
	 * @throws ServerException If a server error occurs.
	 */
	@Override
	public DataIterator getDataIterator(
		Instrument instrument,
		Period period,
		Filter filter,
		long from,
		long to)
		throws ServerException {
//		return new DkDataIterator(this, instrument, period, OfferSide.ASK, filter, from, to);
		return null;
	}

	/**
	 * Returns an iterator aimed to download huge amounts of data.
	 * 
	 * @param instrument The instrument.
	 * @param period The period.
	 * @param offerSide The offer side.
	 * @param filter The filter to apply.
	 * @param from From time.
	 * @param to To time.
	 * @return The iterator.
	 * @throws ServerException If a server error occurs.
	 */
	@Override
	public DataIterator getDataIterator(
		Instrument instrument,
		Period period,
		OfferSide offerSide,
		Filter filter,
		long from,
		long to)
		throws ServerException {
//		return new DkDataIterator(this, instrument, period, offerSide, filter, from, to);
		return null;
	}

	/**
	 * Returns a list with the closed orders.
	 * 
	 * @param instrument The instrument.
	 * @param from From time.
	 * @param to To time.
	 * @return The list of closed orders.
	 * @throws ServerException If a server error occurs.
	 */
	@Override
	public List<Order> getOrders(Instrument instrument, long from, long to) throws ServerException {
		com.dukascopy.api.Instrument dkInstrument = DkConverter.toDkInstrument(instrument);
		com.dukascopy.api.IHistory history = server.getContext().getHistory();
		server.checkSubscribed(instrument);
		try {
			List<com.dukascopy.api.IOrder> dkOrders = history.getOrdersHistory(dkInstrument, from, to);
			List<Order> orders = new ArrayList<>();
			for (int i = 0; i < dkOrders.size(); i++) {
//				orders.add(new DkOrder(server, dkOrders.get(i)));
			}
			return orders;
		} catch (Exception cause) {
			throw new ServerException(cause);
		}
	}

}
