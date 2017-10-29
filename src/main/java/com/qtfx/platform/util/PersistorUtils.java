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

package com.qtfx.platform.util;

import com.qtfx.library.db.Persistor;
import com.qtfx.library.db.PersistorDDL;
import com.qtfx.library.db.View;
import com.qtfx.library.db.rdbms.DBEngine;
import com.qtfx.library.db.rdbms.DBPersistor;
import com.qtfx.library.db.rdbms.DBPersistorDDL;
import com.qtfx.library.mkt.data.Instrument;
import com.qtfx.library.mkt.server.Server;
import com.qtfx.platform.db.tables.TableDataFilters;
import com.qtfx.platform.db.tables.TableDataPrice;
import com.qtfx.platform.db.tables.TableInstruments;
import com.qtfx.platform.db.tables.TableOfferSides;
import com.qtfx.platform.db.tables.TablePeriods;
import com.qtfx.platform.db.tables.TableServers;
import com.qtfx.platform.db.tables.TableStatistics;
import com.qtfx.platform.db.tables.TableTickers;

/**
 * Centralizes persistors access.
 * 
 * @author Miquel Sas
 */
public class PersistorUtils {

	/** The database engine used to set the persistor to tables. */
	private static DBEngine dbEngine;

	/**
	 * Sets the database engine to assign the proper persistor to tables.
	 * 
	 * @param dbEngine The database engine.
	 */
	public static void setDBEngine(DBEngine dbEngine) {
		PersistorUtils.dbEngine = dbEngine;
	}

	/**
	 * Returns the database engine in use.
	 * 
	 * @return The database engine in use.
	 */
	public static DBEngine getDBEngine() {
		return dbEngine;
	}

	/**
	 * Returns the persistor for the view.
	 * 
	 * @param view The view.
	 * @return The persistor.
	 */
	public static Persistor getPersistor(View view) {
		return new DBPersistor(getDBEngine(), view);
	}

	/**
	 * Returns a suitable DDL.
	 * 
	 * @return The DDL.
	 */
	public static PersistorDDL getDDL() {
		return new DBPersistorDDL(getDBEngine());
	}

	/**
	 * Returns the data price persistor.
	 * 
	 * @param server Server.
	 * @param instrument Instrument.
	 * @param name The table name.
	 * @return The persistor.
	 */
	public static Persistor getPersistorDataPrice(Server server, Instrument instrument, String name) {
		return new TableDataPrice(server, instrument, name).getPersistor();
	}

	/**
	 * Returns the instruments persistor.
	 * 
	 * @return The persistor.
	 */
	public static Persistor getPersistorInstruments() {
		return new TableInstruments().getPersistor();
	}

	/**
	 * Returns the periods persistor.
	 * 
	 * @return The persistor.
	 */
	public static Persistor getPersistorPeriods() {
		return new TablePeriods().getPersistor();
	}

	/**
	 * Returns the servers persistor.
	 * 
	 * @return The persistor.
	 */
	public static Persistor getPersistorServers() {
		return new TableServers().getPersistor();
	}

	/**
	 * Returns the statistics persistor.
	 * 
	 * @return The persistor.
	 */
	public static Persistor getPersistorStatistics() {
		return new TableStatistics().getPersistor();
	}

	/**
	 * Returns the tickers persistor.
	 * 
	 * @return The persistor.
	 */
	public static Persistor getPersistorTickers() {
		return new TableTickers().getPersistor();
	}

	/**
	 * Returns the offer side persistor.
	 * 
	 * @return The persistor.
	 */
	public static Persistor getPersistorOfferSides() {
		return new TableOfferSides().getPersistor();
	}

	/**
	 * Returns the data filter side persistor.
	 * 
	 * @return The persistor.
	 */
	public static Persistor getPersistorDataFilters() {
		return new TableDataFilters().getPersistor();
	}
}
