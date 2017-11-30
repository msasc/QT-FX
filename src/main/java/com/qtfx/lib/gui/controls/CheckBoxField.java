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

package com.qtfx.lib.gui.controls;

import com.qtfx.lib.db.Field;
import com.qtfx.lib.db.Value;
import com.qtfx.lib.gui.FieldControl;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;

/**
 * A check box for fields and values of type BOOLEAN.
 *
 * @author Miquel Sas
 */
public class CheckBoxField extends CheckBox implements FieldControl {
	
	/** Field. */
	private Field field;
	/** Observable value. */
	private SimpleObjectProperty<Value> valueProperty;

	/**
	 * Constructor.
	 * 
	 * @param field The field.
	 */
	public CheckBoxField(Field field) {
		super();
		this.field = field;
		
		// Field must be boolean.
		if (!field.isBoolean()) {
			throw new IllegalArgumentException("Field must be of type BOOLEAN");
		}
		// Initialize the value property.
		valueProperty = new SimpleObjectProperty<>(field.getDefaultValue());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field getField() {
		return field;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Value getValue() {
		return valueProperty.get();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setValue(Value value) {
		valueProperty.set(value);
		setSelected(value.getBoolean());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ObservableValue<Value> valueProperty() {
		return valueProperty;
	}
}
