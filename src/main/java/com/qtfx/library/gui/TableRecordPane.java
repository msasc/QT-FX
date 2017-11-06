/*
 * Copyright (C) 2015 Miquel Sas
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

package com.qtfx.library.gui;

import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

/**
 * A pane with a table view on a record set an a bottom status bar.
 *
 * @author Miquel Sas
 */
public class TableRecordPane {

	/** Border pane. */
	private BorderPane borderPane;
	/** Status bar. */
	private StatusBar statusBar = new StatusBar();

	/**
	 * Constructor.
	 */
	public TableRecordPane() {
		super();
		borderPane = new BorderPane();
		statusBar = new StatusBar();
		borderPane.setBottom(statusBar);
	}

	/**
	 * Return the root node of this table record.
	 * 
	 * @return The root node of this table record.
	 */
	public Node getNode() {
		return borderPane;
	}
}
