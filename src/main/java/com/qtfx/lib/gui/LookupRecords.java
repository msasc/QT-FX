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

import com.qtfx.lib.app.Session;
import com.qtfx.lib.db.Field;
import com.qtfx.lib.db.Record;
import com.qtfx.lib.db.RecordSet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.KeyCode;
import javafx.stage.Window;

/**
 * Lookup record from a record-set or observable list of records.
 *
 * @author Miquel Sas
 */
public class LookupRecords {

	/** The table-record-pane. */
	private TableRecordPane tableRecordPane;
	/** Window title. */
	private String title;
	/** First selected row. */
	private int selectedRow = -1;
	/** Subsequent selected rows. */
	private int[] selectedRows;

	/**
	 * Constructor.
	 * 
	 * @param masterRecord The master record.
	 * @param locale The locale.
	 */
	public LookupRecords(Record masterRecord) {
		super();
		this.tableRecordPane = new TableRecordPane(masterRecord);
	}

	/**
	 * Set the window title.
	 * 
	 * @param title The title.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Add a column.
	 * 
	 * @param field The field.
	 */
	public void addColumn(Field field) {
		tableRecordPane.addColumn(field, false);
	}

	/**
	 * Add a column setting whether it can be sorted by clicking the header.
	 * 
	 * @param field The field.
	 * @param sortable A boolean.
	 */
	public void addColumn(Field field, boolean sortable) {
		tableRecordPane.addColumn(field, sortable);
	}

	/**
	 * Add a column.
	 * 
	 * @param alias The field alias.
	 */
	public void addColumn(String alias) {
		tableRecordPane.addColumn(alias, false);
	}

	/**
	 * Add a column indicating the alias. At least a record must be in the list of items.
	 * 
	 * @param alias The field alias.
	 * @param sortable A boolean.
	 */
	public void addColumn(String alias, boolean sortable) {
		tableRecordPane.addColumn(alias, sortable);
	}

	/**
	 * Set the list of records.
	 * 
	 * @param records The list of records.
	 */
	public void setRecords(ObservableList<Record> records) {
		tableRecordPane.setRecords(records);
	}

	/**
	 * Set the record-set.
	 * 
	 * @param recordSet The record-set.
	 */
	public void setRecordSet(RecordSet recordSet) {
		tableRecordPane.setRecordSet(recordSet);
	}

	/**
	 * Clear selected indices.
	 */
	public void clearSelection() {
		tableRecordPane.clearSelection();
	}

	/**
	 * Set the list of selected indices.
	 * 
	 * @param row At least one row to select.
	 * @param rows The list of rows.
	 */
	public void selectedIndices(int row, int... rows) {
		this.selectedRow = row;
		this.selectedRows = rows;
	}

	/**
	 * Scroll to the argument row.
	 * 
	 * @param row The row to scroll to.
	 */
	public void scrollTo(int row) {
		tableRecordPane.scrollTo(row);
	}

	/**
	 * Lookup a single record.
	 * 
	 * @return The selected record or null.
	 */
	public Record lookupRecord() {
		return lookupRecord(null);
	}

	/**
	 * Lookup a single record.
	 * 
	 * @param owner The owner window.
	 * @return The selected record or null.
	 */
	public Record lookupRecord(Window owner) {
		ObservableList<Record> selected = lookupRecords(owner, SelectionMode.SINGLE);
		if (!selected.isEmpty()) {
			return selected.get(0);
		}
		return null;
	}

	/**
	 * Lookup a single record.
	 * 
	 * @return The selected record or null.
	 */
	public ObservableList<Record> lookupRecords() {
		return lookupRecords((Window) null);
	}

	/**
	 * Lookup a single record.
	 * 
	 * @param owner The owner window.
	 * @return The selected record or null.
	 */
	public ObservableList<Record> lookupRecords(Window owner) {
		return lookupRecords(owner, SelectionMode.MULTIPLE);
	}

	/**
	 * Lookup records indicating the selection mode.
	 * 
	 * @param owner The owner window.
	 * @param selectionMode The selection mode.
	 * @return The list of selected records, can be empty.
	 */
	private ObservableList<Record> lookupRecords(Window owner, SelectionMode selectionMode) {

		// Set records and selection mode.
		tableRecordPane.setSelectionMode(selectionMode);
		tableRecordPane.clearSelection();
		if (selectedRow >= 0) {
			tableRecordPane.selectIndices(selectedRow, selectedRows);
		} else {
			tableRecordPane.selectIndices(0, 0);
		}
		tableRecordPane.setPadding(new Insets(10, 10, 0, 10));

		Dialog dialog = new Dialog(owner);
		if (title != null) {
			dialog.setTitle(title);
		} else {
			dialog.setTitle(Session.getSession().getString("defaultRecordSelection"));
		}
		dialog.getButtonPane().setPadding(new Insets(5, 10, 10, 10));
		dialog.setCenter(tableRecordPane.getPane());
		dialog.addPropertySetter(tableRecordPane.getPropertySetter());
		Button select = Buttons.SELECT;
		Button cancel = Buttons.CANCEL;
		dialog.getButtonPane().getButtons().add(select);
		dialog.getButtonPane().getButtons().add(cancel);

		tableRecordPane.getTableView().setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.ENTER) {
				select.fire();
			}
			if (e.getCode() == KeyCode.ESCAPE) {
				cancel.fire();
			}
		});
		if (selectionMode == SelectionMode.SINGLE) {
			tableRecordPane.getTableView().setOnMouseClicked(e -> {
				if (e.getClickCount() == 2) {
					select.fire();
				}
			});
		}

		tableRecordPane.requestFocus();
		Button result = dialog.show();
		if (Buttons.isSelect(result)) {
			return tableRecordPane.getSelectedRecords();
		}

		return FXCollections.emptyObservableList();
	}
}
