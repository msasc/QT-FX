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
import com.qtfx.lib.gui.FX;
import com.qtfx.lib.gui.FieldControl;
import com.qtfx.lib.gui.action.ActionLookup;

import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;

/**
 * A combo box that launches an <{@link com.qtfx.lib.gui.action.ActionLookup }.
 * 
 *
 * @author Miquel Sas
 */
public class LookupComboBoxField extends FieldControl {

	/**
	 * The combo box.
	 */
	private static class Lookup extends ComboBox<Value> {
		/** Action lookup. */
		private ActionLookup actionLookup;
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void show() {
			// Check action lookup.
			if (actionLookup == null) {
				throw new IllegalStateException("Action lookup required.");
			}
			// Launch the action: source is the field, target the control.
			Field field = FX.getFieldControl(this).getField();
			actionLookup.handle(new ActionEvent(field, this));
		}
	}

	/**
	 * Constructor.
	 * 
	 * @param field The field.
	 * @param actionLookup The action lookup.
	 */
	public LookupComboBoxField(Field field, ActionLookup actionLookup) {
		super(field, new Lookup());
		getLookup().actionLookup = actionLookup;
	}

	/**
	 * Return the lookup control.
	 * 
	 * @return The lookup control.
	 */
	public Lookup getLookup() {
		return (Lookup) getControl();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setValue(Value value) {
		getValueProperty().set(value);
		getLookup().setValue(value);
	}
}
