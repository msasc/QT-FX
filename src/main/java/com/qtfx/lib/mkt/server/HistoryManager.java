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

import java.util.List;

import com.qtfx.lib.mkt.data.Data;
import com.qtfx.lib.mkt.data.Filter;
import com.qtfx.lib.mkt.data.Instrument;
import com.qtfx.lib.mkt.data.OfferSide;
import com.qtfx.lib.mkt.data.Period;
import com.qtfx.lib.mkt.data.Tick;

/**
 * Interface responsible to provide information about the history of data and orders.
 * 
 * @author Miquel Sas
 */
public abstract class HistoryManager {

	//////////////////////////////////////////
	// Retrieving time of first tick and data.

	/**
	 * Returns a long indicating the time of the first tick for the given instrument.
	 * 
	 * @param instrument The instrument.
	 * @return A long indicating the time of the first tick for the given instrument.
	 * @throws ServerException If a server error occurs.
	 */
	public abstract long getTimeOfFirstTick(Instrument instrument) throws ServerException;

	/**
	 * Returns a long indicating the first of price data available for the given instrument and period.
	 * 
	 * @param instrument The instrument.
	 * @param period The period.
	 * @return A long indicating the first of data available for the given instrument and period.
	 * @throws ServerException If a server error occurs.
	 */
	public abstract long getTimeOfFirstData(Instrument instrument, Period period) throws ServerException;

	//////////////////////////////////////////
	// Retrieving time of last tick and data.

	/**
	 * Return the time of last tick.
	 * 
	 * @param instrument The instrument.
	 * @return The time of last tick.
	 * @throws ServerException
	 */
	public long getTimeOfLastTick(Instrument instrument) throws ServerException {
		Tick lastTick = getTick(instrument, 0);
		return lastTick.getTime();
	}

	/**
	 * Return the time of last data.
	 * 
	 * @param instrument The instrument.
	 * @param period The period.
	 * @param filter The filter to apply.
	 * @return The time of last data.
	 * @throws ServerException
	 */
	public long getTimeOfLastData(Instrument instrument, Period period, Filter filter) throws ServerException {
		int shift = 0;
		int maxLoops = 20000;
		Data data = null;
		while (--maxLoops >= 0) {
			data = getData(instrument, period, OfferSide.ASK, shift++);
			if (data != null && Data.accept(data, filter)) {
				break;
			}
		}
		return data.getTime();
	}

	/////////////////////////////////////////////////////
	// Retrieve tick and data from shifting from current.

	/**
	 * Retrieve a tick shifting from current (last) one.
	 * 
	 * @param instrument The instrument.
	 * @param shift The ticks to shift.
	 * @return The required tick.
	 */
	public abstract Tick getTick(Instrument instrument, int shift) throws ServerException;

	/**
	 * Returns the price data element.
	 * 
	 * @param instrument The instrument.
	 * @param period The period.
	 * @param offerSide The offer side.
	 * @param shift Shift, 0 for current, 1 for previous, and so n.
	 * @return The price data element.
	 * @throws ServerException If a server error occurs.
	 */
	public abstract Data getData(
		Instrument instrument,
		Period period,
		OfferSide offerSide,
		int shift)
		throws ServerException;

	////////////////////////////////////
	// Retrieve lists of ticks and data.

	/**
	 * Returns price data items for the specified parameters.
	 * 
	 * @param instrument The instrument.
	 * @param period The period.
	 * @param offerSide The offer side.
	 * @param filter The filter to apply.
	 * @param from Time from.
	 * @param to Time to.
	 * @return The list of price data items.
	 * @throws ServerException If a server error occurs.
	 */
	public abstract List<Data> getDataList(
		Instrument instrument,
		Period period,
		OfferSide offerSide,
		Filter filter,
		long from,
		long to)
		throws ServerException;

	/**
	 * Returns an price iterator aimed to download huge amounts of data.
	 * 
	 * @param instrument The instrument.
	 * @param period The period.
	 * @param offerSide The offer side.
	 * @param filter The filter to apply.
	 * @param from From time.
	 * @return The price iterator.
	 * @throws ServerException If a server error occurs.
	 */
	public abstract DataIterator getDataIterator(
		Instrument instrument,
		Period period,
		OfferSide offerSide,
		Filter filter,
		long from)
		throws ServerException;

	/**
	 * Returns an price iterator aimed to download huge amounts of data.
	 * 
	 * @param instrument The instrument.
	 * @param period The period.
	 * @param offerSide The offer side.
	 * @param filter The filter to apply.
	 * @param from From time.
	 * @param to To time.
	 * @return The price iterator.
	 * @throws ServerException If a server error occurs.
	 */
	public abstract DataIterator getDataIterator(
		Instrument instrument,
		Period period,
		OfferSide offerSide,
		Filter filter,
		long from,
		long to)
		throws ServerException;
}
