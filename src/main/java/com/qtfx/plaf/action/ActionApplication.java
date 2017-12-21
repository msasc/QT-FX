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

package com.qtfx.plaf.action;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qtfx.lib.gui.Alert;
import com.qtfx.lib.gui.StatusBar;
import com.qtfx.lib.gui.action.handlers.ActionEventHandler;
import com.qtfx.lib.gui.action.handlers.WindowEventHandler;
import com.qtfx.lib.mkt.server.Server;
import com.qtfx.lib.util.TextServer;
import com.qtfx.plaf.QTFX;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

/**
 * Exit the application.
 *
 * @author Miquel Sas
 */
public class ActionApplication {

	/** Logger instance. */
	private static final Logger LOGGER = LogManager.getLogger();
	/** Status id. */
	private static final String statusId = "exit";

	/**
	 * Exit from menu.
	 */
	public static class ExitFromMenu extends ActionEventHandler {
		/**
		 * Constructor.
		 * 
		 * @param node The node.
		 */
		public ExitFromMenu(Node node) {
			super(node);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void handle(ActionEvent event) {
			if (!queryExit(getNode())) {
				return;
			}
			disconnectAndClose(getNode());
		}
	}

	/**
	 * Exit from window.
	 */
	public static class ExitFromWindow extends WindowEventHandler {
		/**
		 * Constructor.
		 * 
		 * @param node The node.
		 */
		public ExitFromWindow(Node node) {
			super(node);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void handle(WindowEvent event) {
			if (!queryExit(getNode())) {
				event.consume();
				return;
			}
			disconnectAndClose(getNode());
		}
	}

	/**
	 * Disconnect and close.
	 */
	private static void disconnectAndClose(Node node) {
		try {
			Server server = QTFX.getServer(node);
			StatusBar statusBar = QTFX.getStatusBar(node);
			statusBar.setLabel(statusId, "Disconnecting from server");
			if (server.getConnectionManager().isConnected()) {
				statusBar.setLabel(statusId, "Disconnecting from server " + server.getName());
				server.getConnectionManager().disconnect();
			}
		} catch (Exception exc) {
			LOGGER.catching(exc);
		}

		Window window = node.getScene().getWindow();
		window.hide();
		System.exit(0);
	}

	/**
	 * Ask whether to exit.
	 * 
	 * @return A boolean.
	 */
	private static boolean queryExit(Node node) {
		Window window = node.getScene().getWindow();
		String title = TextServer.getString("tokenExit");
		String message = TextServer.getString("alertExitApp");
		if (Alert.confirm(window, title, message) == Alert.CANCEL) {
			return false;
		}
		return true;
	}

	/**
	 * Not instantiable.
	 */
	private ActionApplication() {
	}
}
