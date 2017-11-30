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

import java.util.Locale;

import com.qtfx.lib.db.Field;
import com.qtfx.lib.db.Value;
import com.qtfx.lib.gui.FieldControl;
import com.qtfx.lib.gui.converters.BooleanStringConverter;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;

/**
 * A choice box for fields of type BOOLEAN.
 *
 * @author Miquel Sas
 */
public class ChoiceBoxField extends ChoiceBox<Value> implements FieldControl {
	
	/** Field. */
	private Field field;
	
	/**
	 * Constructor.
	 * 
	 * @param field The field.
	 */
	public ChoiceBoxField(Field field) {
		this(field, Locale.getDefault());
	}

	/**
	 * Constructor.
	 * 
	 * @param field The field.
	 * @param locale The locale to use to convert yes/no values.
	 */
	public ChoiceBoxField(Field field, Locale locale) {
		super();
		this.field = field;
		
		// Field must be boolean.
		if (!field.isBoolean()) {
			throw new IllegalArgumentException("Field must be of type BOOLEAN");
		}
		
		// Set the list of items and the string converter.
		ObservableList<Value> possibleValues = FXCollections.observableArrayList(new Value(true), new Value(false));
		setItems(possibleValues);
		setConverter(new BooleanStringConverter(field, locale));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field getFieldDef() {
		return field;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Value getFieldValue() {
		return getValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setFieldValue(Value value) {
		setValue(value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ObservableValue<Value> fieldValueProperty() {
		return valueProperty();
	}
}
