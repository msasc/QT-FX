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

import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import com.qtfx.lib.db.Field;
import com.qtfx.lib.db.Order;
import com.qtfx.lib.db.OrderKey;
import com.qtfx.lib.db.Record;
import com.qtfx.lib.db.RecordSet;
import com.qtfx.lib.db.Value;
import com.qtfx.lib.gui.observables.ObservableRecordSet;
import com.qtfx.lib.gui.table.CellFactory;
import com.qtfx.lib.gui.table.CellValueFactory;
import com.qtfx.lib.util.Formats;
import com.qtfx.lib.util.Numbers;
import com.qtfx.lib.util.TextServer;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;

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
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
			setLineOfLines(newValue.intValue() + 1);
		}

	}

	/**
	 * Property setter.
	 */
	class NodePropertySetter implements PropertySetter {
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setProperties(Node node) {
			FX.setTableRecordPane(node, TableRecordPane.this);
		}
	}

	/**
	 * List change listener.
	 */
	class RecordsChangeListener implements ListChangeListener<Record> {
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void onChanged(Change<? extends Record> c) {
			while (c.next()) {
				if (c.wasReplaced()) {
					calculateLinesPercentScale();
				}
			}
		}
	}

	/** Identifier. */
	private String id;
	/** Internal border pane. */
	private BorderPane borderPane;
	/** Table view. */
	private TableView<Record> tableView;
	/** Status bar. */
	private StatusBar statusBar;
	/** The user locale. */
	private Locale locale;

	/** Lines percent scale. */
	private transient int linesPercentScale = -1;

	/** Master record. */
	private Record masterRecord;

	/**
	 * Constructor.
	 * 
	 * @param masterRecord The master record.
	 */
	public TableRecordPane(Record masterRecord) {
		this(masterRecord, Locale.getDefault());
	}

	/**
	 * Constructor.
	 * 
	 * @param masterRecord The master record.
	 * @param locale The required locale.
	 */
	public TableRecordPane(Record masterRecord, Locale locale) {
		super();

		if (masterRecord == null) {
			throw new NullPointerException();
		}

		this.masterRecord = masterRecord;
		this.locale = locale;

		borderPane = new BorderPane();
		tableView = new TableView<>();
		statusBar = new StatusBar();
		borderPane.setCenter(tableView);
		borderPane.setBottom(statusBar.getPane());

		calculateLinesPercentScale();
		setLineOfLines(-1);

		tableView.getSelectionModel().selectedIndexProperty().addListener(new SelectedIndexListener());
	}

	/**
	 * Return the master record.
	 * 
	 * @return The master record.
	 */
	public Record getMasterRecord() {
		return masterRecord;
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
	 * Return a suitable property setter.
	 * 
	 * @return A suitable property setter.
	 */
	public PropertySetter getPropertySetter() {
		return new NodePropertySetter();
	}

	/////////////////////
	// Column management.

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
		TableColumn<Record, Value> column = new TableColumn<>(field.getDisplayHeader());
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
		addColumn(masterRecord.getFieldList().getField(alias), sortable);
	}

	/**
	 * Return the column index of the field.
	 * 
	 * @param alias The alias.
	 * @return The column index or -1.
	 */
	public int getColumnIndex(String alias) {
		Field field = masterRecord.getField(alias);
		if (field != null) {
			return getColumnIndex(field);
		}
		return -1;
	}

	/**
	 * Return the column index of the field.
	 * 
	 * @param field The field.
	 * @return The column index or -1.
	 */
	public int getColumnIndex(Field field) {
		for (int i = 0; i < getTableView().getColumns().size(); i++) {
			if (getTableView().getColumns().get(i).getText().equals(field.getDisplayHeader())) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Set the column preferred width.
	 * 
	 * @param column The column.
	 * @param width The width.
	 */
	public void setColumnPrefWidth(int column, double width) {
		getTableView().getColumns().get(column).setPrefWidth(width);
	}

	/**
	 * Return the default width for the given text.
	 * 
	 * @param text The text.
	 * @return The default width.
	 */
	public double getDefaultColumnWidth(String text) {
		TextFieldTableCell<Record, Value> cell = new TextFieldTableCell<>();
		Font font = cell.getFont();
		double width = FX.getStringWidth(text, font);
		return width;
	}

	/**
	 * Set the record set.
	 * 
	 * @param recordSet The record set.
	 */
	public void setRecordSet(RecordSet recordSet) {
		setRecords(new ObservableRecordSet(recordSet));
	}

	/**
	 * Return the list of records.
	 * 
	 * @return The list of observable records.
	 */
	public ObservableList<Record> getRecords() {
		return getTableView().getItems();
	}

	/**
	 * Set the list of items as an observable list of records.
	 * 
	 * @param records The list of records.
	 */
	public void setRecords(ObservableList<Record> records) {
		getTableView().setItems(records);
		getRecords().addListener(new RecordsChangeListener());
		getTableView().getSelectionModel().selectFirst();
		calculateLinesPercentScale();
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
	 * Calculate the scale to show the percentage of the current line.
	 */
	private void calculateLinesPercentScale() {
		if (getTableView().getItems() != null) {
			linesPercentScale = Math.max(0, Numbers.getDigits(getTableView().getItems().size()) - 2);
		}
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
		if (line > 0 && lines > 0) {
			double percent = 100d * Double.valueOf(line).doubleValue() / Double.valueOf(lines).doubleValue();
			b.append(" (");
			b.append(Formats.formattedFromDouble(percent, linesPercentScale, locale));
			b.append(" %)");
		}
		statusBar.setLabel(LINE_OF_LINES, b.toString(), "-fx-font-size: 12; -fx-font-style: italic;");
	}

	/**
	 * Return the field with the giv en alias.
	 * 
	 * @param alias The alias.
	 * @return The field.
	 */
	public Field getField(String alias) {
		return masterRecord.getFieldList().getField(alias);
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

	/**
	 * Returns the list of selected records.
	 * 
	 * @return The list of selected records.
	 */
	public ObservableList<Record> getSelectedRecords() {
		return getTableView().getSelectionModel().getSelectedItems();
	}

	/**
	 * Clear the current selection.
	 */
	public void clearSelection() {
		getTableView().getSelectionModel().clearSelection();
	}

	/**
	 * Select the list of indices.
	 * 
	 * @param row At least one row to select.
	 * @param rows The list of rows.
	 */
	public void selectIndices(int row, int... rows) {
		getTableView().getSelectionModel().selectIndices(row, rows);
		requestFocus();
	}

	/**
	 * Select the record.
	 * 
	 * @param record The record.
	 */
	public void selectRecord(Record record) {
		int index = indexOf(record);
		if (index >= 0) {
			selectIndices(index, index);
		}
	}

	/**
	 * Return the index of the record.
	 * 
	 * @param record The record.
	 * @return The index.
	 */
	public int indexOf(Record record) {
		return getRecords().indexOf(record);
	}

	/**
	 * Scroll to the argument row.
	 * 
	 * @param row The row to scroll to.
	 */
	public void scrollTo(int row) {
		getTableView().scrollTo(row);
	}

	/**
	 * Request focus to the table view.
	 */
	public void requestFocus() {
		getTableView().requestFocus();
	}

	/**
	 * Sort the underlying table view.
	 * 
	 * @param c The comparator.
	 */
	public void sort(Comparator<? super Record> c) {
		getRecords().sort(c);
	}

	/**
	 * Return the used locale.
	 * 
	 * @return The locale.
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * Gets the insert index using the order key.
	 *
	 * @param record The record.
	 * @return The insert index.
	 */
	public int getInsertIndex(Record record) {
		return getInsertIndex(record, masterRecord.getFieldList().getPrimaryOrder());
	}

	/**
	 * Gets the insert index using the order key.
	 *
	 * @param record The record.
	 * @param order The order.
	 * @return The insert index.
	 */
	public int getInsertIndex(Record record, Order order) {
		OrderKey key = record.getOrderKey(order);
		int index;
		List<Record> records = getRecords();
		for (index = 0; index < records.size(); index++) {
			Record scanRecord = records.get(index);
			OrderKey scanKey = scanRecord.getOrderKey(order);
			if (key.compareTo(scanKey) <= 0) {
				break;
			}
		}
		return index;
	}

}
