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

package com.qtfx.lib.mkt.servers.dukascopy;

import java.util.ArrayList;
import java.util.List;

import com.qtfx.lib.mkt.data.Data;
import com.qtfx.lib.mkt.data.Filter;
import com.qtfx.lib.mkt.data.Instrument;
import com.qtfx.lib.mkt.data.OfferSide;
import com.qtfx.lib.mkt.data.Period;
import com.qtfx.lib.mkt.server.DataIterator;
import com.qtfx.lib.mkt.server.ServerException;

/**
 * Dukascopy implementation of the data iterator.
 *
 * @author Miquel Sas
 */
public class DkDataIterator implements DataIterator {

	/** History manager. */
	private DkHistoryManager historyManager;
	/** Instrument. */
	private Instrument instrument;
	/** Period. */
	private Period period;
	/** Offer side. */
	private OfferSide offerSide;
	/** Filter. */
	private Filter filter;
	/** From time. */
	private long from;
	/** To time. */
	private long to;
	/** The buffer where temporarily the data is loaded. */
	private List<Data> buffer = new ArrayList<>();
	/** The buffer size. */
	private int bufferSize = 10000;
	/** The last time loaded. */
	private long lastTime = -1;

	/**
	 * Constructor.
	 * 
	 * @param historyManager The history manager.
	 * @param instrument The instrument.
	 * @param period The period.
	 * @param offerSide The offer side.
	 * @param filter The filter to apply.
	 * @param from From time.
	 * @param to To time.
	 */
	public DkDataIterator(
		DkHistoryManager historyManager,
		Instrument instrument,
		Period period,
		OfferSide offerSide,
		Filter filter,
		long from,
		long to) {
		super();
		this.historyManager = historyManager;
		this.instrument = instrument;
		this.period = period;
		this.offerSide = offerSide;
		this.filter = filter;
		this.from = from;
		this.to = to;
	}

	/**
	 * Request data from the history manager and load the buffer.
	 */
	private void requestData() throws ServerException {
		// If the buffer is not empty, do nothing.
		if (!buffer.isEmpty()) {
			return;
		}
		// Correct the to time if not set.
		if (to <= 0) {
			to = historyManager.getTimeOfLastData(instrument, period, filter);
		}
		// If there is time to and last time loaded is greater than or equal to final to time, do nothing.
		if (lastTime >= to) {
			return;
		}
		// The last time loaded in the first call.
		if (lastTime == -1) {
			lastTime = from;
		}
		// Load data.
		long loadFrom = lastTime;
		long loadTo = loadFrom;
		for (int i = 0; i < bufferSize; i++) {
			long increase = period.getTime();
			long nextTo = loadTo + increase;
			if (nextTo > to) {
				break;
			}
			loadTo = nextTo;
		}
		List<Data> dataList = historyManager.getDataList(instrument, period, offerSide, filter, loadFrom, loadTo);
		if (dataList == null) {
			throw new ServerException();
		}
		for (Data data : dataList) {
			lastTime = data.getTime();
			if (lastTime <= to) {
				buffer.add(data);
			}
			if (lastTime >= to) {
				break;
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() throws ServerException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasNext() throws ServerException {
		requestData();
		return !buffer.isEmpty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Data next() throws ServerException {
		try {
			return buffer.remove(0);
		} catch (Exception cause) {
			throw new ServerException("No more data to download", cause);
		}
	}
}
