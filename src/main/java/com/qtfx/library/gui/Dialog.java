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
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

/**
 * Implementation of the dialog functionality.
 * <p>
 * The root is a border pane. Buttons can be disposed top, right, bottom or left, and there is a content pane, the
 * center of a border layout, where it is expected to dispose any other layout. If not buttons are set, the show method
 * returns the default close option. The rest of panes, left, right, top, bottom or center is available to set any node.
 * <p>
 * Options (buttons) always close the window unless a filter is set and the event is consumed.
 * 
 * @author Miquel Sas
 */
public class Dialog {

	/** Stage. */
	private Stage stage;
	/** Main border pane. */
	private BorderPane borderPane;
	/** Options pane. */
	private OptionPane optionPane;
	/** Result option. */
	private Option result;

	/**
	 * Constructor assigning the buttons position.
	 */
	public Dialog(Window owner) {
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
		Scene scene = new Scene(borderPane);
		stage.setScene(scene);
	}

	/**
	 * Return the parent stage.
	 * 
	 * @return The stage.
	 */
	protected Stage getStage() {
		return stage;
	}

	/**
	 * Set the button on the top pane.
	 */
	public void setButtonsTop() {
		optionPane = new OptionPane(Orientation.HORIZONTAL);
		borderPane.setTop(optionPane);
	}

	/**
	 * Set the button on the bottom pane.
	 */
	public void setButtonsBottom() {
		optionPane = new OptionPane(Orientation.HORIZONTAL);
		borderPane.setBottom(optionPane);
	}

	/**
	 * Set the button on the right pane.
	 */
	public void setButtonsRight() {
		optionPane = new OptionPane(Orientation.VERTICAL);
		borderPane.setRight(optionPane);
	}

	/**
	 * Set the button on the left pane.
	 */
	public void setButtonsLeft() {
		optionPane = new OptionPane(Orientation.VERTICAL);
		borderPane.setLeft(optionPane);
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
	 * Set the dialog center.
	 * 
	 * @param node The node.
	 */
	public void setCenter(Node node) {
		borderPane.setCenter(node);
	}

	/**
	 * Set the dialog top.
	 * 
	 * @param node The node.
	 */
	public void setTop(Node node) {
		if (borderPane.getTop() != null && borderPane.getTop() instanceof OptionPane) {
			throw new IllegalStateException("Buttons position is top");
		}
		borderPane.setTop(node);
	}

	/**
	 * Set the dialog left.
	 * 
	 * @param node The node.
	 */
	public void setLeft(Node node) {
		if (borderPane.getLeft() != null && borderPane.getLeft() instanceof OptionPane) {
			throw new IllegalStateException("Buttons position is left");
		}
		borderPane.setLeft(node);
	}

	/**
	 * Set the dialog bottom.
	 * 
	 * @param node The node.
	 */
	public void setBottom(Node node) {
		if (borderPane.getBottom() != null && borderPane.getBottom() instanceof OptionPane) {
			throw new IllegalStateException("Buttons position is bottom");
		}
		borderPane.setBottom(node);
	}

	/**
	 * Set the dialog right.
	 * 
	 * @param node The node.
	 */
	public void setRight(Node node) {
		if (borderPane.getRight() != null && borderPane.getRight() instanceof OptionPane) {
			throw new IllegalStateException("Buttons position is right");
		}
		borderPane.setRight(node);
	}

	/**
	 * Show the dialog and return the selected option.
	 * 
	 * @return The selected option.
	 */
	public Option show() {

		// Layout options and set the event handler to close the stage and set the result.
		if (optionPane != null) {
			optionPane.layoutOptions();
			result = null;
			Button[] buttons = optionPane.getButtons();
			for (Button button : buttons) {
				button.setOnAction(e -> {
					result = (Option) button.getUserData();
					stage.close();
				});
			}
		}
		stage.getScene().setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.ESCAPE) {
				stage.close();
			}
		});

		// Do show.
		stage.showAndWait();

		// If result is null expect close by other means, look for a cancel button to assign the result.
		if (result == null && optionPane != null) {
			// Buttons can have changed during edition.
			Button[] buttons = optionPane.getButtons();
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
		// No buttons, result is default close button.
		if (result == null && (optionPane == null || optionPane.getOptions().isEmpty())) {
			result = Option.close();
		}

		// Return the result option, can be null.
		return result;
	}
}
