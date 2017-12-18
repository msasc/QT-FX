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

import com.qtfx.lib.mkt.data.Data;
import com.qtfx.lib.mkt.data.Instrument;
import com.qtfx.lib.mkt.data.OfferSide;
import com.qtfx.lib.mkt.data.Period;

/**
 * Listener to data (bar) feed.
 *
 * @author Miquel Sas
 */
public interface DataListener {
	/**
	 * Returns the instrument this listener wishes to listen to bars.
	 * 
	 * @return The instrument.
	 */
	Instrument getInstrument();

	/**
	 * Return the period the listener wishes to listen to.
	 * 
	 * @return The period.
	 */
	Period getPeriod();

	/**
	 * Return the offer side the listener wishes to listen to.
	 * 
	 * @return The offer side.
	 */
	OfferSide getOfferSide();

	/**
	 * Called when the current data (bar) of the instrument, period and offer side starts.
	 * 
	 * @param data The data.
	 */
	void dataStart(Data data);
	
	/**
	 * Called during the current data (bar) of the instrument, period and offer side to update values.
	 * 
	 * @param data The data.
	 */
	void dataUpdate(Data data);
	
	/**
	 * Called when the current data (bar) of the instrument, period and offer side ends and is completed.
	 * 
	 * @param data The data.
	 */
	void dataEnd(Data data);
}
