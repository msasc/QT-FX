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

package com.qtfx.platform.db.tables;

import com.qtfx.library.db.Order;
import com.qtfx.library.db.Table;
import com.qtfx.platform.db.Fields;
import com.qtfx.platform.db.Schemas;
import com.qtfx.platform.db.Tables;
import com.qtfx.platform.db.fields.FieldPeriodId;
import com.qtfx.platform.db.fields.FieldPeriodName;
import com.qtfx.platform.db.fields.FieldPeriodSize;
import com.qtfx.platform.db.fields.FieldPeriodUnitIndex;
import com.qtfx.platform.util.PersistorUtils;

/**
 * Servers table definition.
 * 
 * @author Miquel Sas
 */
public class TablePeriods extends Table {

	/**
	 * Constructor.
	 */
	public TablePeriods() {
		super();
		
		setName(Tables.Periods);
		setSchema(Schemas.qtp);
		
		addField(new FieldPeriodId(Fields.PERIOD_ID));
		addField(new FieldPeriodName(Fields.PERIOD_NAME));
		addField(new FieldPeriodUnitIndex(Fields.PERIOD_UNIT_INDEX));
		addField(new FieldPeriodSize(Fields.PERIOD_SIZE));
		
		getField(Fields.PERIOD_ID).setPrimaryKey(true);

		Order order = new Order();
		order.add(getField(Fields.PERIOD_UNIT_INDEX));
		order.add(getField(Fields.PERIOD_SIZE));

		setPersistor(PersistorUtils.getPersistor(getSimpleView(order)));
	}

}
