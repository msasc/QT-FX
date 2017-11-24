/*
 * Copyright (C) 2017 Miquel Sas
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

package com.qtfx.lib.gui.table;

import com.qtfx.lib.db.Field;
import com.qtfx.lib.db.Record;
import com.qtfx.lib.db.Value;

import javafx.scene.control.TableColumn;

/**
 * Table column utilities.
 *
 * @author Miquel Sas
 */
public class TableColumns {

	public static TableColumn<Record, Value> getColumn(Field field, boolean sortable) {
		TableColumn<Record, Value> column = new TableColumn<>(field.getHeader());
		column.setCellValueFactory(new CellValueFactory(field));
		column.setCellFactory(new CellFactory(field));
		column.setSortable(sortable);
		column.setUserData(field);
		return column;
	}
}
