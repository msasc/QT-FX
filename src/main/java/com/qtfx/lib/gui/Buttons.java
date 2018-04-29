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
	
	private static String getString(String key) {
		return Session.getSession().getString(key);
	}

	/** Default accept button. */
	public final static Button ACCEPT = _button("ACCEPT", getString("buttonAccept"), true, false, true);
	/** Default apply button. */
	public static final Button APPLY = _button("APPLY", getString("buttonApply"), false, false, true);
	/** Default cancel button. */
	public static final Button CANCEL = _button("CANCEL", getString("buttonCancel"), false, true, true);
	/** Default close button. */
	public static final Button CLOSE = _button("CLOSE", getString("buttonClose"), false, false, true);
	/** Default finish button. */
	public static final Button FINISH = _button("FINISH", getString("buttonFinish"), false, false, true);
	/** Default ignore button. */
	public static final Button IGNORE = _button("IGNORE", getString("buttonIgnore"), false, false, true);
	/** Default next button. */
	public static final Button NEXT = _button("NEXT", getString("buttonNext"), false, false, false);
	/** Default no button. */
	public static final Button NO = _button("NO", getString("buttonNo"), false, true, true);
	/** Default ok button. */
	public static final Button OK = _button("OK", getString("buttonOk"), true, false, true);
	/** Default open button. */
	public static final Button OPEN = _button("OPEN", getString("buttonOpen"), true, false, true);
	/** Default previous button. */
	public static final Button PREVIOUS = _button("PREVIOUS", getString("buttonPrevious"), false, false, false);
	/** Default retry button. */
	public static final Button RETRY = _button("RETRY", getString("buttonRetry"), false, false, true);
	/** Default accept button. */
	public static final Button SELECT = _button("SELECT", getString("buttonSelect"), true, false, true);
	/** Default yes button. */
	public static final Button YES = _button("YES", getString("buttonYes"), true, false, true);

	/**
	 * Check the button.
	 * 
	 * @param button The button.
	 * @param id The id.
	 * @return A boolean.
	 */
	public static final boolean is(Button button, String id) {
		return button.getId().equals(id);
	}

	public static final boolean isAccept(Button button) {
		return is(button, "ACCEPT");
	}

	public static final boolean isApply(Button button) {
		return is(button, "APPLY");
	}

	public static final boolean isCancel(Button button) {
		return is(button, "CANCEL");
	}

	public static final boolean isClose(Button button) {
		return is(button, "CLOSE");
	}

	public static final boolean isFinish(Button button) {
		return is(button, "FINISH");
	}

	public static final boolean isIgnore(Button button) {
		return is(button, "IGNORE");
	}

	public static final boolean isNext(Button button) {
		return is(button, "NEXT");
	}

	public static final boolean isNo(Button button) {
		return is(button, "NO");
	}

	public static final boolean isOk(Button button) {
		return is(button, "OK");
	}

	public static final boolean isOpen(Button button) {
		return is(button, "OPEN");
	}

	public static final boolean isPrevious(Button button) {
		return is(button, "PREVIOUS");
	}

	public static final boolean isRetry(Button button) {
		return is(button, "RETRY");
	}

	public static final boolean isSelect(Button button) {
		return is(button, "SELECT");
	}

	public static final boolean isYes(Button button) {
		return is(button, "YES");
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
	public static Button _button(
		String id,
		String text,
		boolean defaultButton,
		boolean cancelButton,
		boolean close) {
		Button button = new Button(text);
		button.setId(id);
		button.setDefaultButton(defaultButton);
		button.setCancelButton(cancelButton);
		FX.setClose(button, close);
		return button;
	}
}
