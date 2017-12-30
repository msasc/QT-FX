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

package com.qtfx.lib.mkt.chart.plotter.data;

import com.qtfx.lib.gui.FX;
import com.qtfx.lib.mkt.chart.plotter.PlotterContext;
import com.qtfx.lib.mkt.data.Data;
import com.qtfx.lib.mkt.data.DataList;

import javafx.scene.canvas.GraphicsContext;

/**
 * Plotter of bars.
 *
 * @author Miquel Sas
 */
public class BarPlotter extends DataPlotter {

	/** Line width. */
	private double lineWidth = 1.0;

	/**
	 * Constructor.
	 */
	public BarPlotter() {
		super();
		setIndexes(new int[] { 0, 1, 2, 3 });
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void plot(GraphicsContext gc, DataList dataList, int startIndex, int endIndex) {
		gc.save();
		for (int index = startIndex; index <= endIndex; index++) {
			if (index >= 0 && index < dataList.size()) {
				plot(gc, dataList, index);
			}
		}
		gc.restore();
	}

	/**
	 * Plot the index.
	 * 
	 * @param gc Graphics context.
	 * @param dataList Data list.
	 * @param index Index.
	 */
	private void plot(GraphicsContext gc, DataList dataList, int index) {

		// Data.
		Data data = dataList.get(index);
		double open = Data.getOpen(data);
		double high = Data.getHigh(data);
		double low = Data.getLow(data);
		double close = Data.getClose(data);

		// Context.
		PlotterContext context = getContext();

		// The X coordinate to start painting.
		double x = context.getCoordinateX(index);

		// And the Y coordinate for each value.
		double openY = context.getCoordinateY(open);
		double highY = context.getCoordinateY(high);
		double lowY = context.getCoordinateY(low);
		double closeY = context.getCoordinateY(close);

		// The X coordinate of the vertical line, either the candle.
		double barWidth = context.getDataWidth();
		double verticalLineX = context.getCenterCoordinateX(x);

		// Do plot.
		gc.setLineWidth(lineWidth);
		gc.beginPath();

		// The vertical bar line.
		FX.moveTo(gc, verticalLineX, highY);
		FX.lineTo(gc, verticalLineX, lowY);
		// Open and close horizontal lines if the bar width is greater than 1.
		if (barWidth > 1) {
			// Open horizontal line.
			FX.moveTo(gc, x, openY);
			FX.lineTo(gc, verticalLineX - lineWidth, openY);
			// Close horizontal line
			FX.moveTo(gc, verticalLineX + lineWidth, closeY);
			FX.lineTo(gc, x + barWidth - lineWidth, closeY);
		}
		gc.closePath();
		gc.fill();
		gc.stroke();
	}
}
