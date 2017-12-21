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

package com.qtfx.plaf.db.fields;

import com.qtfx.lib.db.Field;
import com.qtfx.plaf.db.Domains;

/**
 * Period name field.
 *
 * @author Miquel Sas
 */
public class FieldPeriodName extends Field {

	/**
	 * Constructor.
	 * 
	 * @param name Field name.
	 */
	public FieldPeriodName(String name) {
		super(Domains.getString(name, 15, "Period name", "Period name"));
	}
}
