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
import com.qtfx.lib.db.Relation;
import com.qtfx.lib.gui.action.ActionLookup;
import com.qtfx.lib.gui.action.ActionSearchAndRefreshDB;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Tab;
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
	 * Grid item structure. It is a box that contains a list of fields laid vertically, label and input and optionally a
	 * description if the input has a lookup that can provide such description.
	 */
	private static class GridItem {
		int columnIndex;
		int rowIndex;
		List<Field> fields = new ArrayList<>();

		GridItem(int columnIndex, int rowIndex) {
			this.columnIndex = columnIndex;
			this.rowIndex = rowIndex;
		}

		@Override
		public boolean equals(Object o) {
			if (o instanceof GridItem) {
				GridItem gridItem = (GridItem) o;
				return (columnIndex == gridItem.columnIndex && rowIndex == gridItem.rowIndex);
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

	/** Identifier. */
	private String id;
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

	/**
	 * Return the pane identifier to get it from node properties.
	 * 
	 * @return The identifier.
	 */
	public String getId() {
		return (id != null ? id : "");
	}

	/**
	 * Set the pane identifier.
	 * 
	 * @param id The identifier.
	 */
	public void setId(String id) {
		this.id = id;
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
	 * @param column The column index.
	 * @param row The row index.
	 */
	public void addField(String alias, int column, int row) {

		// State validation.
		if (getRecord() == null) {
			throw new IllegalStateException("The record to edit must be set prior to add any field.");
		}
		if (column < 0) {
			throw new IllegalArgumentException("Invalid column index.");
		}
		if (row < 0) {
			throw new IllegalArgumentException("Invalid row index.");
		}

		// Field (a copy), group item and grid item.
		Field field = new Field(getRecord().getField(alias));
		GroupItem groupItem = getGroupItem(field.getFieldGroup());
		GridItem gridItem = getGridItem(groupItem, column, row);
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

		// If we have only one GroupItem (FieldGroup), we will add a single GriPane in the center of the border
		// pane, otherwise we will add a TabPane with a GridPane for each FieldGroup in each Tab.
		//
		// A GroupItem is a GridPane with a GridItem in each cell. A GridItem is in turn another GridPane with a row for
		// each field, a column for the labels, a column for the field edit control, and optionally a column for the
		// field related description.
		List<GridPane> groupItemPanes = new ArrayList<>();
		groupItems.forEach(groupItem -> groupItemPanes.add(getGroupItemPane(groupItem)));
		
		// Only one pane, add directly to the center, otherwise build a tab pane.
		if (groupItemPanes.size() == 1) {
			borderPane.setCenter(groupItemPanes.get(0));
		} else {
			TabPane tabPane = new TabPane();
			for (GridPane pane : groupItemPanes) {
				FieldGroup fieldGroup = FX.getFieldGroup(pane);
				Tab tab = new Tab(fieldGroup.getDisplayTitle());
				tab.setContent(pane);
				tabPane.getTabs().add(tab);
			}
			borderPane.setCenter(tabPane);
		}

		// Setup the form record pane to all the controls.
		List<Control> controls = FX.getControls(borderPane);
		controls.forEach(control -> FX.setFormRecordPane(control, this));
	}
	
	/**
	 * Update field control with the record values.
	 */
	public void updateFieldControls() {
		List<FieldControl> fieldControls = FX.getFieldControls(borderPane);
		for (FieldControl fieldControl : fieldControls) {
			String alias = fieldControl.getField().getAlias();
			fieldControl.setValue(record.getValue(alias));
		}
	}
	
	/**
	 * Update the record with the field control values.
	 */
	public void updateRecord() {
		List<FieldControl> fieldControls = FX.getFieldControls(borderPane);
		for (FieldControl fieldControl : fieldControls) {
			String alias = fieldControl.getField().getAlias();
			record.setValue(alias, fieldControl.getValue());
		}
	}

	/**
	 * Returns the GridPane related to a GroupItem.
	 * 
	 * @param groupItem The GroupItem.
	 * @return The GridPane.
	 */
	private GridPane getGroupItemPane(GroupItem groupItem) {
		GridPane groupItemPane = new GridPane();
		FX.setFieldGroup(groupItemPane, groupItem.fieldGroup);
		int columns = getGroupItemColumns(groupItem);
		for (int column = 0; column < columns; column++) {
			int rows = getGroupItemRows(groupItem, column);
			for (int row = 0; row < rows; row++) {
				GridItem gridItem = getGridItem(groupItem, column, row);
				GridPane gridItemPane = getGridItemPane(gridItem);
				groupItemPane.add(gridItemPane, column, row);
			}
		}
		groupItemPane.setGridLinesVisible(true);
		return groupItemPane;
	}

	/**
	 * Returns the GridPane related to a GridItem.
	 * 
	 * @param gridItem The GridItem.
	 * @return The GridPane.
	 */
	private GridPane getGridItemPane(GridItem gridItem) {

		// The grid pane to fill.
		GridPane gridPane = new GridPane();

		// List of field contexts.
		List<FieldContext> contexts = getFieldContexts(gridItem.fields);

		// Layout the controls. If any refresh field in the list of contexts, the number of columns will be 3, otherwise
		// 2. First get the number of columns to decide column span. A row per context.
		boolean span = isColumnSpan(contexts);
		for (int row = 0; row < contexts.size(); row++) {
			FieldContext context = contexts.get(row);

			// Label.
			gridPane.add(context.getFieldLabel(), 0, row);
			
			// The main field.
			int colspan = (span && context.isSpan() ? 2 : 1);
			int rowspan = 1;
			gridPane.add(context.getControl(), 1, row, colspan, rowspan);
			
			// Refresh field if applicable.
			if (context.isRefresh()) {
				gridPane.add(context.getRefreshControl(), 2, row);
			}
		}

		gridPane.setPadding(new Insets(10,10,10,10));
		return gridPane;
	}

	/**
	 * Check if fields should span columns when applicable.
	 * 
	 * @param contexts The list of field contexts.
	 * @return A boolean
	 */
	private boolean isColumnSpan(List<FieldContext> contexts) {
		for (FieldContext context : contexts) {
			if (context.isRefresh()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Return a list a field contexts given the field list.
	 * 
	 * @param fields The list of fields.
	 * @return The list a field contexts.
	 */
	private List<FieldContext> getFieldContexts(List<Field> fields) {

		// List of already laid out fields as foreign refresh fields.
		List<Field> usedForeignRefreshFields = new ArrayList<>();

		// Build the list of field contexts.
		List<FieldContext> contexts = new ArrayList<>();
		for (Field field : fields) {

			// Skip used foreign refresh fields and increase the row number.
			if (usedForeignRefreshFields.contains(field)) {
				continue;
			}

			// Field context.
			FieldContext context = new FieldContext(locale);
			context.setRecord(record);
			context.setField(field);

			// Scan the relations from the field to see if all the local fields are present in the list of fields, and
			// the field scanned is the last field of the relation, in which case we should build a lookup.
			Relation relation = getRelation(field, fields);

			// Local and foreign key fields and refresh fields. Foreign refresh fields are all the fields from the
			// foreign table that are not key fields, adding first those that are main description, second those that
			// are lookup, and then the rest.
			List<Field> localKeyFields = new ArrayList<>();
			List<Field> foreignKeyFields = new ArrayList<>();
			List<Field> foreignRefreshFields = new ArrayList<>();

			// If the relation is not null and is a lookup relation...
			if (relation != null && relation.getType().equals(Relation.Type.LOOKUP)) {
				fillKeyAndRefreshFields(fields, relation, localKeyFields, foreignKeyFields, foreignRefreshFields);
			}

			// Register used foreign refresh fields.
			usedForeignRefreshFields.addAll(foreignRefreshFields);

			// The search record belongs to the foreign table.
			Record searchRecord = null;
			if (relation != null) {
				searchRecord = relation.getForeignTable().getDefaultRecord();
			}

			// Check lookup action with local and foreign key fields and perhaps refresh fields).
			if (!localKeyFields.isEmpty()) {
				ActionLookup actionLookup = new ActionLookup();
				actionLookup.setMasterRecord(searchRecord);
				for (int i = 0; i < localKeyFields.size(); i++) {
					Field local = localKeyFields.get(i);
					Field foreign = foreignKeyFields.get(i);
					actionLookup.addKeyFields(local, foreign);
				}
				context.setActionLookup(actionLookup);
			}

			// If there are refresh fields, we must build a search and refresh action.
			if (!foreignRefreshFields.isEmpty()) {
				ActionSearchAndRefreshDB actionSR = new ActionSearchAndRefreshDB(searchRecord);
				context.setActionSearchAndRefresh(actionSR);
				// Local key fields are all present as edit controls.
				for (int i = 0; i < localKeyFields.size(); i++) {
					Field localKeyField = localKeyFields.get(i);
					Field foreignKeyField = foreignKeyFields.get(i);
					actionSR.addKeyField(localKeyField.getAlias());
					actionSR.addSearchKeyAlias(foreignKeyField.getAlias());
				}
				// Add only the first refresh field.
				Field refreshField = foreignRefreshFields.get(0);
				actionSR.addRefreshField(refreshField.getAlias());
				actionSR.addSearchRefreshAlias(refreshField.getAlias());
				context.setRefreshField(refreshField);
				// If this field is a segment of another multiple-segment lookup, add clear fields for the rest of the
				// multiple-segment lookup.
				List<String> clearFields = getClearFields(field, fields, relation);
				for (String clearField : clearFields) {
					actionSR.addClearField(clearField);
				}
			}

			// Register the context.
			contexts.add(context);
		}
		return contexts;
	}

	/**
	 * Returns the list of clear edit field names. If the field is a segment of another multi-segment relation, add
	 * clear fields for the rest of the multi-segment fields.
	 * 
	 * @param field Scan field.
	 * @param fields List of fields.
	 * @param skipRelation Relation to skip.
	 * @return The list of field names.
	 */
	private List<String> getClearFields(Field field, List<Field> fields, Relation skipRelation) {

		// All present relations.
		List<Relation> relations = Field.getRelations(fields);

		// The list of clear edit fields.
		List<String> clearEditFieldNames = new ArrayList<>();

		// Scan relations.
		for (Relation relation : relations) {

			// Skip the relation used to build the search and refresh action.
			if (relation.equals(skipRelation)) {
				continue;
			}

			// If all local fields of the relation are not included in the list of fields, skip it.
			if (!allLocalFieldsIncluded(relation, fields)) {
				continue;
			}

			// If the relation does not contains the field as a local field, skip it.
			if (!relation.containsLocalField(field)) {
				continue;
			}

			// Get the field position in the list of local fields.
			int index = relation.getLocalFieldIndex(field);

			// Add the rest of local fields, if not already added.
			for (int i = index + 1; i < relation.size(); i++) {
				Field clearField = relation.get(i).getLocalField();
				if (!clearEditFieldNames.contains(clearField.getAlias())) {
					clearEditFieldNames.add(clearField.getAlias());
				}
			}
		}

		return clearEditFieldNames;
	}

	/**
	 * Check if all the local fields of the relation are present in the list of fields.
	 * 
	 * @param relation The relation.
	 * @param fields The source list of fields.
	 * @return A boolean.
	 */
	private boolean allLocalFieldsIncluded(Relation relation, List<Field> fields) {
		for (Relation.Segment segment : relation) {
			if (!fields.contains(segment.getLocalField())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Fill the lists of local and foreign key fields, and foreign refresh fields.
	 * 
	 * @param fields The master list of fields.
	 * @param relation The relation.
	 * @param localKeyFields List of local key fields to fill.
	 * @param foreignKeyFields List of foreign key fields to fill.
	 * @param foreignRefreshFields List of foreign refresh fields to fill.
	 */
	private void fillKeyAndRefreshFields(
		List<Field> fields,
		Relation relation,
		List<Field> localKeyFields,
		List<Field> foreignKeyFields,
		List<Field> foreignRefreshFields) {

		// Local and foreign key fields.
		for (Relation.Segment segment : relation) {
			localKeyFields.add(segment.getLocalField());
			foreignKeyFields.add(segment.getForeignField());
		}
		// Temporary list of refresh fields: those foreign not in the key.
		List<Field> foreignRefreshFieldsTmp = new ArrayList<>();
		for (Field field : fields) {
			if (field.getTable().equals(relation.getForeignTable())) {
				if (!foreignKeyFields.contains(field)) {
					foreignRefreshFieldsTmp.add(field);
				}
			}
		}
		// Add first the main descriptions.
		for (Field field : foreignRefreshFieldsTmp) {
			if (field.isMainDescription()) {
				if (!foreignRefreshFields.contains(field)) {
					foreignRefreshFields.add(field);
				}
			}
		}
		// Second the lookup fields.
		for (Field field : foreignRefreshFieldsTmp) {
			if (field.isLookup()) {
				if (!foreignRefreshFields.contains(field)) {
					foreignRefreshFields.add(field);
				}
			}
		}
		// Then the rest.
		for (Field field : foreignRefreshFieldsTmp) {
			if (!foreignRefreshFields.contains(field)) {
				foreignRefreshFields.add(field);
			}
		}
	}

	/**
	 * Returns the relation from all the relations associated to the argument field, where all the local fields are
	 * present in the list of fields, and the argument field is the last field of the relation.
	 * 
	 * @param field The scanned field.
	 * @param fields The source list of fields.
	 * @return The required relation or null.
	 */
	private Relation getRelation(Field field, List<Field> fields) {
		List<Relation> relations = field.getRelations();
		for (Relation relation : relations) {
			// Check if all the local fields of the relation are present in the list of fields.
			boolean allLocalFieldsIncluded = true;
			for (Relation.Segment segment : relation) {
				if (!fields.contains(segment.getLocalField())) {
					allLocalFieldsIncluded = false;
					break;
				}
			}
			// If all local fields included, check if the field scanned is the last field of the relation and if so
			// return the relation.
			if (allLocalFieldsIncluded) {
				if (relation.get(relation.size() - 1).getLocalField().equals(field)) {
					return relation;
				}
			}
		}
		return null;
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
			rows = Math.max(rows, gridItem.rowIndex);
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
			columns = Math.max(columns, gridItem.columnIndex);
		}
		return columns + 1;
	}

	/**
	 * Returns the list of grid items from a group item, to locate in the argument column.
	 * 
	 * @param groupItem The group item.
	 * @param columnIndex The column.
	 * @return The list of grid items.
	 */
	private List<GridItem> getGridItems(GroupItem groupItem, int columnIndex) {
		List<GridItem> columnGridItems = new ArrayList<>();
		for (GridItem gridItem : groupItem.gridItems) {
			if (gridItem.columnIndex == columnIndex) {
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
	 * @param column Column index..
	 * @param row Row index.
	 * @return The grid item in the group item or null.
	 */
	private GridItem getGridItem(GroupItem groupItem, int column, int row) {
		for (GridItem gridItem : groupItem.gridItems) {
			if (gridItem.columnIndex == column && gridItem.rowIndex == row) {
				return gridItem;
			}
		}
		GridItem gridItem = new GridItem(column, row);
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
