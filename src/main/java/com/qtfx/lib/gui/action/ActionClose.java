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

import com.qtfx.lib.gui.action.handlers.ActionEventHandler;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Window;

/**
 * An action that closes the window.
 *
 * @author Miquel Sas
 */
public class ActionClose extends ActionEventHandler {

	/**
	 * Constructor.
	 * 
	 * @param node The reference node.
	 */
	public ActionClose(Node node) {
		super(node);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handle(ActionEvent event) {
		Window window = getNode().getScene().getWindow();
		if (window != null) {
			window.hide();
		}
	}
}
