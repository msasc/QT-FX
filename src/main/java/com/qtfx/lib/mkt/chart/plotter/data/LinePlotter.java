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
import com.qtfx.lib.util.Numbers;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Line plotter.
 *
 * @author Miquel Sas
 */
public class LinePlotter extends DataPlotter {

	/** Line width. */
	private double lineWidth = 1.0;

	/**
	 * Constructor.
	 */
	public LinePlotter() {
		super();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void plot(GraphicsContext gc, DataList dataList, int startIndex, int endIndex) {
		PlotterContext context = getContext();
		gc.setLineWidth(lineWidth);
		gc.beginPath();

		double lastX = Numbers.MIN_DOUBLE;
		double lastY = Numbers.MIN_DOUBLE;
		Color lastColor = null;
		for (int index = startIndex; index <= endIndex; index++) {
			if (index >= 0 && index < dataList.size()) {
				Data data = dataList.get(index);
				double value = data.getValue(getIndex());
				double x = context.getCenterCoordinateX(context.getCoordinateX(index));
				double y = context.getCoordinateY(value);

				// First.
				if (lastX == Numbers.MIN_DOUBLE && lastY == Numbers.MIN_DOUBLE) {
					FX.moveTo(gc, x, y);
				} else {
					boolean bullish = (y > lastY);
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
					if (lastColor == null) {
						lastColor = color;
					}
					if (color.equals(lastColor)) {
						FX.lineTo(gc, x, y);
					} else {

						gc.setStroke(lastColor);
						gc.stroke();

						gc.beginPath();
						gc.setStroke(color);
						FX.moveTo(gc, lastX, lastY);
						FX.lineTo(gc, x, y);
					}
					lastColor = color;
				}
				lastX = x;
				lastY = y;
			}
		}
		gc.setStroke(lastColor);
		gc.stroke();
	}
}
