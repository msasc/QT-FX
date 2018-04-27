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
import com.qtfx.lib.db.FieldProperties;

/**
 * A columns pane to select/unselect columns from a table record pane.
 *
 * @author Miquel Sas
 */
public class ColumnsPane {

	/** Identifier. */
	private String id;

	/** The left table record pane. */
	private TableRecordPane leftTable;
	/** The right table record pane. */
	private TableRecordPane rightTable;
	/** The related master table record pane. */
	private TableRecordPane masterTable;
	/** Field properties. */
	private FieldProperties fieldProperties;

	/**
	 * Constructor.
	 * 
	 * @param masterTable The table record pane of the master table to configure columns.
	 */
	public ColumnsPane(TableRecordPane masterTable) {
		super();
		this.masterTable = masterTable;

		// Configure the field properties.
		fieldProperties = new FieldProperties(masterTable.getSession());
	}

	/**
	 * Return the working session.
	 * 
	 * @return The session.
	 */
	public Session getSession() {
		return masterTable.getSession();
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

}
