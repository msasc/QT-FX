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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dukascopy.api.IBar;
import com.dukascopy.api.IDataService;
import com.dukascopy.api.IHistory;
import com.dukascopy.api.JFException;
import com.qtfx.lib.mkt.data.Data;
import com.qtfx.lib.mkt.data.Filter;
import com.qtfx.lib.mkt.data.Instrument;
import com.qtfx.lib.mkt.data.OfferSide;
import com.qtfx.lib.mkt.data.Period;
import com.qtfx.lib.mkt.data.Tick;
import com.qtfx.lib.mkt.server.DataIterator;
import com.qtfx.lib.mkt.server.HistoryManager;
import com.qtfx.lib.mkt.server.ServerException;

/**
 *
 * @author Miquel Sas
 *
 */
public class DkHistoryManager extends HistoryManager {

	/** Logger instance. */
	private static final Logger LOGGER = LogManager.getLogger();

	/** Dukascopy server. */
	private DkServer server;

	/**
	 * Constructor.
	 * 
	 * @param server The Dukascopy server.
	 */
	public DkHistoryManager(DkServer server) {
		super();
		this.server = server;
	}

	/**
	 * Access to the data service.
	 * 
	 * @return The IDataService.
	 */
	private IDataService getDataService() {
		return server.core.context.getDataService();
	}

	/**
	 * Access to the history service.
	 * 
	 * @return The history service.
	 */
	private IHistory getHistory() {
		return server.core.context.getHistory();
	}

	//////////////////////////////////////////
	// Retrieving time of first tick and data.

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getTimeOfFirstTick(Instrument instrument) throws ServerException {
		server.ensureSubscribed(instrument);
		return getDataService().getTimeOfFirstTick(DkCore.toDkInstrument(instrument));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getTimeOfFirstData(Instrument instrument, Period period) throws ServerException {
		server.ensureSubscribed(instrument);
		return getDataService().getTimeOfFirstCandle(DkCore.toDkInstrument(instrument), DkCore.toDkPeriod(period));
	}

	/////////////////////////////////////////////////////
	// Retrieve tick and data from shifting from current.

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Tick getTick(Instrument instrument, int shift) throws ServerException {
		try {
			server.ensureSubscribed(instrument);
			return DkCore.fromDkTick(getHistory().getTick(DkCore.toDkInstrument(instrument), shift));
		} catch (JFException e) {
			throw new ServerException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Data getData(Instrument instrument, Period period, OfferSide offerSide, int shift) throws ServerException {
		try {
			server.ensureSubscribed(instrument);
			return DkCore.fromDkBar(getHistory().getBar(
				DkCore.toDkInstrument(instrument), 
				DkCore.toDkPeriod(period), 
				DkCore.toDkOfferSide(offerSide), shift));
		} catch (JFException e) {
			throw new ServerException(e);
		}
	}

	/**
	 * {@inheritDoc}
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

		server.ensureSubscribed(instrument);
		List<Data> dataList = new ArrayList<>();
		try {
			List<IBar> bars = getHistory().getBars(
				DkCore.toDkInstrument(instrument),
				DkCore.toDkPeriod(period),
				DkCore.toDkOfferSide(offerSide),
				DkCore.toDkFilter(filter),
				from, to);
			bars.forEach(bar -> dataList.add(DkCore.fromDkBar(bar)));
		} catch (JFException e) {
			LOGGER.catching(e);
		}
		return dataList;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DataIterator getDataIterator(
		Instrument instrument,
		Period period,
		OfferSide offerSide,
		Filter filter,
		long from)
		throws ServerException {
		return getDataIterator(instrument, period, offerSide, filter, from, -1);
	}

	/**
	 * {@inheritDoc}
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
		server.ensureSubscribed(instrument);
		return new DkDataIterator(this, instrument, period, offerSide, filter, from, to);
	}

}
