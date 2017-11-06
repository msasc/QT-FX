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

package com.qtfx.platform.action;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qtfx.library.gui.Alert;
import com.qtfx.library.mkt.server.Server;
import com.qtfx.library.mkt.server.ServerFactory;
import com.qtfx.library.util.TextServer;
import com.qtfx.platform.QTPlatform;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;

/**
 * Exit the application.
 *
 * @author Miquel Sas
 */
public class ActionExitApplication  implements EventHandler<ActionEvent> {

	/** Logger instance. */
	private static final Logger LOGGER = LogManager.getLogger();
	/** Status id. */
	private static final String statusId = "exit";

	/**
	 * Constructor.
	 */
	public ActionExitApplication() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handle(ActionEvent event) {
		
		// Do ask.
		Stage stage = QTPlatform.getPrimaryStage();
		String title = TextServer.getString("tokenExit");
		String message = TextServer.getString("alertExitApp");
		if (Alert.confirm(stage, title, message) == Alert.CANCEL) {
			return;
		}
		
		try {
			QTPlatform.getStatusBar().setLabel(statusId, "Disconnecting servers");
			List<Server> servers = ServerFactory.getSupportedServers();
			for (Server server : servers) {
				if (server.getConnectionManager().isConnected()) {
					QTPlatform.getStatusBar().setLabel(statusId, "Disconnecting from server " + server.getName());
					server.getConnectionManager().disconnect();
				}
			}
		} catch (Exception exc) {
			LOGGER.catching(exc);
		}
		
		QTPlatform.getPrimaryStage().close();
		System.exit(0);
	}
}
