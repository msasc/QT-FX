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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.qtfx.lib.db.Field;
import com.qtfx.lib.db.Value;
import com.qtfx.lib.gui.FieldControl;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.util.converter.LocalDateStringConverter;

/**
 * Date picker for fields of type DATE. Standard ISO format is used.
 *
 * @author Miquel Sas
 */
public class DatePickerField extends DatePicker implements FieldControl {
	
	/**
	 * Change listener to forward changes.
	 */
	class ValueListener implements ChangeListener<LocalDate> {
		@Override
		public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
			fieldValueProperty.set(new Value(newValue));
		}
	}

	/** Field. */
	private Field field;
	/** Observable value. */
	private SimpleObjectProperty<Value> fieldValueProperty;

	/**
	 * Constructor.
	 * 
	 * @param field The field.
	 */
	public DatePickerField(Field field) {
		this(field, null);
	}

	/**
	 * Constructor.
	 * 
	 * @param field The field.
	 * @param localDate The local date.
	 */
	public DatePickerField(Field field, LocalDate localDate) {
		super(localDate);
		this.field = field;
		
		// Field must be DATE.
		if (!field.isDate()) {
			throw new IllegalArgumentException("Field must be of type DATE");
		}
		// Initialize the value property.
		fieldValueProperty = new SimpleObjectProperty<>(field.getDefaultValue());
		
		setEditable(false);
		setConverter(new LocalDateStringConverter(DateTimeFormatter.ISO_LOCAL_DATE,DateTimeFormatter.ISO_LOCAL_DATE));
		
		valueProperty().addListener(new ValueListener());
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
		setValue(value.getLocalDate());
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
