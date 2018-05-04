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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.qtfx.lib.app.Session;
import com.qtfx.lib.db.Field;
import com.qtfx.lib.db.FieldGroup;
import com.qtfx.lib.db.FieldProperties;
import com.qtfx.lib.db.Order;
import com.qtfx.lib.db.Record;
import com.qtfx.lib.db.RecordSet;
import com.qtfx.lib.gui.action.handlers.MouseEventHandler;
import com.qtfx.lib.util.Icons;

import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Window;

/**
 * A columns pane to select/unselect columns from a table record pane.
 *
 * @author Miquel Sas
 */
public class ColumnsPane {

	/**
	 * Enumerates the possible operations or modes.
	 */
	public enum Operation {
		/** Fields selection. */
		SELECTION,
		/** Order definition. */
		ORDER;
	}

	/**
	 * Double click event handler.
	 */
	class DoubleClickHandler extends MouseEventHandler {

		public DoubleClickHandler(Node node) {
			super(node);
		}

		@Override
		public void handle(MouseEvent event) {
			if (event.getClickCount() == 2) {
				Button button = (Button) getNode();
				button.fire();
			}
		}

	}

	/** The operation to perform. */
	private Operation operation;

	/** Identifier. */
	private String id;

	/** The left table record pane. */
	private TableRecordPane leftTable;
	/** The right table record pane. */
	private TableRecordPane rightTable;
	/** The related master table record pane. */
	private TableRecordPane masterTable;

	/** Button pane to move fields left-right-up-down. */
	private ButtonPane buttonsPane;
	/** Button left. */
	private Button buttonLeft;
	/** Button right. */
	private Button buttonRight;
	/** Button up. */
	private Button buttonUp;
	/** Button down. */
	private Button buttonDown;
	/** Button asc. */
	private Button buttonAsc;
	/** Button desc. */
	private Button buttonDesc;

	/** Field properties. */
	private FieldProperties fieldProperties;

	/** List of available field aliases. By default, all the fields of the master record can be selected. */
	private List<String> availableFields = new ArrayList<>();
	/** List of not selected fields. */
	private List<String> notSelectedFields = new ArrayList<>();
	/** List of selected fields. */
	private List<String> selectedFields = new ArrayList<>();
	/** The map with all possible fields (properties). */
	private Map<String, Record> possibleFields = new HashMap<>();

	/** Internal border pane. */
	private BorderPane borderPane;

	/**
	 * The order in a sort mode. If the order has not initially been set, then the key order of the table is taken.
	 */
	private Order order;

	/**
	 * Constructor.
	 * 
	 * @param operation The operation, either selection or order.
	 * @param masterTable The table record pane of the master table to configure columns.
	 */
	public ColumnsPane(Operation operation, TableRecordPane masterTable) {
		super();
		this.operation = operation;
		this.masterTable = masterTable;
	}

	/**
	 * Set the initial order if the table record set has previously been ordered.
	 * 
	 * @param order The order.
	 */
	public void setOrder(Order order) {
		if (operation != Operation.ORDER) {
			throw new IllegalStateException();
		}
		this.order = order;
	}

	/**
	 * Optionally define the available fields.
	 * 
	 * @param alias The field alias.
	 */
	public void addAvailableField(String alias) {
		Record masterRecord = masterTable.getMasterRecord();
		if (!masterRecord.getFieldList().containsField(alias)) {
			throw new IllegalArgumentException("Invalid field alias: " + alias);
		}
		availableFields.add(alias);
	}

	/**
	 * Return the pane to install in the scene.
	 * 
	 * @return The pane to install in the scene.
	 */
	public Pane getPane() {
		return borderPane;
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

	/**
	 * Show the columns pane in a dialog and perform the action or cancel.
	 * 
	 * @param owner The window owner.
	 * @return A boolean indicating that the selection of columns or the order has been set.
	 */
	public void show(Window owner) {

		/////////////////////
		// Prepare components

		// The master record.
		Record masterRecord = masterTable.getMasterRecord();

		// Build the list of possible fields.
		fieldProperties = new FieldProperties(Session.getSession());
		for (int i = 0; i < masterRecord.size(); i++) {
			Field field = masterRecord.getField(i);
			String alias = field.getAlias();
			possibleFields.put(alias, fieldProperties.getProperties(field, i, true));
		}

		// Selected fields in a selection operation.
		if (operation == Operation.SELECTION) {
			List<Field> masterTableFields = masterTable.getColumnFields();
			for (Field field : masterTableFields) {
				selectedFields.add(field.getAlias());
			}
		}

		// Selected fields in an order operation.
		if (operation == Operation.ORDER) {
			if (order == null) {
				order = masterTable.getOrder();
			}
			if (order == null) {
				order = masterTable.getMasterRecord().getPrimaryOrder();
			}
			if (order != null) {
				for (int i = 0; i < order.size(); i++) {
					Field field = order.getField(i);
					boolean asc = order.isAsc(i);
					String alias = field.getAlias();
					selectedFields.add(alias);
					Record properties = possibleFields.get(alias);
					fieldProperties.setPropertyAscending(properties, asc);
				}
			}
		}

		// If no available fields where defined, add all.
		if (availableFields.isEmpty() || operation == Operation.ORDER) {
			availableFields.clear();
			for (int i = 0; i < masterRecord.size(); i++) {
				availableFields.add(masterRecord.getField(i).getAlias());
			}
		}

		// At least, the selected fields must be available.
		for (String alias : selectedFields) {
			if (!availableFields.contains(alias)) {
				availableFields.add(alias);
			}
		}

		// The list of not selected fields.
		for (String alias : availableFields) {
			if (!selectedFields.contains(alias)) {
				notSelectedFields.add(alias);
			}
		}

		// Buttons to move and setup fields.
		buttonLeft = getButton(Icons.FLAT_24x24_LEFT, "colPaneButtonLeftTooltip");
		buttonRight = getButton(Icons.FLAT_24x24_RIGHT, "colPaneButtonRightTooltip");
		buttonUp = getButton(Icons.FLAT_24x24_UP, "colPaneButtonUpTooltip");
		buttonDown = getButton(Icons.FLAT_24x24_DOWN, "colPaneButtonDownTooltip");

		buttonAsc = new Button();
		buttonAsc.setDefaultButton(false);
		buttonAsc.setCancelButton(false);
		buttonAsc.setText(Session.getSession().getString("colPaneButtonAscText"));
		buttonAsc.setTooltip(new Tooltip(Session.getSession().getString("colPaneButtonAscText")));
		buttonAsc.setStyle("-fx-font-style: oblique;");

		buttonDesc = new Button();
		buttonDesc.setDefaultButton(false);
		buttonDesc.setCancelButton(false);
		buttonDesc.setText(Session.getSession().getString("colPaneButtonDescText"));
		buttonDesc.setTooltip(new Tooltip(Session.getSession().getString("colPaneButtonDescText")));
		buttonDesc.setStyle("-fx-font-style: oblique;");

		buttonsPane = new ButtonPane(Orientation.VERTICAL);
		buttonsPane.getButtons().add(buttonLeft);
		buttonsPane.getButtons().add(buttonRight);
		buttonsPane.getButtons().add(buttonUp);
		buttonsPane.getButtons().add(buttonDown);
		if (operation == Operation.ORDER) {
			buttonsPane.getButtons().add(buttonAsc);
			buttonsPane.getButtons().add(buttonDesc);
		}
		buttonsPane.setPadding(new Insets(5, 5, 5, 5));
		buttonsPane.setAlignment(Pos.CENTER);
		buttonsPane.layoutButtons();
		// buttonsPane.getNode().setPrefWidth(40);

		// Left table.
		leftTable = new TableRecordPane(new Record(fieldProperties.getFieldList()));
		if (anyFieldGroupDefined()) {
			leftTable.addColumn(FieldProperties.GROUP);
		}
		leftTable.addColumn(FieldProperties.ALIAS);
		leftTable.addColumn(FieldProperties.HEADER);
		leftTable.addColumn(FieldProperties.TITLE);
		leftTable.addColumn(FieldProperties.LENGTH);
		leftTable.addColumn(FieldProperties.DECIMALS);
		leftTable.setRecordSet(getRecordSetNotSelected());
		leftTable.getTableView().addEventHandler(MouseEvent.MOUSE_CLICKED, new DoubleClickHandler(buttonRight));

		// Right table.
		rightTable = new TableRecordPane(new Record(fieldProperties.getFieldList()));
		if (anyFieldGroupDefined()) {
			rightTable.addColumn(FieldProperties.GROUP);
		}
		rightTable.addColumn(FieldProperties.ALIAS);
		rightTable.addColumn(FieldProperties.HEADER);
		rightTable.addColumn(FieldProperties.TITLE);
		rightTable.addColumn(FieldProperties.LENGTH);
		rightTable.addColumn(FieldProperties.DECIMALS);
		rightTable.setRecordSet(getRecordSetSelected());
		if (operation == Operation.ORDER) {
			rightTable.addColumn(FieldProperties.ASCENDING);
		}
		rightTable.getTableView().addEventHandler(MouseEvent.MOUSE_CLICKED, new DoubleClickHandler(buttonLeft));

		////////////////////
		// Build the layout.

		// The border pane.
		borderPane = new BorderPane();
		borderPane.setPadding(new Insets(10, 10, 10, 10));
		// borderPane.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

		// The center of the border pane will be an HBox with the left table, the buttons pane and the right table.
		HBox hbox = new HBox();
		borderPane.setCenter(hbox);
		hbox.getChildren().add(leftTable.getPane());
		hbox.getChildren().add(buttonsPane.getPane());
		hbox.getChildren().add(rightTable.getPane());

		Pane lp = leftTable.getPane();
		Pane rp = rightTable.getPane();
		Pane bp = buttonsPane.getPane();

		lp.prefWidthProperty().bind(
			Bindings.selectDouble(lp.parentProperty(), "width").divide(2).subtract(bp.widthProperty().get() / 2));
		rp.prefWidthProperty().bind(
			Bindings.selectDouble(rp.parentProperty(), "width").divide(2).subtract(bp.widthProperty().get() / 2));

		// leftTable.getTableView().addEventHandler(MouseEvent.ANY, new DoubleClickHandler(getNode()));

		/////////////////////////
		// Handle button actions.

		buttonLeft.setOnAction(e -> {
			moveLeft();
		});
		buttonRight.setOnAction(e -> {
			moveRight();
		});
		buttonUp.setOnAction(e -> {
			moveUp();
		});
		buttonDown.setOnAction(e -> {
			moveDown();
		});
		buttonAsc.setOnAction(e -> {
			setAsc(true);
		});
		buttonDesc.setOnAction(e -> {
			setAsc(false);
		});

		//////////////////////
		// Show it in a dialog.

		Dialog dialog = new Dialog(owner);
		if (operation == Operation.ORDER) {
			dialog.setTitle(Session.getSession().getString("colPaneTitleOrder"));
		} else {
			dialog.setTitle(Session.getSession().getString("colPaneTitleSelection"));
		}
		dialog.setCenter(borderPane);
		dialog.getButtonPane().getButtons().add(Buttons.ACCEPT);
		dialog.getButtonPane().getButtons().add(Buttons.CANCEL);
		dialog.getButtonPane().setPadding(new Insets(10, 10, 10, 10));

		FX.factorScreenWidth(dialog.getStage(), 0.9);
		FX.factorScreenHeight(dialog.getStage(), 0.6);

		Button result = dialog.show();
		if (result == Buttons.ACCEPT) {
			if (operation == Operation.SELECTION) {
				masterTable.clearColumns();
				selectedFields.forEach(alias -> masterTable.addColumn(alias));
			}
			if (operation == Operation.ORDER) {
				if (selectedFields.isEmpty()) {
					return;
				}
				order = new Order();
				for (String alias : selectedFields) {
					Record properties = possibleFields.get(alias);
					Field field = masterTable.getField(alias);
					boolean asc = fieldProperties.getPropertyAscending(properties);
					order.add(field, asc);
				}
				masterTable.sort(order);
			}
		}
	}

	/**
	 * Move the selected field on the right to the left.
	 */
	private void moveLeft() {
		int index = rightTable.getSelectedIndex();
		if (index >= 0) {
			Record record = rightTable.getRecords().get(index);
			String alias = record.getValue(FieldProperties.ALIAS).getString();
			notSelectedFields.add(alias);
			selectedFields.remove(alias);
			leftTable.setRecordSet(getRecordSetNotSelected());
			rightTable.setRecordSet(getRecordSetSelected());
			List<Record> records = rightTable.getRecords();
			if (!records.isEmpty()) {
				if (index >= records.size()) {
					index = records.size() - 1;
				}
				rightTable.selectIndices(index);
			}
		}
	}

	/**
	 * Move the selected field on the left to the right.
	 */
	private void moveRight() {
		int index = leftTable.getSelectedIndex();
		if (index >= 0) {
			Record record = leftTable.getRecords().get(index);
			String alias = record.getValue(FieldProperties.ALIAS).getString();
			notSelectedFields.remove(alias);
			selectedFields.add(alias);
			leftTable.setRecordSet(getRecordSetNotSelected());
			rightTable.setRecordSet(getRecordSetSelected());
			List<Record> records = leftTable.getRecords();
			if (!records.isEmpty()) {
				if (index >= records.size()) {
					index = records.size() - 1;
				}
				leftTable.selectIndices(index);
			}
		}
	}

	/**
	 * Move the selected field on the right up.
	 */
	private void moveUp() {
		int index = rightTable.getSelectedIndex();
		if (index > 0) {
			Record record = rightTable.getRecords().get(index);
			String alias = record.getValue(FieldProperties.ALIAS).getString();
			selectedFields.remove(alias);
			index--;
			selectedFields.add(index, alias);
			rightTable.setRecordSet(getRecordSetSelected());
			List<Record> records = rightTable.getRecords();
			if (!records.isEmpty()) {
				if (index >= records.size()) {
					index = records.size() - 1;
				}
				rightTable.selectIndices(index);
			}
		}
	}

	/**
	 * Move the selected field on the right down.
	 */
	private void moveDown() {
		int index = rightTable.getSelectedIndex();
		if (index >= 0) {
			Record record = rightTable.getRecords().get(index);
			String alias = record.getValue(FieldProperties.ALIAS).getString();
			selectedFields.remove(alias);
			index++;
			if (index >= rightTable.getRecords().size()) {
				index = rightTable.getRecords().size() - 1;
			}
			selectedFields.add(index, alias);
			rightTable.setRecordSet(getRecordSetSelected());
			List<Record> records = rightTable.getRecords();
			if (!records.isEmpty()) {
				if (index >= records.size()) {
					index = records.size() - 1;
				}
				rightTable.selectIndices(index);
			}
		}
	}
	
	private void setAsc(boolean asc) {
		int index = rightTable.getSelectedIndex();
		if (index >= 0) {
			Record record = rightTable.getRecords().get(index);
			fieldProperties.setPropertyAscending(record, asc);
			leftTable.setRecordSet(getRecordSetNotSelected());
			rightTable.setRecordSet(getRecordSetSelected());
		}
	}

	/**
	 * Create and return a button for the middle buttons pane.
	 * 
	 * @param icon Icon resource.
	 * @param keyTooltip Tooltip key.
	 * @return The button.
	 */
	private Button getButton(String icon, String keyTooltip) {
		Button button = new Button();
		button.setDefaultButton(false);
		button.setCancelButton(false);
		button.setGraphic(Icons.get(icon));
		button.setPadding(new Insets(0, 0, 0, 0));
		button.setTooltip(new Tooltip(Session.getSession().getString(keyTooltip)));
		button.setStyle("-fx-content-display: graphic-only;");
		return button;
	}

	/**
	 * Check if any field group has been defined within the available fields.
	 * 
	 * @return A boolean.
	 */
	private boolean anyFieldGroupDefined() {
		for (String alias : availableFields) {
			Record properties = possibleFields.get(alias);
			if (fieldProperties.getPropertyGroupIndex(properties) != FieldGroup.EMPTY_FIELD_GROUP.getIndex()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the record set of not selected fields.
	 * 
	 * @return The record set of not selected fields.
	 */
	private RecordSet getRecordSetNotSelected() {
		RecordSet recordSet = new RecordSet();
		recordSet.setFieldList(fieldProperties.getFieldList());
		for (String alias : notSelectedFields) {
			recordSet.add(possibleFields.get(alias));
		}
		recordSet.sort(new FieldProperties.Sorter());
		return recordSet;
	}

	/**
	 * Returns the record set of selected fields.
	 * 
	 * @return The record set of selected fields.
	 */
	private RecordSet getRecordSetSelected() {
		RecordSet recordSet = new RecordSet();
		recordSet.setFieldList(fieldProperties.getFieldList());
		for (String alias : selectedFields) {
			recordSet.add(possibleFields.get(alias));
		}
		return recordSet;
	}
}
