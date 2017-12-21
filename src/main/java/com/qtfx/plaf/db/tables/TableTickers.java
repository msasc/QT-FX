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

import com.qtfx.lib.db.ForeignKey;
import com.qtfx.lib.db.Order;
import com.qtfx.lib.db.Table;
import com.qtfx.lib.db.rdbms.DBEngine;
import com.qtfx.lib.db.rdbms.DBPersistor;
import com.qtfx.plaf.db.Database;
import com.qtfx.plaf.db.Fields;
import com.qtfx.plaf.db.fields.FieldInstrumentId;
import com.qtfx.plaf.db.fields.FieldPeriodId;
import com.qtfx.plaf.db.fields.FieldServerId;
import com.qtfx.plaf.db.fields.FieldTableName;

/**
 * Tickers table definition.
 * 
 * @author Miquel Sas
 */
public class TableTickers extends Table {

	/**
	 * Constructor.
	 */
	public TableTickers(DBEngine dbEngine) {
		super();

		setName(Database.TICKERS);
		setSchema(Database.SYSTEM_SCHEMA);

		addField(new FieldServerId(Fields.SERVER_ID));
		addField(new FieldInstrumentId(Fields.INSTRUMENT_ID));
		addField(new FieldPeriodId(Fields.PERIOD_ID));
		addField(new FieldTableName(Fields.TABLE_NAME));

		getField(Fields.SERVER_ID).setPrimaryKey(true);
		getField(Fields.INSTRUMENT_ID).setPrimaryKey(true);
		getField(Fields.PERIOD_ID).setPrimaryKey(true);

		Table tablePeriods = new TablePeriods(dbEngine);
		ForeignKey fkPeriods = new ForeignKey(false);
		fkPeriods.setLocalTable(this);
		fkPeriods.setForeignTable(tablePeriods);
		fkPeriods.add(getField(Fields.PERIOD_ID), tablePeriods.getField(Fields.PERIOD_ID));
		addForeignKey(fkPeriods);

		Order order = new Order();
		order.add(getField(Fields.SERVER_ID));
		order.add(getField(Fields.INSTRUMENT_ID));
		order.add(tablePeriods.getField(Fields.PERIOD_UNIT_INDEX));
		order.add(tablePeriods.getField(Fields.PERIOD_SIZE));
		
		setPersistor(new DBPersistor(dbEngine, getComplexView(order)));
	}

}
