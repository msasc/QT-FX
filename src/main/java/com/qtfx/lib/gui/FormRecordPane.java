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
import java.util.Locale;

import com.qtfx.lib.db.Field;
import com.qtfx.lib.db.FieldGroup;
import com.qtfx.lib.db.Record;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

/**
 * A panel that holds a grid form of fields from a record. Fields are laid out in tabs by field group if there are field
 * groups, and optionally in sub-panels defined by (grid_x, grid_y) values.
 * <p>
 * Usage:
 * <ul>
 * <li>Instantiate the panel.</li>
 * <li>Set the edit mode.</li>
 * <li>Set the master record.</li>
 * <li>Add fields optionally indicating the grid coordinates of the sub-panel.</li>
 * </ul>
 *
 * @author Miquel Sas
 *
 */
public class FormRecordPane {

	/**
	 * Utility class to manage the controls automatically used by the form record pane.
	 * <p>
	 * Controls in a form-record-pane depend of the field type and can only be the following.
	 * <ul>
	 * <li>Instantiate the panel.</li>
	 * </ul>
	 */
	public static class Controls {

	}

	/**
	 * Grid item structure.
	 */
	private static class GridItem {
		int gridx;
		int gridy;
		List<Field> fields = new ArrayList<>();

		GridItem(int gridx, int gridy) {
			this.gridx = gridx;
			this.gridy = gridy;
		}

		@Override
		public boolean equals(Object o) {
			if (o instanceof GridItem) {
				GridItem gridItem = (GridItem) o;
				return (gridx == gridItem.gridx && gridy == gridItem.gridy);
			}
			return false;
		}
	}

	/**
	 * Group item structure.
	 */
	private static class GroupItem implements Comparable<GroupItem> {
		FieldGroup fieldGroup;
		List<GridItem> gridItems = new ArrayList<>();

		GroupItem(FieldGroup fieldGroup) {
			this.fieldGroup = fieldGroup;
		}

		@Override
		public boolean equals(Object o) {
			if (o instanceof GroupItem) {
				GroupItem groupItem = (GroupItem) o;
				return fieldGroup.equals(groupItem.fieldGroup);
			}
			return false;
		}

		@Override
		public int compareTo(GroupItem groupItem) {
			return fieldGroup.compareTo(groupItem.fieldGroup);
		}
	}

	/////////////////////////
	// Definition properties.

	/** Locale. */
	private Locale locale;
	/** Edit mode. */
	private EditMode editMode = EditMode.NO_RESTRICTION;
	/** The record. */
	private Record record;
	/** List of group items. */
	private List<GroupItem> groupItems = new ArrayList<>();

	////////////////////////
	// Layout FX components.

	/**
	 * The border pane that has in the center either a tab pane or a grid pane, depending on the number of field groups
	 * defined by the fields added to the form.
	 */
	private BorderPane borderPane;

	/**
	 * Constructor.
	 */
	public FormRecordPane() {
		this(Locale.getDefault());
	}

	/**
	 * Constructor.
	 * 
	 * @param locale The locale.
	 */
	public FormRecordPane(Locale locale) {
		super();
		this.locale = locale;
		this.borderPane = new BorderPane();
	}

	//////////////////////////////////////////////////
	// Layout FX components and additional operations.

	/**
	 * Return the node to install in the scene.
	 * 
	 * @return The node to install in the scene.
	 */
	public Node getNode() {
		return borderPane;
	}

	/**
	 * Set the padding.
	 * 
	 * @param insets The padding.
	 */
	public void setPadding(Insets insets) {
		borderPane.setPadding(insets);
	}

	////////////////////////////////////
	// Methods to add and remove fields.

	/**
	 * Add a field to the default (0, 0) sub-panel.
	 * 
	 * @param alias The field alias.
	 */
	public void addField(String alias) {
		addField(alias, 0, 0);
	}

	/**
	 * Add a field setting its grid coordinates. Grid coordinates are relative to the field group of the field, that is,
	 * the tab where the field group will be assigned. Grid coordinates are recommended to be set sequentially.
	 * 
	 * @param alias The field alias.
	 * @param gridx The grid x coordinate.
	 * @param gridy The grid y coordinate.
	 */
	public void addField(String alias, int gridx, int gridy) {

		// State validation.
		if (getRecord() == null) {
			throw new IllegalStateException("The record to edit must be set prior to add any field.");
		}
		if (gridx < 0) {
			throw new IllegalArgumentException("Invalid grid x coordinate.");
		}
		if (gridy < 0) {
			throw new IllegalArgumentException("Invalid grid y coordinate.");
		}

		// Field (a copy), group item and grid item.
		Field field = new Field(getRecord().getField(alias));
		GroupItem groupItem = getGroupItem(field.getFieldGroup());
		GridItem gridItem = getGridItem(groupItem, gridx, gridy);
		gridItem.fields.add(field);
		groupItems.sort(null);
	}

	/**
	 * Clear the list of fields to restart layout.
	 */
	public void clearFields() {
		groupItems.clear();
	}

	//////////////////////////////////////////////
	// Layout fields and required utility helpers.

	/**
	 * Auto-layout fields, attending at the group and grid definition.
	 */
	public void layoutFields() {

		// Current field groups.
		List<FieldGroup> fieldGroups = getFieldGroups();

		// If we have only one field group, we will add a single grid pane in the center of the border pane, otherwise
		// we will add a tab pane with a grid pane for each field group in each tab.

	}

	/**
	 * Returns the list of defined/used field groups.
	 * 
	 * @return The list of defined/used field groups.
	 */
	private List<FieldGroup> getFieldGroups() {
		List<FieldGroup> fieldGroups = new ArrayList<>();
		for (GroupItem groupItem : groupItems) {
			FieldGroup fieldGroup = groupItem.fieldGroup;
			if (!fieldGroups.contains(fieldGroup)) {
				fieldGroups.add(fieldGroup);
			}
		}
		return fieldGroups;
	}

	/**
	 * Returns the number of rows in a group item.
	 * 
	 * @param groupItem The group item.
	 * @param column The column.
	 * @return The number of rows.
	 */
	private int getGroupItemRows(GroupItem groupItem, int column) {
		int rows = 0;
		List<GridItem> gridItems = getGridItems(groupItem, column);
		for (GridItem gridItem : gridItems) {
			rows = Math.max(rows, gridItem.gridy);
		}
		return rows + 1;
	}

	/**
	 * Returns the number of columns in a group item.
	 * 
	 * @param groupItem The group item.
	 * @return The number of columns.
	 */
	private int getGroupItemColumns(GroupItem groupItem) {
		int columns = 0;
		for (GridItem gridItem : groupItem.gridItems) {
			columns = Math.max(columns, gridItem.gridx);
		}
		return columns + 1;
	}

	/**
	 * Returns the list of grid items from a group item, to locate in the argument column.
	 * 
	 * @param groupItem The group item.
	 * @param column The column.
	 * @return The list of grid items.
	 */
	private List<GridItem> getGridItems(GroupItem groupItem, int column) {
		List<GridItem> columnGridItems = new ArrayList<>();
		for (GridItem gridItem : groupItem.gridItems) {
			if (gridItem.gridx == column) {
				columnGridItems.add(gridItem);
			}
		}
		return columnGridItems;
	}

	/**
	 * Returns the group item of the argument field group.
	 * 
	 * @param fieldGroup The field group.
	 * @return The group item or null.
	 */
	private GroupItem getGroupItem(FieldGroup fieldGroup) {
		for (GroupItem groupItem : groupItems) {
			if (groupItem.fieldGroup.equals(fieldGroup)) {
				return groupItem;
			}
		}
		GroupItem groupItem = new GroupItem(fieldGroup);
		groupItems.add(groupItem);
		return groupItem;
	}

	/**
	 * Returns the grid item in the group item or null.
	 * 
	 * @param groupItem The grid item.
	 * @param gridx Grid x coordinate.
	 * @param gridy Grid y coordinate.
	 * @return The grid item in the group item or null.
	 */
	private GridItem getGridItem(GroupItem groupItem, int gridx, int gridy) {
		for (GridItem gridItem : groupItem.gridItems) {
			if (gridItem.gridx == gridx && gridItem.gridy == gridy) {
				return gridItem;
			}
		}
		GridItem gridItem = new GridItem(gridx, gridy);
		groupItem.gridItems.add(gridItem);
		return gridItem;
	}

	//////////////////////////////////////////
	// Generic properties getters and setters.

	/**
	 * Return the edit mode.
	 * 
	 * @return The edit mode.
	 */
	public EditMode getEditMode() {
		return editMode;
	}

	/**
	 * Set the edit mode.
	 * 
	 * @param editMode The edit mode.
	 */
	public void setEditMode(EditMode editMode) {
		this.editMode = editMode;
	}

	/**
	 * Return the edited record.
	 * 
	 * @return The record.
	 */
	public Record getRecord() {
		return record;
	}

	/**
	 * Set the record to edit, make a copy.
	 * 
	 * @param record The record to edit.
	 */
	public void setRecord(Record record) {
		this.record = Record.copyDataAndFields(record);
	}

}
