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

package com.qtfx.library.task;

import java.util.concurrent.RecursiveAction;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * An observable recursive action that is not forced to be observed from the FX application thread.
 * <p>
 * For the sake of simplicity in the class architecture, only the properties are published, no <code>setOn...</code>
 * methods are provided, enforcing to set listeners directly to the properties.
 * <p>
 * Four string messages are published:
 * <ul>
 * <li><em>title</em>
 * <li><em>message</em>
 * </ul>
 *
 * @author Miquel Sas
 */
public abstract class Task extends RecursiveAction {

	/** State property. */
	private ObjectProperty<State> state = new SimpleObjectProperty<>(State.READY);

	/**
	 * Default constructor.
	 */
	public Task() {
		super();
	}

	/**
	 * Publish the state property.
	 * 
	 * @return The state property.
	 */
	public ReadOnlyObjectProperty<State> stateProperty() {
		return state;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected abstract void compute();

}
