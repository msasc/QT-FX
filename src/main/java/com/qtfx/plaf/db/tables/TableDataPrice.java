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

package com.qtfx.plaf.db.tables;

import com.qtfx.lib.db.Index;
import com.qtfx.lib.db.Table;
import com.qtfx.lib.db.rdbms.DBEngine;
import com.qtfx.lib.db.rdbms.DBPersistor;
import com.qtfx.lib.mkt.data.Instrument;
import com.qtfx.lib.mkt.data.Period;
import com.qtfx.lib.mkt.server.Server;
import com.qtfx.plaf.db.Database;
import com.qtfx.plaf.db.Fields;
import com.qtfx.plaf.db.fields.FieldDataInst;
import com.qtfx.plaf.db.fields.FieldIndex;
import com.qtfx.plaf.db.fields.FieldTime;
import com.qtfx.plaf.db.fields.FieldTimeFmt;
import com.qtfx.plaf.db.fields.FieldVolume;

/**
 * Tickers table definition.
 * 
 * @author Miquel Sas
 */
public class TableDataPrice extends Table {

	/**
	 * Constructor.
	 */
	public TableDataPrice(DBEngine dbEngine, Server server, Instrument instrument, Period period) {
		super();

		setName(Database.getName_Ticker(instrument, period));
		setSchema(Database.getSchema(server));
		
		addField(new FieldIndex(Fields.INDEX));
		addField(new FieldTime(Fields.TIME));
		addField(new FieldDataInst(instrument, Fields.OPEN, "Open", "Open"));
		addField(new FieldDataInst(instrument, Fields.HIGH, "High", "High"));
		addField(new FieldDataInst(instrument, Fields.LOW, "Low", "Low"));
		addField(new FieldDataInst(instrument, Fields.CLOSE, "Close", "Close"));
		addField(new FieldVolume(Fields.VOLUME));
		addField(new FieldTimeFmt(Fields.TIME_FMT, period));

		getField(Fields.TIME).setPrimaryKey(true);
		
		Index index = new Index();
		index.add(getField(Fields.INDEX));
		index.setUnique(true);
		addIndex(index);
		
		setPersistor(new DBPersistor(dbEngine, getComplexView(getPrimaryKey())));
	}

}
