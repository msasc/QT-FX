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

import java.util.Locale;

import com.qtfx.lib.db.Field;
import com.qtfx.lib.db.Record;
import com.qtfx.lib.db.Value;
import com.qtfx.lib.util.Formats;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
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
			return Formats.getNumberFormat(field.getDisplayDecimals(), locale).format(value.getDouble());
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Value fromString(String string) {
			return null;
		}
	}

	/**
	 * String converter for booleans.
	 */
	class BooleanStringConverter extends StringConverter<Value> {
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
			return null;
		}
	}

	/**
	 * String converter for a possible value.
	 */
	class PossibleValueStringConverter extends StringConverter<Value> {
		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString(Value value) {
			if (value instanceof Field.PossibleValue) {
				Field.PossibleValue pv = (Field.PossibleValue) value;
				return pv.getLabel();
			}
			return value.toString();
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
	
	/** List for boolean values. */
	private ObservableList<Value> booleanValues;
	/** List for possible values. */
	private ObservableList<Value> possibleValues;

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
		
		booleanValues = FXCollections.observableArrayList(new Value(true), new Value(false));
		if (field.isPossibleValues()) {
			possibleValues = FXCollections.observableArrayList(field.getPossibleValues());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TableCell<Record, Value> call(TableColumn<Record, Value> param) {
		items = param.getTableView().getItems();
		if (field.isPossibleValues()) {
			tableCell = new ComboBoxTableCell<>(new PossibleValueStringConverter(), possibleValues);
		} else if (field.getStringConverter() != null) {
			tableCell = new TextFieldTableCell<>(field.getStringConverter());
		} else {
			switch (field.getType()) {
			case BOOLEAN:
				if (field.isEditBooleanInCheckBox()) {
					tableCell = new CheckBoxTableCell<Record, Value>(new CheckBoxCell());
				} else {
					tableCell = new ComboBoxTableCell<>(new BooleanStringConverter(), booleanValues);
				}
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
		}
		if (field.getStyle() != null) {
			tableCell.setStyle(field.getStyle());
		}
		return tableCell;
	}

}
