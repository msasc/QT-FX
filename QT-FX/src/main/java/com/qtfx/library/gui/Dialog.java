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
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

/**
 * Implementation of the dialog functionality.
 * <p>
 * Buttons can be disposed top, right, bottom or left, and there is a content pane, the center of a border layout, where
 * it is expected to dispose any other layout. Default buttons disposition is bottom.
 * <p>
 * Options (buttons) always close the windo unless a filter is set and the event is consumed.
 * 
 * @author Miquel Sas
 */
public class Dialog {

	/**
	 * Enumerate buttons position.
	 */
	public static enum Pos {
		TOP, RIGHT, BOTTOM, LEFT
	}

	/** Stage. */
	private Stage stage;
	/** Main border pane. */
	private BorderPane borderPane;
	/** Options pane. */
	private OptionPane optionPane;
	/** Result option. */
	private Option result;

	/**
	 * Constructor with a bottom disposition for buttons.
	 */
	public Dialog(Window owner) {
		this(owner, Pos.BOTTOM);
	}

	/**
	 * Constructor assigning the buttons position.
	 * 
	 * @param position The buttons position.
	 */
	public Dialog(Window owner, Pos position) {
		super();

		// Initialize components and build the scene.
		stage = new Stage();
		if (owner == null) {
			stage.initModality(Modality.APPLICATION_MODAL);
		} else {
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(owner);
		}
		stage.initStyle(StageStyle.DECORATED);
		borderPane = new BorderPane();
		if (position.equals(Pos.TOP)) {
			optionPane = new OptionPane(Orientation.HORIZONTAL);
			borderPane.setTop(optionPane);
		}
		if (position.equals(Pos.BOTTOM)) {
			optionPane = new OptionPane(Orientation.HORIZONTAL);
			borderPane.setBottom(optionPane);
		}
		if (position.equals(Pos.LEFT)) {
			optionPane = new OptionPane(Orientation.VERTICAL);
			borderPane.setLeft(optionPane);
		}
		if (position.equals(Pos.RIGHT)) {
			optionPane = new OptionPane(Orientation.VERTICAL);
			borderPane.setRight(optionPane);
		}
		Scene scene = new Scene(borderPane);
		stage.setScene(scene);
	}

	/**
	 * Gives access to the option pane to manage, add or remove, options.
	 * 
	 * @return The option pane.
	 */
	public OptionPane getOptionPane() {
		return optionPane;
	}

	/**
	 * Set the title.
	 * 
	 * @param title The title.
	 */
	public void setTitle(String title) {
		stage.setTitle(title);
	}

	/**
	 * Set the dialog content.
	 * 
	 * @param node The content.
	 */
	public void setContent(Node node) {
		borderPane.setCenter(node);
	}

	/**
	 * Show the dialog and return the selected option.
	 * 
	 * @return The selected option.
	 */
	public Option show() {

		// Layout options.
		optionPane.layoutOptions();

		// Set the event handler to close the stage and set the result.
		result = null;
		Button[] buttons = optionPane.getButtons();
		for (Button button : buttons) {
			button.setOnAction(e -> {
				result = (Option) button.getUserData();
				stage.close();
			});
		}
		
		// Do show.
		stage.showAndWait();

		// If result is null expect close by other means, look for a cancel button to assign the result.
		if (result == null) {
			// Buttons can have changed during edition.
			buttons = optionPane.getButtons();
			for (Button button : buttons) {
				if (button.isCancelButton()) {
					result = (Option) button.getUserData();
					break;
				}
			}
			// Only one button, take it.
			if (result == null && buttons.length == 1) {
				result = (Option) buttons[0].getUserData();
			}
		}
		
		// Return the result option, can be null.
		return result;
	}
}
