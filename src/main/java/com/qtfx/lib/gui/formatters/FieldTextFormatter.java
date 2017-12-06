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

package com.qtfx.lib.gui.formatters;

import java.util.function.UnaryOperator;

import com.qtfx.lib.db.Field;
import com.qtfx.lib.db.Value;
import com.qtfx.lib.gui.converters.FieldStringConverter;

import javafx.scene.control.TextFormatter;

/**
 * Formatter for string fields that uses all related field properties.
 *
 * @author Miquel Sas
 */
public class FieldTextFormatter extends TextFormatter<Value> {

	/**
	 * Filter.
	 */
	private static class Filter implements UnaryOperator<Change> {
		/** The field. */
		private Field field;

		/**
		 * Constructor.
		 * 
		 * @param field The field.
		 */
		public Filter(Field field) {
			super();
			this.field = field;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Change apply(Change t) {
			if (field.getTextFormatter() != null) {
				t = field.getTextFormatter().getFilter().apply(t);
			}
			if (field.isUppercase()) {
				t.setText(t.getText().toUpperCase());
			}
			if (field.getLength() > 0) {
				if (t.getControlNewText().length() > field.getLength()) {
					String text = t.getText();
					int subtract = t.getControlNewText().length() - field.getLength();
					if (text.length() >= subtract) {
						text = text.substring(0, text.length() - subtract);
					}
					t.setText(text);
				}
			}
			return t;
		}
	}

	/**
	 * Constructor.
	 * 
	 * @param field The field.
	 */
	public FieldTextFormatter(Field field) {
		super(new FieldStringConverter(field), field.getDefaultValue(), new Filter(field));
	}
}
