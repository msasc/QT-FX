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

package com.qtfx.plaf.action;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qtfx.lib.db.Criteria;
import com.qtfx.lib.db.Record;
import com.qtfx.lib.gui.StatusBar;
import com.qtfx.lib.gui.action.handlers.ActionEventHandler;
import com.qtfx.lib.mkt.data.Instrument;
import com.qtfx.lib.mkt.server.Server;
import com.qtfx.plaf.QTFX;
import com.qtfx.plaf.ServerConnector;
import com.qtfx.plaf.db.Database;

import javafx.event.ActionEvent;
import javafx.scene.Node;

/**
 * Show available instruments.
 *
 * @author Miquel Sas
 */
public class ActionInstrumentSynchronize extends ActionEventHandler {

	/** Logger instance. */
	private static final Logger LOGGER = LogManager.getLogger();

	/**
	 * Runnable to launch it in a thread.
	 */
	class RunAction implements Runnable {
		@Override
		public void run() {
			String id = "sync";
			StatusBar statusBar = QTFX.getStatusBar(getNode());
			try {
				Server server = QTFX.getServer(getNode());
				statusBar.setStatus(id, "Connecting to server " + server.getName(), 1, 5);
				ServerConnector.connect(server);

				statusBar.setStatus(id, "Retrieving available instruments", 2, 5);
				List<Instrument> instruments = server.getAvailableInstruments();

				statusBar.setStatus(id, "Deleting registered instruments", 3, 5);
				Database db = QTFX.getDatabase(getNode());
				db.getPersistor_Instruments().delete((Criteria) null);

				statusBar.setStatus(id, "Inserting available instruments", 4, 5);
				for (Instrument instrument : instruments) {
					Record record = db.getRecord_Instrument(server, instrument);
					db.getPersistor_Instruments().insert(record);
				}

				statusBar.setStatus(id, "Disconnecting from server " + server.getName(), 5, 5);
				ServerConnector.disconnect(server);
			} catch (Exception exc) {
				LOGGER.catching(exc);
			} finally {
				statusBar.removeStatus(id);
			}
		}
	}

	/**
	 * Constructor.
	 * 
	 * @param node The reference node.
	 */
	public ActionInstrumentSynchronize(Node node) {
		super(node);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handle(ActionEvent event) {
		new Thread(new RunAction()).start();
	}
}
