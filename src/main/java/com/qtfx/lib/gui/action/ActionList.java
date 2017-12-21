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

package com.qtfx.lib.gui.action;

import java.util.ArrayList;
import java.util.List;

import com.qtfx.lib.gui.action.handlers.ActionEventHandler;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;

/**
 * An action event handler that chains a list of action event handlers.
 *
 * @author Miquel Sas
 */
public class ActionList extends ActionEventHandler {

	/** List of action event handlers. */
	private List<EventHandler<ActionEvent>> handlers = new ArrayList<>();

	/**
	 * Constructor.
	 * 
	 * @param node The reference node.
	 */
	public ActionList(Node node) {
		super(node);
	}

	/**
	 * Add an action event handler.
	 * 
	 * @param handler The action event handler.
	 */
	public void addHandler(EventHandler<ActionEvent> handler) {
		handlers.add(handler);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handle(ActionEvent event) {
		handlers.forEach(handler -> handler.handle(event));
	}

}
