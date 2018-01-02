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

import com.qtfx.lib.mkt.data.PlotData;
import com.qtfx.lib.mkt.data.PlotScale;
import com.qtfx.lib.util.Numbers;

import javafx.geometry.Dimension2D;
import javafx.geometry.Insets;

/**
 * Context information about the plot task.
 *
 * @author Miquel Sas
 */
public class PlotterContext {

	/** The plot data. */
	private PlotData plotData;
	/** Calculated chart insets. */
	private Insets chartInsets;
	/** Calculated chart width */
	private double chartWidth;
	/** Calculated chart height. */
	private double chartHeight;
	/** The calculated data item (candlestick or bar) width. */
	private double dataWidth;

	/**
	 * Constructor assigning the context values.
	 * 
	 * @param chartPlotter The parent chart plotter.
	 * @param plotData The plot data.
	 */
	public PlotterContext(Dimension2D size, Insets insets, PlotData plotData) {
		super();
		this.plotData = plotData;

		// Calculate chart insets, width and height.
		chartInsets = insets;
		chartWidth = size.getWidth() - chartInsets.getLeft() - chartInsets.getRight();
		chartHeight = size.getHeight() - chartInsets.getTop() - chartInsets.getBottom();

		// Calculate the available width per data item.
		int startIndex = plotData.getStartIndex();
		int endIndex = plotData.getEndIndex();
		double periods = endIndex - startIndex + 1;

		// Calculate the plot width of a bar. As a general rule, it can be 75% of the available width per bar, as anodd
		// number, and if the result is less than 2, plot just a vertical line of 1 pixel width.
		double widthPerItem = chartWidth / periods;
		if (widthPerItem <= 4) {
			dataWidth = 1;
		} else {
			dataWidth = Numbers.round(widthPerItem * 0.75, 0);
		}
	}

	/**
	 * Return the plot data.
	 * 
	 * @return The plot data.
	 */
	public PlotData getPlotData() {
		return plotData;
	}

	/**
	 * Returns the X coordinate where starts the area to plot the data of index, given the index of the data, the start
	 * index, the end index, the left inset and the plot area width.
	 * 
	 * @param index The data index.
	 * @return The X coordinate.
	 * @throws IllegalStateException If the data index is not between the start and the end indexes.
	 */
	public double getCoordinateX(double index) throws IllegalStateException {
		// Start and end indexes.
		double startIndex = plotData.getStartIndex();
		double endIndex = plotData.getEndIndex();
		// Check the index is in the start-end range.
		if (index < startIndex || index > endIndex) {
			// throw new IllegalStateException();
		}
		// The index factor: relation between index and the difference endIndex - startIndex.
		double indexFactor = (index - startIndex) / (endIndex - startIndex);
		// The relative X coordinate counted from the left of the plot area.
		double relativeX = indexFactor * chartWidth;
		// Final X coordinate counted from the left of the paint area.
		double coordinateX = chartInsets.getLeft() + relativeX;
		return coordinateX;
	}

	/**
	 * Returns the Y coordinate, starting at the top of the paint area, given the value, the maximum and minimum visible
	 * values, the top inset, the plot area height, once removed top and bottom insets, and the plot scale.
	 * 
	 * @param value The value to retrieve its Y coordinate.
	 * @return The Y coordinate for the argument value.
	 * @throws IllegalStateException If the value is not in the maximum-minimum value range.
	 */
	public double getCoordinateY(double value) throws IllegalStateException {

		// Maximum and minimum values.
		double maximumValue = plotData.getMaximumValue();
		double minimumValue = plotData.getMinimumValue();
		// Check that the value to plot is in the maximum-minimum range.
		if (value > maximumValue || value < minimumValue) {
			// throw new IllegalStateException();
		}

		// Apply scale to values if necessary.
		if (plotData.getPlotScale().equals(PlotScale.LOGARITHMIC)) {
			maximumValue = Math.log1p(maximumValue);
			minimumValue = Math.log1p(minimumValue);
			value = Math.log1p(value);
		}

		// The value factor: relation between value and the difference maximumValue - minimumValue.
		double valueFactor = (value - minimumValue) / (maximumValue - minimumValue);
		// The relative Y coordinate (in a linear scale) counted from the bottom of the plot area.
		double relativeY = 0;
		if (Double.isFinite(value) && Double.isFinite(valueFactor)) {
			relativeY = valueFactor * chartHeight;
		}
		// Final Y coordinate counted from the top of the paint area.
		double coordinateY = chartInsets.getTop() + chartHeight - relativeY;
		return coordinateY;
	}

	/**
	 * Returns the index on the data given the x coordinate in the plot area. The returned index is greater than or
	 * equal to <i>PlotData.startIndex</i> and less equal than <i>PlotData.endIndex</i>.
	 * 
	 * @param x The x coordinate in the plot area.
	 * @return The index on the data.
	 */
	public int getDataIndex(double x) {

		// If the x coordinate is less that the left inset, return the start index.
		if (x < chartInsets.getLeft()) {
			return plotData.getStartIndex();
		}

		// If the x coordinate is greater than the available width, return the end index.
		if (x > chartInsets.getLeft() + chartWidth - 1) {
			return plotData.getEndIndex();
		}

		// The x coordinate relative to the plot area width.
		double xRelative = x - chartInsets.getLeft();

		// Start and end index.
		int startIndex = plotData.getStartIndex();
		int endIndex = plotData.getEndIndex();

		// The index.
		double factor = xRelative / Double.valueOf(chartWidth);
		if (!Double.isFinite(factor)) {
			return 0;
		}
		double indexes = endIndex - startIndex + 1;
		double numPeriods = indexes * factor;
		int periods = Double.valueOf(Numbers.round(numPeriods, 0)).intValue();
		int index = startIndex + periods - 1;
		return index;
	}

	/**
	 * Returns the candlestick or bar width.
	 * 
	 * @return The candlestick or bar width.
	 */
	public double getDataWidth() {
		return dataWidth;
	}

	/**
	 * Returns the data value given the y coordinate in the plot area. The returned value is greater than or equal to
	 * <i>PlotData.minimumValue</i> and less equal than <i>PlotData.maximumValue</i>.
	 * 
	 * @param y The y coordinate in the plot area.
	 * @return The data value.
	 */
	public double getDataValue(double y) {

		// The y coordinate relative to the plot area.
		double yRelative = y - chartInsets.getTop();

		// Minimum and maximum values.
		double minimumValue = plotData.getMinimumValue();
		double maximumValue = plotData.getMaximumValue();

		// Apply scale to minimum and maximum values if necessary.
		if (plotData.getPlotScale().equals(PlotScale.LOGARITHMIC)) {
			maximumValue = Math.log(maximumValue);
			minimumValue = Math.log(minimumValue);
		}

		// The value. Note that y is top-down.
		double height = chartHeight;
		double factor = (height - yRelative) / height;
		double value = minimumValue + ((maximumValue - minimumValue) * factor);

		// Apply the inverse scale if necessary.
		if (plotData.getPlotScale().equals(PlotScale.LOGARITHMIC)) {
			value = Math.pow(Math.E, value);
		}

		int tickScale = plotData.getTickScale();
		return Numbers.round(value, tickScale);
	}

	/**
	 * Returns the coordinate of the drawing center for a bar, candle, line or histogram, given the starting X
	 * coordinate.
	 * 
	 * @param x The starting x coordinate.
	 * @return The vertical line X coordinate.
	 */
	public double getCenterCoordinateX(double x) {
		double verticalLineWidth = 1;
		if (dataWidth > 1) {
			x += ((dataWidth - verticalLineWidth) / 2);
		}
		return x;
	}

}
