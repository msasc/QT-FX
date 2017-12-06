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

import javafx.scene.control.PasswordField;

/**
 * A password field control.
 *
 * @author Miquel Sas
 */
public class PasswordTextField extends FieldControl {

	/**
	 * Constructor.
	 * 
	 * @param field The field.
	 */
	public PasswordTextField(Field field) {
		super(field, new PasswordField());
		// Field must be STRING.
		if (!field.isString()) {
			throw new IllegalArgumentException("Field must be of type STRING");
		}
	}

	/**
	 * Return the internal password field.
	 * 
	 * @return The password field.
	 */
	public PasswordField getPasswordField() {
		return (PasswordField) getControl();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setValue(Value value) {
		getValueProperty().set(value);
		getPasswordField().setText(value.getString());
	}
}
