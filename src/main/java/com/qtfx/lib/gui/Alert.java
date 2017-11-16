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

import com.qtfx.lib.util.Icons;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Window;

/**
 * An alert dialog that supports the standard PLAIN, INFORMATION, WARNING, CONFIRMATION and ERROR configurations, with
 * some predefined buttons. The image is laid out in the left and the content is a text flow pane.
 *
 * @author Miquel Sas
 */
public class Alert {

	/**
	 * Enumerate alert types.
	 */
	public static enum Type {
		/** No icon with default OK option. */
		PLAIN,
		/** Information icon with default OK option. */
		INFORMATION,
		/** Warning icon with default OK option. */
		WARNING,
		/** Error icon with default OK option. */
		ERROR,
		/** Confirmation icon with default OK and CANCEL options. */
		CONFIRMATION
	}

	/** Default ACCEPT button. */
	public static final Button ACCEPT = Buttons.buttonAccept();
	/** Default CANCEL button. */
	public static final Button CANCEL = Buttons.buttonCancel();
	/** Default NO button. */
	public static final Button NO = Buttons.buttonNo();
	/** Default OK button. */
	public static final Button OK = Buttons.buttonOk();
	/** Default YES button. */
	public static final Button YES = Buttons.buttonYes();

	/**
	 * Simple predefined alert by type.
	 * 
	 * @param title The title.
	 * @param message The message.
	 * @param type The type.
	 * @return The selected button.
	 */
	public static Button alert(String title, String message, Type type) {
		return alert(null, title, message, type);
	}

	/**
	 * Simple predefined alert by type.
	 * 
	 * @param owner The owner window or null for application modal.
	 * @param title The title.
	 * @param message The message.
	 * @param type The type.
	 * @return The selected button.
	 */
	public static Button alert(Window owner, String title, String message, Type type) {
		Alert alert = new Alert(owner);
		alert.setTitle(title);
		alert.addText(message);
		alert.setType(type);
		Button result = alert.show();
		return result;
	}

	/**
	 * Simple predefined alert by buttons.
	 * 
	 * @param title The title.
	 * @param message The message.
	 * @param buttons The list of buttons.
	 * @return The selected button.
	 */
	public static Button alert(String title, String message, Button... buttons) {
		return alert(null, title, message, buttons);
	}

	/**
	 * Simple predefined alert by buttons.
	 * 
	 * @param owner The owner window or null for application modal.
	 * @param title The title.
	 * @param message The message.
	 * @param buttons The list of buttons.
	 * @return The selected button.
	 */
	public static Button alert(Window owner, String title, String message, Button... buttons) {
		Alert alert = new Alert(owner);
		alert.setTitle(title);
		alert.addText(message);
		alert.setButtons(buttons);
		Button result = alert.show();
		return result;
	}

	/**
	 * Plain alert.
	 * 
	 * @param title The title.
	 * @param message The message.
	 * @return The button.
	 */
	public static Button plain(String title, String message) {
		return plain(null, title, message);
	}

	/**
	 * Plain alert.
	 * 
	 * @param owner The window owner.
	 * @param title The title.
	 * @param message The message.
	 * @return The button.
	 */
	public static Button plain(Window owner, String title, String message) {
		return alert(owner, title, message, Type.PLAIN);
	}

	/**
	 * Information alert.
	 * 
	 * @param title The title.
	 * @param message The message.
	 * @return The button.
	 */
	public static Button info(String title, String message) {
		return info(null, title, message);
	}

	/**
	 * Information alert.
	 * 
	 * @param owner The window owner.
	 * @param title The title.
	 * @param message The message.
	 * @return The button.
	 */
	public static Button info(Window owner, String title, String message) {
		return alert(owner, title, message, Type.INFORMATION);
	}

	/**
	 * Warning alert.
	 * 
	 * @param title The title.
	 * @param message The message.
	 * @return The button.
	 */
	public static Button warning(String title, String message) {
		return warning(null, title, message);
	}

	/**
	 * Warning alert.
	 * 
	 * @param owner The window owner.
	 * @param title The title.
	 * @param message The message.
	 * @return The button.
	 */
	public static Button warning(Window owner, String title, String message) {
		return alert(owner, title, message, Type.WARNING);
	}

	/**
	 * Error alert.
	 * 
	 * @param title The title.
	 * @param message The message.
	 * @return The button.
	 */
	public static Button error(String title, String message) {
		return error(null, title, message);
	}

	/**
	 * Error alert.
	 * 
	 * @param owner The window owner.
	 * @param title The title.
	 * @param message The message.
	 * @return The button.
	 */
	public static Button error(Window owner, String title, String message) {
		return alert(owner, title, message, Type.ERROR);
	}

	/**
	 * Confirmation alert.
	 * 
	 * @param title The title.
	 * @param message The message.
	 * @return The button.
	 */
	public static Button confirm(String title, String message) {
		return confirm(null, title, message);
	}

	/**
	 * Confirmation alert.
	 * 
	 * @param owner The window owner.
	 * @param title The title.
	 * @param message The message.
	 * @return The button.
	 */
	public static Button confirm(Window owner, String title, String message) {
		return alert(owner, title, message, Type.CONFIRMATION);
	}

	/** Internal dialog. */
	private Dialog dialog;
	/** Optional graphics node (or not). */
	private Node graphics;
	/** Text flow. */
	private TextFlow textFlow;
	/** All element padding. */
	private double padding = 10;

	/**
	 * Constructor.
	 */
	public Alert() {
		this(null);
	}

	/**
	 * Constructor.
	 * 
	 * @param owner The window owner.
	 */
	public Alert(Window owner) {
		super();
		dialog = new Dialog(owner);
		dialog.setButtonsBottom();
		textFlow = new TextFlow();
	}

	/**
	 * Set the title.
	 * 
	 * @param title The title.
	 */
	public void setTitle(String title) {
		dialog.setTitle(title);
	}

	/**
	 * Set the default padding for image, text and buttons.
	 * 
	 * @param padding The default padding.
	 */
	public void setPadding(double padding) {
		this.padding = padding;
	}

	/**
	 * Set one of the standard types with default options.
	 * 
	 * @param type The type.
	 */
	public void setType(Type type) {
		if (type == null) {
			throw new NullPointerException();
		}
		switch (type) {
		case PLAIN:
			dialog.getButtonPane().getButtons().add(OK);
			break;
		case INFORMATION:
			dialog.getButtonPane().getButtons().add(OK);
			graphics = Icons.get(Icons.APP_32x32_DIALOG_INFORMATION);
			break;
		case ERROR:
			dialog.getButtonPane().getButtons().add(OK);
			graphics = Icons.get(Icons.APP_32x32_DIALOG_ERROR);
			break;
		case WARNING:
			dialog.getButtonPane().getButtons().add(OK);
			graphics = Icons.get(Icons.APP_32x32_DIALOG_WARNING);
			break;
		case CONFIRMATION:
			dialog.getButtonPane().getButtons().addAll(OK, CANCEL);
			graphics = Icons.get(Icons.APP_32x32_DIALOG_CONFIRMATION);
			break;
		}
	}

	/**
	 * Set the graphics node, normally an image view.
	 * 
	 * @param graphics The graphics node.
	 */
	public void setGraphics(Node graphics) {
		this.graphics = graphics;
	}

	/**
	 * Add the list of buttons.
	 * 
	 * @param buttons The list of buttons.
	 */
	public void addButtons(Button... buttons) {
		dialog.getButtonPane().getButtons().addAll(buttons);
	}

	/**
	 * Set the list of buttons.
	 * 
	 * @param buttons The list of buttons.
	 */
	public void setButtons(Button... buttons) {
		dialog.getButtonPane().getButtons().clear();
		dialog.getButtonPane().getButtons().addAll(buttons);
	}

	/**
	 * Add a text to the text flow pane.
	 * 
	 * @param text The text to add.
	 */
	public void addText(Text text) {
		textFlow.getChildren().add(text);
	}

	/**
	 * Add a simple string text.
	 * 
	 * @param text The text.
	 */
	public void addText(String text) {
		textFlow.getChildren().add(new Text(text));
	}

	/**
	 * Add a string text with a CSS style.
	 * 
	 * @param text The text.
	 * @param style The style.
	 */
	public void addText(String text, String style) {
		Text textNode = new Text(text);
		textNode.setStyle(style);
		textFlow.getChildren().add(textNode);
	}

	/**
	 * Show the alert and return the selected button.
	 * 
	 * @return The selected button.
	 */
	public Button show() {

		textFlow.setPadding(new Insets(padding, padding, padding, padding));
		dialog.getButtonPane().setPadding(new Insets(0, padding, padding, padding));

		BorderPane content = new BorderPane();
		if (graphics != null) {
			VBox left = new VBox();
			left.setPadding(new Insets(padding, 0, padding, padding));
			left.getChildren().add(graphics);
			content.setLeft(left);
		}
		content.setCenter(textFlow);
		dialog.setCenter(content);

		Button result = dialog.show();
		return result;
	}
}
