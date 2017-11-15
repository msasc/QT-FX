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

import com.qtfx.lib.util.Properties;

import javafx.scene.Node;

/**
 * Nodes in this JavaFX development tools are sometimes expected to have properties in the user data. This class help
 * manage those properties.
 *
 * @author Miquel Sas
 */
public class Nodes {

	/**
	 * Return a string property for the given key and an empty string default value.
	 * 
	 * @param node The node.
	 * @param key The key.
	 * @return The string property.
	 */
	public static String getString(Node node, Object key) {
		return getProperties(node).getString(key, "");
	}

	/**
	 * Set the string property to the node.
	 * 
	 * @param node The node.
	 * @param key The key.
	 * @param value The value.
	 */
	public static void setString(Node node, Object key, String value) {
		getProperties(node).setString(key, value);
	}

	/**
	 * Return the object property.
	 * 
	 * @param node The node.
	 * @param key The key.
	 * @return The object property.
	 */
	public static Object getObject(Node node, Object key) {
		return getProperties(node).getObject(key);
	}

	/**
	 * Set the object property to the node.
	 * 
	 * @param node The node.
	 * @param key The key.
	 * @param value The value.
	 */
	public static void setObject(Node node, Object key, Object value) {
		getProperties(node).setObject(key, value);
	}

	/**
	 * Return the properties in the user data of the node. If the node has a user data of a non properties class, an
	 * illegal argument exception is thrown.
	 * 
	 * @param node The node.
	 * @return The <em>Properties</em> in the user data.
	 */
	public static Properties getProperties(Node node) {
		Object userData = node.getUserData();
		if (userData != null && !(userData instanceof Properties)) {
			throw new IllegalArgumentException("Noide has user data of type: " + userData.getClass());
		}
		Properties properties;
		if (userData instanceof Properties) {
			properties = (Properties) userData;
		} else {
			properties = new Properties();
			node.setUserData(properties);
		}
		return properties;
	}
}
