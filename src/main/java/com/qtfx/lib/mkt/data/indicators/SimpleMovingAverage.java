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
package com.qtfx.lib.mkt.data.indicators;

import java.util.List;

import com.qtfx.lib.mkt.data.Data;
import com.qtfx.lib.mkt.data.DataList;
import com.qtfx.lib.mkt.data.IndicatorSource;
import com.qtfx.lib.mkt.data.info.IndicatorInfo;

/**
 * Simple moving average.
 * 
 * @author Miquel Sas
 */
public class SimpleMovingAverage extends PeriodIndicator {

	/**
	 * A boolean that indicates if calculation should be optimized by removing the last value and adding the next.
	 */
	private boolean optimize = true;

	/**
	 * Constructor.
	 */
	public SimpleMovingAverage() {
		super();

		// Indicator info to be configured.
		IndicatorInfo info = getIndicatorInfo();

		// Name and title.
		info.setName("SMA");
		info.setTitle("Simple moving average");

		// Instrument, period and scales will be setup at start using those of the unique <i>DataInfo</i> used.

		// Setup input information. Uses an unique input source, with one output value, of any data type.
		info.addInput(getDefaultInputInfo());

		// Setup the input parameter and default value: period.
		info.addParameter(getPeriodParameter());
	}

	/**
	 * Returns the optimize flag.
	 * 
	 * @return The optimize flag.
	 */
	public boolean isOptimize() {
		return optimize;
	}

	/**
	 * Set the optimize flag.
	 * 
	 * @param optimize The optimize flag.
	 */
	public void setOptimize(boolean optimize) {
		this.optimize = optimize;
	}

	/**
	 * Calculates the indicator data at the given index, for the list of indicator sources.
	 * <p>
	 * This indicator already calculated data is passed as a parameter because some indicators may need previous
	 * calculated values or use them to improve calculation performance.
	 * 
	 * @param index The data index.
	 * @param indicatorSources The list of indicator sources.
	 * @param indicatorData This indicator already calculated data.
	 * @return The result data.
	 */
	@Override
	public Data calculate(int index, List<IndicatorSource> indicatorSources, DataList indicatorData) {
		if (index < 0) {
			return null;
		}
		return getSMA(this, index, indicatorSources, indicatorData, isOptimize());
	}
}
