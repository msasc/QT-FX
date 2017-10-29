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

package com.qtfx.platform.stats;

import com.qtfx.library.mkt.data.Instrument;
import com.qtfx.library.mkt.data.Period;
import com.qtfx.library.mkt.server.Server;

/**
 * Statistics on tickers.
 *
 * @author Miquel Sas
 */
public abstract class TickerStatistics extends Statistics {

	/** The server. */
	private Server server;
	/** The instrument. */
	private Instrument instrument;
	/** The period. */
	private Period period;

	/**
	 * Constructor.
	 */
	public TickerStatistics() {
		super();
	}

	/**
	 * Returns the server.
	 * 
	 * @return The server.
	 */
	public Server getServer() {
		return server;
	}

	/**
	 * Set the server.
	 * 
	 * @param server The server
	 */
	public void setServer(Server server) {
		this.server = server;
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
	 * Set the instrument.
	 * 
	 * @param instrument The instrument.
	 */
	public void setInstrument(Instrument instrument) {
		this.instrument = instrument;
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
	 * Set the period.
	 * 
	 * @param period The period.
	 */
	public void setPeriod(Period period) {
		this.period = period;
	}

}
