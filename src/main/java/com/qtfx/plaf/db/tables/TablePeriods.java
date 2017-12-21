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

import com.qtfx.lib.db.Order;
import com.qtfx.lib.db.Table;
import com.qtfx.lib.db.rdbms.DBEngine;
import com.qtfx.lib.db.rdbms.DBPersistor;
import com.qtfx.plaf.db.Database;
import com.qtfx.plaf.db.Fields;
import com.qtfx.plaf.db.fields.FieldPeriodId;
import com.qtfx.plaf.db.fields.FieldPeriodName;
import com.qtfx.plaf.db.fields.FieldPeriodSize;
import com.qtfx.plaf.db.fields.FieldPeriodUnitIndex;

/**
 * Servers table definition.
 * 
 * @author Miquel Sas
 */
public class TablePeriods extends Table {

	/**
	 * Constructor.
	 */
	public TablePeriods(DBEngine dbEngine) {
		super();
		
		setName(Database.PERIODS);
		setSchema(Database.SYSTEM_SCHEMA);
		
		addField(new FieldPeriodId(Fields.PERIOD_ID));
		addField(new FieldPeriodName(Fields.PERIOD_NAME));
		addField(new FieldPeriodUnitIndex(Fields.PERIOD_UNIT_INDEX));
		addField(new FieldPeriodSize(Fields.PERIOD_SIZE));
		
		getField(Fields.PERIOD_ID).setPrimaryKey(true);
		
		Order order = new Order();
		order.add(getField(Fields.PERIOD_UNIT_INDEX));
		order.add(getField(Fields.PERIOD_SIZE));
		
		setPersistor(new DBPersistor(dbEngine, getComplexView(order)));
	}
}
