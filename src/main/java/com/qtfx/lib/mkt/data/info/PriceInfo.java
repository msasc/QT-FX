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
package com.qtfx.lib.mkt.data.info;

import com.qtfx.lib.mkt.data.Data;
import com.qtfx.lib.mkt.data.Instrument;
import com.qtfx.lib.mkt.data.Period;

/**
 * Data information for prices.
 * 
 * @author Miquel Sas
 */
public class PriceInfo extends DataInfo {

	/**
	 * Constructor assigning instrument and period.
	 * 
	 * @param instrument The instrument.
	 * @param period The period.
	 */
	public PriceInfo(Instrument instrument, Period period) {
		super();
		setInstrument(instrument);
		setName(instrument.getId());
		setDescription(instrument.getDescription());
		setPeriod(period);
		addOutput("Open", "O", Data.OPEN, "Open data value");
		addOutput("High", "H", Data.HIGH, "High data value");
		addOutput("Low", "L", Data.LOW, "Low data value");
		addOutput("Close", "C", Data.CLOSE, "Close data value");
		addOutput("Volume", "V", Data.VOLUME, "Volume data value");
	}
}
