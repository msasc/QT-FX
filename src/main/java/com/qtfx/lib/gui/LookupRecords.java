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
import com.qtfx.lib.db.Record;

import javafx.collections.ObservableList;
import javafx.scene.control.SelectionMode;
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

	/**
	 * Constructor.
	 * 
	 * @param masterRecord The master record.
	 */
	public LookupRecords(Record masterRecord) {
		this(masterRecord, Locale.getDefault());
	}

	/**
	 * Constructor.
	 * 
	 * @param masterRecord The master record.
	 * @param locale The locale.
	 */
	public LookupRecords(Record masterRecord, Locale locale) {
		super();
		this.tableRecordPane = new TableRecordPane(masterRecord, locale);
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
	 * Lookup records indicating the selection mode.
	 * 
	 * @param owner The owner window.
	 * @param records The list of records.
	 * @param selectionMode The selection mode.
	 * @return The list of selected records, can be empty.
	 */
	private ObservableList<Record> lookup(Window owner, ObservableList<Record> records, SelectionMode selectionMode) {

		// Set records and selection mode.
		tableRecordPane.setSelectionMode(selectionMode);
		tableRecordPane.setRecords(records);

		return tableRecordPane.getSelectedRecords();
	}
}
