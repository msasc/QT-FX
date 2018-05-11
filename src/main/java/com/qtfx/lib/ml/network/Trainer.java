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

package com.qtfx.lib.ml.network;

import com.qtfx.lib.app.Session;
import com.qtfx.lib.ml.data.PatternSource;
import com.qtfx.lib.task.Task;

/**
 * Trainer task to train a network that can backward errors to movify internal data, e.g. weights.
 *
 * @author Miquel Sas
 */
public class Trainer extends Task {
	
	/** The network. */
	private Network network;
	/** The pattern source. */
	private PatternSource patternSource;
	/** The number of epochs or turns to the full list of patterns. */
	private int epochs = 100;

	/**
	 * @param session
	 */
	public Trainer(Session session) {
		super(session);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void compute() throws Exception {
		// TODO Auto-generated method stub

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isIndeterminate() {
		// TODO Auto-generated method stub
		return false;
	}
}
