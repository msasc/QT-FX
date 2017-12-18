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
package com.qtfx.lib.mkt.servers.dukascopy;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dukascopy.api.system.IClient;
import com.qtfx.lib.mkt.server.AccountType;
import com.qtfx.lib.mkt.server.ConnectionManager;
import com.qtfx.lib.mkt.server.ServerException;

/**
 * Dukascopy connection manager implementation.
 * 
 * @author Miquel Sas
 */
public class DkConnectionManager extends ConnectionManager {

	/** Logger instance. */
	private static final Logger LOGGER = LogManager.getLogger();

	/** Dukascopy server reference. */
	private DkServer server;
	/** The account/connection type. */
	private AccountType accountType;

	/**
	 * Constructor assigning the reference server.
	 * 
	 * @param server The Dukascopy server.
	 */
	public DkConnectionManager(DkServer server) {
		super();
		this.server = server;
	}
	
	/**
	 * Access to the IClient.
	 * @return The IClient interface.
	 */
	private IClient getClient() {
		return server.core.client;
	}

	/**
	 * Connect to the server, using the given string and password, for the given connection type.
	 * <p>
	 * It is the responsibility of the server implementation to ask for any additional information to connect, like for
	 * instance a PIN code.
	 * 
	 * @param username The user name.
	 * @param password The password.
	 * @param accountType The type of connection.
	 * @throws ServerException If a server error occurs.
	 */
	@Override
	public void connect(String username, String password, AccountType accountType) throws ServerException {

		// Check if already connected.
		if (isConnected()) {
			throw new ServerException("Server is already connected, please disconnect first.");
		}

		// Remember the connection type.
		this.accountType = accountType;

		// Set the corresponding URL.
		String url = null;
		if (accountType.equals(AccountType.DEMO)) {
			url = "https://www.dukascopy.com/client/demo/jclient/jforex.jnlp";
		}

		// Do connect.
		try {
			getClient().connect(url, username, password);

			// Wait for connection.
			int i = 10; // wait max ten seconds
			while (i > 0 && !getClient().isConnected()) {
				LOGGER.info("i=" + i);
				Thread.sleep(1000);
				i--;
			}
			if (!getClient().isConnected()) {
				LOGGER.error("Failed to connect Dukascopy servers");
//				System.exit(1);
			}
		} catch (Exception cause) {
			throw new ServerException(cause);
		}

		// Install the strategy listener.
		getClient().startStrategy(server.core.strategyListener);
	}

	/**
	 * Disconnect from the server.
	 * 
	 * @throws ServerException If a server error occurs.
	 */
	@Override
	public void disconnect() throws ServerException {
		getClient().disconnect();
	}

	/**
	 * Returns the connection type of the connection or null if not connected.
	 * 
	 * @return The connection type.
	 */
	@Override
	public AccountType getAccountType() {
		return accountType;
	}

	/**
	 * Returns a boolean indicating if the client is correctly connected to the server.
	 * 
	 * @return A boolean indicating if the client is correctly connected to the server.
	 */
	@Override
	public boolean isConnected() {
		return getClient().isConnected();
	}

	/**
	 * Tries to reconnect to the server using the current client information.
	 * 
	 * @throws ServerException If a server error occurs.
	 */
	@Override
	public void reconnect() throws ServerException {
		try {
			getClient().reconnect();
		} catch (Exception cause) {
			throw new ServerException(cause);
		}
	}
}
