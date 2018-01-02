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

package com.qtfx.lib.mkt.chart;

import com.qtfx.lib.mkt.chart.plotter.PlotterContext;
import com.qtfx.lib.mkt.chart.plotter.data.DataPlotter;
import com.qtfx.lib.mkt.data.DataList;
import com.qtfx.lib.mkt.data.PlotData;

import javafx.beans.binding.Bindings;
import javafx.geometry.Dimension2D;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;

/**
 * The chart panel that effectively plots charts. The types of charts are <i>line</i>, <i>bar</i>, <i>candlestick</i>
 * and <i>histogram</i>.
 * 
 * @author Miquel Sas
 */
public class ChartPlotter {

	/**
	 * The frame or plot insets, as a top, right, bottom and left factor of the available plot area. If, for example,
	 * the available width is 1400 pixels, a left inset of 0.02 will leave 28 pixels free of any paint to the left.
	 */
	private static double[] chartInsets = new double[] { 0.02, 0.02, 0.01, 0.01 };
	/** Container. */
	private ChartContainer container;
	/** Effective JavaFX pane. */
	private Pane pane;
	/** Plot canvas. */
	private Canvas canvas;

	/**
	 * Constructor.
	 * 
	 * @param chartContainer The chart container.
	 */
	public ChartPlotter(ChartContainer container) {
		super();
		this.container = container;

		pane = new Pane();
		canvas = new Canvas();
		pane.getChildren().add(canvas);
		canvas.widthProperty().bind(Bindings.selectDouble(canvas.parentProperty(), "width"));
		canvas.heightProperty().bind(Bindings.selectDouble(canvas.parentProperty(), "height"));
	}

	/**
	 * Return the effective JavaFX pane.
	 * 
	 * @return The pane.
	 */
	public Pane getPane() {
		return pane;
	}

	/**
	 * Return the chart container.
	 * 
	 * @return The chart container.
	 */
	public ChartContainer getContainer() {
		return container;
	}

	/**
	 * Returns the plot insets calculated with the plot factors.
	 * 
	 * @param size The chart size.
	 * @return The plot insets.
	 */
	public Insets getPlotInsets() {
		double areaWidth = getSize().getWidth();
		double areaHeight = getSize().getHeight();
		double insetTop = areaHeight * chartInsets[0];
		double insetRight = areaWidth * chartInsets[1];
		double insetBottom = areaHeight * chartInsets[2];
		double insetLeft = areaWidth * chartInsets[3];
		return new Insets(insetTop, insetRight, insetBottom, insetLeft);
	}

	/**
	 * Return the size of the chart pane.
	 * 
	 * @return The size.
	 */
	public Dimension2D getSize() {
		return new Dimension2D(pane.getWidth(), pane.getHeight());
	}

	/**
	 * Do the plot.
	 */
	public void plot() {

		Dimension2D size = getSize();
		GraphicsContext gc = canvas.getGraphicsContext2D();

		PlotterContext context = getContext();
		PlotData plotData = container.getPlotData();
		plotData.getDataLists().forEach(dataList -> dataList.setPlotterContext(context));

		gc.clearRect(0, 0, size.getWidth(), size.getHeight());
		int startIndex = plotData.getStartIndex();
		int endIndex = plotData.getEndIndex();
		for (DataList dataList : plotData.getDataLists()) {
			if (dataList.isPlot()) {
				for (DataPlotter plotter : dataList.getDataPlotters()) {
					plotter.plot(gc, dataList, startIndex, endIndex);
				}
			}
		}
	}

	/**
	 * Return a suitable context.
	 * 
	 * @return The context.
	 */
	public PlotterContext getContext() {
		Dimension2D size = getSize();
		Insets insets = getPlotInsets();
		PlotData plotData = container.getPlotData();
		return new PlotterContext(size, insets, plotData);
	}
}
