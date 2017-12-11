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
package com.qtfx.lib.mkt.server_bk;

import java.util.List;

import com.qtfx.lib.mkt.data.Instrument;
import com.qtfx.lib.mkt.server_bk.ok.AccountType;
import com.qtfx.lib.mkt.server_bk.ok.ConnectionManager;
import com.qtfx.lib.mkt.server_bk.ok.ServerException;

/**
 * Interface responsible to provide access to all the server services.
 * 
 * @author Miquel Sas
 */
public abstract class Server {

	/** Server name. */
	private String name;
	/** Server id. */
	private String id;
	/** Server title. */
	private String title;
	
	/** Feed dispatcher to receive and forward feed events. */
	private FeedDispatcher dispatcher = new FeedDispatcher();

	/**
	 * Returns the URL to connect to the given account type.
	 * 
	 * @param accountType The account type (Live/Demo)
	 * @return The URL.
	 */
	public abstract String getURL(AccountType accountType);

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

	/**
	 * Returns a list with all available instruments.
	 * 
	 * @return A list with all available instruments.
	 * @throws ServerException If a server error occurs.
	 */
	public abstract List<Instrument> getAvailableInstruments() throws ServerException;

	/**
	 * Returns the connection manager associated to this server.
	 * 
	 * @return The connection manager.
	 * @throws ServerException If a server error occurs.
	 */
	public abstract ConnectionManager getConnectionManager() throws ServerException;

	/**
	 * Returns the order manager associated to this server.
	 * 
	 * @return The order manager.
	 * @throws ServerException If a server error occurs.
	 */
	public abstract OrderManager getOrderManager() throws ServerException;

	/**
	 * Returns the history manager associated to this server.
	 * 
	 * @return The history manager.
	 * @throws ServerException If a server error occurs.
	 */
	public abstract HistoryManager getHistoryManager() throws ServerException;

	/**
	 * Returns the feed dispatcher to receive and forward live feed events.
	 * 
	 * @return The feed dispatcher.
	 * @throws ServerException If a server error occurs.
	 */
	public FeedDispatcher getFeedDispatcher() {
		return dispatcher;
	}
}
