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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qtfx.lib.app.Session;
import com.qtfx.lib.db.Persistor;
import com.qtfx.lib.db.PersistorException;
import com.qtfx.lib.db.Record;
import com.qtfx.lib.db.RecordSet;
import com.qtfx.lib.gui.TableRecordPane;
import com.qtfx.lib.gui.action.handlers.ActionEventHandler;
import com.qtfx.lib.mkt.server.Server;
import com.qtfx.plaf.QTFX;
import com.qtfx.plaf.db.Fields;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

/**
 * Show available instruments.
 *
 * @author Miquel Sas
 */
public class ActionInstrumentBrowse extends ActionEventHandler {
	
	/** Logger instance. */
	private static final Logger LOGGER = LogManager.getLogger();
	
	/**
	 * Runnable to launch it in a thread.
	 */
	class RunAction implements Runnable {
		@Override
		public void run() {
			try {
				Server server = QTFX.getServer(getNode());
				Persistor persistor = QTFX.getDatabase(getNode()).getPersistor_Instruments();
				RecordSet recordSet = QTFX.getDatabase(getNode()).getRecordSet_AvailableInstruments(server);
				Record masterRecord = persistor.getDefaultRecord();
				Platform.runLater(() -> {
					TableRecordPane table = new TableRecordPane(masterRecord);
					table.addColumn(Fields.INSTRUMENT_ID);
					table.addColumn(Fields.INSTRUMENT_DESC);
					table.addColumn(Fields.INSTRUMENT_PIP_VALUE);
					table.addColumn(Fields.INSTRUMENT_PIP_SCALE);
					table.addColumn(Fields.INSTRUMENT_TICK_VALUE);
					table.addColumn(Fields.INSTRUMENT_TICK_SCALE);
					table.addColumn(Fields.INSTRUMENT_VOLUME_SCALE);
					table.addColumn(Fields.INSTRUMENT_PRIMARY_CURRENCY);
					table.addColumn(Fields.INSTRUMENT_SECONDARY_CURRENCY);
					table.setPadding(new Insets(10, 10, 0, 10));
					table.setRecordSet(recordSet);
					
					Tab tab = new Tab();
					tab.setText(Session.getSession().getString("menuInstrumentsAvailable"));
					tab.setContent(table.getNode());
					
					TabPane tabPane = QTFX.getTabPane(getNode());
					tabPane.getTabs().add(tab);
					tabPane.getSelectionModel().select(tab);
				});
			} catch (PersistorException exc) {
				LOGGER.catching(exc);
			}
		}
	}

	/**
	 * Constructor.
	 * 
	 * @param node The reference node.
	 */
	public ActionInstrumentBrowse(Node node) {
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
