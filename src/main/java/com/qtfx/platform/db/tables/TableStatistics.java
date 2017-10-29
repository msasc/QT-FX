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

import com.qtfx.library.db.ForeignKey;
import com.qtfx.library.db.Order;
import com.qtfx.library.db.Table;
import com.qtfx.platform.db.Fields;
import com.qtfx.platform.db.Schemas;
import com.qtfx.platform.db.Tables;
import com.qtfx.platform.db.fields.FieldInstrumentId;
import com.qtfx.platform.db.fields.FieldPeriodId;
import com.qtfx.platform.db.fields.FieldServerId;
import com.qtfx.platform.db.fields.FieldStatisticsId;
import com.qtfx.platform.util.PersistorUtils;

/**
 * Statistics table definition.
 *
 * @author Miquel Sas
 */
public class TableStatistics extends Table {

	/**
	 * Constructor.
	 */
	public TableStatistics() {
		super();

		setName(Tables.Statistics);
		setSchema(Schemas.qtp);

		addField(new FieldServerId(Fields.SERVER_ID));
		addField(new FieldInstrumentId(Fields.INSTRUMENT_ID));
		addField(new FieldPeriodId(Fields.PERIOD_ID));
		addField(new FieldStatisticsId(Fields.STATISTICS_ID));

		getField(Fields.SERVER_ID).setPrimaryKey(true);
		getField(Fields.INSTRUMENT_ID).setPrimaryKey(true);
		getField(Fields.PERIOD_ID).setPrimaryKey(true);
		getField(Fields.STATISTICS_ID).setPrimaryKey(true);

		Table tablePeriods = new TablePeriods();
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
		order.add(getField(Fields.STATISTICS_ID));
		
		setPersistor(PersistorUtils.getPersistor(getComplexView(order)));
	}

}
