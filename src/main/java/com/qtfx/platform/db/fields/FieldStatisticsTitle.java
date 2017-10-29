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

package com.qtfx.platform.db.fields;

import com.qtfx.library.db.Field;
import com.qtfx.platform.db.Domains;

/**
 * Statistics title field.
 *
 * @author Miquel Sas
 */
public class FieldStatisticsTitle extends Field {

	/**
	 * Constructor.
	 * 
	 * @param name Field name.
	 */
	public FieldStatisticsTitle(String name) {
		super(Domains.getString(name, 100, "Statistics title", "Statistics title"));
	}
}
