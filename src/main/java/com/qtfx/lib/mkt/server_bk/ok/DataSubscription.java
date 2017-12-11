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
package com.qtfx.lib.mkt.server_bk.ok;

import com.qtfx.lib.mkt.data.Instrument;
import com.qtfx.lib.mkt.data.Period;

/**
 * Defines a subscription to an instrument price data.
 * 
 * @author Miquel Sas
 */
public class DataSubscription {

	/** Instrument. */
	private Instrument instrument;
	/** Period. */
	private Period period;
	/**
	 * Constructor.
	 * 
	 * @param instrument The instrument.
	 * @param period The period.
	 */
	public DataSubscription(Instrument instrument, Period period) {
		super();
		this.instrument = instrument;
		this.period = period;
	}

	/**
	 * Returns the instrument.
	 * 
	 * @return The instrument.
	 */
	public Instrument getInstrument() {
		return instrument;
	}

	/**
	 * Returns the period.
	 * 
	 * @return The period.
	 */
	public Period getPeriod() {
		return period;
	}

	/**
	 * Check if this subscription should accept the argument data.
	 * 
	 * @param instrument The instrument.
	 * @param period The period.
	 * @return A boolean indicating if this subscription should accept the argument data.
	 */
	public boolean acceptsData(Instrument instrument, Period period) {
		return this.instrument.equals(instrument) && this.period.equals(period);
	}
}
