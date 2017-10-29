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

import com.qtfx.library.db.Table;
import com.qtfx.platform.db.Fields;
import com.qtfx.platform.db.Schemas;
import com.qtfx.platform.db.Tables;
import com.qtfx.platform.db.fields.FieldInstrumentDesc;
import com.qtfx.platform.db.fields.FieldInstrumentId;
import com.qtfx.platform.db.fields.FieldInstrumentPipScale;
import com.qtfx.platform.db.fields.FieldInstrumentPipValue;
import com.qtfx.platform.db.fields.FieldInstrumentPrimaryCurrency;
import com.qtfx.platform.db.fields.FieldInstrumentSecondaryCurrency;
import com.qtfx.platform.db.fields.FieldInstrumentTickScale;
import com.qtfx.platform.db.fields.FieldInstrumentTickValue;
import com.qtfx.platform.db.fields.FieldInstrumentVolumeScale;
import com.qtfx.platform.db.fields.FieldServerId;
import com.qtfx.platform.util.PersistorUtils;

/**
 * Instruments table definition.
 * 
 * @author Miquel Sas
 */
public class TableInstruments extends Table {

	/**
	 * Constructor.
	 */
	public TableInstruments() {
		super();
		
		setName(Tables.Instruments);
		setSchema(Schemas.qtp);
		
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
		
		setPersistor(PersistorUtils.getPersistor(getSimpleView()));
	}

}
