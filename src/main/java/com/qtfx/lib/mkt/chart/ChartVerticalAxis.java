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

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

import com.qtfx.lib.gui.FX;
import com.qtfx.lib.mkt.chart.plotter.PlotterContext;
import com.qtfx.lib.mkt.data.PlotData;
import com.qtfx.lib.mkt.data.PlotScale;
import com.qtfx.lib.util.Formats;
import com.qtfx.lib.util.Numbers;

import javafx.beans.binding.Bindings;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * A panel that contains the vertical axis in a chart view.
 *
 * @author Miquel Sas
 */
public class ChartVerticalAxis {

	/** Chart container. */
	private ChartContainer container;
	/** Effective pane. */
	private Pane pane;
	/** Plot canvas. */
	private Canvas canvas;

	/** Vertical axis: the line color. */
	private Color lineColor = Color.BLACK;
	/** Vertical axis: the length of the small line before each value. */
	private double lineLength = 5;
	/** The surround cursor value fill color. */
	private Color surroundFillColor = Color.ANTIQUEWHITE;
	/** Vertical axis: the insets to surround the price with a rectangle. */
	private Insets surroundInsets = new Insets(1, 4, 1, 4);
	/** Vertical axis: the vertical text font. */
	private Font textFont = Font.font("System Regular", 12);
	/** Vertical axis: insets of the text. */
	private Insets textInsets = new Insets(5, 8, 5, 8);

	/**
	 * Constructor.
	 * 
	 * @param container The parent container.
	 */
	public ChartVerticalAxis(ChartContainer container) {
		super();
		this.container = container;

		pane = new Pane();
		canvas = new Canvas();
		pane.getChildren().add(canvas);
		canvas.widthProperty().bind(Bindings.selectDouble(canvas.parentProperty(), "width"));
		canvas.heightProperty().bind(Bindings.selectDouble(canvas.parentProperty(), "height"));
	}

	/**
	 * Return the effective pane.
	 * 
	 * @return the pane The pane.
	 */
	public Pane getPane() {
		return pane;
	}

	/**
	 * Sets the maximum, minimum and preferred widths based on the data and the insets.
	 */
	public void setMaximumMinimumAndPreferredWidths() {
		Locale locale = container.getChart().getLocale();
		PlotData plotData = container.getPlotData();
		if (!plotData.areMaximumAndMinimumValuesCalculated()) {
			plotData.calculateFrame();
		}
		double maximumValue = plotData.getMaximumValue();
		double minimumValue = plotData.getMinimumValue();
		int tickScale = plotData.getTickScale();
		String smaximumValue = Formats.formattedFromDouble(maximumValue, tickScale, locale);
		String sminimumValue = Formats.formattedFromDouble(minimumValue, tickScale, locale);
		double widthMax = FX.getStringWidth(smaximumValue, textFont);
		double widthMin = FX.getStringWidth(sminimumValue, textFont);
		double textWidth = Math.max(widthMax, widthMin);
		double width = lineLength + textInsets.getLeft() + textWidth + textInsets.getRight();
		pane.setMinWidth(width);
		pane.setMaxWidth(width);
		pane.setPrefWidth(width);
	}

	/**
	 * Plot the axis.
	 * 
	 * @param y Current y (mouse) value.
	 */
	public void plot(double y) {
		plotScale();
		plotCursorValue(y);
	}

	/**
	 * Plot the vertical axis scale of numbers.
	 */
	private void plotScale() {

		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		FX.strokeLine(gc, 0, 0, 0, canvas.getHeight());

		// Save context.
		gc.save();

		// Plot data.
		PlotData plotData = getContext().getPlotData();

		// Retrieve the increase to apply and the decimal places to floor.
		BigDecimal increase = getIncreaseValue(gc, plotData.getMaximumValue());
		if (increase == null) {
			return;
		}
		int floorScale = increase.scale();
		int pipScale = plotData.getPipScale();

		// Iterate starting at the floor of the maximum value until the minimum value would be passed.
		double maximumValue = new BigDecimal(plotData.getMaximumValue()).setScale(pipScale - 1,
			BigDecimal.ROUND_FLOOR).doubleValue();
		double minimumValue = plotData.getMinimumValue();
		double plotValue = Numbers.floor(maximumValue, floorScale);

		PlotScale plotScale = plotData.getPlotScale();
		while (plotValue > minimumValue) {
			double y = getContext().getCoordinateY(plotValue);
			plotValue(y, plotValue, pipScale, null);
			if (plotScale.equals(PlotScale.LOGARITHMIC)) {
				increase = getIncreaseValue(gc, plotValue);
			}
			if (increase == null) {
				break;
			}
			plotValue -= increase.doubleValue();
		}

		// Restore.
		gc.restore();
	}

	/**
	 * Draw the cursor value with the small line, surrounded by a rectangle.
	 * 
	 * @param y The y coordinate.
	 */
	private void plotCursorValue(double y) {
		double value = getContext().getDataValue(y);
		int scale = getContext().getPlotData().getTickScale();
		plotValue(y, value, scale, surroundFillColor);
	}

	/**
	 * Draw a vertical axis value.
	 * 
	 * @param y The y coordinate.
	 * @param value The value to plot.
	 * @param scale The scale.
	 * @param surroundColor The surround color or null if not surrounded.
	 */
	private void plotValue(double y, double value, int scale, Color surroundColor) {

		GraphicsContext gc = canvas.getGraphicsContext2D();

		// Save context.
		gc.save();

		// Draw the small line.
		gc.setStroke(lineColor);
		FX.strokeLine(gc, 0, y, lineLength - 1, y);

		// Set the font and retrieve the font bounds.
		gc.setFont(textFont);
		// The string to draw and its coordinates.
		String strValue = Formats.formattedFromDouble(value, scale, container.getChart().getLocale());
		Bounds bounds = FX.getStringBounds(strValue, textFont);
		double xStrValue = lineLength + textInsets.getLeft() - 1;
		double yStrValue = y + bounds.getHeight() / 5.0;

		// If need to surround it.
		if (surroundColor != null) {
			double xRectSurround = xStrValue - surroundInsets.getLeft();
			double yRectSurround = yStrValue - surroundInsets.getTop() - bounds.getHeight() * 3.0 / 4.0;
			double widthRectSurround = surroundInsets.getLeft() + bounds.getWidth() + surroundInsets.getRight();
			double heightRectSurround = surroundInsets.getTop() + bounds.getHeight() + surroundInsets.getBottom();
			gc.setFill(surroundFillColor);
			gc.setStroke(Color.BLACK);
			FX.fillRect(gc, xRectSurround, yRectSurround, widthRectSurround, heightRectSurround);
			FX.strokeRect(gc, xRectSurround, yRectSurround, widthRectSurround, heightRectSurround);
		}

		// The color of the text is black or white depending on the brightness of the surround color if present.
		Color textColor = Color.BLACK;
		if (surroundColor != null) {
			if (surroundColor.getBrightness() < 0.5) {
				textColor = Color.WHITE;
			}
		}
		gc.setFill(textColor);
		FX.fillText(gc, strValue, xStrValue, yStrValue);
		// FX.strokeRect(gc, xStrValue, yStrValue - bounds.getHeight() + bounds.getHeight() / 4.0, bounds.getWidth() +
		// 5, bounds.getHeight());

		// restore context.
		gc.restore();
	}

	/**
	 * Returns the value by which should be increased an initial value to plot the rounded values.
	 * 
	 * @param g2 The graphics object.
	 * @param value The starting value.
	 * @return The increase value.
	 */
	private BigDecimal getIncreaseValue(GraphicsContext gc, double value) {

		// The increase that do not overlap texts.
		BigDecimal increase = null;

		// Save context.
		gc.save();

		// Calculate the minimum line height to not overlap text adding some padding.
		double minimumHeight = FX.getStringHeight(textFont) * 1.5;

		// The maximum value and its y coordinate.
		int integerDigits = Numbers.getDigits(value);
		int decimalDigits = getContext().getPlotData().getPipScale();
		double y = getContext().getCoordinateY(value);

		// The list of increases.
		List<BigDecimal> increases = Numbers.getIncreases(integerDigits, decimalDigits, 1, 2, 5);

		// Take the first increase that do not overlaps the text.
		for (BigDecimal incr : increases) {
			double nextValue = value - incr.doubleValue();
			double nextY = getContext().getCoordinateY(nextValue);
			if (nextY - y >= minimumHeight) {
				increase = incr;
				break;
			}
		}

		// Restore context.
		gc.restore();

		return increase;
	}

	/**
	 * Return a convenient plotter context.
	 * 
	 * @return The context.
	 */
	private PlotterContext getContext() {
		return container.getChartPlotter().getContext();
	}

	/**
	 * Set the vertical axis line color.
	 * 
	 * @param verticalAxisLineColor The vertical axis line color.
	 */
	public void setLineColor(Color verticalAxisLineColor) {
		this.lineColor = verticalAxisLineColor;
	}

	/**
	 * Set the vertical axis line length.
	 * 
	 * @param verticalAxisLineLength The vertical axis line length.
	 */
	public void setLineLength(double verticalAxisLineLength) {
		this.lineLength = verticalAxisLineLength;
	}

	/**
	 * Set the vertical axis surround fill color.
	 * 
	 * @param verticalAxisSurroundFillColor The vertical axis surround fill color.
	 */
	public void setSurroundFillColor(Color verticalAxisSurroundFillColor) {
		this.surroundFillColor = verticalAxisSurroundFillColor;
	}

	/**
	 * Set the vertical axis surround insets.
	 * 
	 * @param verticalAxisSurroundInsets The vertical axis surround insets.
	 */
	public void setSurroundInsets(Insets verticalAxisSurroundInsets) {
		this.surroundInsets = verticalAxisSurroundInsets;
	}

	/**
	 * Set the vertical axis text font.
	 * 
	 * @param verticalAxisTextFont The vertical axis text font.
	 */
	public void setTextFont(Font verticalAxisTextFont) {
		this.textFont = verticalAxisTextFont;
	}

	/**
	 * Set the vertical axis text insets.
	 * 
	 * @param verticalAxisTextInsets The vertical axis text insets.
	 */
	public void setTextInsets(Insets verticalAxisTextInsets) {
		this.textInsets = verticalAxisTextInsets;
	}
}
