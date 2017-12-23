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

package com.qtfx.plaf.task;

import com.qtfx.lib.db.Condition;
import com.qtfx.lib.db.Criteria;
import com.qtfx.lib.db.Field;
import com.qtfx.lib.db.Order;
import com.qtfx.lib.db.PersistorException;
import com.qtfx.lib.db.Record;
import com.qtfx.lib.db.RecordIterator;
import com.qtfx.lib.db.Value;
import com.qtfx.lib.mkt.data.Data;
import com.qtfx.lib.mkt.data.DataPersistor;
import com.qtfx.lib.mkt.data.Filter;
import com.qtfx.lib.mkt.data.Instrument;
import com.qtfx.lib.mkt.data.OfferSide;
import com.qtfx.lib.mkt.data.Period;
import com.qtfx.lib.mkt.server.DataIterator;
import com.qtfx.lib.mkt.server.Server;
import com.qtfx.lib.task.Task;
import com.qtfx.lib.util.TextServer;
import com.qtfx.plaf.ServerConnector;
import com.qtfx.plaf.db.Database;
import com.qtfx.plaf.db.Fields;

/**
 *
 * @author Miquel Sas
 *
 */
public class TaskDownloadTicker extends Task {

	/** Database. */
	private Database database;
	/** The server to download the ticker from. */
	private Server server;
	/** The instrument to download. */
	private Instrument instrument;
	/** The period. */
	private Period period;
	/** The offer side. */
	private OfferSide offerSide;
	/** The data filter. */
	private Filter filter;

	/** Persistor. */
	private DataPersistor persistor;

	/**
	 * @param database Database.
	 * @param server Server
	 * @param instrument Instrument.
	 * @param period Period.
	 * @param offerSide Offer side.
	 * @param filter Period.
	 */
	public TaskDownloadTicker(
		Database database,
		Server server,
		Instrument instrument,
		Period period,
		OfferSide offerSide,
		Filter filter) {
		super();
		this.database = database;
		this.server = server;
		this.instrument = instrument;
		this.period = period;
		this.offerSide = offerSide;
		this.filter = filter;
		
		StringBuilder title = new StringBuilder();
		title.append(TextServer.getString("buttonDownload") + " " + instrument.getId() + " " + period.toString());
		setTitle(title.toString());
	}
	
	private long getTimeOfLastDowloaded() throws PersistorException {
		Order order = new Order();
		order.add(persistor.getField(Fields.TIME), false);
		Record record = null;
		RecordIterator i = persistor.iterator(null, order);
		if (i.hasNext()) {
			record = i.next();
		}
		i.close();
		if (record != null) {
			return record.getValue(Fields.TIME).getLong();
		}
		return -1;
	}
	
	private void deleteFromn(long timeFrom) throws PersistorException {
		Field fTIME = persistor.getField(Fields.TIME);
		Value vTIME = new Value(timeFrom);
		Criteria criteria = new Criteria();
		criteria.add(Condition.fieldGE(fTIME, vTIME));
		persistor.delete(criteria);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void compute() throws Exception {
		updateCounting();
		persistor = new DataPersistor(database.getPersistor_DataPrice(server, instrument, period));
		ServerConnector.connect(server);
		
		long timeFrom = getTimeOfLastDowloaded();
		if (timeFrom < 0) {
			timeFrom = server.getHistoryManager().getTimeOfFirstData(instrument, period);
		}
		long timeTo = server.getHistoryManager().getTimeOfLastData(instrument, period, filter);
		long steps = timeTo - timeFrom;
		deleteFromn(timeFrom);
		
		DataIterator i = 
			server.getHistoryManager().getDataIterator(instrument, period, offerSide, filter, timeFrom, timeTo);
		while (i.hasNext()) {
			if (isCancelled()) {
				break;
			}
			Data data = i.next();
			long step = data.getTime() - timeFrom;
			update("Downloading", step, steps);
			Record record = persistor.getDefaultRecord();
			record.setValue(Fields.TIME, new Value(data.getTime()));
			record.setValue(Fields.OPEN, new Value(Data.getOpen(data)));
			record.setValue(Fields.HIGH, new Value(Data.getHigh(data)));
			record.setValue(Fields.LOW, new Value(Data.getLow(data)));
			record.setValue(Fields.CLOSE, new Value(Data.getClose(data)));
			record.setValue(Fields.VOLUME, new Value(Data.getVolume(data)));
			persistor.insert(record);
		}
		i.close();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isIndeterminate() {
		return false;
	}

}
