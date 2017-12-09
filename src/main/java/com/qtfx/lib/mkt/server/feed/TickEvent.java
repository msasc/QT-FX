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
package com.qtfx.lib.mkt.server.feed;

import java.util.EventObject;

import com.qtfx.lib.mkt.data.Instrument;
import com.qtfx.lib.mkt.data.Tick;

/**
 * Tick event.
 * 
 * @author Miquel Sas
 */
public class TickEvent extends EventObject {

	/**
	 * Instrument.
	 */
	private Instrument instrument;
	/**
	 * Tick data.
	 */
	private Tick tick;

	/**
	 * Constructor assigning fields.
	 * 
	 * @param source The source that generated the event.
	 * @param instrument The instruent.
	 * @param tick The tick data.
	 */
	public TickEvent(Object source, Instrument instrument, Tick tick) {
		super(source);
		this.instrument = instrument;
		this.tick = tick;
	}

	/**
	 * Returns the instruent.
	 * 
	 * @return The instruent.
	 */
	public Instrument getInstrument() {
		return instrument;
	}

	/**
	 * Sets the instruent.
	 * 
	 * @param instrument The instrument.
	 */
	public void setInstrument(Instrument instrument) {
		this.instrument = instrument;
	}

	/**
	 * Returns the tick data.
	 * 
	 * @return The tick data.
	 */
	public Tick getTick() {
		return tick;
	}

	/**
	 * Sets the tick data.
	 * 
	 * @param tick The tick data.
	 */
	public void setTick(Tick tick) {
		this.tick = tick;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append(getSource().getClass().getSimpleName());
		b.append(", ");
		b.append(getInstrument().getDescription());
		b.append(", ");
		b.append(getTick());
		return b.toString();
	}
}
