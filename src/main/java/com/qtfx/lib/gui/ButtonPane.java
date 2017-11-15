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

package com.qtfx.lib.gui;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.qtfx.lib.util.Numbers;
import com.qtfx.lib.util.Strings;

import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * A flow pane to layout buttons.
 * 
 * @author Miquel Sas
 */
public class ButtonPane extends FlowPane {

	/**
	 * Button comparator by order.
	 */
	static class ButtonComparator implements Comparator<Button> {
		@Override
		public int compare(Button o1, Button o2) {
			return Buttons.getPropertyOrder(o1).compareTo(Buttons.getPropertyOrder(o2));
		}
	}

	/** List of buttons. */
	private ObservableList<Button> buttons = FXCollections.observableArrayList();
	/** A boolean that indicates if the pane has been laid out for the first time. */
	private boolean armed = false;
	/** HBox or VBox insets. */
	private Insets insets;
	/** HBox or VBox spacing. */
	private double spacing;

	/**
	 * Constructor.
	 */
	public ButtonPane() {
		this(Orientation.HORIZONTAL);
	}

	/**
	 * Constructor indicating the orientation, either HORIZONTAL or VERTICAL.
	 * 
	 * @param orientation The orientation, either HORIZONTAL or VERTICAL.
	 */
	public ButtonPane(Orientation orientation) {
		super(orientation);
		if (orientation.equals(Orientation.HORIZONTAL)) {
			setAlignment(Pos.CENTER_RIGHT);
		} else {
			setAlignment(Pos.TOP_CENTER);
		}
	}

	/**
	 * Return the list of buttons.
	 * 
	 * @return The list of buttons.
	 */
	public ObservableList<Button> getButtons() {
		return buttons;
	}

	/**
	 * Layout the buttons.
	 */
	public void layoutButtons() {

		// Ensure that only one button is default and only one is cancel. Take the first of both.
		boolean defaultSet = false;
		boolean cancelSet = false;
		for (Button button : buttons) {
			if (defaultSet && button.isDefaultButton()) {
				button.setDefaultButton(false);
			}
			if (!defaultSet && button.isDefaultButton()) {
				defaultSet = true;
			}
			if (cancelSet && button.isCancelButton()) {
				button.setCancelButton(false);
			}
			if (!cancelSet && button.isCancelButton()) {
				cancelSet = true;
			}
		}
		
		// Set insets and spacing.
		if (countGroups() == 1) {
			insets = new Insets(2, 2, 2, 2);
			spacing = 0;
		} else {
			spacing = 3;
			if (getOrientation().equals(Orientation.HORIZONTAL)) {
				insets = new Insets(2, 6, 2, 6);
			} else {
				insets = new Insets(6, 2, 6, 2);
			}
		}

		// Clear current children.
		getChildren().clear();
		Map<String, List<Button>> groups = getGroups();
		Iterator<String> keys = groups.keySet().iterator();
		while (keys.hasNext()) {
			getChildren().add(getGroupBox(groups.get(keys.next())));
		}
	
		// If not armed add a listener to layout again if the list changes or is invalidated.
		if (!armed) {
			buttons.addListener((ListChangeListener<? super Button>) e -> {
				layoutButtons();
			});
			buttons.addListener((InvalidationListener) e -> {
				layoutButtons();
			});
			armed = true;
		}
	}

	/**
	 * Return the group node, either an HBox or a VBox.
	 * 
	 * @param group The group of buttons.
	 * @return The group node.
	 */
	private Node getGroupBox(List<Button> group) {
		if (getOrientation().equals(Orientation.HORIZONTAL)) {
			HBox hbox = new HBox(spacing);
			hbox.setPadding(insets);
			for (Button button : group) {
				hbox.getChildren().add(button);
			}
			return hbox;
		} else {
			VBox vbox = new VBox(spacing);
			vbox.setPadding(insets);
			for (Button button : group) {
				button.setMaxWidth(Double.MAX_VALUE);
				vbox.getChildren().add(button);
			}
			return vbox;
		}
	}

	/**
	 * Returns the list of button groups with the proper order among groups and among buttons within the group.
	 * 
	 * @return The list of button groups.
	 */
	private Map<String, List<Button>> getGroups() {
		
		ButtonComparator comparator = new ButtonComparator();
		List<Button> buttons = new ArrayList<>(this.buttons);
		
		// If the number of groups is 1, return one group per option.
		// If the number of orders is 1, set the natural add order.
		if (countGroups() == 1) {
			if (countOrders() > 1) {
				buttons.sort(comparator);
			}
			int digits = Numbers.getDigits(buttons.size());
			Map<String, List<Button>> groups = new TreeMap<>();
			for (int i = 0; i < buttons.size(); i++) {
				String groupKey = Strings.leftPad(Integer.toString(i), digits, "0");
				List<Button> group = new ArrayList<>();
				group.add(buttons.get(i));
				groups.put(groupKey, group);
			}
			return groups;
		}		
		
		// Fill a sorted map with groups.
		Map<String, List<Button>> groups = new TreeMap<>();
		for (Button button : buttons) {
			List<Button> group = groups.get(Buttons.getPropertyGroup(button));
			if (group == null) {
				group = new ArrayList<>();
				groups.put(Buttons.getPropertyGroup(button), group);
			}
			group.add(button);
		}

		// Sort options within groups.
		Iterator<String> keys = groups.keySet().iterator();
		while (keys.hasNext()) {
			List<Button> group = groups.get(keys.next());
			group.sort(comparator);
		}

		return groups;
	}

	/**
	 * Return the number of different groups.
	 * 
	 * @return The number of different groups.
	 */
	private int countGroups() {
		List<String> groups = new ArrayList<>();
		for (Button button : buttons) {
			String group = Buttons.getPropertyGroup(button);
			if (!groups.contains(group)) {
				groups.add(group);
			}
		}
		return groups.size();
	}

	/**
	 * Return the number of different order.
	 * 
	 * @return The number of different orders.
	 */
	private int countOrders() {
		List<String> orders = new ArrayList<>();
		for (Button button : buttons) {
			String order = Buttons.getPropertyOrder(button);
			if (!orders.contains(order)) {
				orders.add(order);
			}
		}
		return orders.size();
	}
}
