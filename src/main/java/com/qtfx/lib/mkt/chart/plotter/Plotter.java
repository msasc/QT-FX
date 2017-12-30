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

package com.qtfx.lib.mkt.chart.plotter;

/**
 * Base abstract class of all plotter subclasses. Note that the frame for the plot data must have been calculated by a
 * call to <i>PlotData.calculateFrame</i> prior to any plot operation, except for those repaints in a small clip bounds
 * that do not modify the frame maximum and minimum.
 * <p>
 * This base plotter primarily offers the context that has methods to calculate coordinates from values and values from
 * coordinates.
 * 
 * @author Miquel Sas
 */
public abstract class Plotter {

	/** Plotter context. */
	private PlotterContext context;

	/**
	 * Constructor.
	 */
	public Plotter() {
	}

	/**
	 * Return the plotter context.
	 * 
	 * @return The plotter context.
	 */
	public PlotterContext getContext() {
		return context;
	}

	/**
	 * Set the plotter context.
	 * 
	 * @param context The plotter context.
	 */
	public void setContext(PlotterContext context) {
		this.context = context;
	}

}
