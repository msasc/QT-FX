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

import com.qtfx.library.util.TextServer;

import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

/**
 * An option packs the necessary attributes to define a button to be laid in an option pane. Buttons are ordered by the
 * order key within groups that in turn are ordered by the group key.
 * 
 * @author Miquel Sas
 */
public class Option {

	/**
	 * @return A new ACCEPT option, true, false.
	 */
	public static Option accept() {
		return new Option(TextServer.getString("alertOptionAccept"), true, false);
	}

	/**
	 * @return A new APPLY option, false, false.
	 */
	public static Option apply() {
		return new Option(TextServer.getString("alertOptionApply"), false, false);
	}

	/**
	 * @return A new CANCEL option, false, true.
	 */
	public static Option cancel() {
		return new Option(TextServer.getString("alertOptionCancel"), false, true);
	}

	/**
	 * @return A new CLOSE option, false, false.
	 */
	public static Option close() {
		return new Option(TextServer.getString("alertOptionClose"), false, false);
	}

	/**
	 * @return A new FINISH option, false, false.
	 */
	public static Option finish() {
		return new Option(TextServer.getString("alertOptionFinish"), false, false);
	}

	/**
	 * @return A new IGNORE option, false, false.
	 */
	public static Option ignore() {
		return new Option(TextServer.getString("alertOptionIgnore"), false, false);
	}

	/**
	 * @return A new NEXT option, false, false.
	 */
	public static Option next() {
		return new Option(TextServer.getString("alertOptionNext"), false, false);
	}

	/**
	 * @return A new NO option, false, true.
	 */
	public static Option no() {
		return new Option(TextServer.getString("alertOptionNo"), false, true);
	}

	/**
	 * @return A new OK option, true, false.
	 */
	public static Option ok() {
		return new Option(TextServer.getString("alertOptionOk"), true, false);
	}

	/**
	 * @return A new OPEN option, false, false.
	 */
	public static Option open() {
		return new Option(TextServer.getString("alertOptionOpen"), true, false);
	}

	/**
	 * @return A new PREVIOUS option, false, false.
	 */
	public static Option previous() {
		return new Option(TextServer.getString("alertOptionPrevious"), false, false);
	}

	/**
	 * @return A new RETRY option, false, false.
	 */
	public static Option retry() {
		return new Option(TextServer.getString("alertOptionRetry"), false, false);
	}

	/**
	 * @return A new YES option, true, false.
	 */
	public static Option yes() {
		return new Option(TextServer.getString("alertOptionYes"), true, false);
	}

	/** Option text. */
	private String text;
	/** Default button indicator. */
	private boolean defaultButton;
	/** Cancel button indicator. */
	private boolean cancelButton;
	/** Graphic (image view). */
	private ImageView imageView;
	/** Order string. */
	private String order;
	/** Group string. */
	private String group;

	/** The button associated with this option. */
	private Button button;

	/** Action event handler. */
	private ActionHandler actionHandler;

	/**
	 * Constructor assigning text and default and cancel flags.
	 * 
	 * @param text The button text.
	 * @param defaultButton Default flag.
	 * @param cancelButton Cancel flag.
	 */
	public Option(String text, boolean defaultButton, boolean cancelButton) {
		super();
		this.text = text;
		this.defaultButton = defaultButton;
		this.cancelButton = cancelButton;
	}

	/**
	 * Return the underlying button of this option.
	 * 
	 * @return The button.
	 */
	public Button getButton() {
		if (button == null) {
			button = new Button();

			// Text if present.
			if (text != null) {
				button.setText(getText());
			}

			// Default and cancel properties.
			button.setDefaultButton(defaultButton);
			button.setCancelButton(cancelButton);

			// Image if present.
			if (imageView != null) {
				button.setGraphic(imageView);
			}

			// Set this option as user data.
			button.setUserData(this);
		}
		return button;
	}

	/**
	 * Return the text.
	 * 
	 * @return The text.
	 */
	public String getText() {
		return text;
	}

	/**
	 * Set the button text.
	 * 
	 * @param text The text.
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * Check if this is a default button.
	 * 
	 * @return A boolean.
	 */
	public boolean isDefaultButton() {
		return defaultButton;
	}

	/**
	 * Set that the button will be a default button. Only a button in a dialog can be the default button.
	 * 
	 * @param defaultButton A boolean.
	 */
	public void setDefaultButton(boolean defaultButton) {
		this.defaultButton = defaultButton;
	}

	/**
	 * Check if this is a cancel button.
	 * 
	 * @return A boolean.
	 */
	public boolean isCancelButton() {
		return cancelButton;
	}

	/**
	 * Set that the button will be a cancel button. Only a button in a dialog can be the cancel button.
	 * 
	 * @param cancelButton A boolean.
	 */
	public void setCancelButton(boolean cancelButton) {
		this.cancelButton = cancelButton;
	}

	/**
	 * Return the image view.
	 * 
	 * @return The image.
	 */
	public ImageView getImageView() {
		return imageView;
	}

	/**
	 * set the image to be shown in the button.
	 * 
	 * @param imageView The image.
	 */
	public void setImageView(ImageView imageView) {
		this.imageView = imageView;
	}

	/**
	 * Return the order.
	 * 
	 * @return The order.
	 */
	public String getOrder() {
		if (order == null) {
			return "";
		}
		return order;
	}

	/**
	 * Set the order.
	 * 
	 * @param order The order.
	 */
	public void setOrder(String order) {
		this.order = order;
	}

	/**
	 * Return the group.
	 * 
	 * @return The group.
	 */
	public String getGroup() {
		if (group == null) {
			return "";
		}
		return group;
	}

	/**
	 * Set the group.
	 * 
	 * @param group The group.
	 */
	public void setGroup(String group) {
		this.group = group;
	}

	/**
	 * Return this option action handler.
	 * 
	 * @return The action handler.
	 */
	public ActionHandler getActionHandler() {
		return actionHandler;
	}

	/**
	 * Set this option action handler.
	 * 
	 * @param actionHandler The action handler.
	 */
	public void setActionHandler(ActionHandler actionHandler) {
		this.actionHandler = actionHandler;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj || (this != null && this == obj)) {
			return true;
		}
		if (obj instanceof Option) {
			Option o = (Option) obj;
			return text != null && o.text != null && text.equals(o.text);
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return getText().toString();
	}
}
