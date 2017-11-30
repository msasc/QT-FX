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

import com.qtfx.lib.db.Field;
import com.qtfx.lib.db.Value;

import javafx.beans.value.ObservableValue;

/**
 * Interface for controls to edit fields.
 *
 * @author Miquel Sas
 */
public interface FieldControl {

	/**
	 * Return the field under the control.
	 * 
	 * @return The field.
	 */
	Field getFieldDef();

	/**
	 * Return the value. Used instead of getValue to avoid conflicts.
	 * 
	 * @return The value.
	 */
	Value getFieldValue();

	/**
	 * Set the value. Used instead of getValue to avoid conflicts.
	 * 
	 * @param value The value.
	 */
	void setFieldValue(Value value);

	/**
	 * Return the value property to be able to add change and invalidation listeners. Used instead of valueProperty to
	 * avoid conflicts.
	 * 
	 * @return The value property.
	 */
	ObservableValue<Value> fieldValueProperty();
}
