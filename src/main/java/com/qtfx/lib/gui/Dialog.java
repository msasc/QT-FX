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
 * The root is a border pane. Buttons can be disposed top, right, bottom or left, and there is a content pane, the
 * center of a border layout, where it is expected to dispose any other layout. If not buttons are set, the show method
 * returns the default close button. The rest of panes, left, right, top, bottom or center is available to set any node.
 * <p>
 * The main window is set as a property to the buttons, so they can use it to close the dialog when handling the action
 * event.
 *
 * @author Miquel Sas
 *
 */
public class Dialog {

	/** Stage. */
	private Stage stage;
	/** Main border pane. */
	private BorderPane borderPane;
	/** Button pane. */
	private ButtonPane buttonPane;
	/** Result button. */
	private Button result;
	
	/**
	 * Constructor, application modal.
	 */
	public Dialog() {
		this(null);
	}
	
	/**
	 * Constructor.
	 * 
	 * @param owner The owner window.
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
		buttonPane = new ButtonPane(Orientation.HORIZONTAL);
		borderPane.setTop(buttonPane);
	}

	/**
	 * Set the button on the bottom pane.
	 */
	public void setButtonsBottom() {
		buttonPane = new ButtonPane(Orientation.HORIZONTAL);
		borderPane.setBottom(buttonPane);
	}

	/**
	 * Set the button on the right pane.
	 */
	public void setButtonsRight() {
		buttonPane = new ButtonPane(Orientation.VERTICAL);
		borderPane.setRight(buttonPane);
	}

	/**
	 * Set the button on the left pane.
	 */
	public void setButtonsLeft() {
		buttonPane = new ButtonPane(Orientation.VERTICAL);
		borderPane.setLeft(buttonPane);
	}

	/**
	 * Gives access to the buttons pane to manage, add or remove, buttons.
	 * 
	 * @return The option pane.
	 */
	public ButtonPane getOptionPane() {
		return buttonPane;
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
		if (borderPane.getTop() != null && borderPane.getTop() instanceof ButtonPane) {
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
		if (borderPane.getLeft() != null && borderPane.getLeft() instanceof ButtonPane) {
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
		if (borderPane.getBottom() != null && borderPane.getBottom() instanceof ButtonPane) {
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
		if (borderPane.getRight() != null && borderPane.getRight() instanceof ButtonPane) {
			throw new IllegalStateException("Buttons position is right");
		}
		borderPane.setRight(node);
	}

}
