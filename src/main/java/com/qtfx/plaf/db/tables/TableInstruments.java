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

import com.qtfx.lib.db.Table;
import com.qtfx.lib.db.rdbms.DBEngine;
import com.qtfx.lib.db.rdbms.DBPersistor;
import com.qtfx.plaf.db.Database;
import com.qtfx.plaf.db.Fields;
import com.qtfx.plaf.db.fields.FieldInstrumentDesc;
import com.qtfx.plaf.db.fields.FieldInstrumentId;
import com.qtfx.plaf.db.fields.FieldInstrumentPipScale;
import com.qtfx.plaf.db.fields.FieldInstrumentPipValue;
import com.qtfx.plaf.db.fields.FieldInstrumentPrimaryCurrency;
import com.qtfx.plaf.db.fields.FieldInstrumentSecondaryCurrency;
import com.qtfx.plaf.db.fields.FieldInstrumentTickScale;
import com.qtfx.plaf.db.fields.FieldInstrumentTickValue;
import com.qtfx.plaf.db.fields.FieldInstrumentVolumeScale;
import com.qtfx.plaf.db.fields.FieldServerId;

/**
 * Instruments table definition.
 * 
 * @author Miquel Sas
 */
public class TableInstruments extends Table {

	/**
	 * Constructor.
	 */
	public TableInstruments(DBEngine dbEngine) {
		super();
		
		setName(Database.INSTRUMENTS);
		setSchema(Database.SYSTEM_SCHEMA);
		
		addField(new FieldServerId(Fields.SERVER_ID));
		addField(new FieldInstrumentId(Fields.INSTRUMENT_ID));
		addField(new FieldInstrumentDesc(Fields.INSTRUMENT_DESC));
		addField(new FieldInstrumentPipValue(Fields.INSTRUMENT_PIP_VALUE));
		addField(new FieldInstrumentPipScale(Fields.INSTRUMENT_PIP_SCALE));
		addField(new FieldInstrumentTickValue(Fields.INSTRUMENT_TICK_VALUE));
		addField(new FieldInstrumentTickScale(Fields.INSTRUMENT_TICK_SCALE));
		addField(new FieldInstrumentVolumeScale(Fields.INSTRUMENT_VOLUME_SCALE));
		addField(new FieldInstrumentPrimaryCurrency(Fields.INSTRUMENT_PRIMARY_CURRENCY));
		addField(new FieldInstrumentSecondaryCurrency(Fields.INSTRUMENT_SECONDARY_CURRENCY));
		
		getField(Fields.SERVER_ID).setPrimaryKey(true);
		getField(Fields.INSTRUMENT_ID).setPrimaryKey(true);
		
		setPersistor(new DBPersistor(dbEngine, getComplexView(getPrimaryKey())));
	}

}
