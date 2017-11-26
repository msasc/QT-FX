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
import com.qtfx.lib.db.FieldList;
import com.qtfx.lib.db.Record;
import com.qtfx.lib.db.RecordSet;
import com.qtfx.lib.db.Value;
import com.qtfx.lib.gui.table.CellFactory;
import com.qtfx.lib.gui.table.CellValueFactory;
import com.qtfx.lib.util.Formats;
import com.qtfx.lib.util.TextServer;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;

/**
 * Table view on record set functionality.
 *
 * @author Miquel Sas
 */
public class TableRecordPane {

	/** Id for status bar line of lines. */
	private static final String LINE_OF_LINES = "line_of_lines";

	/**
	 * Selection change listener.
	 */
	class SelectedIndexListener implements ChangeListener<Number> {
		@Override
		public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
			setLineOfLines(newValue.intValue() + 1);
		}

	}

	/** Internal border pane. */
	private BorderPane borderPane;
	/** Table view. */
	private TableView<Record> tableView;
	/** Status bar. */
	private StatusBar statusBar;
	/** The user locale. */
	private Locale locale;

	/**
	 * Constructor.
	 */
	public TableRecordPane() {
		this(Locale.getDefault());
	}

	/**
	 * Constructor.
	 * 
	 * @param locale The required locale.
	 */
	public TableRecordPane(Locale locale) {
		super();
		this.locale = locale;

		borderPane = new BorderPane();
		tableView = new TableView<>();
		statusBar = new StatusBar();
		borderPane.setCenter(tableView);
		borderPane.setBottom(statusBar.getPane());

		tableView.getSelectionModel().selectedIndexProperty().addListener(new SelectedIndexListener());
	}

	/**
	 * Add a column.
	 * 
	 * @param field The field.
	 */
	public void addColumn(Field field) {
		addColumn(field, false);
	}

	/**
	 * Add a column setting whether it can be sorted by clicking the header.
	 * 
	 * @param field The field.
	 * @param sortable A boolean.
	 */
	public void addColumn(Field field, boolean sortable) {
		TableColumn<Record, Value> column = new TableColumn<>(field.getHeader());
		column.setCellValueFactory(new CellValueFactory(field));
		column.setCellFactory(new CellFactory(field));
		column.setSortable(sortable);
		column.setUserData(field);
		getTableView().getColumns().add(column);
	}

	/**
	 * Add a column.
	 * 
	 * @param alias The field alias.
	 */
	public void addColumn(String alias) {
		addColumn(alias, false);
	}

	/**
	 * Add a column indicating the alias. At least a record must be in the list of items.
	 * 
	 * @param alias The field alias.
	 * @param sortable A boolean.
	 */
	public void addColumn(String alias, boolean sortable) {
		addColumn(getFieldList().getField(alias), sortable);
	}

	/**
	 * Set the record set.
	 * 
	 * @param recordSet The record set.
	 */
	public void setRecordSet(RecordSet recordSet) {
		getTableView().setItems(recordSet.getObservableList());
		getTableView().getSelectionModel().select(0);
	}

	/**
	 * Set the list of items as an observable list of records.
	 * 
	 * @param records The list of records.
	 */
	public void setRecords(ObservableList<Record> records) {
		getTableView().setItems(records);
		getTableView().getSelectionModel().select(0);
	}

	/**
	 * Set the selection mode.
	 * 
	 * @param selectionMode The selection mode.
	 */
	public void setSelectionMode(SelectionMode selectionMode) {
		getTableView().getSelectionModel().setSelectionMode(selectionMode);
	}

	/**
	 * Set the line of lines information in the status bar.
	 * 
	 * @param line The line number.
	 */
	private void setLineOfLines(int line) {
		StringBuilder b = new StringBuilder();
		b.append(TextServer.getString("tokenLine"));
		b.append(" ");
		if (line > 0) {
			b.append(Formats.getNumberFormat(0, locale).format(line));
		} else {
			b.append("#");
		}
		b.append(" ");
		b.append(TextServer.getString("tokenOf"));
		b.append(" ");
		int lines = -1;
		if (getTableView().getItems() != null) {
			lines = getTableView().getItems().size();
		}
		if (lines > 0) {
			b.append(Formats.getNumberFormat(0, locale).format(lines));
		} else {
			b.append("#");
		}
		statusBar.setLabel(LINE_OF_LINES, b.toString(), "-fx-font-size: 12; -fx-font-style: italic;");
	}

	/**
	 * Return the field list if record are set.
	 * 
	 * @return The field list.
	 */
	private FieldList getFieldList() {
		if (getTableView().getItems() == null || getTableView().getItems().isEmpty()) {
			throw new IllegalStateException("Items must be set to access the field list.");
		}
		return getTableView().getItems().get(0).getFieldList();
	}

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

	/**
	 * Return the table view.
	 * 
	 * @return The table view.
	 */
	public TableView<Record> getTableView() {
		return tableView;
	}

	/**
	 * Return the status bar.
	 * 
	 * @return The status bar.
	 */
	public StatusBar getStatusBar() {
		return statusBar;
	}
}
