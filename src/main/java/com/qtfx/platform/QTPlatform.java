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

package com.qtfx.platform;

import java.io.File;
import java.util.List;
import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qtfx.library.db.Persistor;
import com.qtfx.library.db.PersistorDDL;
import com.qtfx.library.db.Record;
import com.qtfx.library.db.RecordSet;
import com.qtfx.library.db.Table;
import com.qtfx.library.db.rdbms.DBEngine;
import com.qtfx.library.db.rdbms.DBEngineAdapter;
import com.qtfx.library.db.rdbms.DataSourceInfo;
import com.qtfx.library.db.rdbms.adapters.PostgreSQLAdapter;
import com.qtfx.library.gui.Alert;
import com.qtfx.library.gui.StatusBar;
import com.qtfx.library.gui.launch.Argument;
import com.qtfx.library.gui.launch.ArgumentManager;
import com.qtfx.library.mkt.data.Filter;
import com.qtfx.library.mkt.data.Period;
import com.qtfx.library.mkt.server.OfferSide;
import com.qtfx.library.mkt.server.Server;
import com.qtfx.library.mkt.server.ServerException;
import com.qtfx.library.mkt.server.ServerFactory;
import com.qtfx.library.util.TextServer;
import com.qtfx.platform.action.ActionAvailableInstruments;
import com.qtfx.platform.action.ActionExitApplication;
import com.qtfx.platform.action.ActionSynchronizeInstruments;
import com.qtfx.platform.db.Fields;
import com.qtfx.platform.db.Schemas;
import com.qtfx.platform.db.Tables;
import com.qtfx.platform.db.tables.TableDataFilters;
import com.qtfx.platform.db.tables.TableInstruments;
import com.qtfx.platform.db.tables.TableOfferSides;
import com.qtfx.platform.db.tables.TablePeriods;
import com.qtfx.platform.db.tables.TableServers;
import com.qtfx.platform.db.tables.TableStatistics;
import com.qtfx.platform.db.tables.TableTickers;
import com.qtfx.platform.util.PersistorUtils;
import com.qtfx.platform.util.RecordUtils;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * QT-Platform (JavaFX) entry.
 * 
 * @author Miquel Sas
 */
public class QTPlatform extends Application {

	/** Logger configuration and text server initialization. */
	static {
		System.setProperty("log4j.configurationFile", "resources/LoggerQTPlatform.xml");
		TextServer.addBaseResource("resources/StringsLibrary.xml");
		TextServer.addBaseResource("resources/StringsQTPlatform.xml");
	}
	/** Logger instance. */
	private static final Logger LOGGER = LogManager.getLogger();

	/** Platform primary stage. */
	private static Stage primaryStage;
	/** Platform default status bar. */
	private static StatusBar statusBar;

	/**
	 * Return the platform primary stage.
	 * 
	 * @return The primary stage.
	 */
	public static Stage getPrimaryStage() {
		return primaryStage;
	}

	/**
	 * Return the default platform status bar.
	 * 
	 * @return The default platform status bar.
	 */
	public static StatusBar getStatusBar() {
		return statusBar;
	}

	/**
	 * Start and launch the application.
	 * 
	 * @param args Startup arguments.
	 */
	public static void main(String[] args) {
		launch(args);
	}

	/** Argument manager. */
	private ArgumentManager argumentManager;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {

		// Register the primary state.
		QTPlatform.primaryStage = primaryStage;

		// Default locale.
		Locale.setDefault(Locale.UK);

		// Validate command line arguments.
		if (!validateArguments(primaryStage)) {
			System.exit(0);
		}

		// Root.
		BorderPane root = new BorderPane();

		// A bottom status bar.
		statusBar = new StatusBar();
		statusBar.setId("status-bar");
		root.setBottom(statusBar);

		try {
			// Ensure database.
			LOGGER.info("Database checking...");
			configureDatabase(getArgumentManager().getValue("dataSourceFile"));
			LOGGER.info("Database checked");

			// Configure and set the menu.
			LOGGER.info("Configuring menu...");
			MenuBar menuBar = configureMenu(primaryStage);
			root.setTop(menuBar);

		} catch (Exception exc) {
			LOGGER.catching(exc);
			System.exit(0);
		}

		// Scene.
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);

		// Factor dimensions
		primaryStage.setWidth(Screen.getPrimary().getBounds().getWidth() * 0.6);
		primaryStage.setHeight(Screen.getPrimary().getBounds().getHeight() * 0.6);
		primaryStage.centerOnScreen();

		// Full screen mode -> Ctrl-F11.
		primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
			if (e.getCode() == KeyCode.F11) {
				if (e.isControlDown()) {
					primaryStage.setFullScreen(true);
				}
			}
		});

		primaryStage.show();
	}

	/**
	 * Configure the menu bar.
	 * 
	 * @return The menu bar.
	 * @throws ServerException If a server exception occurs.
	 */
	private MenuBar configureMenu(Stage primaryStage) throws ServerException {
		MenuBar menuBar = new MenuBar();

		Menu itemFile = new Menu(TextServer.getString("menuFile"));
		MenuItem itemExit = new MenuItem(TextServer.getString("menuExit"));
		itemExit.setOnAction(e -> {
			new ActionExitApplication().handle(e);
		});
		itemFile.getItems().add(itemExit);

		Menu itemServers = new Menu(TextServer.getString("menuServers"));

		// One menu item for each supported server.
		List<Server> servers = ServerFactory.getSupportedServers();
		for (Server server : servers) {
			String name = server.getName();

			Menu itemServer = new Menu(name);

			MenuItem itemSync = new MenuItem(TextServer.getString("menuSyncInstruments"));
			itemSync.setOnAction(e -> {
				new ActionSynchronizeInstruments(server).handle(e);
			});
			itemServer.getItems().add(itemSync);
			
			MenuItem itemAvInst = new MenuItem(TextServer.getString("menuServersAvInst"));
			itemAvInst.setOnAction(e -> {
				new ActionAvailableInstruments(server).handle(e);
			});
			itemServer.getItems().add(itemAvInst);

			itemServers.getItems().add(itemServer);
		}

		menuBar.getMenus().add(itemFile);
		menuBar.getMenus().add(itemServers);
		return menuBar;
	}

	/**
	 * Validate the command line arguments and show the possible error.
	 * 
	 * @param stage The stage.
	 * @return A boolean.
	 */
	private boolean validateArguments(Stage stage) {
		if (!getArgumentManager().parse(getParameters())) {
			Alert alert = new Alert(stage);
			alert.setTitle("Argument errors");
			alert.setType(Alert.Type.ERROR);
			for (String error : getArgumentManager().getErrors()) {
				alert.addText(error + "\n");
			}
			alert.show();
			return false;
		}
		return true;
	}

	/**
	 * Returns the argument manager setup for this application.
	 * 
	 * @return The argument manager.
	 */
	private ArgumentManager getArgumentManager() {
		if (argumentManager == null) {
			argumentManager = new ArgumentManager();
			Argument argConnection = new Argument("dataSourceFile", "Database connection file", true, true, false);
			argumentManager.add(argConnection);
		}
		return argumentManager;
	}

	/**
	 * Ensure the database connection and required schemas.
	 * <ul>
	 * <li>QT-Platform system schema <tt>QTP</tt></li>
	 * <li>One schema for each supported server, for instance <tt>QTP_DKCP</tt></li>
	 * </ul>
	 *
	 * @param connectionFile The connection file name.
	 * @throws Exception If any error occurs.
	 */
	private void configureDatabase(String connectionFile) throws Exception {

		// Connection file.
		File cnFile = new File(connectionFile);

		// Data source info and db engine.
		DataSourceInfo info = DataSourceInfo.getDataSourceInfo(cnFile);
		DBEngineAdapter adapter = new PostgreSQLAdapter();
		DBEngine dbEngine = new DBEngine(adapter, info);
		PersistorUtils.setDBEngine(dbEngine);

		// Persistor DDL.
		PersistorDDL ddl = PersistorUtils.getDDL();

		// Check for the system schema.
		if (!ddl.existsSchema(Schemas.qtp)) {
			ddl.createSchema(Schemas.qtp);
		}

		// Check for supported servers schemas.
		List<Server> servers = ServerFactory.getSupportedServers();
		for (Server server : servers) {
			String schema = Schemas.server(server);
			if (!ddl.existsSchema(schema)) {
				ddl.createSchema(schema);
			}
		}

		// Check for the necessary table KeyServer in the system schema.
		if (!ddl.existsTable(Schemas.qtp, Tables.Servers)) {
			ddl.buildTable(new TableServers());
		}
		synchronizeSupportedServer();

		// Check for the necessary table Periods in the system schema.
		if (!ddl.existsTable(Schemas.qtp, Tables.Periods)) {
			ddl.buildTable(new TablePeriods());
		}
		synchronizeStandardPeriods();

		// Check for the necessary table OfferSides in the system schema.
		if (!ddl.existsTable(Schemas.qtp, Tables.OfferSides)) {
			ddl.buildTable(new TableOfferSides());
		}
		synchronizeStandardOfferSides(dbEngine);

		// Check for the necessary table DataFilters in the system schema.
		if (!ddl.existsTable(Schemas.qtp, Tables.DataFilters)) {
			ddl.buildTable(new TableDataFilters());
		}
		synchronizeStandardDataFilters();

		// Check for the necessary table Instruments in the system schema.
		if (!ddl.existsTable(Schemas.qtp, Tables.Instruments)) {
			ddl.buildTable(new TableInstruments());
		}

		// Check for the necessary table Tickers in the system schema.
		if (!ddl.existsTable(Schemas.qtp, Tables.Tickers)) {
			ddl.buildTable(new TableTickers());
		}

		// Check for the necessary table Statistics in the system schema.
		if (!ddl.existsTable(Schemas.qtp, Tables.Statistics)) {
			ddl.buildTable(new TableStatistics());
		}
	}

	/**
	 * Synchronize standard periods.
	 * 
	 * @throws Exception If any error occurs.
	 */
	private void synchronizeStandardPeriods() throws Exception {
		List<Period> periods = Period.getStandardPeriods();
		Persistor persistor = PersistorUtils.getPersistorPeriods();
		for (Period period : periods) {
			Record record = RecordUtils.getRecordPeriod(persistor.getDefaultRecord(), period);
			if (!persistor.exists(record)) {
				persistor.insert(record);
			}
		}
	}

	/**
	 * Synchronize standard offer sides.
	 * 
	 * @param dbEngine The database engine.
	 * @throws Exception If any error occurs.
	 */
	private void synchronizeStandardOfferSides(DBEngine dbEngine) throws Exception {
		OfferSide[] offerSides = OfferSide.values();
		Table table = new TableOfferSides();
		for (OfferSide offerSide : offerSides) {
			Record record = RecordUtils.getRecordOfferSide(table.getDefaultRecord(), offerSide);
			if (!dbEngine.existsRecord(table, record)) {
				dbEngine.executeInsert(table, record);
			}
		}
	}

	/**
	 * Synchronize standard data filters.
	 * 
	 * @throws Exception If any error occurs.
	 */
	private void synchronizeStandardDataFilters() throws Exception {
		Filter[] dataFilters = Filter.values();
		Persistor persistor = PersistorUtils.getPersistorDataFilters();
		for (Filter dataFilter : dataFilters) {
			Record record = RecordUtils.getRecordDataFilter(persistor.getDefaultRecord(), dataFilter);
			if (!persistor.exists(record)) {
				persistor.insert(record);
			}
		}
	}

	/**
	 * Synchronize supported servers.
	 * 
	 * @throws Exception If any error occurs.
	 */
	private void synchronizeSupportedServer() throws Exception {
		List<Server> servers = ServerFactory.getSupportedServers();
		Persistor persistor = PersistorUtils.getPersistorServers();
		RecordSet recordSet = persistor.select(null);

		// Remove not supported servers.
		for (int i = 0; i < recordSet.size(); i++) {
			Record record = recordSet.get(i);
			boolean remove = true;
			for (Server server : servers) {
				if (server.getId().toLowerCase().equals(record.getValue(Fields.SERVER_ID).toString().toLowerCase())) {
					remove = false;
					break;
				}
			}
			if (remove) {
				persistor.delete(record);
			}
		}

		// Add add non-existing supported servers.
		for (Server server : servers) {
			String id = server.getId().toLowerCase();
			boolean included = false;
			for (int i = 0; i < recordSet.size(); i++) {
				Record record = recordSet.get(i);
				if (record.getValue(Fields.SERVER_ID).toString().toLowerCase().equals(id)) {
					included = true;
					break;
				}
			}
			if (!included) {
				persistor.insert(RecordUtils.getRecordServer(persistor.getDefaultRecord(), server));
			}
		}
	}

}
