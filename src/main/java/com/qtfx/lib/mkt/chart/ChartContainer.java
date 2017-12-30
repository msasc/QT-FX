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

import java.sql.Timestamp;

import com.qtfx.lib.gui.FX;
import com.qtfx.lib.mkt.chart.plotter.PlotterContext;
import com.qtfx.lib.mkt.data.Data;
import com.qtfx.lib.mkt.data.Period;
import com.qtfx.lib.mkt.data.PlotData;
import com.qtfx.lib.mkt.data.info.DataInfo;
import com.qtfx.lib.util.Formats;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Line;

/**
 * A pane that contains a left pane to display prices, indicators and volumes. It contains a chart plotter, a vertical
 * axis and information pane on the top.
 * 
 * @author Miquel Sas
 */
public class ChartContainer {
	
	/**
	 * Width and height change listener to respond to size events.
	 */
	class SizeListener implements ChangeListener<Number> {
		@Override
		public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
			chart.plot(plotData);
		}
	}
	/**
	 * Mouse event handler.
	 */
	class MouseListener implements EventHandler<MouseEvent> {
		@Override
		public void handle(MouseEvent e) {

			if (e.getEventType() == MouseEvent.MOUSE_DRAGGED) {
				if (e.isPrimaryButtonDown()) {
					if (!mouseDragging) {
						mouseDragging = true;
						mouseDraggingX = e.getX();
					}
					if (mouseDragging) {
						if (!e.isAltDown() && !e.isControlDown() && !e.isShiftDown()) {
							double x = e.getX();
							if (x >= 0 && x <= chartPlotter.getSize().getWidth()) {
								// The absolute width factor.
								double widthFactor = Math.abs((x - mouseDraggingX) / chartPlotter.getSize().getWidth());
								// Convert the width factor into index length.
								int startIndex = plotData.getStartIndex();
								int endIndex = plotData.getEndIndex();
								int indexScroll = Math.abs((int) ((endIndex - startIndex) * widthFactor));
								if (indexScroll < 1) {
									indexScroll = 1;
								}
								if (x < mouseDraggingX) {
									plotData.scroll(indexScroll);
								} else {
									plotData.scroll(-indexScroll);
								}
								mouseDraggingX = x;
								chart.plot(plotData);
								setChartInfo(e.getX(), e.getY());
							}
						}
					}
				}
			}

			if (e.getEventType() == MouseEvent.MOUSE_RELEASED) {
				if (mouseDragging && e.isPrimaryButtonDown()) {
					mouseDragging = false;
					mouseDraggingX = -1;
				}
			}

			if (e.getEventType() == MouseEvent.MOUSE_MOVED) {
				setChartInfo(e.getX(), e.getY());
			}
		}
	}

	/**
	 * Scroll event handler.
	 */
	class ScrollListener implements EventHandler<ScrollEvent> {
		@Override
		public void handle(ScrollEvent e) {
			int startIndex = plotData.getStartIndex();
			int endIndex = plotData.getEndIndex();
			int barsVisible = endIndex - startIndex + 1;
			int barsToScrollOrZoom = (int) (barsVisible * 0.1);
			if (barsToScrollOrZoom < 1) {
				barsToScrollOrZoom = 1;
			}
			barsToScrollOrZoom *= (e.getDeltaY() > 0 ? 1 : -1);

			// If control, zoom.
			if (e.isControlDown()) {
				if (plotData.zoom(barsToScrollOrZoom)) {
					chart.plot(plotData);
				}
			} else {
				if (plotData.scroll(barsToScrollOrZoom)) {
					chart.plot(plotData);
				}
			}
		}
	}

	/** Parent chart. */
	private Chart chart;
	/** Chart plotter to plot the chart. */
	private ChartPlotter chartPlotter;
	/** Chart info. */
	private ChartInfo chartInfo;
	/** Plot data. */
	private PlotData plotData;
	/** Effective border pane. */
	private BorderPane pane = new BorderPane();
	
	/** Size listener. */
	private SizeListener sizeListener;
	/** Mouse listener. */
	private MouseListener mouseListener;
	/** Scroll listener. */
	private ScrollListener scrollListener;
	
	/** Last mouse x. */
	private transient double lastX;
	/** Last mouse y. */
	private transient double lastY;
	/** Mouse dragging control (left button) to scroll the chart. */
	private transient boolean mouseDragging = false;
	/** The previous X coordinate when mouse dragging (always in the chart/component area). */
	private transient double mouseDraggingX = 0;

	/** Cursor horizontal line. */
	private Line cursorHorizontal;
	/** Cursor vertical line. */
	private Line cursorVertical;

	/**
	 * Constructor.
	 * 
	 * @param chart The parent chart.
	 * @param plotData The plot data.
	 */
	public ChartContainer(Chart chart, PlotData plotData) {
		super();
		this.chart = chart;
		this.plotData = plotData;

		// Chart plotter.
		chartPlotter = new ChartPlotter(this);
		pane.setCenter(chartPlotter.getPane());

		// Chart info.
		chartInfo = new ChartInfo(this);
		pane.setTop(chartInfo.getPane());
		
		// Listeners.
		sizeListener = new SizeListener();
		mouseListener = new MouseListener();
		scrollListener = new ScrollListener();

		chartPlotter.getPane().widthProperty().addListener(sizeListener);
		chartPlotter.getPane().heightProperty().addListener(sizeListener);
		chartPlotter.getPane().addEventHandler(MouseEvent.ANY, mouseListener);
		chartPlotter.getPane().addEventHandler(ScrollEvent.ANY, scrollListener);
		
		// Cursor.
		cursorHorizontal = new Line();
		cursorHorizontal.setStrokeWidth(1.0);
		cursorHorizontal.getStrokeDashArray().addAll(2.0, 5.0);
		cursorVertical = new Line();
		cursorVertical.setStrokeWidth(1.0);
		cursorVertical.getStrokeDashArray().addAll(2.0, 5.0);
		chartPlotter.getPane().getChildren().addAll(cursorHorizontal, cursorVertical);
	}

	/**
	 * Return the effective border pane.
	 * 
	 * @return The pane.
	 */
	public BorderPane getPane() {
		return pane;
	}

	/**
	 * Return the parent chart.
	 * 
	 * @return The parent chart.
	 */
	public Chart getChart() {
		return chart;
	}

	/**
	 * Return the chart plotter.
	 * 
	 * @return The chart plotter.
	 */
	public ChartPlotter getChartPlotter() {
		return chartPlotter;
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
	 * Do plot.
	 */
	public void plot() {
		
		// Calculate frame.
		plotData.calculateFrame();
		
		// Defer data plot.
		chartPlotter.plot(plotData);

		// Info.
		setChartInfo(lastX, lastY);
	}

	/**
	 * Set the cursor lines.
	 * 
	 * @param mouseX The cursor x coordinate.
	 * @param mouseY The cursor y coordinate.
	 */
	private void setCursor(double mouseX, double mouseY) {
		
		double lineWidth = cursorHorizontal.getStrokeWidth();
		double x = FX.coord(lineWidth, mouseX);
		double y = FX.coord(lineWidth, mouseY);
		double width = FX.coord(lineWidth, chartPlotter.getSize().getWidth());
		double height = FX.coord(lineWidth, chartPlotter.getSize().getHeight());

		cursorHorizontal.setStartX(0.0);
		cursorHorizontal.setStartY(y);
		cursorHorizontal.setEndX(width);
		cursorHorizontal.setEndY(y);

		cursorVertical.setStartX(x);
		cursorVertical.setStartY(0.0);
		cursorVertical.setEndX(x);
		cursorVertical.setEndY(height);
	}

	/**
	 * Set the chart info.
	 * 
	 * @param x The mouse x.
	 * @param y The mouse y.
	 */
	private void setChartInfo(double x, double y) {
		if (chartPlotter == null || plotData == null || plotData.isEmpty()) {
			return;
		}
		lastX = x;
		lastY = y;

		setCursor(x, y);

		PlotterContext context = new PlotterContext(chartPlotter.getSize(), chartPlotter.getPlotInsets(), plotData);
		int index = context.getDataIndex(x);
		boolean outOfRange = (index < 0 || index >= plotData.get(0).size());

		chartInfo.startInfo();
		chartInfo.addInfo(getInfoInstrument(), "-fx-fill: black; -fx-font-weight: bold");
		chartInfo.addInfo(getInfoPeriod(), "-fx-fill: blue;");

		// Iterate data lists.
		if (!outOfRange) {
			chartInfo.addInfo(getInfoTime(index), "-fx-fill: black;");
			boolean black = false;
			for (int i = 0; i < plotData.size(); i++) {
				String color = (black ? "black" : "blue");
				black = !black;
				DataInfo info = plotData.get(i).getDataInfo();
				String text = "";
				if (index >= 0 && index < plotData.get(i).size()) {
					Data data = plotData.get(i).get(index);
					if (data.isValid()) {
						text = info.getInfoData(data);
					}
				}
				if (!text.isEmpty()) {
					chartInfo.addInfo(text, "-fx-fill: " + color + ";");
				}
			}
		}

		// The cursor value.
		if (y >= 0) {
			chartInfo.addInfo(getInfoValue(context.getDataValue(y)), "-fx-fill: red;");
			chartInfo.addInfo("(" + x + ", " + y + ")", "-fx-fill: black;");
		}

		// Number of visible periods.
		int startIndex = plotData.getStartIndex();
		int endIndex = plotData.getEndIndex();
		int periods = endIndex - startIndex + 1;
		chartInfo.addInfo(" Periods " + periods, "-fx-fill: black;");

		// Do show.
		chartInfo.endInfo();
	}

	/**
	 * Returns the instrument info.
	 * 
	 * @return The instrument info.
	 */
	private String getInfoInstrument() {
		StringBuilder b = new StringBuilder();
		b.append(plotData.get(0).getDataInfo().getInstrument().getId());
		return b.toString();
	}

	/**
	 * Returns the period info.
	 * 
	 * @return The period info.
	 */
	private String getInfoPeriod() {
		Period period = plotData.getPeriod();
		int size = period.getSize();
		StringBuilder b = new StringBuilder();
		b.append(size);
		b.append(" ");
		b.append(period.getUnit().getShortName());
		if (size > 1) {
			b.append("s");
		}
		return b.toString();
	}

	/**
	 * Returns the time information given an idex.
	 * 
	 * @param index The index.
	 * @return The time information.
	 */
	private String getInfoTime(int index) {
		StringBuilder b = new StringBuilder();
		Data data = plotData.getData(0, index);
		long time = data.getTime();
		Timestamp timestamp = new Timestamp(time);
		boolean year = true;
		boolean month = true;
		boolean day = true;
		boolean hour = true;
		boolean minute = true;
		boolean second = true;
		boolean millis = true;
		switch (plotData.getPeriod().getUnit()) {
		case MILLISECOND:
			break;
		case SECOND:
			millis = false;
			break;
		case MINUTE:
			millis = false;
			second = false;
			break;
		case HOUR:
			millis = false;
			second = false;
			break;
		case DAY:
		case WEEK:
			millis = false;
			second = false;
			minute = false;
			hour = false;
			break;
		case MONTH:
		case YEAR:
			millis = false;
			second = false;
			hour = false;
			minute = false;
			day = false;
			break;
		default:
			break;
		}
		b.append(Formats.unformattedFromTimestamp(timestamp, year, month, day, hour, minute, second, millis, true));
		return b.toString();
	}

	/**
	 * Returns the information of the data values.
	 * 
	 * @param y The y coordinate.
	 * @return The information string.
	 */
	private String getInfoValue(double value) {
		StringBuilder b = new StringBuilder();
		// Scale to apply to value.
		int tickScale = plotData.getTickScale();
		b.append("P: ");
		b.append(Formats.formattedFromDouble(value, tickScale, chart.getLocale()));
		return b.toString();
	}
}
