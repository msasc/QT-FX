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

import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qtfx.lib.db.Criteria;
import com.qtfx.lib.db.Persistor;
import com.qtfx.lib.db.PersistorException;
import com.qtfx.lib.db.Record;
import com.qtfx.lib.db.RecordSet;
import com.qtfx.lib.db.Table;
import com.qtfx.lib.gui.Alert;
import com.qtfx.lib.gui.TableRecordPane;
import com.qtfx.lib.gui.TaskPane;
import com.qtfx.lib.gui.action.handlers.ActionEventHandler;
import com.qtfx.lib.mkt.chart.Chart;
import com.qtfx.lib.mkt.data.Data;
import com.qtfx.lib.mkt.data.DataListPersistor;
import com.qtfx.lib.mkt.data.DataPersistor;
import com.qtfx.lib.mkt.data.DataRecordSet;
import com.qtfx.lib.mkt.data.Filter;
import com.qtfx.lib.mkt.data.IndicatorDataList;
import com.qtfx.lib.mkt.data.IndicatorUtils;
import com.qtfx.lib.mkt.data.Instrument;
import com.qtfx.lib.mkt.data.OfferSide;
import com.qtfx.lib.mkt.data.Period;
import com.qtfx.lib.mkt.data.PlotData;
import com.qtfx.lib.mkt.data.PlotType;
import com.qtfx.lib.mkt.data.info.DataInfo;
import com.qtfx.lib.mkt.data.info.PriceInfo;
import com.qtfx.lib.mkt.server.Server;
import com.qtfx.lib.util.TextServer;
import com.qtfx.plaf.QTFX;
import com.qtfx.plaf.db.Database;
import com.qtfx.plaf.db.Fields;
import com.qtfx.plaf.task.TaskDownloadTicker;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.paint.Color;
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
	class ActionCreate extends ActionEventHandler {

		public ActionCreate(Node node) {
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
					Alert.warning(stage, null, TextServer.getString("alertTickerExists"));
					return;
				}

				// ActionCreate the table.
				Table tableDataPrice = db.getTable_DataPrice(server, instrument, period);
				if (!db.getDDL().existsTable(tableDataPrice)) {
					db.getDDL().buildTable(tableDataPrice);
				}

				// ActionCreate the record.
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
	class ActionDelete extends ActionEventHandler {

		public ActionDelete(Node node) {
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
				if (Alert.confirm(
					TextServer.getString("alertConfirmDeleteTitle"),
					TextServer.getString("alertConfirmDeleteText")).equals(Alert.CANCEL)) {
					return;
				}
				String instrumentId = selected.getValue(Fields.INSTRUMENT_ID).getString();
				Instrument instrument = db.fromRecordToInstrument(db.getRecord_Instrument(server, instrumentId));
				String periodId = selected.getValue(Fields.PERIOD_ID).getString();
				Period period = Period.parseId(periodId);

				Persistor persistor = db.getPersistor_Tickers();
				persistor.delete(selected);

				// ActionDelete the table.
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
	class ActionBrowse extends ActionEventHandler {

		public ActionBrowse(Node node) {
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
				tableData.addColumn(Fields.TIME_FMT);
				tableData.addColumn(Fields.OPEN);
				tableData.addColumn(Fields.HIGH);
				tableData.addColumn(Fields.LOW);
				tableData.addColumn(Fields.CLOSE);
				tableData.addColumn(Fields.VOLUME);
				tableData.setRecordSet(new DataRecordSet(dataPersistor));

				// Set a proper width of the index column.
				double width = tableData.getDefaultColumnWidth("999.999.999");
				tableData.setColumnPrefWidth(0, width);

				Tab tab = new Tab();
				tab.setText(
					TextServer.getString("tabList") + " " + instrument.getDescription() + ", " + period.toString());
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
	 * Purge a ticker.
	 */
	class ActionPurge extends ActionEventHandler {

		public ActionPurge(Node node) {
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
				if (Alert.confirm(
					TextServer.getString("alertConfirmPurgeTitle"),
					TextServer.getString("alertConfirmPurgeText")).equals(Alert.CANCEL)) {
					return;
				}
				String instrumentId = selected.getValue(Fields.INSTRUMENT_ID).getString();
				Instrument instrument = db.fromRecordToInstrument(db.getRecord_Instrument(server, instrumentId));
				String periodId = selected.getValue(Fields.PERIOD_ID).getString();
				Period period = Period.parseId(periodId);

				Persistor persistor = db.getPersistor_DataPrice(server, instrument, period);
				persistor.delete(new Criteria());

			} catch (Exception exc) {
				LOGGER.catching(exc);
			}
		}
	}

	/**
	 * Download a ticker.
	 */
	class ActionDownload extends ActionEventHandler {

		public ActionDownload(Node node) {
			super(node);
		}

		@Override
		public void handle(ActionEvent event) {
			try {
				Database db = QTFX.getDatabase(getNode());
				Server server = QTFX.getServer(getNode());
				Record selected = table.getSelectedRecords().get(0);
				String instrumentId = selected.getValue(Fields.INSTRUMENT_ID).getString();
				Instrument instrument = db.fromRecordToInstrument(db.getRecord_Instrument(server, instrumentId));
				String periodId = selected.getValue(Fields.PERIOD_ID).getString();
				Period period = Period.parseId(periodId);

				TaskDownloadTicker task = new TaskDownloadTicker(db, server, instrument, period, OfferSide.ASK,
					Filter.ALL_FLATS);

				String tabText = TextServer.getString("tabDownload");
				if (!QTFX.isTab(getNode(), tabText)) {
					TaskPane taskPane = new TaskPane();
					taskPane.addTask(task);

					Tab tab = new Tab();
					tab.setText(tabText);
					tab.setContent(taskPane.getNode());

					TabPane tabPane = QTFX.getTabPane(getNode());
					tabPane.getTabs().add(tab);
					tabPane.getSelectionModel().select(tab);
				} else {
					Tab tab = QTFX.getTab(getNode(), tabText);
					TaskPane taskPane = TaskPane.getTaskPane(tab.getContent());
					taskPane.addTask(task);

					TabPane tabPane = QTFX.getTabPane(getNode());
					tabPane.getSelectionModel().select(tab);
				}

			} catch (Exception exc) {
				LOGGER.catching(exc);
			}
		}
	}
	
	/**
	 * Chart a ticker.
	 */
	class ActionChart extends ActionEventHandler {

		public ActionChart(Node node) {
			super(node);
		}

		@Override
		public void handle(ActionEvent event) {
			try {
				if (table.getSelectedRecords().isEmpty()) {
					return;
				}
				Locale locale = QTFX.getLocale(getNode());
				Server server = QTFX.getServer(getNode());
				Database db = QTFX.getDatabase(getNode());
				Record selected = table.getSelectedRecords().get(0);
				String instrumentId = selected.getValue(Fields.INSTRUMENT_ID).getString();
				Instrument instrument = db.fromRecordToInstrument(db.getRecord_Instrument(server, instrumentId));
				String periodId = selected.getValue(Fields.PERIOD_ID).getString();
				Period period = Period.parseId(periodId);
				Persistor persistor = db.getPersistor_DataPrice(server, instrument, period);
				
				// Build the plot data.
				DataInfo infoPrice = new PriceInfo(locale, instrument, period);
				DataListPersistor price = new DataListPersistor(infoPrice, persistor);
				price.setPlotType(PlotType.CANDLESTICK);
				PlotData plotData = new PlotData();
				plotData.add(price);

				// By default in this view add two SMA of 50 and 200 periods.
				IndicatorDataList sma50 = 
					IndicatorUtils.getSmoothedWeightedMovingAverage(price, Data.CLOSE, Color.BLUE, 10, 5, 3, 3);
				IndicatorDataList sma200 = 
					IndicatorUtils.getSmoothedSimpleMovingAverage(price, Data.CLOSE, Color.BLACK, 200, 10, 5, 5);
				plotData.add(sma50);
				plotData.add(sma200);
				
				Chart chart = new Chart(locale);
				chart.addPlotData(plotData);
				
				Tab tab = new Tab();
				tab.setText(
					TextServer.getString("tabChart") + " " + instrument.getDescription() + ", " + period.toString());
				tab.setContent(chart.getPane());
				
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
			new ActionCreate(getNode()).handle(e);
		});
		MenuItem delete = new MenuItem(TextServer.getString("buttonDelete"));
		delete.setOnAction(e -> {
			new ActionDelete(getNode()).handle(e);
		});
		MenuItem browse = new MenuItem(TextServer.getString("buttonBrowse"));
		browse.setOnAction(e -> {
			new ActionBrowse(getNode()).handle(e);
		});
		MenuItem chart = new MenuItem(TextServer.getString("buttonChart"));
		chart.setOnAction(e -> {
			new ActionChart(getNode()).handle(e);
		});
		MenuItem purge = new MenuItem(TextServer.getString("buttonPurge"));
		purge.setOnAction(e -> {
			new ActionPurge(getNode()).handle(e);
		});
		MenuItem download = new MenuItem(TextServer.getString("buttonDownload"));
		download.setOnAction(e -> {
			new ActionDownload(getNode()).handle(e);
		});
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
