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

package com.qtfx.lib.gui.converters;

import java.util.function.UnaryOperator;

import com.qtfx.lib.db.Field;
import com.qtfx.lib.db.Value;

import javafx.scene.control.TextFormatter.Change;
import javafx.util.StringConverter;

/**
 * String converter for string fields that uses the field properties.
 *
 * @author Miquel Sas
 */
public class FieldStringConverter extends StringConverter<Value> {
	
	static class Filter implements UnaryOperator<Change> {
		@Override
		public Change apply(Change t) {
			t.setText(t.getText().toUpperCase());
			return t;
		}
	}
	
	/** Field. */
	private Field field;

	/**
	 * Constructor.
	 * 
	 * @param field The field.
	 */
	public FieldStringConverter(Field field) {
		super();
		this.field = field;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString(Value value) {
		if (value == null) {
			return toString(field.getDefaultValue());
		}
		String string = value.toString();
		if (field.getStringConverter() != null) {
			string = field.getStringConverter().toString(value);
		}
		return string;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Value fromString(String string) {
		if (field.getStringConverter() != null) {
			return field.getStringConverter().fromString(string);
		}
		return new Value(string);
	}
}
