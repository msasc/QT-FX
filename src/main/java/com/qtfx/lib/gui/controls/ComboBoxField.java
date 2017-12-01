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
import com.qtfx.lib.gui.converters.PossibleValueStringConverter;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;

/**
 * A combo box for fields with multiple value validation.
 *
 * @author Miquel Sas
 */
public class ComboBoxField extends ComboBox<Value> implements FieldControl {
	
	/** Field. */
	private Field field;
	
	/**
	 * Constructor.
	 * 
	 * @param field The field.
	 */
	public ComboBoxField(Field field) {
		super();
		this.field = field;
		
		// Field must be boolean.
		if (!field.isPossibleValues()) {
			throw new IllegalArgumentException("Field must have a possible values validation.");
		}
		
		// Set the list of items as the possible values.
		setItems(FXCollections.observableArrayList(field.getPossibleValues()));
		setConverter(new PossibleValueStringConverter(field));
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Node getNode() {
		return this;
	}
}
