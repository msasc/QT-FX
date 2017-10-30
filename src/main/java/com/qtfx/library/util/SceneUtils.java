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

package com.qtfx.library.util;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

/**
 * Scene utilities.
 * 
 * @author Miquel Sas
 */
public class SceneUtils {

	/**
	 * Return the child with the given id or null if a child with the given id is not contained.
	 * 
	 * @param pane Parent pane.
	 * @param childId The child id.
	 * @return The child node or null.
	 */
	public static Node getChild(Pane pane, String childId) {
		for (Node child : pane.getChildren()) {
			String id = child.getId();
			if (id != null && id.equals(childId)) {
				return child;
			}
		}
		return null;
	}

}
