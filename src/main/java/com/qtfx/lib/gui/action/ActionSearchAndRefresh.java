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

package com.qtfx.lib.gui.action;

import java.util.ArrayList;
import java.util.List;

import com.qtfx.lib.db.Value;
import com.qtfx.lib.gui.FX;
import com.qtfx.lib.gui.FieldControl;
import com.qtfx.lib.gui.FormRecordPane;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Control;

/**
 * Action to search values to refresh field controls, executed as a value action.
 * 
 * @author Miquel Sas
 */
public abstract class ActionSearchAndRefresh extends ValueAction {

	/** The list of aliases of key field controls to build the search key. */
	private List<String> keyFields = new ArrayList<>();
	/** The list of aliases of refresh field controls. */
	private List<String> refreshFields = new ArrayList<>();
	/** The list of aliases of clear field controls. */
	private List<String> clearFields = new ArrayList<>();

	/**
	 * Constructor.
	 */
	public ActionSearchAndRefresh() {
		super();
	}

	/**
	 * Adds a field alias to the list of key aliases.
	 * 
	 * @param alias The key alias.
	 */
	public void addKeyField(String alias) {
		keyFields.add(alias);
	}

	/**
	 * Adds a field alias to the list of refresh aliases.
	 * 
	 * @param alias The refresh alias.
	 */
	public void addRefreshField(String alias) {
		refreshFields.add(alias);
	}

	/**
	 * Adds a field alias to the list of clear aliases.
	 * 
	 * @param alias The component name.
	 */
	public void addClearField(String alias) {
		clearFields.add(alias);
	}

	/**
	 * Get by any means the list of refresh values, in the same order as the refresh fields.
	 * 
	 * @param keyControls The list of key field controls.
	 * @param refreshControls The list of refresh field controls.
	 * @return The list of refresh values.
	 */
	protected abstract List<Value> getRefreshValues(List<FieldControl> keyControls, List<FieldControl> refreshControls);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handle(ActionEvent event) {

		// Get the list of controls.
		Node node = (Node) event.getTarget();
		FormRecordPane pane = FX.getFormRecordPane(node);
		if (pane == null) {
			throw new IllegalStateException("No FormRecordPane");
		}
		List<Control> controls = FX.getControls(pane.getNode());

		// Build the list of key controls.
		List<FieldControl> keyControls = new ArrayList<>();
		for (String alias : keyFields) {
			FieldControl keyControl = FX.getFieldControl(alias, controls);
			if (keyControl == null) {
				throw new IllegalStateException("Key control " + alias + " not found");
			}
			keyControls.add(keyControl);
		}

		// Build the list of refresh controls.
		List<FieldControl> refreshControls = new ArrayList<>();
		for (String alias : refreshFields) {
			FieldControl refreshControl = FX.getFieldControl(alias, controls);
			if (refreshControl == null) {
				throw new IllegalStateException("Refresh control " + alias + " not found");
			}
			refreshControls.add(refreshControl);
		}

		// Get the list of values to apply to refresh controls.
		if (!refreshControls.isEmpty()) {
			List<Value> refreshValues = getRefreshValues(keyControls, refreshControls);
			if (!refreshValues.isEmpty()) {
				for (int i = 0; i < refreshControls.size(); i++) {
					if (refreshValues.size() <= i) {
						break;
					}
					refreshControls.get(i).setValue(refreshValues.get(i));
				}
			} else {
				// Clear refresh controls.
				for (int i = 0; i < refreshControls.size(); i++) {
					refreshControls.get(i).clear();
				}
			}
		}

		// Clear the clear controls.
		for (String alias : clearFields) {
			FieldControl clearControl = FX.getFieldControl(alias, controls);
			if (clearControl == null) {
				throw new IllegalStateException("Clear control " + alias + " not found");
			}
			clearControl.clear();
		}
	}

}
