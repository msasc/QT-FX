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

import com.qtfx.lib.app.Session;

import javafx.scene.control.Button;

/**
 * Button utilities. Buttons all around this development system are expected to have properties set in the user data,
 * properties like the button group, the order in the group, or any useful property to handle the action.
 *
 * @author Miquel Sas
 */
public class Buttons {

	/**
	 * Returns a default accept button.
	 * 
	 * @param session The working session.
	 * @return A new ACCEPT button, true, false.
	 */
	public static Button ACCEPT(Session session) {
		return _button(session, "ACCEPT", session.getString("buttonAccept"), true, false, true);
	}

	/**
	 * Returns a default accept button.
	 * 
	 * @param session The working session.
	 * @return A new APPLY button, false, false.
	 */
	public static Button APPLY(Session session) {
		return _button(session, "APPLY", session.getString("buttonApply"), false, false, true);
	}

	/**
	 * Returns a default accept button.
	 * 
	 * @param session The working session.
	 * @return A new CANCEL button, false, true.
	 */
	public static Button CANCEL(Session session) {
		return _button(session, "CANCEL", session.getString("buttonCancel"), false, true, true);
	}

	/**
	 * Returns a default accept button.
	 * 
	 * @param session The working session.
	 * @return A new CLOSE button, false, false.
	 */
	public static Button CLOSE(Session session) {
		return _button(session, "CLOSE", session.getString("buttonClose"), false, false, true);
	}

	/**
	 * Returns a default accept button.
	 * 
	 * @param session The working session.
	 * @return A new FINISH button, false, false.
	 */
	public static Button FINISH(Session session) {
		return _button(session, "FINISH", session.getString("buttonFinish"), false, false, true);
	}

	/**
	 * Returns a default accept button.
	 * 
	 * @param session The working session.
	 * @return A new IGNORE button, false, false.
	 */
	public static Button IGNORE(Session session) {
		return _button(session, "IGNORE", session.getString("buttonIgnore"), false, false, true);
	}

	/**
	 * Returns a default accept button.
	 * 
	 * @param session The working session.
	 * @return A new NEXT button, false, false.
	 */
	public static Button NEXT(Session session) {
		return _button(session, "NEXT", session.getString("buttonNext"), false, false, false);
	}

	/**
	 * Returns a default accept button.
	 * 
	 * @param session The working session.
	 * @return A new NO button, false, true.
	 */
	public static Button NO(Session session) {
		return _button(session, "NO", session.getString("buttonNo"), false, true, true);
	}

	/**
	 * Returns a default accept button.
	 * 
	 * @param session The working session.
	 * @return A new OK button, true, false.
	 */
	public static Button OK(Session session) {
		return _button(session, "OK", session.getString("buttonOk"), true, false, true);
	}

	/**
	 * Returns a default accept button.
	 * 
	 * @param session The working session.
	 * @return A new OPEN button, false, false.
	 */
	public static Button OPEN(Session session) {
		return _button(session, "OPEN", session.getString("buttonOpen"), true, false, true);
	}

	/**
	 * Returns a default accept button.
	 * 
	 * @param session The working session.
	 * @return A new PREVIOUS button, false, false.
	 */
	public static Button PREVIOUS(Session session) {
		return _button(session, "PREVIOUS", session.getString("buttonPrevious"), false, false, false);
	}

	/**
	 * Returns a default accept button.
	 * 
	 * @param session The working session.
	 * @return A new RETRY button, false, false.
	 */
	public static Button RETRY(Session session) {
		return _button(session, "RETRY", session.getString("buttonRetry"), false, false, true);
	}

	/**
	 * Returns a default accept button.
	 * 
	 * @param session The working session.
	 * @return A new SELECT button, false, false.
	 */
	public static Button SELECT(Session session) {
		return _button(session, "SELECT", session.getString("buttonSelect"), true, false, true);
	}

	/**
	 * Returns a default accept button.
	 * 
	 * @param session The working session.
	 * @return A new YES button, true, false.
	 */
	public static Button YES(Session session) {
		return _button(session, "YES", session.getString("buttonYes"), true, false, true);
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
	public static Button _button(Session session, String id, String text, boolean defaultButton, boolean cancelButton, boolean close) {
		Button button = new Button(text);
		button.setId(id);
		button.setDefaultButton(defaultButton);
		button.setCancelButton(cancelButton);
		FX.setClose(button, close);
		FX.setSession(button, session);
		return button;
	}
}
