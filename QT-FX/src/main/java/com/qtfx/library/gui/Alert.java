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

import com.qtfx.library.util.Icons;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Window;

/**
 * An alert dialog that supports the standard PLAIN, INFORMATION, WARNING, CONFIRMATION and ERROR configurations, with
 * some predefined options. The image is laid out in the left and the content is a text flow pane.
 *
 * @author Miquel Sas
 */
public class Alert {

	/** Default ACCEPT option. */
	public static final Option ACCEPT = Option.accept();
	/** Default CANCEL option. */
	public static final Option CANCEL = Option.cancel();
	/** Default NO option. */
	public static final Option NO = Option.no();
	/** Default OK option. */
	public static final Option OK = Option.ok();
	/** Default YES option. */
	public static final Option YES = Option.yes();

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

	/** Internal dialog. */
	private Dialog dialog;
	/** Optional graphics node (or not). */
	private Node graphics;
	/** Text flow. */
	private TextFlow textFlow;
	/** All element padding. */
	private double padding = 10;

	/**
	 * Constructor, application modal.
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
			dialog.getOptionPane().getOptions().add(OK);
			break;
		case INFORMATION:
			dialog.getOptionPane().getOptions().add(OK);
			graphics = Icons.get(Icons.APP_32x32_DIALOG_INFORMATION);
			break;
		case ERROR:
			dialog.getOptionPane().getOptions().add(OK);
			graphics = Icons.get(Icons.APP_32x32_DIALOG_ERROR);
			break;
		case WARNING:
			dialog.getOptionPane().getOptions().add(OK);
			graphics = Icons.get(Icons.APP_32x32_DIALOG_WARNING);
			break;
		case CONFIRMATION:
			dialog.getOptionPane().getOptions().addAll(OK, CANCEL);
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
	 * Set (add) the list of options.
	 * 
	 * @param options The list of options.
	 */
	public void setOptions(Option... options) {
		dialog.getOptionPane().getOptions().addAll(options);
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
	 * Show the alert and return the selected option.
	 * 
	 * @return The selected option.
	 */
	public Option show() {

		textFlow.setPadding(new Insets(padding, padding, padding, padding));
		dialog.getOptionPane().setPadding(new Insets(0, padding, padding, padding));

		BorderPane content = new BorderPane();
		if (graphics != null) {
			VBox left = new VBox();
			left.setPadding(new Insets(padding, 0, padding, padding));
			left.getChildren().add(graphics);
			content.setLeft(left);
		}
		content.setCenter(textFlow);
		dialog.setContent(content);

		Option result = dialog.show();
		return result;
	}
}
