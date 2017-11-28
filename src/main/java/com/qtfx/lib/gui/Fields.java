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

import java.util.Locale;

import com.qtfx.lib.db.Field;

import javafx.scene.control.Control;
import javafx.scene.control.Label;

/**
 * Field utilities for the GUI.
 *
 * @author Miquel Sas
 */
public class Fields {

	/**
	 * Return a suitable id for a control of a field, using its alias.
	 * 
	 * @param alias The field alias.
	 * @return The control id.
	 */
	public static String getIdControl(String alias) {
		return "control" + "-" + alias;
	}

	/**
	 * Return a suitable id for a label of a field, using its alias.
	 * 
	 * @param alias The field alias.
	 * @return The label id.
	 */
	public static String getIdLabel(String alias) {
		return "label" + "-" + alias;
	}

	/**
	 * Return the label for a field in a form.
	 * 
	 * @param field The field.
	 * @return The label.
	 */
	public static Label getFormFieldLabel(Field field) {
		Label label = new Label(field.getDisplayLabel());
		label.setId(getIdLabel(field.getAlias()));
		return label;
	}

	/**
	 * Return a suitable edit control for the field.
	 * 
	 * @param field The field.
	 * @param locale The locale.
	 * @return The edit control.
	 */
	public static Control getFormFieldControl(Field field, Locale locale) {
		
		return null;
	}
}
