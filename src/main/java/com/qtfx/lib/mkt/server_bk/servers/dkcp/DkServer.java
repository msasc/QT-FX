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

package com.qtfx.lib.mkt.server_bk.servers.dkcp;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.dukascopy.api.IContext;
import com.dukascopy.api.system.ClientFactory;
import com.dukascopy.api.system.IClient;
import com.qtfx.lib.mkt.data.Instrument;
import com.qtfx.lib.mkt.server_bk.HistoryManager;
import com.qtfx.lib.mkt.server_bk.OrderManager;
import com.qtfx.lib.mkt.server_bk.Server;
import com.qtfx.lib.mkt.server_bk.ok.AccountType;
import com.qtfx.lib.mkt.server_bk.ok.ConnectionManager;
import com.qtfx.lib.mkt.server_bk.ok.ServerException;

/**
 * Dukascopy server implementation.
 * 
 * @author Miquel Sas
 */
public class DkServer extends Server {

	/////////////////////
	// Dukascopy objects.

	/** Client interface. */
	private IClient client;
	/** System listener. */
	private DkSystemListener systemListener;
	/** The strategy listener to get access to the history, the data service, the engine and the account. */
	private DkStrategyListener strategyListener;
	/** Subscribed instruments. */
	private Set<Instrument> subscribedInstruments = new HashSet<>();
	
	///////////////
	// MKT objects.
	
	/** Instance of the connection manager. */
	private DkConnectionManager connectionManager;

	/**
	 * Constructor.
	 */
	public DkServer() throws ServerException {
		super();

		// Name, id, title.
		setName("Dukascopy");
		setId("dkcp");
		setTitle("Dukascopy Bank SA");

		try {

			// Initialize the Dukascopy client.
			client = ClientFactory.getDefaultInstance();

			// Initialize and set the system listener.
			systemListener = new DkSystemListener(this);
			client.setSystemListener(systemListener);

			// Initialize the context strategy.
			strategyListener = new DkStrategyListener(this);

		} catch (ClassNotFoundException | IllegalAccessException | InstantiationException cause) {
			throw new ServerException(cause);
		}
	}
	
	public void checkSubscribed(Instrument instrument) throws ServerException {
		if (!getConnectionManager().isConnected()) {
			return;
		}
		if (!subscribedInstruments.contains(instrument)) {
			subscribedInstruments.add(instrument);
			DkConverter.setSubscribedIntruments(getClient(), subscribedInstruments);
		}		
	}

	/**
	 * Returns the Dukascopy reference to the <em>IClient</em> interface.
	 * 
	 * @return the client
	 */
	IClient getClient() {
		return client;
	}

	/**
	 * Return the Dukascopy system listener.
	 * 
	 * @return The system listener.
	 */
	DkSystemListener getSystemListener() {
		return systemListener;
	}

	/**
	 * Return the Dukascopy strategy listener.
	 * 
	 * @return The strategy listener.
	 */
	DkStrategyListener getStrategyListener() {
		return strategyListener;
	}
	
	/**
	 * Returns the reference to the context.
	 * 
	 * @return The context.
	 */
	IContext getContext() {
		return strategyListener.getContext();
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
	public List<Instrument> getAvailableInstruments() throws ServerException {
		return DkConverter.getAvailableInstruments(getClient());
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

}
