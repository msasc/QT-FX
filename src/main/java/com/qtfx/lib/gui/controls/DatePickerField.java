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

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.DatePicker;
import javafx.util.converter.LocalDateStringConverter;

/**
 * Date picker for fields of type DATE. Standard ISO format is used.
 *
 * @author Miquel Sas
 */
public class DatePickerField extends FieldControl {

	/**
	 * Change listener to forward changes.
	 */
	class ValueListener implements ChangeListener<LocalDate> {
		@Override
		public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
			DatePickerField.this.getValueProperty().set(new Value(newValue));
		}
	}

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
		super(field, new DatePicker());

		// Field must be DATE.
		if (!field.isDate()) {
			throw new IllegalArgumentException("Field must be of type DATE");
		}

		// Date picker setup
		getDatePicker().setEditable(false);
		getDatePicker().setConverter(
			new LocalDateStringConverter(
				DateTimeFormatter.ISO_LOCAL_DATE,
				DateTimeFormatter.ISO_LOCAL_DATE));
		getDatePicker().valueProperty().addListener(new ValueListener());
	}

	/**
	 * Return the date picker control.
	 * 
	 * @return The date picker control.
	 */
	public DatePicker getDatePicker() {
		return (DatePicker) getControl();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setValue(Value value) {
		getValueProperty().set(value);
		getDatePicker().setValue(value.getLocalDate());
	}
}
