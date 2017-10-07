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

import javafx.scene.control.Dialog;

/**
 * An alert box that internally uses a {@link Dialog} and exposes a simple and more fluent interface.
 * 
 * @author Miquel Sas
 */
public class Alert {
	
	/**
	 * Enumerate alert types. The types determine the icon and if not set the buttons.
	 */
	public static enum Type {
		/**
		 * The PLAIN type sets no icon and a default OK button.
		 */
		PLAIN,
		/**
		 * The INFORMATION type sets the information icon and a default OK button.
		 */
		INFORMATION,
		/**
		 * The WARNING type sets the information icon and a default OK button.
		 */
		WARNING,
		/**
		 * The CONFIRMATION type sets the confirmation icon and default OK and Cancel buttons.
		 */
		CONFIRMATION,
		/**
		 * The ERROR type sets the information icon and a default OK button.
		 */
		ERROR
	}
	
	/** Alert type. */
	private Type type;
	/** Dialog. */
	private Dialog<Option> dialog;

	/**
	 * 
	 * @param alertType
	 */
	public Alert(Type type) {
		super();
		this.type = type;
	}
	

}
