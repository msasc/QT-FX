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
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

/**
 * A text field for fields of type STRING edited in a single line control.
 *
 * @author Miquel Sas
 */
public class StringTextField extends TextField implements FieldControl {

	/** Field. */
	private Field field;
	/** Observable value. */
	private SimpleObjectProperty<Value> fieldValueProperty;

	/**
	 * Constructor.
	 * 
	 * @param field The field.
	 */
	public StringTextField(Field field) {
		super();
		this.field = field;

		// Field must be STRING.
		if (!field.isString()) {
			throw new IllegalArgumentException("Field must be of type STRING");
		}
		// Initialize the value property.
		fieldValueProperty = new SimpleObjectProperty<>(field.getDefaultValue());
		// If there is a text formatter, install it, else, if there is a string formatter install a default text
		// formatter using it.
		if (field.getTextFormatter() != null) {
			setTextFormatter(field.getTextFormatter());
		} else if (field.getStringConverter() != null) {
			setTextFormatter(new TextFormatter<Value>(field.getStringConverter()));
		}
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
		return fieldValueProperty.get();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setFieldValue(Value value) {
		fieldValueProperty.set(value);
		@SuppressWarnings("unchecked")
		TextFormatter<Value> formatter = (TextFormatter<Value>) getTextFormatter();
		if (formatter != null) {
			formatter.setValue(value);
		} else {
			setText(value.toString());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ObservableValue<Value> fieldValueProperty() {
		return fieldValueProperty;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Node getNode() {
		return this;
	}
}
