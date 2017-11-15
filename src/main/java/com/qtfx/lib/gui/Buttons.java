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

import com.qtfx.lib.util.TextServer;

import javafx.scene.control.Button;

/**
 * Buttons all around this development system are expected to have properties set in the user data, properties like the
 * button group, the order in the group, or any useful property to handle the action.
 * <p>
 * The button can be accessed through the <em>getSource()</em> or <em>getTarget()</em> methods of the
 * <em>ActionEvent</em>, and this class facilitates accessing those properties.
 *
 * @author Miquel Sas
 */
public class Buttons {

	/** Id for the ACCEPT button. */
	public static final String ID_ACCEPT = "ACCEPT";
	/** Id for the APPLY button. */
	public static final String ID_APPLY = "APPLY";
	/** Id for the CANCEL button. */
	public static final String ID_CANCEL = "CANCEL";
	/** Id for the CLOSE button. */
	public static final String ID_CLOSE = "CLOSE";
	/** Id for the FINISH button. */
	public static final String ID_FINISH = "FINISH";
	/** Id for the IGNORE button. */
	public static final String ID_IGNORE = "IGNORE";
	/** Id for the NEXT button. */
	public static final String ID_NEXT = "NEXT";
	/** Id for the NO button. */
	public static final String ID_NO = "NO";
	/** Id for the OK button. */
	public static final String ID_OK = "OK";
	/** Id for the OPEN button. */
	public static final String ID_OPEN = "OPEN";
	/** Id for the PREVIOUS button. */
	public static final String ID_PREVIOUS = "PREVIOUS";
	/** Id for the RETRY button. */
	public static final String ID_RETRY = "RETRY";
	/** Id for the YES button. */
	public static final String ID_YES = "YES";

	/**
	 * Return the button.
	 * 
	 * @param id The button id.
	 * @param text The text.
	 * @param defaultButton A boolean.
	 * @param cancelButton A boolean.
	 * @return The button.
	 */
	public static Button button(String id, String text, boolean defaultButton, boolean cancelButton) {
		return button(id, text, defaultButton, cancelButton, true);
	}

	/**
	 * Return the button.
	 * 
	 * @param id The button id.
	 * @param text The text.
	 * @param defaultButton A boolean.
	 * @param cancelButton A boolean.
	 * @param close A boolean to force close the window.
	 * @return The button.
	 */
	public static Button button(String id, String text, boolean defaultButton, boolean cancelButton, boolean close) {
		Button button = new Button(text);
		button.setId(id);
		button.setDefaultButton(defaultButton);
		button.setCancelButton(cancelButton);
		Nodes.setClose(button, close);
		return button;
	}

	/**
	 * @return A new ACCEPT button, true, false.
	 */
	public static Button buttonAccept() {
		return button(ID_ACCEPT, TextServer.getString("buttonAccept"), true, false);
	}

	/**
	 * @return A new APPLY button, false, false.
	 */
	public static Button buttonApply() {
		return button(ID_APPLY, TextServer.getString("buttonApply"), false, false);
	}

	/**
	 * @return A new CANCEL button, false, true.
	 */
	public static Button buttonCancel() {
		return button(ID_CANCEL, TextServer.getString("buttonCancel"), false, true);
	}

	/**
	 * @return A new CLOSE button, false, false.
	 */
	public static Button buttonClose() {
		return button(ID_CLOSE, TextServer.getString("buttonClose"), false, false);
	}

	/**
	 * @return A new FINISH button, false, false.
	 */
	public static Button buttonFinish() {
		return button(ID_FINISH, TextServer.getString("buttonFinish"), false, false);
	}

	/**
	 * @return A new IGNORE button, false, false.
	 */
	public static Button buttonIgnore() {
		return button(ID_IGNORE, TextServer.getString("buttonIgnore"), false, false);
	}

	/**
	 * @return A new NEXT button, false, false.
	 */
	public static Button buttonNext() {
		return button(ID_NEXT, TextServer.getString("buttonNext"), false, false);
	}

	/**
	 * @return A new NO button, false, true.
	 */
	public static Button buttonNo() {
		return button(ID_NO, TextServer.getString("buttonNo"), false, true);
	}

	/**
	 * @return A new OK button, true, false.
	 */
	public static Button buttonOk() {
		return button(ID_OK, TextServer.getString("buttonOk"), true, false);
	}

	/**
	 * @return A new OPEN button, false, false.
	 */
	public static Button buttonOpen() {
		return button(ID_OPEN, TextServer.getString("buttonOpen"), true, false);
	}

	/**
	 * @return A new PREVIOUS button, false, false.
	 */
	public static Button buttonPrevious() {
		return button(ID_PREVIOUS, TextServer.getString("buttonPrevious"), false, false);
	}

	/**
	 * @return A new RETRY button, false, false.
	 */
	public static Button buttonRetry() {
		return button(ID_RETRY, TextServer.getString("buttonRetry"), false, false);
	}

	/**
	 * @return A new YES button, true, false.
	 */
	public static Button buttonYes() {
		return button(ID_YES, TextServer.getString("buttonYes"), true, false);
	}

}
