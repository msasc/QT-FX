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

import java.util.ArrayList;
import java.util.List;

import com.qtfx.lib.gui.action.ActionClose;
import com.qtfx.lib.gui.action.ActionList;
import com.qtfx.lib.gui.action.handlers.ActionEventHandler;

import javafx.beans.InvalidationListener;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
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
 * The root is a border pane. Buttons can be disposed top, right, bottom or left, default is bottom, and there is a
 * content pane, the center of a border layout, where it is expected to dispose any other layout. If not buttons are
 * set, the show method returns the default close button. The rest of panes, left, right, top, bottom or center is
 * available to set any node.
 * <p>
 * The main window is set as a property to the buttons, so they can use it to close the dialog when handling the action
 * event.
 *
 * @author Miquel Sas
 *
 */
public class Dialog {

	/**
	 * Action to set the result button.
	 */
	private class Result extends ActionEventHandler {
		/**
		 * Constructor.
		 * 
		 * @param node Reference node.
		 */
		private Result(Node node) {
			super(node);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void handle(ActionEvent event) {
			if (getNode() instanceof Button) {
				result = (Button) getNode();
			}
		}
	}

	/**
	 * Private instance of action list. A private extension is used to identify that the action saved is the original
	 * button action and not the list of actions setup by this dialog to close and set the result.
	 */
	private class Actions extends ActionList {
		private Actions(Node node) {
			super(node);
		}
	}

	/**
	 * Private enumeration of buttons positions.
	 */
	private enum ButtonPanePos {
		TOP, LEFT, BOTTOM, RIGHT
	}

	/** Stage. */
	private Stage stage;
	/** Main border pane. */
	private BorderPane borderPane;
	/** Button pane. */
	private ButtonPane buttonPane;
	/** Button pane pos. */
	private ButtonPanePos buttonPanePos;
	/** Result button. */
	private Button result;

	/** User property setters. */
	private List<PropertySetter> propertySetters = new ArrayList<>();

	/** Default behavior to close on escape. Set the key handler on all nodes. */
	private boolean closeOnEscape = true;

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

		setButtonsBottom();
	}

	/**
	 * Check the default close behavior on escape.
	 * 
	 * @return A boolean.
	 */
	public boolean isCloseOnEscape() {
		return closeOnEscape;
	}

	/**
	 * Set the default close behavior on escape.
	 * 
	 * @param closeOnEscape A boolean.
	 */
	public void setCloseOnEscape(boolean closeOnEscape) {
		this.closeOnEscape = closeOnEscape;
	}

	/**
	 * Return the parent stage.
	 * 
	 * @return The stage.
	 */
	public Stage getStage() {
		return stage;
	}

	/**
	 * Set the width.
	 * 
	 * @param width The width.
	 * @see javafx.stage.Window#setWidth(double)
	 */
	public final void setWidth(double width) {
		stage.setWidth(width);
	}

	/**
	 * Return the width.
	 * 
	 * @return The width.
	 * @see javafx.stage.Window#getWidth()
	 */
	public final double getWidth() {
		return stage.getWidth();
	}

	/**
	 * Set the height.
	 * 
	 * @param height The height.
	 * @see javafx.stage.Window#setHeight(double)
	 */
	public final void setHeight(double height) {
		stage.setHeight(height);
	}

	/**
	 * Return the height.
	 * 
	 * @return The height.
	 * @see javafx.stage.Window#getHeight()
	 */
	public final double getHeight() {
		return stage.getHeight();
	}

	/**
	 * Set the minimum width.
	 * 
	 * @param minWidth The minimum width.
	 * @see javafx.stage.Stage#setMinWidth(double)
	 */
	public final void setMinWidth(double minWidth) {
		stage.setMinWidth(minWidth);
	}

	/**
	 * Return the minimum width.
	 * 
	 * @return The minimum width.
	 * @see javafx.stage.Stage#getMinWidth()
	 */
	public final double getMinWidth() {
		return stage.getMinWidth();
	}

	/**
	 * Set the minimum height.
	 * 
	 * @param minHeight The minimum height.
	 * @see javafx.stage.Stage#setMinHeight(double)
	 */
	public final void setMinHeight(double minHeight) {
		stage.setMinHeight(minHeight);
	}

	/**
	 * Return the minimum height.
	 * 
	 * @return The minimum height.
	 * @see javafx.stage.Stage#getMinHeight()
	 */
	public final double getMinHeight() {
		return stage.getMinHeight();
	}

	/**
	 * Set the maximum width.
	 * 
	 * @param maxWidth The maximum width.
	 * @see javafx.stage.Stage#setMaxWidth(double)
	 */
	public final void setMaxWidth(double maxWidth) {
		stage.setMaxWidth(maxWidth);
	}

	/**
	 * Return the maximum width.
	 * 
	 * @return The maximum width.
	 * @see javafx.stage.Stage#getMaxWidth()
	 */
	public final double getMaxWidth() {
		return stage.getMaxWidth();
	}

	/**
	 * Set the maximum height.
	 * 
	 * @param maxHeight The maximum height.
	 * @see javafx.stage.Stage#setMaxHeight(double)
	 */
	public final void setMaxHeight(double maxHeight) {
		stage.setMaxHeight(maxHeight);
	}

	/**
	 * Return the maximum height.
	 * 
	 * @return The maximum height.
	 * @see javafx.stage.Stage#getMaxHeight()
	 */
	public final double getMaxHeight() {
		return stage.getMaxHeight();
	}

	/**
	 * Set the button on the top pane.
	 */
	public void setButtonsTop() {
		buttonPane = new ButtonPane(Orientation.HORIZONTAL);
		borderPane.setTop(buttonPane.getPane());
		buttonPanePos = ButtonPanePos.TOP;
	}

	/**
	 * Set the button on the bottom pane.
	 */
	public void setButtonsBottom() {
		buttonPane = new ButtonPane(Orientation.HORIZONTAL);
		borderPane.setBottom(buttonPane.getPane());
		buttonPanePos = ButtonPanePos.BOTTOM;
	}

	/**
	 * Set the button on the right pane.
	 */
	public void setButtonsRight() {
		buttonPane = new ButtonPane(Orientation.VERTICAL);
		borderPane.setRight(buttonPane.getPane());
		buttonPanePos = ButtonPanePos.RIGHT;
	}

	/**
	 * Set the button on the left pane.
	 */
	public void setButtonsLeft() {
		buttonPane = new ButtonPane(Orientation.VERTICAL);
		borderPane.setLeft(buttonPane.getPane());
		buttonPanePos = ButtonPanePos.LEFT;
	}

	/**
	 * Gives access to the buttons pane to manage, add or remove, buttons.
	 * 
	 * @return The option pane.
	 */
	public ButtonPane getButtonPane() {
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
		if (buttonPanePos != null && buttonPanePos.equals(ButtonPanePos.TOP)) {
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
		if (buttonPanePos != null && buttonPanePos.equals(ButtonPanePos.LEFT)) {
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
		if (buttonPanePos != null && buttonPanePos.equals(ButtonPanePos.BOTTOM)) {
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
		if (buttonPanePos != null && buttonPanePos.equals(ButtonPanePos.RIGHT)) {
			throw new IllegalStateException("Buttons position is right");
		}
		borderPane.setRight(node);
	}

	/**
	 * Add a property setter to the list.
	 * 
	 * @param propertySetter
	 */
	public void addPropertySetter(PropertySetter propertySetter) {
		propertySetters.add(propertySetter);
	}

	/**
	 * Setup the button. Set the stage, save the initial action, and set a list of actions to close the stage if
	 * required and to set the result.
	 * 
	 * @param button The button to setup.
	 */
	private void setupButton(Button button) {
		// The list of actions that will be set to the node. Use an instance of the private extension of action-list to
		// correctly identify and manage the original button action.
		Actions actions = new Actions(button);
		// If the button has an initial action, save it.
		if (button.getOnAction() != null && !(button.getOnAction() instanceof Actions)) {
			FX.setAction(button, button.getOnAction());
		}
		// If the button had an initial current action, add it.
		if (FX.getAction(button) != null) {
			actions.addHandler(FX.getAction(button));
		}
		// If the button wants to close the stage, add the proper action.
		if (FX.isClose(button)) {
			actions.addHandler(new ActionClose(button));
		}
		// Add the action to set the result to be the button.
		actions.addHandler(new Result(button));
		// Set the action list as the button action.
		button.setOnAction(actions);
		// User properties.
		propertySetters.forEach(propertySetter -> propertySetter.setProperties(button));
	}

	/**
	 * Show the dialog and return the button that closed it.
	 * 
	 * @return The button that closed the dialog.
	 */
	public Button show() {

		// Layout options and set the event handler to close the stage and set the result.
		if (buttonPane != null) {
			buttonPane.layoutButtons();
			// Set listeners to setup the buttons if the list of buttons has changed.
			buttonPane.getButtons().addListener((ListChangeListener<? super Button>) e -> {
				buttonPane.getButtons().forEach(button -> setupButton(button));
			});
			buttonPane.getButtons().addListener((InvalidationListener) e -> {
				buttonPane.getButtons().forEach(button -> setupButton(button));
			});
			// Initial buttons setup.
			buttonPane.getButtons().forEach(button -> setupButton(button));
		}

		// Set ESC to close the stage.
		if (isCloseOnEscape()) {
			List<Node> nodes = new ArrayList<>();
			FX.fillNodesFrom(stage.getScene().getRoot(), nodes);
			for (Node node : nodes) {
				node.setOnKeyPressed(e -> {
					if (e.getCode() == KeyCode.ESCAPE) {
						stage.close();
					}
				});
			}
		}

		// Do show.
		stage.showAndWait();

		// If result is null expect close by other means, look for a cancel button to assign the result.
		if (result == null && buttonPane != null) {
			// Look for a cancel button.
			for (Button button : buttonPane.getButtons()) {
				if (button.isCancelButton()) {
					result = button;
					break;
				}
			}
			// Only one button and not cancel button, take it anyway.
			if (result == null && buttonPane.getButtons().size() == 1) {
				result = buttonPane.getButtons().get(0);
			}
		}
		// No buttons, result is default close button.
		if (result == null && (buttonPane == null || buttonPane.getButtons().isEmpty())) {
			result = Buttons.CLOSE;
		}

		return result;
	}
}
