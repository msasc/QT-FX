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
import com.qtfx.lib.gui.converters.NumberStringConverter;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

/**
 * A text field for fields of type NUMBER.
 *
 * @author Miquel Sas
 */
public class NumberTextField extends FieldControl {

	/**
	 * Constructor.
	 * 
	 * @param field The field.
	 */
	public NumberTextField(Field field) {
		this(field, Locale.getDefault());
	}

	/**
	 * Constructor.
	 * 
	 * @param field The field.
	 * @param locale The locale.
	 */
	public NumberTextField(Field field, Locale locale) {
		super(field, new TextField());

		// Field must be NUMBER.
		if (!field.isString()) {
			throw new IllegalArgumentException("Field must be of type NUMBER");
		}
		// If there is a text formatter, install it, else, if there is a string formatter install a default text
		// formatter using it.
		if (field.getTextFormatter() != null) {
			getTextField().setTextFormatter(field.getTextFormatter());
		} else if (field.getStringConverter() != null) {
			getTextField().setTextFormatter(new TextFormatter<Value>(field.getStringConverter()));
		} else {
			getTextField().setTextFormatter(new TextFormatter<Value>(new NumberStringConverter(field, locale)));
		}
	}

	/**
	 * Return the text field.
	 * 
	 * @return The text field.
	 */
	private TextField getTextField() {
		return (TextField) getControl();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setValue(Value value) {
		getValueProperty().set(value);
		@SuppressWarnings("unchecked")
		TextFormatter<Value> formatter = (TextFormatter<Value>) getTextField().getTextFormatter();
		if (formatter != null) {
			formatter.setValue(value);
		} else {
			getTextField().setText(value.toString());
		}
	}
}
