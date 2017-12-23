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

package com.qtfx.plaf;

import com.qtfx.lib.mkt.server.AccountType;
import com.qtfx.lib.mkt.server.Server;
import com.qtfx.lib.mkt.server.ServerException;

/**
 * Server connector utility.
 *
 * @author Miquel Sas
 */
public class ServerConnector {

	public static void connect(Server server) throws ServerException {
		if (!server.getConnectionManager().isConnected()) {
			server.getConnectionManager().connect("msasc", "C1a2r3l4a5", AccountType.DEMO);
		}
	}
	
	public static void disconnect(Server server) throws ServerException {
		server.getConnectionManager().disconnect();
	}
}
