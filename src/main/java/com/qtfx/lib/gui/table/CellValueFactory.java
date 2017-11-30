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

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

/**
 * A table cell value factory that relies on fields.
 *
 * @author Miquel Sas
 */
public class CellValueFactory implements Callback<CellDataFeatures<Record, Value>, ObservableValue<Value>> {

	/** The field. */
	private Field field;
	/** The call back record. */
	private transient Record record;

	/**
	 * Constructor assigning the field alias.
	 * 
	 * @param field The field.
	 * @param locale The required locale.
	 */
	public CellValueFactory(Field field) {
		super();
		this.field = field;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ObservableValue<Value> call(CellDataFeatures<Record, Value> param) {
		this.record = param.getValue();
		Value value = record.getValue(field.getAlias());
		return new SimpleObjectProperty<Value>(value);
	}
}
