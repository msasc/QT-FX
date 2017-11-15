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

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.stage.Stage;

/**
 * Nodes in this JavaFX development tools are sometimes expected to have properties in the user data. This class help
 * manage those properties.
 *
 * @author Miquel Sas
 */
public class Nodes {
	
	/** Property ACTION. */
	public static final String PROPERTY_ACTION = "ACTION";
	/** Property CLOSE. */
	public static final String PROPERTY_CLOSE = "CLOSE";
	/** Property GROUP. */
	public static final String PROPERTY_GROUP = "GROUP";
	/** Property ORDER. */
	public static final String PROPERTY_ORDER = "ORDER";
	/** Property WINDOW. */
	public static final String PROPERTY_STAGE = "STAGE";

	/**
	 * Return the action property.
	 * 
	 * @param node The node.
	 * @return The action property.
	 */
	@SuppressWarnings("unchecked")
	public static EventHandler<ActionEvent> getAction(Node node) {
		return (EventHandler<ActionEvent>) getObject(node, PROPERTY_ACTION);
	}

	/**
	 * Set the action property.
	 * 
	 * @param node The node.
	 * @param action The action.
	 */
	public static void setAction(Node node, EventHandler<ActionEvent> action) {
		setObject(node, PROPERTY_ACTION, action);
	}

	/**
	 * Return the close property.
	 * 
	 * @param node The node.
	 * @return A boolean.
	 */
	public static boolean isClose(Node node) {
		return getBoolean(node, PROPERTY_CLOSE);
	}

	/**
	 * Set the close property.
	 * 
	 * @param node The node.
	 * @param close A boolean.
	 */
	public static void setClose(Node node, boolean close) {
		setBoolean(node, PROPERTY_CLOSE, close);
	}

	/**
	 * Return the group property.
	 * 
	 * @param node The node.
	 * @return The group.
	 */
	public static String getGroup(Node node) {
		return getString(node, PROPERTY_GROUP);
	}

	/**
	 * Set the group property.
	 * 
	 * @param node The node.
	 * @param group The group.
	 */
	public static void setGroup(Node node, String group) {
		setString(node, PROPERTY_GROUP, group);
	}

	/**
	 * Return the order in the group.
	 * 
	 * @param node The node.
	 * @return The order in the group.
	 */
	public static String getOrder(Node node) {
		return getString(node, PROPERTY_ORDER);
	}

	/**
	 * Set the order in the group.
	 * 
	 * @param node The node.
	 * @param order The order in the group.
	 */
	public static void setOrder(Node node, String order) {
		setString(node, PROPERTY_ORDER, order);
	}

	/**
	 * Return the stage property.
	 * 
	 * @param node The node.
	 * @return The stage property.
	 */
	public static Stage getStage(Node node) {
		return (Stage) getObject(node, PROPERTY_STAGE);
	}

	/**
	 * Set the stage property.
	 * 
	 * @param node The node.
	 * @param stage The stage property.
	 */
	public static void setStage(Node node, Stage stage) {
		setObject(node, PROPERTY_STAGE, stage);
	}

	/**
	 * Return a boolean property for the given key, with false default value.
	 * 
	 * @param node The node.
	 * @param key The key.
	 * @return A boolean.
	 */
	public static boolean getBoolean(Node node, Object key) {
		return getProperties(node).getBoolean(key);
	}

	/**
	 * Set the boolean property.
	 * 
	 * @param node The node.
	 * @param key The key.
	 * @param value A boolean.
	 */
	public static void setBoolean(Node node, Object key, boolean value) {
		getProperties(node).setBoolean(key, value);
	}

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
