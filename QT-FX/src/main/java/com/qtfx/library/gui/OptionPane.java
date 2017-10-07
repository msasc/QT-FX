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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleListProperty;
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
 * A flow pane to layout options, e.g. buttons.
 * 
 * @author Miquel Sas
 */
public class OptionPane extends FlowPane {

	/**
	 * Options comparator.
	 */
	class OptionComparator implements Comparator<Option> {
		@Override
		public int compare(Option o1, Option o2) {
			return o1.getOrder().compareTo(o2.getOrder());
		}
	}

	/** List of options. */
	private ObservableList<Option> options = new SimpleListProperty<>();
	/** A boolean that indicates if the pane has been laid out for the first time. */
	private boolean armed = false;

	/**
	 * Constructor.
	 */
	public OptionPane() {
		this(Orientation.HORIZONTAL);
	}

	/**
	 * Constructor indicating the orientation, either HORIZONTAL or VERTICAL.
	 * 
	 * @param orientation The orientation, either HORIZONTAL or VERTICAL.
	 */
	public OptionPane(Orientation orientation) {
		super(orientation);
		if (orientation.equals(Orientation.HORIZONTAL)) {
			setAlignment(Pos.CENTER_RIGHT);
		} else {
			setAlignment(Pos.TOP_CENTER);
		}
	}

	/**
	 * Return the list of options.
	 * 
	 * @return The list of options.
	 */
	public ObservableList<Option> getOptions() {
		return options;
	}

	/**
	 * Layout the options.
	 */
	public void layoutOptions() {

		// Ensure that only one option is default and only one is cancel. Change it in the button to not modify the
		// original option.
		boolean defaultSet = false;
		boolean cancelSet = false;
		for (Option option : options) {
			Button button = option.getButton();
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

		// Clear current children.
		getChildren().clear();

		// Get the groups of options and lay them out.
		Map<String, List<Option>> groups = getGroups();
		Iterator<String> keys = groups.keySet().iterator();
		while (keys.hasNext()) {
			getChildren().add(getGroupBox(groups.get(keys.next())));
		}

		// If not armed add a listener to layout again if the list changes or is invalidated.
		if (!armed) {
			options.addListener((ListChangeListener<? super Option>) e -> {
				layoutOptions();
			});
			options.addListener((InvalidationListener) e -> {
				layoutOptions();
			});
			armed = true;
		}
	}

	/**
	 * Return the group node, either an HBox or a VBox.
	 * 
	 * @param group The group of options.
	 * @return The group node.
	 */
	private Node getGroupBox(List<Option> group) {
		if (getOrientation().equals(Orientation.HORIZONTAL)) {
			HBox hbox = new HBox(3);
			hbox.setPadding(new Insets(2, 6, 2, 6));
			for (Option option : group) {
				hbox.getChildren().add(option.getButton());
			}
			return hbox;
		} else {
			VBox vbox = new VBox(3);
			vbox.setPadding(new Insets(2, 6, 2, 6));
			for (Option option : group) {
				vbox.getChildren().add(option.getButton());
			}
			return vbox;
		}
	}

	/**
	 * Returns the list of option groups with the proper order among groups and among options in the group.
	 * 
	 * @return The list of option groups.
	 */
	private Map<String, List<Option>> getGroups() {

		// Fill a sorted map with groups.
		Map<String, List<Option>> groups = new TreeMap<>();
		for (Option option : options) {
			List<Option> group = groups.get(option.getGroup());
			if (group == null) {
				group = new ArrayList<>();
				groups.put(option.getGroup(), group);
			}
			group.add(option);
		}

		// Sort options within groups.
		OptionComparator comparator = new OptionComparator();
		Iterator<String> keys = groups.keySet().iterator();
		while (keys.hasNext()) {
			List<Option> group = groups.get(keys.next());
			group.sort(comparator);
		}

		return groups;
	}

	/**
	 * Return an array with all current buttons.
	 * 
	 * @return An array with all current buttons.
	 */
	public Button[] getButtons() {
		Button[] buttons = new Button[options.size()];
		for (int i = 0; i < options.size(); i++) {
			buttons[i] = options.get(i).getButton();
		}
		return buttons;
	}
}
