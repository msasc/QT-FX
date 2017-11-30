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

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleMapProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Nodes in this JavaFX development tools are sometimes expected to have properties in the user data. This class help
 * manage those properties.
 *
 * @author Miquel Sas
 */
public class Nodes {

	//////////////////////
	// Keys of properties.

	/** Property ACTION. */
	private static final String PROPERTY_ACTION = "ACTION";
	/** Property CLOSE. */
	private static final String PROPERTY_CLOSE = "CLOSE";
	/** Property GROUP. */
	private static final String PROPERTY_GROUP = "GROUP";
	/** Property ORDER. */
	private static final String PROPERTY_ORDER = "ORDER";
	/** Property WINDOW. */
	private static final String PROPERTY_STAGE = "STAGE";
	/** Property TABLE_RECORD_PANE. */
	private static final String PROPERTY_TABLE_RECORD_PANE = "TABLE_RECORD_PANE";

	//////////////////////////////////
	// Properties getters and setters.

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
		Object value = getProperty(node, PROPERTY_STAGE).get();
		checkType(value, PROPERTY_STAGE, Stage.class);
		return (value == null ? null : (Stage) value);
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
	 * Return the table record pane with the default empty id.
	 * 
	 * @param node The node.
	 * @return The action property.
	 */
	public static TableRecordPane getTableRecordPane(Node node) {
		return getTableRecordPane("", node);
	}

	/**
	 * Return the table record pane with the given id.
	 * 
	 * @param id Pane id.
	 * @param node The node.
	 * @return The action property.
	 */
	public static TableRecordPane getTableRecordPane(String id, Node node) {
		return (TableRecordPane) getObject(node, PROPERTY_TABLE_RECORD_PANE + "-" + id);
	}

	/**
	 * Set the action property.
	 * 
	 * @param node The node.
	 * @param action The action.
	 */
	public static void setTableRecordPane(Node node, TableRecordPane tableRecordPane) {
		setObject(node, PROPERTY_TABLE_RECORD_PANE + "-" + tableRecordPane.getId(), tableRecordPane);
	}

	/**
	 * Return a boolean property for the given key, with false default value.
	 * 
	 * @param node The node.
	 * @param key The key.
	 * @return A boolean.
	 */
	public static boolean getBoolean(Node node, Object key) {
		Object value = getProperty(node, key).get();
		checkType(value, key, Boolean.class);
		return (value == null ? false : (Boolean) value);
	}

	/**
	 * Set the boolean property.
	 * 
	 * @param node The node.
	 * @param key The key.
	 * @param value A boolean.
	 */
	public static void setBoolean(Node node, Object key, boolean value) {
		getProperty(node, key).set(value);
	}

	/**
	 * Return a string property for the given key and an empty string default value.
	 * 
	 * @param node The node.
	 * @param key The key.
	 * @return The string property.
	 */
	public static String getString(Node node, Object key) {
		Object value = getProperty(node, key).get();
		checkType(value, key, String.class);
		return (value == null ? "" : (String) value);
	}

	/**
	 * Set the string property to the node.
	 * 
	 * @param node The node.
	 * @param key The key.
	 * @param value The value.
	 */
	public static void setString(Node node, Object key, String value) {
		getProperty(node, key).set(value);
	}

	/**
	 * Return the object property.
	 * 
	 * @param node The node.
	 * @param key The key.
	 * @return The object property.
	 */
	public static Object getObject(Node node, Object key) {
		return getProperty(node, key).get();
	}

	/**
	 * Set the object property to the node.
	 * 
	 * @param node The node.
	 * @param key The key.
	 * @param value The value.
	 */
	public static void setObject(Node node, Object key, Object value) {
		getProperty(node, key).set(value);
	}

	/**
	 * Return the property with the given key. If the property has not been set, a new empty property with the given key
	 * is set.
	 * 
	 * @param node The node.
	 * @param key The key.
	 * @return The property.
	 */
	public static SimpleObjectProperty<Object> getProperty(Node node, Object key) {
		SimpleObjectProperty<Object> property = getPropertyMap(node).get(key);
		if (property == null) {
			property = new SimpleObjectProperty<>(node, key.toString());
			getPropertyMap(node).put(key, property);
		}
		return property;
	}

	/**
	 * Return a property map set in the user data of the node. If the node has a user data of a non
	 * <em>SimpleMapProperty</em> class, an illegal argument exception is thrown.
	 * <p>
	 * The usage of an observable map with observable object properties allows adding listeners either to the map or the
	 * properties.
	 * 
	 * @param node The node.
	 * @return The property map.
	 */
	@SuppressWarnings("unchecked")
	public static SimpleMapProperty<Object, SimpleObjectProperty<Object>> getPropertyMap(Node node) {
		Object userData = node.getUserData();
		if (userData != null && !(userData instanceof SimpleMapProperty)) {
			throw new IllegalArgumentException("Node has user data of type: " + userData.getClass());
		}
		SimpleMapProperty<Object, SimpleObjectProperty<Object>> propertyMap;
		if (userData instanceof SimpleMapProperty) {
			propertyMap = (SimpleMapProperty<Object, SimpleObjectProperty<Object>>) userData;
		} else {
			propertyMap = new SimpleMapProperty<>(node, "PROPERTIES", FXCollections.observableHashMap());
			node.setUserData(propertyMap);
		}
		return propertyMap;
	}

	/**
	 * Check the proper type of the value for the key.
	 * 
	 * @param value The value.
	 * @param key The key.
	 * @param clazz The required type.
	 */
	@SuppressWarnings("rawtypes")
	private static void checkType(Object value, Object key, Class clazz) {
		if (value != null && !clazz.isInstance(value)) {
			throw new IllegalStateException("Invalid type for key: " + key);
		}
	}

	///////////////
	// Node access.

	/**
	 * Return the node with the given id from the list.
	 * 
	 * @param nodes The list of nodes.
	 * @param id The id.
	 * @return The node or null.
	 */
	public static Node getNode(ObservableList<Node> nodes, String id) {
		for (Node node : nodes) {
			if (node.getId() != null && node.getId().equals(id)) {
				return node;
			}
		}
		return null;
	}

	/**
	 * Lookup for the node with the give id exploring the scene graph.
	 * 
	 * @param id The required id.
	 * @param node The start node.
	 * @return The required node or null.
	 */
	public static Node lookup(String id, Node node) {
		return lookup(id, node.getScene());
	}

	/**
	 * Lookup for the node with the give id exploring the scene graph.
	 * 
	 * @param id The required id.
	 * @param scene The scene.
	 * @return The required node or null.
	 */
	public static Node lookup(String id, Scene scene) {
		if (!id.startsWith("#")) {
			id = "#" + id;
		}
		return scene.lookup(id);
	}

	/**
	 * Fill the source list of nodes with nodes starting at the argument node, included.
	 * 
	 * @param startNode The starting node.
	 * @param nodes The list to fill.
	 */
	public void fillNodesFrom(Node startNode, List<Node> nodes) {
		nodes.add(startNode);
		if (startNode instanceof Parent) {
			Parent parent = (Parent) startNode;
			List<Node> children = parent.getChildrenUnmodifiable();
			children.forEach(child -> fillNodesFrom(child, nodes));
		}
	}

	/**
	 * Return the list of field controls contained within the starting node.
	 * 
	 * @param startNode The starting node.
	 * @return The list of field controls contained within the starting node.
	 */
	public List<FieldControl> getFieldControls(Node startNode) {
		List<Node> nodes = new ArrayList<>();
		fillNodesFrom(startNode, nodes);
		List<FieldControl> controls = new ArrayList<>();
		nodes.forEach(node -> {
			if (node instanceof FieldControl) {
				controls.add((FieldControl) node);
			}
		});
		return controls;
	}
}
