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

package com.qtfx.lib.gui;

import java.util.ArrayList;
import java.util.List;

import com.qtfx.lib.db.Field;
import com.qtfx.lib.db.Value;
import com.qtfx.lib.gui.action.ValueAction;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.Control;

/**
 * Root of controls to edit fields. Extenders should define the FX control and override <em>setValue</em>, call the
 * super method and set the control value.
 *
 * @author Miquel Sas
 */
public abstract class FieldControl {

	/**
	 * Listener to fire value actions.
	 */
	class Listener implements ChangeListener<Value> {
		@Override
		public void changed(ObservableValue<? extends Value> observable, Value oldValue, Value newValue) {
			for (ValueAction action : valueActions) {
				action.setOldValue(oldValue);
				action.setNewValue(newValue);
				// Launch the action: source is the field, target the control.
				action.handle(new ActionEvent(field, getControl()));
			}
		}
	}

	/** Field. */
	private Field field;
	/** Observable value. */
	private SimpleObjectProperty<Value> valueProperty;
	/** List of action value. */
	private List<ValueAction> valueActions = new ArrayList<>();
	/** FX control. */
	private Control control;

	/**
	 * Constructor.
	 * 
	 * @param field The field.
	 */
	public FieldControl(Field field, Control control) {
		super();
		this.field = field;
		this.valueProperty = new SimpleObjectProperty<>(field.getDefaultValue());
		this.valueProperty.addListener(new Listener());
		this.control = control;
		FX.setFieldControl(this.control, this);
	}

	/**
	 * Return the field under the control.
	 * 
	 * @return The field.
	 */
	public Field getField() {
		return field;
	}

	/**
	 * Return the value.
	 * 
	 * @return The value.
	 */
	public Value getValue() {
		return valueProperty.get();
	}

	/**
	 * Set the value.
	 * 
	 * @param value The value.
	 */
	public abstract void setValue(Value value);

	/**
	 * Read-write protected access to the value property.
	 * 
	 * @return The value property.
	 */
	protected SimpleObjectProperty<Value> getValueProperty() {
		return valueProperty;
	}

	/**
	 * Clear the control.
	 */
	public void clear() {
		setValue(field.getDefaultValue());
	}

	/**
	 * Add a value action.
	 * 
	 * @param valueAction The value action.
	 */
	public void addValueAction(ValueAction valueAction) {
		valueActions.add(valueAction);
	}

	/**
	 * Give access to the value property.
	 * 
	 * @return The value property.
	 */
	public ObservableValue<Value> valueProperty() {
		return valueProperty;
	}

	/**
	 * Return the underlying control.
	 * 
	 * @return The underlying control.
	 */
	public Control getControl() {
		return control;
	}
}
