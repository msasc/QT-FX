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

package com.qtfx.lib.mkt.server.servers.dkcp;

import java.util.List;

import com.qtfx.lib.mkt.data.Instrument;
import com.qtfx.lib.mkt.server.AccountType;
import com.qtfx.lib.mkt.server.ConnectionManager;
import com.qtfx.lib.mkt.server.FeedManager;
import com.qtfx.lib.mkt.server.HistoryManager;
import com.qtfx.lib.mkt.server.OrderManager;
import com.qtfx.lib.mkt.server.Server;
import com.qtfx.lib.mkt.server.ServerException;

/**
 * Dukascopy server implementation.
 * 
 * @author Miquel Sas
 */
public class DkServer implements Server {

	/**
	 * 
	 */
	public DkServer() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getURL(AccountType accountType) {
		if (accountType.equals(AccountType.LIVE)) {
			return "http://platform.dukascopy.com/live/jforex.jnlp";
		}
		if (accountType.equals(AccountType.DEMO)) {
			return "https://www.dukascopy.com/client/demo/jclient/jforex.jnlp";
		}
		throw new IllegalArgumentException("Invalid account type " + accountType);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return "Dukascopy";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getId() {
		return "dkcp";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getTitle() {
		return "Dukascopy Bank SA";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Instrument> getAvailableInstruments() throws ServerException {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConnectionManager getConnectionManager() throws ServerException {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OrderManager getOrderManager() throws ServerException {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HistoryManager getHistoryManager() throws ServerException {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FeedManager getFeedManager() throws ServerException {
		return null;
	}
}
