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

package com.qtfx.app.plaf;

import java.io.File;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qtfx.app.plaf.action.ActionApplication;
import com.qtfx.app.plaf.action.ActionInstrumentBrowse;
import com.qtfx.app.plaf.action.ActionInstrumentSynchronize;
import com.qtfx.app.plaf.action.ActionInstrumentTickers;
import com.qtfx.app.plaf.action.ActionTrainNetwork;
import com.qtfx.app.plaf.db.Database;
import com.qtfx.app.plaf.db.Fields;
import com.qtfx.lib.app.Session;
import com.qtfx.lib.db.Persistor;
import com.qtfx.lib.db.PersistorDDL;
import com.qtfx.lib.db.Record;
import com.qtfx.lib.db.Value;
import com.qtfx.lib.db.rdbms.DBEngine;
import com.qtfx.lib.db.rdbms.DBEngineAdapter;
import com.qtfx.lib.db.rdbms.DataSourceInfo;
import com.qtfx.lib.db.rdbms.adapters.PostgreSQLAdapter;
import com.qtfx.lib.gui.Alert;
import com.qtfx.lib.gui.Console;
import com.qtfx.lib.gui.FX;
import com.qtfx.lib.gui.StatusBar;
import com.qtfx.lib.gui.launch.Argument;
import com.qtfx.lib.gui.launch.ArgumentManager;
import com.qtfx.lib.mkt.data.Period;
import com.qtfx.lib.mkt.server.Server;
import com.qtfx.lib.mkt.servers.dukascopy.DkServer;
import com.qtfx.lib.ml.data.mnist.NumberImage;
import com.qtfx.lib.ml.network.Trainer;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * QTFX platform entry.
 * 
 * @author Miquel Sas
 */
public class QTFX extends Application {

	// Keys to public objects.

	private static final String PRIMARY_STAGE = "primary-stage";
	private static final String TAB_PANE = "tab-pane";
	private static final String STATUS_BAR = "status-bar";
	private static final String SERVER = "server";
	private static final String CONSOLE = "console";
	private static final String DATABASE = "database";

	/**
	 * Access to the primary stage.
	 * 
	 * @param node The reference node.
	 * @return The primary stage.
	 */
	public static Stage getPrimaryStage(Node node) {
		return (Stage) FX.getRootObject(node, PRIMARY_STAGE);
	}

	/**
	 * Access to the application tab pane.
	 * 
	 * @param node The reference node.
	 * @return The tab pane.
	 */
	public static TabPane getTabPane(Node node) {
		return (TabPane) FX.getRootObject(node, TAB_PANE);
	}

	/**
	 * Access to the application status bar.
	 * 
	 * @param node The reference node.
	 * @return The status bar.
	 */
	public static StatusBar getStatusBar(Node node) {
		return (StatusBar) FX.getRootObject(node, STATUS_BAR);
	}

	/**
	 * Access to the application server.
	 * 
	 * @param node The reference node.
	 * @return The sever.
	 */
	public static Server getServer(Node node) {
		return (Server) FX.getRootObject(node, SERVER);
	}

	/**
	 * Access to the application console.
	 * 
	 * @param node The reference node.
	 * @return The console.
	 */
	public static Console getConsole(Node node) {
		return (Console) FX.getRootObject(node, CONSOLE);
	}

	/**
	 * Access to the application database.
	 * 
	 * @param node The reference node.
	 * @return The database.
	 */
	public static Database getDatabase(Node node) {
		return (Database) FX.getRootObject(node, DATABASE);
	}

	/**
	 * Check if a tab with the text is present.
	 * 
	 * @param node The reference node.
	 * @param text The tab text.
	 * @return A boolean.
	 */
	public static boolean isTab(Node node, String text) {
		return getTab(node, text) != null;
	}

	/**
	 * Return the first tab with the given text.
	 * 
	 * @param node The reference node.
	 * @param text The tab text.
	 * @return The tab or null.
	 */
	public static Tab getTab(Node node, String text) {
		TabPane tabPane = getTabPane(node);
		for (Tab tab : tabPane.getTabs()) {
			if (tab.getText().equals(text)) {
				return tab;
			}
		}
		return null;
	}

	/** Logger configuration and text server initialization. */
	static {
		System.setProperty("log4j.configurationFile", "resources/LoggerQTPlatform.xml");
		Session.addBaseResource("resources/StringsLibrary.xml");
		Session.addBaseResource("resources/StringsQTPlatform.xml");
	}
	
	/** Logger instance. */
	private static final Logger LOGGER = LogManager.getLogger();

	/**
	 * Start and launch the application.
	 * 
	 * @param args Startup arguments.
	 */
	public static void main(String[] args) {
		// Do launch.
		launch(args);
	}

	/** Scene root. */
	private BorderPane root;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		// Argument manager.
		ArgumentManager argMngr = new ArgumentManager();
		Argument argConnection = new Argument("dataSourceFile", "Database connection file", true, true, false);
		Argument argServer = new Argument("server", "Trading server", true, false, "Dukascopy");
		Argument argAccount = new Argument("account", "Trading account", true, false, "demo", "live");
		argMngr.add(argConnection);
		argMngr.add(argServer);
		argMngr.add(argAccount);

		// Validate arguments.
		if (!argMngr.parse(getParameters())) {
			Alert alert = new Alert(primaryStage);
			alert.setTitle("Argument errors");
			alert.setType(Alert.Type.ERROR);
			for (String error : argMngr.getErrors()) {
				alert.addText(error + "\n");
			}
			alert.show();
			return;
		}

		// Initialize the trading server.
		Server server = null;
		if (argMngr.getValue("server").equals("Dukascopy")) {
			server = new DkServer();
		}

		// Root: a border pane with a menu bar on the top, a status bar on the bottom and a tab pane on the center.
		root = new BorderPane();

		// Define console, re-direct, tab pane ans status bar.
		Console console = new Console();
		// System.setOut(console.getPrintStream());
		// System.setErr(console.getPrintStream());
		TabPane tabPane = new TabPane();
		root.setCenter(tabPane);
		StatusBar statusBar = new StatusBar();
		root.setBottom(statusBar.getPane());

		// Set properties to the root to access them from anywhere.
		FX.setObject(root, PRIMARY_STAGE, primaryStage);
		FX.setObject(root, TAB_PANE, tabPane);
		FX.setObject(root, STATUS_BAR, statusBar);
		FX.setObject(root, SERVER, server);
		FX.setObject(root, CONSOLE, console);

		// Set menu and add console.
		root.setTop(getMenu());
		addConsole();

		// Ensure that the database is ok.
		try {
			LOGGER.info("Database checking...");
			configureDatabase(server, argMngr.getValue("dataSourceFile"));
			LOGGER.info("Database checked");
		} catch (Exception exc) {
			LOGGER.catching(exc);
			System.exit(0);
		}

		// Scene.
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);

		// Factor dimensions
		primaryStage.setWidth(Screen.getPrimary().getBounds().getWidth() * 0.9);
		primaryStage.setHeight(Screen.getPrimary().getBounds().getHeight() * 0.9);
		primaryStage.centerOnScreen();

		// Full screen mode -> Ctrl-F11.
		primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
			if (e.getCode() == KeyCode.F11) {
				if (e.isControlDown()) {
					primaryStage.setFullScreen(true);
				}
			}
		});

		// On request to close.
		primaryStage.setOnCloseRequest(e -> {
			new ActionApplication.ExitFromWindow(tabPane).handle(e);
		});

		primaryStage.show();
	}

	/**
	 * Configure the menu bar.
	 * 
	 * @return The menu bar.
	 */
	private MenuBar getMenu() {
		MenuBar menuBar = new MenuBar();

		// File menu.
		Menu file = new Menu(Session.getSession().getString("menuFile"));
		MenuItem fileExit = new MenuItem(Session.getSession().getString("menuFileExit"));
		fileExit.setOnAction(e -> {
			new ActionApplication.ExitFromMenu(root).handle(e);
		});
		file.getItems().add(fileExit);

		// Instruments menu.
		Menu instruments = new Menu(Session.getSession().getString("menuInstruments"));
		MenuItem instrumentsAvail = new MenuItem(Session.getSession().getString("menuInstrumentsAvailable"));
		instrumentsAvail.setOnAction(e -> {
			new ActionInstrumentBrowse(root).handle(e);
		});
		MenuItem instrumentsSync = new MenuItem(Session.getSession().getString("menuInstrumentsSynchronize"));
		instrumentsSync.setOnAction(e -> {
			new ActionInstrumentSynchronize(root).handle(e);
		});
		instruments.getItems().add(instrumentsAvail);
		instruments.getItems().add(instrumentsSync);
		instruments.setOnShowing(e -> {
			instrumentsAvail.setDisable(isTab(root, Session.getSession().getString("menuInstrumentsAvailable")));
		});

		// Tickers menu.
		Menu tickers = new Menu(Session.getSession().getString("menuTickers"));
		MenuItem tickersDef = new MenuItem(Session.getSession().getString("menuTickersDefine"));
		tickersDef.setOnAction(e -> {
			new ActionInstrumentTickers(root).handle(e);
		});
		MenuItem tickersStats = new MenuItem(Session.getSession().getString("menuTickersStatistics"));
		tickers.getItems().add(tickersDef);
		tickers.getItems().add(tickersStats);
		tickers.setOnShowing(e -> {
			tickersDef.setDisable(isTab(root, Session.getSession().getString("menuTickersDefine")));
		});
		
		// Training menu.
		Menu training = new Menu(Session.getSession().getString("menuTraining"));
		MenuItem training_mnist_bp = new MenuItem(Session.getSession().getString("menuTraining_mnist_bp"));
		training_mnist_bp.setOnAction(e -> {
			ActionTrainNetwork actionTrain = new ActionTrainNetwork(root);
			actionTrain.handle(e);
		});
		training.getItems().add(training_mnist_bp);
		
		// Window menu.
		Menu window = new Menu(Session.getSession().getString("menuWindow"));
		MenuItem windowConsole = new MenuItem(Session.getSession().getString("menuWindowConsole"));
		windowConsole.setOnAction(e -> {
			if (!isTab(root, Session.getSession().getString("outputConsole"))) {
				addConsole();
			}
		});
		window.setOnShowing(e -> {
			windowConsole.setDisable(isTab(root, Session.getSession().getString("outputConsole")));
		});
		window.getItems().add(windowConsole);

		menuBar.getMenus().add(file);
		menuBar.getMenus().add(instruments);
		menuBar.getMenus().add(tickers);
		menuBar.getMenus().add(training);
		menuBar.getMenus().add(window);
		return menuBar;
	}

	/**
	 * Add the console tab if not present.
	 */
	private void addConsole() {
		Console console = (Console) FX.getObject(root, CONSOLE);
		TabPane tabPane = (TabPane) FX.getObject(root, TAB_PANE);
		Tab consoleTab = new Tab(Session.getSession().getString("outputConsole"));
		consoleTab.setContent(console.getControl());
		tabPane.getTabs().add(consoleTab);
	}

	/**
	 * Ensure the database connection and required schemas.
	 * <ul>
	 * <li>QTFX system schema <tt>qtfx</tt></li>
	 * <li>One schema for each supported server, for instance <tt>qtfx_dkcp</tt></li>
	 * </ul>
	 *
	 * @param server The server.
	 * @param connectionFile The connection file name.
	 * @throws Exception If any error occurs.
	 */
	private void configureDatabase(Server server, String connectionFile) throws Exception {

		// Connection file.
		File cnFile = new File(connectionFile);

		// Data source info and db engine.
		DataSourceInfo info = DataSourceInfo.getDataSourceInfo(cnFile);
		DBEngineAdapter adapter = new PostgreSQLAdapter();
		DBEngine dbEngine = new DBEngine(adapter, info);
		Database db = new Database(dbEngine);
		FX.setObject(root, DATABASE, db);

		// Persistor DDL.
		PersistorDDL ddl = db.getDDL();

		// Check for the system schema.
		if (!ddl.existsSchema(Database.SYSTEM_SCHEMA)) {
			ddl.createSchema(Database.SYSTEM_SCHEMA);
		}

		// Check for server schema.
		String schema = Database.getSchema(server);
		if (!ddl.existsSchema(schema)) {
			ddl.createSchema(schema);
		}

		// Check for the necessary table Servers in the system schema.
		if (!ddl.existsTable(Database.SYSTEM_SCHEMA, Database.SERVERS)) {
			ddl.buildTable(db.getTable_Servers());
		}
		synchronizeSupportedServer(db.getPersistor_Servers(), server);

		// Check for the necessary table Periods in the system schema.
		if (!ddl.existsTable(Database.SYSTEM_SCHEMA, Database.PERIODS)) {
			ddl.buildTable(db.getTable_Periods());
		}
		synchronizeStandardPeriods(db.getPersistor_Periods());

		// Check for the necessary table Instruments in the system schema.
		if (!ddl.existsTable(Database.SYSTEM_SCHEMA, Database.INSTRUMENTS)) {
			ddl.buildTable(db.getTable_Instruments());
		}

		// Check for the necessary table Tickers in the system schema.
		if (!ddl.existsTable(Database.SYSTEM_SCHEMA, Database.TICKERS)) {
			ddl.buildTable(db.getTable_Tickers());
		}
	}

	/**
	 * Synchronize supported servers.
	 * 
	 * @param persistor The persistor.
	 * @param server The server.
	 * @throws Exception If any error occurs.
	 */
	private void synchronizeSupportedServer(Persistor persistor, Server server) throws Exception {
		Record record = persistor.getDefaultRecord();
		record.setValue(Fields.SERVER_ID, new Value(server.getId()));
		record.setValue(Fields.SERVER_NAME, new Value(server.getName()));
		record.setValue(Fields.SERVER_TITLE, new Value(server.getTitle()));
		persistor.save(record);
	}

	/**
	 * Synchronize standard periods.
	 * 
	 * @param persistor The persistor.
	 * @throws Exception If any error occurs.
	 */
	private void synchronizeStandardPeriods(Persistor persistor) throws Exception {
		List<Period> periods = Period.getStandardPeriods();
		for (Period period : periods) {
			Record record = persistor.getDefaultRecord();
			record.setValue(Fields.PERIOD_ID, new Value(period.getId()));
			record.setValue(Fields.PERIOD_NAME, new Value(period.toString()));
			record.setValue(Fields.PERIOD_SIZE, new Value(period.getSize()));
			record.setValue(Fields.PERIOD_UNIT_INDEX, new Value(period.getUnit().ordinal()));
			if (!persistor.exists(record)) {
				persistor.insert(record);
			}
		}
	}
	
	/**
	 * Return a trainer for the MNIST images.
	 * @return The trainer.
	 */
	private Trainer getTrainerMNIST() {
		Trainer trainer = new Trainer(Session.getSession());
		
		int inputLayerSize = NumberImage.ROWS * NumberImage.COLUMNS;
		int hiddenLayerSize = 100;
		int outputLayerSize = 10;
		
		
		trainer.setEpochs(100);
		return trainer;
	}
}
