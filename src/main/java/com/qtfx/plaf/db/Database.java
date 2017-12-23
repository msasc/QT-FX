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

package com.qtfx.plaf.db;

import java.util.Currency;
import java.util.HashMap;

import com.qtfx.lib.db.Condition;
import com.qtfx.lib.db.Criteria;
import com.qtfx.lib.db.Persistor;
import com.qtfx.lib.db.PersistorDDL;
import com.qtfx.lib.db.PersistorException;
import com.qtfx.lib.db.Record;
import com.qtfx.lib.db.RecordSet;
import com.qtfx.lib.db.Value;
import com.qtfx.lib.db.rdbms.DBEngine;
import com.qtfx.lib.db.rdbms.DBPersistorDDL;
import com.qtfx.lib.gui.LookupRecords;
import com.qtfx.lib.mkt.data.Instrument;
import com.qtfx.lib.mkt.data.Period;
import com.qtfx.lib.mkt.server.Server;
import com.qtfx.plaf.QTFX;
import com.qtfx.plaf.db.tables.TableDataPrice;
import com.qtfx.plaf.db.tables.TableInstruments;
import com.qtfx.plaf.db.tables.TablePeriods;
import com.qtfx.plaf.db.tables.TableServers;
import com.qtfx.plaf.db.tables.TableTickers;

import javafx.scene.Node;

/**
 * Provide access to the database objects.
 *
 * @author Miquel Sas
 */
public class Database {

	/////////////////////////
	// QTFX main schema name.

	public static final String SYSTEM_SCHEMA = "qtfx";

	///////////////
	// Table names.

	public static final String INSTRUMENTS = "instruments";
	public static final String PERIODS = "periods";
	public static final String SERVERS = "servers";
	public static final String TICKERS = "tickers";

	private DBEngine dbEngine;

	private TableInstruments tableInstruments;
	private TablePeriods tablePeriods;
	private TableServers tableServers;
	private TableTickers tableTickers;
	private HashMap<String, TableDataPrice> tablesPrice = new HashMap<>();

	/**
	 * Constructor.
	 */
	public Database(DBEngine dbEngine) {
		super();
		this.dbEngine = dbEngine;
	}

	//////
	// DDL

	/**
	 * Returns a suitable DDL.
	 * 
	 * @return The DDL.
	 */
	public PersistorDDL getDDL() {
		return new DBPersistorDDL(dbEngine);
	}

	//////////
	// Namnes.

	/**
	 * Return the generic ticker name.
	 * 
	 * @param instrument The instrument.
	 * @param period The period.
	 * @return The 'ticker' table name.
	 */
	public static String getName_Ticker(Instrument instrument, Period period) {
		return getName_Ticker(instrument, period, null);
	}

	/**
	 * Return the generic ticker name.
	 * 
	 * @param instrument The instrument.
	 * @param period The period.
	 * @param suffix Optional suffix.
	 * @return The 'ticker' table name.
	 */
	public static String getName_Ticker(Instrument instrument, Period period, String suffix) {
		StringBuilder b = new StringBuilder();
		b.append(instrument.getId().toLowerCase());
		b.append("_");
		b.append(period.getId().toLowerCase());
		if (suffix != null) {
			b.append("_");
			b.append(suffix.toLowerCase());
		}
		return b.toString();
	}

	//////////////
	// Persistors.

	/**
	 * Returns the data price persistor.
	 * 
	 * @return The persistor.
	 */
	public Persistor getPersistor_DataPrice(Server server, Instrument instrument, Period period) {
		return getTable_DataPrice(server, instrument, period).getPersistor();
	}

	/**
	 * Returns the instruments persistor.
	 * 
	 * @return The persistor.
	 */
	public Persistor getPersistor_Instruments() {
		return getTable_Instruments().getPersistor();
	}

	/**
	 * Returns the periods persistor.
	 * 
	 * @return The persistor.
	 */
	public Persistor getPersistor_Periods() {
		return getTable_Periods().getPersistor();
	}

	/**
	 * Returns the servers persistor.
	 * 
	 * @return The persistor.
	 */
	public Persistor getPersistor_Servers() {
		return getTable_Servers().getPersistor();
	}

	/**
	 * Returns the tickers persistor.
	 * 
	 * @return The persistor.
	 */
	public Persistor getPersistor_Tickers() {
		return getTable_Tickers().getPersistor();
	}

	///////////
	// Records.

	/**
	 * Returns the filled record for the instrument.
	 * 
	 * @param server The server.
	 * @param instrument The instrument.
	 * @return The record.
	 */
	public Record getRecord_Instrument(Server server, Instrument instrument) {
		Record record = getPersistor_Instruments().getDefaultRecord();
		record.setValue(Fields.SERVER_ID, new Value(server.getId()));
		record.setValue(Fields.INSTRUMENT_ID, new Value(instrument.getId()));
		record.setValue(Fields.INSTRUMENT_DESC, new Value(instrument.getDescription()));
		record.setValue(Fields.INSTRUMENT_PIP_VALUE, new Value(instrument.getPipValue()));
		record.setValue(Fields.INSTRUMENT_PIP_SCALE, new Value(instrument.getPipScale()));
		record.setValue(Fields.INSTRUMENT_TICK_VALUE, new Value(instrument.getTickValue()));
		record.setValue(Fields.INSTRUMENT_TICK_SCALE, new Value(instrument.getTickScale()));
		record.setValue(Fields.INSTRUMENT_VOLUME_SCALE, new Value(instrument.getVolumeScale()));
		record.setValue(Fields.INSTRUMENT_PRIMARY_CURRENCY, new Value(instrument.getPrimaryCurrency().toString()));
		record.setValue(Fields.INSTRUMENT_SECONDARY_CURRENCY, new Value(instrument.getSecondaryCurrency().toString()));
		return record;
	}

	/**
	 * Returns the filled record for the instrument.
	 * 
	 * @param instrument The instrument.
	 * @return The record.
	 */
	public Record getRecord_Instrument(Server server, String id) throws PersistorException {
		Persistor persistor = getPersistor_Instruments();
		Record record = getPersistor_Instruments().getDefaultRecord();
		record.setValue(Fields.SERVER_ID, new Value(server.getId()));
		record.setValue(Fields.INSTRUMENT_ID, new Value(id));
		persistor.refresh(record);
		return record;
	}

	/**
	 * Return the ticker record.
	 * 
	 * @param server Server.
	 * @param instrument Instrument.
	 * @param period Period.
	 * @return The record.
	 */
	public Record getRecord_Ticker(Server server, Instrument instrument, Period period) {
		Record record = getPersistor_Tickers().getDefaultRecord();
		record.setValue(Fields.SERVER_ID, new Value(server.getId()));
		record.setValue(Fields.INSTRUMENT_ID, new Value(instrument.getId()));
		record.setValue(Fields.PERIOD_ID, new Value(period.getId()));
		record.setValue(Fields.TABLE_NAME, new Value(getName_Ticker(instrument, period)));
		return record;
	}
	
	///////////////
	// Record sets.

	/**
	 * Returns a record set with the available instruments for the argument server.
	 * 
	 * @param server The server.
	 * @return The record set.
	 * @throws PersistorException If any persistence error occurs.
	 */
	public RecordSet getRecordSet_AvailableInstruments(Server server) throws PersistorException {

		Persistor persistor = getPersistor_Instruments();
		Criteria criteria = new Criteria();
		criteria.add(Condition.fieldEQ(persistor.getField(Fields.SERVER_ID), new Value(server.getId())));
		RecordSet recordSet = persistor.select(criteria);

		// Track max pip and tick scale to set their values decimals.
		int maxPipScale = 0;
		int maxTickScale = 0;
		for (int i = 0; i < recordSet.size(); i++) {
			Record record = recordSet.get(i);
			maxPipScale = Math.max(maxPipScale, record.getValue(Fields.INSTRUMENT_PIP_SCALE).getInteger());
			maxTickScale = Math.max(maxTickScale, record.getValue(Fields.INSTRUMENT_TICK_SCALE).getInteger());
		}
		recordSet.getField(Fields.INSTRUMENT_PIP_VALUE).setDisplayDecimals(maxPipScale);
		recordSet.getField(Fields.INSTRUMENT_TICK_VALUE).setDisplayDecimals(maxTickScale);
		return recordSet;
	}

	/**
	 * Returns a record set with the standard periods.
	 * 
	 * @return The record set.
	 * @throws PersistorException If any persistence error occurs.
	 */
	public RecordSet getRecordSet_Periods() throws PersistorException {
		Persistor persistor = getPersistor_Periods();
		RecordSet recordSet = persistor.select((Criteria) null);
		return recordSet;
	}

	/**
	 * Returns a record set with the defined tickers for the argument server.
	 * 
	 * @param server The server.
	 * @return The record set.
	 * @throws PersistorException If any persistence error occurs.
	 */
	public RecordSet getRecordSet_Tickers(Server server) throws PersistorException {
		Persistor persistor = getPersistor_Tickers();
		Criteria criteria = new Criteria();
		criteria.add(Condition.fieldEQ(persistor.getField(Fields.SERVER_ID), new Value(server.getId())));
		RecordSet recordSet = persistor.select(criteria);
		return recordSet;
	}

	////////////////
	// Schema names.

	/**
	 * Return the schema name.
	 * 
	 * @param server The server.
	 * @return The name.
	 */
	public static String getSchema(Server server) {
		return SYSTEM_SCHEMA + "_" + server.getId();
	}

	////////////////
	// Table access.

	/**
	 * Access to the price table.
	 * 
	 * @param server Server.
	 * @param instrument Instrument.
	 * @param period Period.
	 * @return The table.
	 */
	public TableDataPrice getTable_DataPrice(Server server, Instrument instrument, Period period) {
		String name = getName_Ticker(instrument, period);
		TableDataPrice table = tablesPrice.get(name);
		if (table == null) {
			table = new TableDataPrice(dbEngine, server, instrument, period);
			tablesPrice.put(name, table);
		}
		return table;
	}

	/**
	 * Access the instruments table.
	 * 
	 * @return The table.
	 */
	public TableInstruments getTable_Instruments() {
		if (tableInstruments == null) {
			tableInstruments = new TableInstruments(dbEngine);
		}
		return tableInstruments;
	}

	/**
	 * Access the periods table.
	 * 
	 * @return The table.
	 */
	public TablePeriods getTable_Periods() {
		if (tablePeriods == null) {
			tablePeriods = new TablePeriods(dbEngine);
		}
		return tablePeriods;
	}

	/**
	 * Access the servers table.
	 * 
	 * @return The table.
	 */
	public TableServers getTable_Servers() {
		if (tableServers == null) {
			tableServers = new TableServers(dbEngine);
		}
		return tableServers;
	}

	/**
	 * Access the tickers table.
	 * 
	 * @return The table.
	 */
	public TableTickers getTable_Tickers() {
		if (tableTickers == null) {
			tableTickers = new TableTickers(dbEngine);
		}
		return tableTickers;
	}

	///////////
	// Lookups.

	/**
	 * Lookup an instrument.
	 * 
	 * @param node The reference node.
	 * @return The selected instrument record.
	 * @throws PersistorException
	 */
	public Record lookupInstrument(Node node) throws PersistorException {
		LookupRecords lookup = new LookupRecords(getPersistor_Instruments().getDefaultRecord());
		lookup.addColumn(Fields.INSTRUMENT_ID);
		lookup.addColumn(Fields.INSTRUMENT_DESC);
		lookup.addColumn(Fields.INSTRUMENT_PIP_VALUE);
		lookup.addColumn(Fields.INSTRUMENT_PIP_SCALE);
		lookup.addColumn(Fields.INSTRUMENT_TICK_VALUE);
		lookup.addColumn(Fields.INSTRUMENT_TICK_SCALE);
		lookup.addColumn(Fields.INSTRUMENT_VOLUME_SCALE);
		lookup.addColumn(Fields.INSTRUMENT_PRIMARY_CURRENCY);
		lookup.addColumn(Fields.INSTRUMENT_SECONDARY_CURRENCY);
		lookup.setRecordSet(getRecordSet_AvailableInstruments(QTFX.getServer(node)));
		Record record = lookup.lookupRecord(QTFX.getPrimaryStage(node));
		return record;
	}

	/**
	 * Lookup a period.
	 * 
	 * @param node The reference node.
	 * @return The selected period.
	 * @throws PersistorException
	 */
	public Record lookupPeriod(Node node) throws PersistorException {
		LookupRecords lookup = new LookupRecords(getPersistor_Periods().getDefaultRecord());
		lookup.addColumn(Fields.PERIOD_ID);
		lookup.addColumn(Fields.PERIOD_NAME);
		lookup.addColumn(Fields.PERIOD_SIZE);
		lookup.setRecordSet(getRecordSet_Periods());
		Record record = lookup.lookupRecord(QTFX.getPrimaryStage(node));
		return record;
	}

	/////////////////////////////////////
	// Converting from record to objects.

	public Instrument fromRecordToInstrument(Record record) {
		Instrument instrument = new Instrument();
		instrument.setId(record.getValue(Fields.INSTRUMENT_ID).getString());
		instrument.setDescription(record.getValue(Fields.INSTRUMENT_DESC).getString());
		instrument.setPipValue(record.getValue(Fields.INSTRUMENT_PIP_VALUE).getDouble());
		instrument.setPipScale(record.getValue(Fields.INSTRUMENT_PIP_SCALE).getInteger());
		instrument.setTickValue(record.getValue(Fields.INSTRUMENT_TICK_VALUE).getDouble());
		instrument.setTickScale(record.getValue(Fields.INSTRUMENT_TICK_SCALE).getInteger());
		instrument.setVolumeScale(record.getValue(Fields.INSTRUMENT_VOLUME_SCALE).getInteger());
		String primaryCurrency = record.getValue(Fields.INSTRUMENT_PRIMARY_CURRENCY).getString();
		instrument.setPrimaryCurrency(Currency.getInstance(primaryCurrency));
		String secondaryCurrency = record.getValue(Fields.INSTRUMENT_SECONDARY_CURRENCY).getString();
		instrument.setSecondaryCurrency(Currency.getInstance(secondaryCurrency));
		return instrument;
	}

	public Period fromRecordToPeriod(Record record) {
		String id = record.getValue(Fields.PERIOD_ID).getString();
		return Period.parseId(id);
	}
}
