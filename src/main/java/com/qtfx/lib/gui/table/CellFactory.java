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

import java.text.NumberFormat;
import java.util.Locale;

import com.qtfx.lib.db.Field;
import com.qtfx.lib.db.Record;
import com.qtfx.lib.db.Value;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 * A table cell factory that relies on fields.
 *
 * @author Miquel Sas
 */
public class CellFactory implements Callback<TableColumn<Record, Value>, TableCell<Record, Value>> {

	/**
	 * Check box callback for boolean values.
	 */
	class CheckBoxCell implements Callback<Integer, ObservableValue<Boolean>> {
		/**
		 * {@inheritDoc}
		 */
		@Override
		public ObservableValue<Boolean> call(Integer index) {
			Record record = items.get(index);
			Value value = record.getValue(field.getAlias());
			return new SimpleBooleanProperty(value.getBoolean());
		}
	}

	/**
	 * String converter for numbers.
	 */
	class NumberStringConverter extends StringConverter<Value> {
		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString(Value value) {
			int decimals = field.getDisplayDecimals();
			NumberFormat formatted = NumberFormat.getNumberInstance(locale);
			if (decimals >= 0) {
				formatted.setMaximumFractionDigits(decimals);
				formatted.setMinimumFractionDigits(decimals);
			}
			return formatted.format(value.getDouble());
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Value fromString(String string) {
			return null;
		}

	}

	/** The underlying field. */
	private Field field;
	/** Optional locale. */
	private Locale locale;
	/** List of items. */
	private transient ObservableList<Record> items;
	/** Table cell. */
	private transient TableCell<Record, Value> tableCell;

	/**
	 * Constructor.
	 * 
	 * @param field The underlying field.
	 */
	public CellFactory(Field field) {
		this(field, Locale.getDefault());
	}

	/**
	 * Constructor.
	 * 
	 * @param field The underlying field.
	 * @param locale The locale.
	 */
	public CellFactory(Field field, Locale locale) {
		super();
		this.field = field;
		this.locale = locale;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TableCell<Record, Value> call(TableColumn<Record, Value> param) {
		items = param.getTableView().getItems();
		switch (field.getType()) {
		case BOOLEAN:
			tableCell = new CheckBoxTableCell<Record, Value>(new CheckBoxCell());
			break;
		case STRING:
			tableCell = new TextFieldTableCell<>();
			break;
		case DECIMAL:
		case DOUBLE:
		case INTEGER:
		case LONG:
			tableCell = new TextFieldTableCell<>(new NumberStringConverter());
			tableCell.setAlignment(Pos.CENTER_RIGHT);
			break;
		default:
			tableCell = new TableColumn<Record, Value>().getCellFactory().call(param);
			break;
		}
		return tableCell;
	}

}
