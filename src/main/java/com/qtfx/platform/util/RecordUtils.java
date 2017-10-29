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
import com.qtfx.library.db.PersistorException;
import com.qtfx.library.db.Record;
import com.qtfx.library.db.Value;
import com.qtfx.library.mkt.data.Data;
import com.qtfx.library.mkt.data.Filter;
import com.qtfx.library.mkt.data.Instrument;
import com.qtfx.library.mkt.data.Period;
import com.qtfx.library.mkt.server.OfferSide;
import com.qtfx.library.mkt.server.Server;
import com.qtfx.platform.db.Fields;

/**
 * Centralizes record operations.
 *
 * @author Miquel Sas
 */
public class RecordUtils {

	/**
	 * Returns the filled record for the server.
	 * 
	 * @param record The blank server record.
	 * @param server The server.
	 * @return The record.
	 */
	public static Record getRecordServer(Record record, Server server) {
		record.setValue(Fields.SERVER_ID, server.getId());
		record.setValue(Fields.SERVER_NAME, server.getName());
		record.setValue(Fields.SERVER_TITLE, server.getTitle());
		return record;
	}

	/**
	 * Returns the period record from the database, given the period id.
	 * 
	 * @param period The period id value.
	 * @return The record or null.
	 * @throws PersistorException If a persistor error occurs.
	 */
	public static Record getRecordPeriod(Value period) throws PersistorException {
		Persistor persistor = PersistorUtils.getPersistorPeriods();
		return persistor.getRecord(period);
	}

	/**
	 * Returns the filled record for the period.
	 * 
	 * @param record The blank period record.
	 * @param period The period.
	 * @return The record.
	 */
	public static Record getRecordPeriod(Record record, Period period) {
		record.setValue(Fields.PERIOD_ID, period.getId());
		record.setValue(Fields.PERIOD_NAME, period.toString());
		record.setValue(Fields.PERIOD_SIZE, period.getSize());
		record.setValue(Fields.PERIOD_UNIT_INDEX, period.getUnit().ordinal());
		return record;
	}

	/**
	 * Returns the data price record.
	 * 
	 * @param record The default data price record.
	 * @param data The price bar.
	 * @return The filled record.
	 */
	public static Record getRecordDataPrice(Record record, Data data) {
		record.getValue(Fields.TIME).setLong(data.getTime());
		record.getValue(Fields.OPEN).setDouble(Data.getOpen(data));
		record.getValue(Fields.HIGH).setDouble(Data.getHigh(data));
		record.getValue(Fields.LOW).setDouble(Data.getLow(data));
		record.getValue(Fields.CLOSE).setDouble(Data.getClose(data));
		record.getValue(Fields.VOLUME).setDouble(Data.getVolume(data));
		return record;
	}

	/**
	 * Returns the filled record for the instrument.
	 * 
	 * @param record The blank instrument record.
	 * @param instrument The instrument.
	 * @return The record.
	 */
	public static Record getRecordInstrument(Record record, Instrument instrument) {
		record.setValue(Fields.INSTRUMENT_ID, instrument.getId());
		record.setValue(Fields.INSTRUMENT_DESC, instrument.getDescription());
		record.setValue(Fields.INSTRUMENT_PIP_VALUE, instrument.getPipValue());
		record.setValue(Fields.INSTRUMENT_PIP_SCALE, instrument.getPipScale());
		record.setValue(Fields.INSTRUMENT_TICK_VALUE, instrument.getTickValue());
		record.setValue(Fields.INSTRUMENT_TICK_SCALE, instrument.getTickScale());
		record.setValue(Fields.INSTRUMENT_VOLUME_SCALE, instrument.getVolumeScale());
		record.setValue(Fields.INSTRUMENT_PRIMARY_CURRENCY, instrument.getPrimaryCurrency().toString());
		record.setValue(Fields.INSTRUMENT_SECONDARY_CURRENCY, instrument.getSecondaryCurrency().toString());
		return record;
	}

	/**
	 * Returns the instrument record from the database, given the server and instruments ids.
	 * 
	 * @param serverId The server id.
	 * @param instrumentId The instrument id.
	 * @return The record or null.
	 * @throws PersistorException If a persistor error occurs.
	 */
	public static Record getRecordInstrument(String serverId, String instrumentId)
		throws PersistorException {
		return getRecordInstrument(new Value(serverId), new Value(instrumentId));
	}

	/**
	 * Returns the instrument record from the database, given the server and instruments ids.
	 * 
	 * @param server The server id value.
	 * @param instrument The instrument id value.
	 * @return The record or null.
	 * @throws PersistorException If a persistor error occurs.
	 */
	public static Record getRecordInstrument(Value server, Value instrument)
		throws PersistorException {
		Persistor persistor = PersistorUtils.getPersistorInstruments();
		return persistor.getRecord(server, instrument);
	}

	/**
	 * Returns the filled record for the offer side.
	 * 
	 * @param record The blank offer side record.
	 * @param offerSide The offer side.
	 * @return The record.
	 */
	public static Record getRecordOfferSide(Record record, OfferSide offerSide) {
		record.setValue(Fields.OFFER_SIDE, offerSide.name());
		return record;
	}

	/**
	 * Returns the offer side record from the database, given the offer side.
	 * 
	 * @param offerSide The period id value.
	 * @return The record or null.
	 * @throws PersistorException If a persistor error occurs.
	 */
	public static Record getRecordOfferSide(Value offerSide) throws PersistorException {
		Persistor persistor = PersistorUtils.getPersistorOfferSides();
		return persistor.getRecord(offerSide);
	}

	/**
	 * Returns the filled record for the data filter.
	 * 
	 * @param record The blank offer side record.
	 * @param dataFilter The data filter.
	 * @return The record.
	 */
	public static Record getRecordDataFilter(Record record, Filter dataFilter) {
		record.setValue(Fields.DATA_FILTER, dataFilter.name());
		return record;
	}

	/**
	 * Returns the data filter record from the database, given the filter.
	 * 
	 * @param dataFilter The period id value.
	 * @return The record or null.
	 * @throws PersistorException If a persistor error occurs.
	 */
	public static Record getRecordDataFilter(Value dataFilter) throws PersistorException {
		Persistor persistor = PersistorUtils.getPersistorDataFilters();
		return persistor.getRecord(dataFilter);
	}

	/**
	 * Returns the tickers record.
	 * 
	 * @param server Server.
	 * @param instrument Instrument.
	 * @param period Period.
	 * @return The tickers record.
	 * @throws PersistorException If a persistor error occurs.
	 */
	public static Record getRecordTicker(Server server, Instrument instrument, Period period)
		throws PersistorException {
		Persistor persistor = PersistorUtils.getPersistorTickers();
		Value serverId = new Value(server.getId());
		Value instrId = new Value(instrument.getId());
		Value periodId = new Value(period.getId());
		return persistor.getRecord(serverId, instrId, periodId);
	}

}
