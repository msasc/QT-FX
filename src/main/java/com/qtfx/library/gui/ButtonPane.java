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

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;

/**
 * A flow pane to layout buttons.
 * 
 * @author Miquel Sas
 */
public class ButtonPane extends FlowPane {

	/** List of buttons. */
	private ObservableList<Button> buttons = FXCollections.observableArrayList();
	/** A boolean that indicates if the pane has been laid out for the first time. */
	private boolean armed = false;
	/** HBox or VBox insets. */
	private Insets insets;
	/** HBox or VBox spacing. */
	private double spacing;

	/**
	 * Constructor.
	 */
	public ButtonPane() {
		this(Orientation.HORIZONTAL);
	}

	/**
	 * Constructor indicating the orientation, either HORIZONTAL or VERTICAL.
	 * 
	 * @param orientation The orientation, either HORIZONTAL or VERTICAL.
	 */
	public ButtonPane(Orientation orientation) {
		super(orientation);
		if (orientation.equals(Orientation.HORIZONTAL)) {
			setAlignment(Pos.CENTER_RIGHT);
		} else {
			setAlignment(Pos.TOP_CENTER);
		}
	}

	/**
	 * Return the list of buttons.
	 * 
	 * @return The list of buttons.
	 */
	public ObservableList<Button> getButtons() {
		return buttons;
	}

}
