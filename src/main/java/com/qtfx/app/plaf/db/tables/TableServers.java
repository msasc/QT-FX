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

package com.qtfx.app.plaf.db.tables;

import com.qtfx.app.plaf.db.Database;
import com.qtfx.app.plaf.db.Fields;
import com.qtfx.app.plaf.db.fields.FieldServerId;
import com.qtfx.app.plaf.db.fields.FieldServerName;
import com.qtfx.app.plaf.db.fields.FieldServerTitle;
import com.qtfx.lib.db.Table;
import com.qtfx.lib.db.rdbms.DBEngine;
import com.qtfx.lib.db.rdbms.DBPersistor;

/**
 * Servers table definition.
 * 
 * @author Miquel Sas
 */
public class TableServers extends Table {

	/**
	 * Constructor.
	 */
	public TableServers(DBEngine dbEngine) {
		super();
		
		setName(Database.SERVERS);
		setSchema(Database.SYSTEM_SCHEMA);
		
		addField(new FieldServerId(Fields.SERVER_ID));
		addField(new FieldServerName(Fields.SERVER_NAME));
		addField(new FieldServerTitle(Fields.SERVER_TITLE));
		
		getField(Fields.SERVER_ID).setPrimaryKey(true);
		
		setPersistor(new DBPersistor(dbEngine, getComplexView(getPrimaryKey())));
	}

}
