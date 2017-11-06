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

import javafx.scene.Scene;

/**
 * Action utilities.
 * 
 * @author Miquel Sas
 */
public class ActionUtils {

	/** Integer key value. */
	private static int index = 0;

	/** The scene where the action occurred. */
	private static final Integer SCENE = index++;

	/**
	 * Return the scene where the action occurred.
	 * 
	 * @param e The action handler.
	 * @return The scene where the action occurred.
	 */
	public static Scene getScene(ActionHandler e) {
		return (Scene) e.getProperties().getObject(SCENE);
	}

	/**
	 * Set the scene where the action occurred.
	 * 
	 * @param e The action handler.
	 * @param scene The scene where the action occurred.
	 */
	public static void setScene(ActionHandler e, Scene scene) {
		e.getProperties().setObject(SCENE, scene);
	}
}
