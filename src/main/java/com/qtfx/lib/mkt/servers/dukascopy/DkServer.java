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

import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qtfx.lib.mkt.data.Instrument;
import com.qtfx.lib.mkt.data.OfferSide;
import com.qtfx.lib.mkt.data.Period;
import com.qtfx.lib.mkt.server.Account;
import com.qtfx.lib.mkt.server.ConnectionManager;
import com.qtfx.lib.mkt.server.HistoryManager;
import com.qtfx.lib.mkt.server.Server;
import com.qtfx.lib.mkt.server.ServerException;

/**
 * Dukascopy server implementation.
 * 
 * @author Miquel Sas
 */
public class DkServer extends Server {

	/** Logger instance. */
	private static final Logger LOGGER = LogManager.getLogger();
	/** Connection manager. */
	private DkConnectionManager connectionManager;
	/** History manager. */
	private DkHistoryManager historyManager;
	
	/** Dukascopy core. */
	DkCore core;

	/**
	 * Constructor.
	 */
	public DkServer() {
		super();

		// Name, id, title.
		setName("Dukascopy");
		setId("dkcp");
		setTitle("Dukascopy Bank SA");

		try {
			core = new DkCore(this);
		} catch (ServerException exc) {
			LOGGER.catching(exc);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConnectionManager getConnectionManager() throws ServerException {
		if (connectionManager == null) {
			connectionManager = new DkConnectionManager(this);
		}
		return connectionManager;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Account getAccount() {
		return new DkAccount(core.context.getAccount());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HistoryManager getHistoryManager() {
		if (historyManager == null) {
			historyManager = new DkHistoryManager(this);
		}
		return historyManager;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void subscribeInstruments(Set<Instrument> instruments) throws ServerException {
		core.client.setSubscribedInstruments(DkCore.toDkInstrumentSet(instruments));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void unsubscribeInstruments(Set<Instrument> instruments) throws ServerException {
		core.client.unsubscribeInstruments(DkCore.toDkInstrumentSet(instruments));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void subscribeToFeed(Instrument instrument, Period period) {
		
		core.context.subscribeToBarsFeed(
			DkCore.toDkInstrument(instrument), 
			DkCore.toDkPeriod(period), 
			DkCore.toDkOfferSide(OfferSide.ASK), core.feedListener);
		
		core.context.subscribeToBarsFeed(
			DkCore.toDkInstrument(instrument), 
			DkCore.toDkPeriod(period), 
			DkCore.toDkOfferSide(OfferSide.BID), core.feedListener);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Instrument> getAvailableInstruments() throws ServerException {
		// TODO Auto-generated method stub
		return null;
	}

}
