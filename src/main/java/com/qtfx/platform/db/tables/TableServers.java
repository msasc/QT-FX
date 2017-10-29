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
import com.qtfx.platform.db.fields.FieldServerId;
import com.qtfx.platform.db.fields.FieldServerName;
import com.qtfx.platform.db.fields.FieldServerTitle;
import com.qtfx.platform.util.PersistorUtils;

/**
 * Servers table definition.
 * 
 * @author Miquel Sas
 */
public class TableServers extends Table {

	/**
	 * Constructor.
	 */
	public TableServers() {
		super();
		
		setName(Tables.Servers);
		setSchema(Schemas.qtp);
		
		addField(new FieldServerId(Fields.SERVER_ID));
		addField(new FieldServerName(Fields.SERVER_NAME));
		addField(new FieldServerTitle(Fields.SERVER_TITLE));
		
		getField(Fields.SERVER_ID).setPrimaryKey(true);
		
		setPersistor(PersistorUtils.getPersistor(getSimpleView()));
	}

}
