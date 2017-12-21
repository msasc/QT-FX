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

package com.qtfx.lib.gui.action.handlers;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.stage.WindowEvent;

/**
 * Root of FX actions with access to the scene.
 *
 * @author Miquel Sas
 */
public abstract class WindowEventHandler implements EventHandler<WindowEvent> {

	/** Reference node from which we can access the scene tree. */
	private Node node;

	/**
	 * Constructor.
	 * 
	 * @param node The reference node.
	 */
	public WindowEventHandler(Node node) {
		super();
		this.node = node;
	}

	/**
	 * Return the reference node.
	 * 
	 * @return The node.
	 */
	public Node getNode() {
		return node;
	}
}
