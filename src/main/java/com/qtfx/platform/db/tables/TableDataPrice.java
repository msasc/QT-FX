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

import com.qtfx.library.db.Index;
import com.qtfx.library.db.Table;
import com.qtfx.library.mkt.data.Instrument;
import com.qtfx.library.mkt.server.Server;
import com.qtfx.platform.db.Fields;
import com.qtfx.platform.db.Schemas;
import com.qtfx.platform.db.fields.FieldDataInstr;
import com.qtfx.platform.db.fields.FieldIndex;
import com.qtfx.platform.db.fields.FieldTime;
import com.qtfx.platform.db.fields.FieldTimeFmt;
import com.qtfx.platform.db.fields.FieldVolume;
import com.qtfx.platform.util.PersistorUtils;

/**
 * Price table definition.
 * 
 * @author Miquel Sas
 */
public class TableDataPrice extends Table {

	/**
	 * Constructor.
	 * 
	 * @param server The server.
	 * @param instrument Instrument.
	 * @param name The table name.
	 */
	public TableDataPrice(Server server, Instrument instrument, String name) {
		super();

		setName(name);
		setSchema(Schemas.server(server));

		addField(new FieldIndex(Fields.INDEX));
		addField(new FieldTime(Fields.TIME));
		addField(new FieldDataInstr(instrument, Fields.OPEN, "Open", "Open value"));
		addField(new FieldDataInstr(instrument, Fields.HIGH, "High", "High value"));
		addField(new FieldDataInstr(instrument, Fields.LOW, "Low", "Low value"));
		addField(new FieldDataInstr(instrument, Fields.CLOSE, "Close", "Close value"));
		addField(new FieldVolume(Fields.VOLUME));
		addField(new FieldTimeFmt(Fields.TIME_FMT));

		getField(Fields.TIME).setPrimaryKey(true);
		
		Index index = new Index();
		index.add(getField(Fields.INDEX));
		index.setUnique(true);
		addIndex(index);
		
		setPersistor(PersistorUtils.getPersistor(getSimpleView()));
	}

}
