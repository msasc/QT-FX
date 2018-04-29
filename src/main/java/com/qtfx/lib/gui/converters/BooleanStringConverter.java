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

import java.util.Locale;

import com.qtfx.lib.db.Field;
import com.qtfx.lib.db.Value;
import com.qtfx.lib.util.Formats;

import javafx.util.StringConverter;

/**
 *
 * @author Miquel Sas
 *
 */
public class BooleanStringConverter extends StringConverter<Value> {

	/** Field. */
	private Field field;
	/** Locale. */
	private Locale locale;

	/**
	 * Constructor.
	 * 
	 * @param field The field.
	 * @param locale The locale.
	 */
	public BooleanStringConverter(Field field, Locale locale) {
		super();
		this.field = field;
		this.locale = locale;
		if (!this.field.isBoolean()) {
			throw new IllegalArgumentException("Field is not a boolen");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString(Value value) {
		return Formats.formattedFromBoolean(value.getBoolean(), locale);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Value fromString(String string) {
		boolean value = Formats.formattedToBoolean(string, locale);
		return new Value(value);
	}

}
