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

import com.qtfx.lib.db.Persistor;
import com.qtfx.lib.db.PersistorException;
import com.qtfx.lib.db.Record;
import com.qtfx.lib.db.RecordSet;
import com.qtfx.lib.db.Table;
import com.qtfx.lib.gui.Alert;
import com.qtfx.lib.gui.TableRecordPane;
import com.qtfx.lib.gui.action.handlers.ActionEventHandler;
import com.qtfx.lib.mkt.data.DataPersistor;
import com.qtfx.lib.mkt.data.DataRecordSet;
import com.qtfx.lib.mkt.data.Instrument;
import com.qtfx.lib.mkt.data.Period;
import com.qtfx.lib.mkt.server.Server;
import com.qtfx.lib.util.TextServer;
import com.qtfx.plaf.QTFX;
import com.qtfx.plaf.db.Database;
import com.qtfx.plaf.db.Fields;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

/**
 * Show available instruments.
 *
 * @author Miquel Sas
 */
public class ActionInstrumentTickers extends ActionEventHandler {

	/** Logger instance. */
	private static final Logger LOGGER = LogManager.getLogger();
	
	/**
	 * Create a ticker.
	 */
	class Create extends ActionEventHandler {

		public Create(Node node) {
			super(node);
		}

		@Override
		public void handle(ActionEvent event) {
			try {
				Stage stage = QTFX.getPrimaryStage(getNode());
				Server server = QTFX.getServer(getNode());
				Database db = QTFX.getDatabase(getNode());
				
				Record rcInst = db.lookupInstrument(getNode());
				if (rcInst == null) {
					return;
				}
				Instrument instrument = db.fromRecordToInstrument(rcInst);
				
				Record rcPeriod = db.lookupPeriod(getNode());
				if (rcPeriod == null) {
					return;
				}
				Period period = db.fromRecordToPeriod(rcPeriod);
				
				Persistor persistor = db.getPersistor_Tickers();
				Record rcTicker = db.getRecord_Ticker(server, instrument, period);
				if (persistor.exists(rcTicker)) {
					Alert.warning(stage, null, "Ticker already exists");
					return;
				}
				
				// Create the table.
				Table tableDataPrice = db.getTable_DataPrice(server, instrument, period);
				if (!db.getDDL().existsTable(tableDataPrice)) {
					db.getDDL().buildTable(tableDataPrice);
				}
				
				// Create the record.
				persistor.insert(rcTicker);
				persistor.refresh(rcTicker);
				
				// Add to the record set.
				table.getRecords().add(table.getInsertIndex(rcTicker), rcTicker);
				table.selectRecord(rcTicker);
				
			} catch (Exception exc) {
				LOGGER.catching(exc);
			}
		}
	}

	/**
	 * Delete a ticker.
	 */
	class Delete extends ActionEventHandler {

		public Delete(Node node) {
			super(node);
		}

		@Override
		public void handle(ActionEvent event) {
			try {
				if (table.getSelectedRecords().isEmpty()) {
					return;
				}
				Server server = QTFX.getServer(getNode());
				Database db = QTFX.getDatabase(getNode());
				Record selected = table.getSelectedRecords().get(0);
				if (Alert.confirm("Confirm delete", "Delete the selected ticker").equals(Alert.CANCEL)) {
					return;
				}
				String instrumentId = selected.getValue(Fields.INSTRUMENT_ID).getString();
				Instrument instrument = db.fromRecordToInstrument(db.getRecord_Instrument(server, instrumentId));
				String periodId = selected.getValue(Fields.PERIOD_ID).getString();
				Period period = Period.parseId(periodId);
				
				Persistor persistor = db.getPersistor_Tickers();
				persistor.delete(selected);
				
				// Delete the table.
				Table tableDataPrice = db.getTable_DataPrice(server, instrument, period);
				db.getDDL().dropTable(tableDataPrice);
				
				// Add to the record set.
				table.getRecords().remove(selected);
				
			} catch (Exception exc) {
				LOGGER.catching(exc);
			}
		}
	}

	/**
	 * Browse a ticker.
	 */
	class Browse extends ActionEventHandler {

		public Browse(Node node) {
			super(node);
		}

		@Override
		public void handle(ActionEvent event) {
			try {
				if (table.getSelectedRecords().isEmpty()) {
					return;
				}
				Server server = QTFX.getServer(getNode());
				Database db = QTFX.getDatabase(getNode());
				Record selected = table.getSelectedRecords().get(0);
				String instrumentId = selected.getValue(Fields.INSTRUMENT_ID).getString();
				Instrument instrument = db.fromRecordToInstrument(db.getRecord_Instrument(server, instrumentId));
				String periodId = selected.getValue(Fields.PERIOD_ID).getString();
				Period period = Period.parseId(periodId);
				
				Persistor persistor = db.getPersistor_DataPrice(server, instrument, period);
				DataPersistor dataPersistor = new DataPersistor(persistor);
				
				TableRecordPane tableData = new TableRecordPane(dataPersistor.getDefaultRecord());
				tableData.addColumn(Fields.INDEX);
//				tableData.addColumn(Fields.TIME);
				tableData.addColumn(Fields.TIME_FMT);
				tableData.addColumn(Fields.OPEN);
				tableData.addColumn(Fields.HIGH);
				tableData.addColumn(Fields.LOW);
				tableData.addColumn(Fields.CLOSE);
				tableData.addColumn(Fields.VOLUME);
				tableData.setRecordSet(new DataRecordSet(dataPersistor));

				Tab tab = new Tab();
				tab.setText("Browse data " + instrument.getDescription());
				tab.setContent(tableData.getNode());

				TabPane tabPane = QTFX.getTabPane(getNode());
				tabPane.getTabs().add(tab);
				tabPane.getSelectionModel().select(tab);
				
			} catch (Exception exc) {
				LOGGER.catching(exc);
			}
		}
	}

	/**
	 * Runnable to launch it in a thread.
	 */
	class RunAction implements Runnable {
		@Override
		public void run() {
			try {
				Server server = QTFX.getServer(getNode());
				Persistor persistor = QTFX.getDatabase(getNode()).getPersistor_Tickers();
				RecordSet recordSet = QTFX.getDatabase(getNode()).getRecordSet_Tickers(server);
				Record masterRecord = persistor.getDefaultRecord();

				Platform.runLater(() -> {
					table = new TableRecordPane(masterRecord);
					table.addColumn(Fields.INSTRUMENT_ID);
					table.addColumn(Fields.PERIOD_NAME);
					table.addColumn(Fields.TABLE_NAME);
					table.setPadding(new Insets(10, 10, 0, 10));
					table.setRecordSet(recordSet);
					table.getTableView().setContextMenu(getMenu());

					Tab tab = new Tab();
					tab.setText(TextServer.getString("menuTickersDefine"));
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
	
	/** The table pane. */
	private TableRecordPane table;

	/**
	 * Constructor.
	 * 
	 * @param node The reference node.
	 */
	public ActionInstrumentTickers(Node node) {
		super(node);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handle(ActionEvent event) {
		new Thread(new RunAction()).start();
	}

	/**
	 * Return the context menu of the table.
	 * 
	 * @return The context menu.
	 */
	private ContextMenu getMenu() {
		ContextMenu menu = new ContextMenu();
		MenuItem create = new MenuItem(TextServer.getString("buttonCreate"));
		create.setOnAction(e -> {
			new Create(getNode()).handle(e);
		});
		MenuItem delete = new MenuItem(TextServer.getString("buttonDelete"));
		delete.setOnAction(e -> {
			new Delete(getNode()).handle(e);
		});
		MenuItem browse = new MenuItem(TextServer.getString("buttonBrowse"));
		browse.setOnAction(e -> {
			new Browse(getNode()).handle(e);
		});
		MenuItem chart = new MenuItem(TextServer.getString("buttonChart"));
		MenuItem purge = new MenuItem(TextServer.getString("buttonPurge"));
		MenuItem download = new MenuItem(TextServer.getString("buttonDownload"));
		menu.getItems().add(create);
		menu.getItems().add(delete);
		menu.getItems().add(browse);
		menu.getItems().add(chart);
		menu.getItems().add(new SeparatorMenuItem());
		menu.getItems().add(purge);
		menu.getItems().add(download);
		
		menu.setOnShowing(e -> {
			delete.setDisable(!anyItemSelected());
			browse.setDisable(!anyItemSelected());
			chart.setDisable(!anyItemSelected());
			purge.setDisable(!anyItemSelected());
			download.setDisable(!anyItemSelected());
		});
		
		return menu;
	}

	/**
	 * Check if a row is selected.
	 * 
	 * @return A boolean.
	 */
	private boolean anyItemSelected() {
		return !table.getSelectedRecords().isEmpty();
	}
}
