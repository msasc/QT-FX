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

import com.qtfx.lib.db.FieldGroup;
import com.qtfx.lib.util.Numbers;

import javafx.beans.property.SimpleMapProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Control;
import javafx.scene.control.TabPane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.stage.Screen;
import javafx.stage.Window;

/**
 * JavaFX utility functions, including:
 * <ul>
 * <li>Setting additional properties to any node in the user data.</li>
 * <li>Looking up for nodes and field controls.</li>
 * <li>Calculating text and components sizes.</li>
 * </ul>
 * 
 * Nodes in this JavaFX development tools are sometimes expected to have properties in the user data. This class help
 * manage those properties.
 *
 * @author Miquel Sas
 */
public class FX {

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
	/** Property TABLE_RECORD_PANE. */
	private static final String PROPERTY_TABLE_RECORD_PANE = "TABLE_RECORD_PANE";
	/** Property FORM_RECORD_PANE. */
	private static final String PROPERTY_FORM_RECORD_PANE = "FORM_RECORD_PANE";
	/** Property FIELD_CONTROL. */
	private static final String PROPERTY_FIELD_CONTROL = "FIELD_CONTROL";
	/** Property FIELD_GROUP (group of field of the same group). */
	private static final String PROPERTY_FIELD_GROUP = "FIELD_GROUP";
	/** Property CONSOLE. */
	private static final String PROPERTY_CONSOLE = "CONSOLE";

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
	 * Return the button group property.
	 * 
	 * @param node The node.
	 * @return The button group.
	 */
	public static String getButtonGroup(Node node) {
		return getString(node, PROPERTY_GROUP);
	}

	/**
	 * Set the button group property.
	 * 
	 * @param node The node.
	 * @param group The button group.
	 */
	public static void setButtonGroup(Node node, String group) {
		setString(node, PROPERTY_GROUP, group);
	}

	/**
	 * Return the order in the group.
	 * 
	 * @param node The node.
	 * @return The order in the group.
	 */
	public static String getButtonOrder(Node node) {
		return getString(node, PROPERTY_ORDER);
	}

	/**
	 * Set the order in the group.
	 * 
	 * @param node The node.
	 * @param order The order in the group.
	 */
	public static void setButtonOrder(Node node, String order) {
		setString(node, PROPERTY_ORDER, order);
	}
	
	/**
	 * Return the table record pane with the default empty id.
	 * 
	 * @param node The node.
	 * @return The table record pane.
	 */
	public static TableRecordPane getTableRecordPane(Node node) {
		return getTableRecordPane("", node);
	}

	/**
	 * Return the table record pane with the given id.
	 * 
	 * @param id Pane id.
	 * @param node The node.
	 * @return The table record pane.
	 */
	public static TableRecordPane getTableRecordPane(String id, Node node) {
		return (TableRecordPane) getObject(node, PROPERTY_TABLE_RECORD_PANE + "-" + id);
	}

	/**
	 * Set the action property.
	 * 
	 * @param node The node.
	 * @param tableRecordPane The table record pane.
	 */
	public static void setTableRecordPane(Node node, TableRecordPane tableRecordPane) {
		setObject(node, PROPERTY_TABLE_RECORD_PANE + "-" + tableRecordPane.getId(), tableRecordPane);
	}

	/**
	 * Return the form record pane with the default empty id.
	 * 
	 * @param node The node.
	 * @return The form record pane.
	 */
	public static FormRecordPane getFormRecordPane(Node node) {
		return getFormRecordPane("", node);
	}

	/**
	 * Return the form record pane with the given id.
	 * 
	 * @param id Pane id.
	 * @param node The node.
	 * @return The form record pane.
	 */
	public static FormRecordPane getFormRecordPane(String id, Node node) {
		return (FormRecordPane) getObject(node, PROPERTY_FORM_RECORD_PANE + "-" + id);
	}

	/**
	 * Set the action property.
	 * 
	 * @param node The node.
	 * @param formRecordPane The form record pane.
	 */
	public static void setFormRecordPane(Node node, FormRecordPane formRecordPane) {
		setObject(node, PROPERTY_FORM_RECORD_PANE + "-" + formRecordPane.getId(), formRecordPane);
	}

	/**
	 * Return the field control.
	 * 
	 * @param node The node.
	 * @return The field control.
	 */
	public static FieldControl getFieldControl(Node node) {
		return (FieldControl) getObject(node, PROPERTY_FIELD_CONTROL);
	}

	/**
	 * Set the field control.
	 * 
	 * @param node The node.
	 * @param fieldControl The field control.
	 */
	public static void setFieldControl(Node node, FieldControl fieldControl) {
		setObject(node, PROPERTY_FIELD_CONTROL, fieldControl);
	}

	/**
	 * Return the field group.
	 * 
	 * @param node The node.
	 * @return The field group.
	 */
	public static FieldGroup getFieldGroup(Node node) {
		return (FieldGroup) getObject(node, PROPERTY_FIELD_GROUP);
	}

	/**
	 * Set the field group.
	 * 
	 * @param node The node.
	 * @param fieldGroup The field group.
	 */
	public static void setFieldGroup(Node node, FieldGroup fieldGroup) {
		setObject(node, PROPERTY_FIELD_GROUP, fieldGroup);
	}

	/**
	 * Return the console.
	 * 
	 * @param node The node.
	 * @return The console.
	 */
	public static Console getConsole(Node node) {
		return (Console) getObject(node, PROPERTY_CONSOLE);
	}

	/**
	 * Set the console.
	 * 
	 * @param node The node.
	 * @param console The console.
	 */
	public static void setConsole(Node node, Console console) {
		setObject(node, PROPERTY_CONSOLE, console);
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
	 * Return an object stored at the root.
	 * 
	 * @param node The reference node.
	 * @param key The key.
	 * @return The object stored at the root.
	 */
	public static Object getRootObject(Node node, Object key) {
		Scene scene = node.getScene();
		Node root = scene.getRoot();
		return getProperty(root, key).get();
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

	//////////////////////////
	// Node access and lookup.

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
	 * Fill the source list of nodes with nodes starting at the argument node, included.
	 * 
	 * @param startNode The starting node.
	 * @param nodes The list to fill.
	 */
	public static void fillNodesFrom(Node startNode, List<Node> nodes) {
		nodes.add(startNode);
		if (startNode instanceof Parent) {
			Parent parent = (Parent) startNode;
			if (parent instanceof TabPane) {
				((TabPane) parent).getTabs().forEach(tab -> fillNodesFrom(tab.getContent(), nodes));
			} else {
				parent.getChildrenUnmodifiable().forEach(child -> fillNodesFrom(child, nodes));
			}
		}
	}

	/**
	 * Return the list of controls contained within the starting node.
	 * 
	 * @param startNode The starting node.
	 * @return The list of controls contained within the starting node.
	 */
	public static List<Control> getControls(Node startNode) {
		List<Node> nodes = new ArrayList<>();
		fillNodesFrom(startNode, nodes);
		List<Control> controls = new ArrayList<>();
		for (Node node : nodes) {
			if (node instanceof Control) {
				controls.add((Control) node);
			}
		}
		return controls;
	}

	/**
	 * Return the control with the given field alias from the list.
	 * 
	 * @param alias The field alias.
	 * @param controls The list of source controls.
	 * @return The control with the field alias or null.
	 */
	public static FieldControl getFieldControl(String alias, List<Control> controls) {
		for (Control control : controls) {
			FieldControl fieldControl = getFieldControl(control);
			if (fieldControl.getField().getAlias().equals(alias)) {
				return fieldControl;
			}
		}
		return null;
	}

	/**
	 * Return the list of field controls contained within the starting node.
	 * 
	 * @param startNode The starting node.
	 * @return The list of field controls contained within the starting node.
	 */
	public static List<FieldControl> getFieldControls(Node startNode) {
		List<FieldControl> fieldControls = new ArrayList<>();
		List<Control> controls = getControls(startNode);
		for (Control control : controls) {
			FieldControl fieldControl = getFieldControl(control);
			if (fieldControl != null) {
				fieldControls.add(fieldControl);
			}
		}
		return fieldControls;
	}

	///////////////////
	// Text properties.

	/**
	 * String to determine the logical height of a font.
	 */
	private static String stringForHeight = null;
	static {
		StringBuilder b = new StringBuilder();
		for (char c = 32; c < 128; c++) {
			b.append(c);
		}
		stringForHeight = b.toString();
	}

	/**
	 * Return the string width.
	 * 
	 * @param string The string.
	 * @param font Optional font.
	 * @return The width.
	 */
	public static double getStringWidth(String string, Font font) {
		return getStringBounds(string, font).getWidth();
	}

	/**
	 * Return the string height for the font.
	 * 
	 * @param font The font
	 * @return The height.
	 */
	public static double getStringHeight(Font font) {
		return getStringBounds(stringForHeight, font).getHeight();
	}

	/**
	 * Return the average capital letter width.
	 * 
	 * @param font The font.
	 */
	public static double getAverageLetterWidth(Font font) {
		return getAverageCharWidth("ABCDEFGHIJKLMNOPQRSTUVWXYZ", font);
	}

	/**
	 * Return the average digit width.
	 * 
	 * @param font The font.
	 */
	public static double getAverageDigitWidth(Font font) {
		return getAverageCharWidth("0123456789", font);
	}

	/**
	 * Return the average char width of a string.
	 * 
	 * @param string The string.
	 * @param font The font.
	 * @return The average character width.
	 */
	public static double getAverageCharWidth(String string, Font font) {
		double width = getStringWidth(string, font);
		double length = string.length();
		return width / length;
	}

	/**
	 * Return the logical string bounds for text sizes calculations.
	 * 
	 * @param string The string.
	 * @param font The optional font.
	 * @return The bounds.
	 */
	public static Bounds getStringBounds(String string, Font font) {
		Text text = new Text(string);
		if (font != null) {
			text.setFont(font);
		}
		text.setBoundsType(TextBoundsType.LOGICAL_VERTICAL_CENTER);
		return text.getLayoutBounds();
	}

	//////////////////////////////
	// Graphics context utilities.

	/**
	 * Move to.
	 * 
	 * @param gc The graphics context.
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 */
	public static void moveTo(GraphicsContext gc, double x, double y) {
		double lw = gc.getLineWidth();
		gc.moveTo(coord(lw, x), coord(lw, y));
	}

	/**
	 * Line to.
	 * 
	 * @param gc The graphics context.
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 */
	public static void lineTo(GraphicsContext gc, double x, double y) {
		double lw = gc.getLineWidth();
		gc.lineTo(coord(lw, x), coord(lw, y));
	}

	/**
	 * Stroke a line.
	 * 
	 * @param gc The graphics context.
	 * @param x1 The x1 coordinate.
	 * @param y1 The y1 coordinate.
	 * @param x2 The x2 coordinate.
	 * @param y2 The y2 coordinate.
	 */
	public static void strokeLine(GraphicsContext gc, double x1, double y1, double x2, double y2) {
		double lw = gc.getLineWidth();
		gc.strokeLine(coord(lw, x1), coord(lw, y1), coord(lw, x2), coord(lw, y2));
	}

	/**
	 * Stroke a rectangle.
	 * 
	 * @param gc The graphics context.
	 * @param x Coordinate x.
	 * @param y Coordinate y.
	 * @param w Width.
	 * @param h Height.
	 */
	public static void strokeRect(GraphicsContext gc, double x, double y, double w, double h) {
		double lw = gc.getLineWidth();
		gc.strokeRect(coord(lw, x), coord(lw, y), coord(lw, w), coord(lw, h));
	}

	/**
	 * Fill a rectangle.
	 * 
	 * @param gc The graphics context.
	 * @param x Coordinate x.
	 * @param y Coordinate y.
	 * @param w Width.
	 * @param h Height.
	 */
	public static void fillRect(GraphicsContext gc, double x, double y, double w, double h) {
		double lw = gc.getLineWidth();
		gc.fillRect(coord(lw, x), coord(lw, y), coord(lw, w), coord(lw, h));
	}

	/**
	 * Fill text.
	 * 
	 * @param gc The graphics context.
	 * @param text The text.
	 * @param x Coordinate x.
	 * @param y Coordinate y.
	 */
	public static void fillText(GraphicsContext gc, String text, double x, double y) {
		double lw = gc.getLineWidth();
		gc.fillText(text, coord(lw, x), coord(lw, y));
	}

	/**
	 * Return the line with proper parameters.
	 * @param line
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	public static Line getLine(Line line, double x1, double y1, double x2, double y2) {
		double lw = line.getStrokeWidth();
		line.setStartX(coord(lw, x1));
		line.setStartY(coord(lw, y1));
		line.setEndX(coord(lw, x2));
		line.setEndY(coord(lw, y2));
		return line;
	}

	/**
	 * Returns the proper coordinate to correctly stroke.
	 * 
	 * @param w The stroke width.
	 * @param d The coordinate value.
	 * @return The proper coordinate.
	 */
	public static double coord(double w, double d) {
		if (!Numbers.isEven(w)) {
			return Numbers.round(d, 0) + 0.5;
		}
		return Numbers.round(d, 0);
	}
	
	///////////////////////////////
	// Screen and window utilities.
	
	public static void factorScreenWidth(Window window, double widthFactor) {
		Rectangle2D r = Screen.getPrimary().getVisualBounds();
		window.setWidth(r.getWidth() * widthFactor);
	}
	
	public static void factorScreenHeight(Window window, double heightFactor) {
		Rectangle2D r = Screen.getPrimary().getVisualBounds();
		window.setHeight(r.getHeight() * heightFactor);
	}
}
