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

import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.Node;

/**
 * An event filter aimed to intercept events to perform any useful action and/or consume the event.
 * 
 * @author Miquel Sas
 */
public interface EventFilter<T extends Event> {
	/**
	 * Return the event type this filter sould apply.
	 * 
	 * @return The event type.
	 */
	EventType<T> type();

	/**
	 * Filter the event, performing an appropriate action or consuming it.
	 * 
	 * @param node The node that gives access to the scene tree.
	 * @param e The event.
	 */
	void filter(Node node, T e);
}
