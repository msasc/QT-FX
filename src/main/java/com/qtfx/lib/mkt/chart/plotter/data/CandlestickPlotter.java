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
import javafx.scene.paint.Color;

/**
 * Plotter of candlesticks.
 *
 * @author Miquel Sas
 */
public class CandlestickPlotter extends DataPlotter {

	/** Border color. */
	private Color borderColor = Color.BLACK;
	/** A boolean that indicates whether the border should be painted. */
	private boolean paintBorder = true;
	/** Line width. */
	private double lineWidth = 1.0;

	/**
	 * Constructor.
	 */
	public CandlestickPlotter() {
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
		boolean bullish = Data.isBullish(data);

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
		double candleWidth = context.getDataWidth();
		double verticalLineX = context.getCenterCoordinateX(x);

		// The color to fill.
		Color color = null;
		if (dataList.isOdd(index)) {
			if (bullish) {
				color = getColorBullishOdd();
			} else {
				color = getColorBearishOdd();
			}
		} else {
			if (bullish) {
				color = getColorBullishEven();
			} else {
				color = getColorBearishEven();
			}
		}
		gc.setFill(color);

		// The stroke if applicable.
		if (paintBorder) {
			gc.setStroke(borderColor);
		} else {
			gc.setStroke(color);
		}
		
		// Do plot.
		gc.setLineWidth(lineWidth);
		gc.beginPath();
		if (candleWidth <= 1) {
			// The vertical line only.
			gc.setStroke(color);
			FX.moveTo(gc, verticalLineX, highY);
			FX.lineTo(gc, verticalLineX, lowY);
		} else {
			if (bullish) {
				// Upper shadow.
				FX.moveTo(gc, verticalLineX, highY);
				FX.lineTo(gc, verticalLineX, closeY - lineWidth);
				// Body.
				FX.moveTo(gc, x, closeY);
				FX.lineTo(gc, x + candleWidth - lineWidth, closeY);
				FX.lineTo(gc, x + candleWidth - lineWidth, openY);
				FX.lineTo(gc, x, openY);
				FX.lineTo(gc, x, closeY);
				// Lower shadow.
				FX.moveTo(gc, verticalLineX, openY + lineWidth);
				FX.lineTo(gc, verticalLineX, lowY);
			} else {
				// Upper shadow.
				FX.moveTo(gc, verticalLineX, highY);
				FX.lineTo(gc, verticalLineX, openY - lineWidth);
				// Body.
				FX.moveTo(gc, x, openY);
				FX.lineTo(gc, x + candleWidth - lineWidth, openY);
				FX.lineTo(gc, x + candleWidth - lineWidth, closeY);
				FX.lineTo(gc, x, closeY);
				FX.lineTo(gc, x, openY);
				// Lower shadow.
				FX.moveTo(gc, verticalLineX, closeY + lineWidth);
				FX.lineTo(gc, verticalLineX, lowY);
			}
		}
		gc.closePath();
		gc.fill();
		gc.stroke();
	}
}
