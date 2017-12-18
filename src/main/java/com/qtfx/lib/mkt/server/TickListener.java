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

package com.qtfx.lib.mkt.server;

import com.qtfx.lib.mkt.data.Instrument;
import com.qtfx.lib.mkt.data.Tick;

/**
 * Listener to ticks feed.
 *
 * @author Miquel Sas
 */
public interface TickListener {
	/**
	 * Returns the instrument this listener wishes to listen ticks.
	 * 
	 * @return The instrument.
	 */
	Instrument getInstrument();

	/**
	 * Called when a new tick arrives.
	 * 
	 * @param tick The tick.
	 */
	void tick(Tick tick);
}
