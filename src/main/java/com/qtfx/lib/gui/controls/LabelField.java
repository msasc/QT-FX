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

import javafx.scene.control.Label;

/**
 * A field control that is a label, normally used for refresh fields.
 *
 * @author Miquel Sas
 */
public class LabelField extends FieldControl {

	/**
	 * @param field
	 */
	public LabelField(Field field) {
		super(field, new Label());
	}

	/**
	 * Return the label.
	 * 
	 * @return The label.
	 */
	public Label getLabel() {
		return (Label) getControl();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setValue(Value value) {
		getValueProperty().set(value);
		getLabel().setText(value.toString());
	}
}
