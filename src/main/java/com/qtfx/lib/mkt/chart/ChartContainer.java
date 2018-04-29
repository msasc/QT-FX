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

import com.qtfx.lib.app.Session;
import com.qtfx.lib.gui.FX;
import com.qtfx.lib.mkt.chart.plotter.PlotterContext;
import com.qtfx.lib.mkt.data.Data;
import com.qtfx.lib.mkt.data.Period;
import com.qtfx.lib.mkt.data.PlotData;
import com.qtfx.lib.mkt.data.Unit;
import com.qtfx.lib.mkt.data.info.DataInfo;
import com.qtfx.lib.util.Calendar;
import com.qtfx.lib.util.Formats;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
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

	/** Parent chart. */
	private Chart chart;
	/** Chart plotter to plot the chart. */
	private ChartPlotter chartPlotter;
	/** Chart info. */
	private ChartInfo chartInfo;
	/** Chart vertical axis. */
	private ChartVerticalAxis chartVerticalAxis;

	/** Plot data. */
	private PlotData plotData;
	/** Effective border pane. */
	private BorderPane pane = new BorderPane();

	/** Size listener. */
	private SizeListener sizeListener;

	/** Last mouse x. */
	private transient double lastX;
	/** Last mouse y. */
	private transient double lastY;

	/** Cursor horizontal line. */
	private Line cursorHorizontal;
	/** Cursor vertical line. */
	private Line cursorVertical;

	/** Drag index. */
	private int dragIndex = -1;

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

		// Chart vertical axis.
		chartVerticalAxis = new ChartVerticalAxis(this);
		pane.setRight(chartVerticalAxis.getPane());

		// Listeners.
		sizeListener = new SizeListener();

		chartPlotter.getPane().widthProperty().addListener(sizeListener);
		chartPlotter.getPane().heightProperty().addListener(sizeListener);

		// Handle scroll.
		chartPlotter.getPane().setOnScroll(e -> {
			int barsToScrollOrZoom = plotData.getBarsToScrollOrZoom();
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
		});

		// Mouse move.
		chartPlotter.getPane().setOnMouseMoved(e -> {
			setChartInfo(e.getX(), e.getY());
		});

		// Handle drag detected.
		chartPlotter.getPane().setOnDragDetected(e -> {

			// Register the index of the data to drag.
			PlotterContext context = chartPlotter.getContext();
			dragIndex = context.getDataIndex(e.getX());

			// Must have a Dragboard with a ClipboardContent to continue dragging.
			ClipboardContent content = new ClipboardContent();
			content.put(DataFormat.PLAIN_TEXT, "");
			Dragboard db = chartPlotter.getPane().startDragAndDrop(TransferMode.ANY);
			db.setContent(content);
		});

		// Handle drag over.
		chartPlotter.getPane().setOnDragOver(e -> {

			// Get current index
			PlotterContext context = chartPlotter.getContext();

			int currentIndex = context.getDataIndex(e.getX());
			boolean scroll = plotData.scroll(dragIndex - currentIndex);

			// Do plot
			if (scroll) {
				Platform.runLater(() -> {
					chart.plot(plotData);
					setChartInfo(e.getX(), e.getY());
				});
			}

		});

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
	void plot() {

		// Calculate frame.
		plotData.calculateFrame();

		// Vertical axis sizes.
		chartVerticalAxis.setMaximumMinimumAndPreferredWidths();
		chartVerticalAxis.plot(lastY);

		// Defer data plot.
		chartPlotter.plot();

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
	void setChartInfo(double x, double y) {
		if (chartPlotter == null || plotData == null || plotData.isEmpty()) {
			return;
		}
		setCursor(x, y);
		lastX = x;
		lastY = y;

		PlotterContext context = chartPlotter.getContext();
		int index = context.getDataIndex(x);
		boolean outOfRange = (index < 0 || index >= plotData.getDataSize());

		chartInfo.startInfo();
		chartInfo.addInfo(getInfoInstrument(), "-fx-fill: black; -fx-font-weight: bold;");
		chartInfo.addInfo(getInfoPeriod(), "-fx-fill: blue;");

		// Iterate data lists.
		if (!outOfRange) {
			chartInfo.addInfo(getInfoTimeFromIndex(index), "-fx-fill: black;");
			boolean black = false;
			for (int i = 0; i < plotData.size(); i++) {
				String color = (black ? "black" : "blue");
				black = !black;
				DataInfo info = plotData.getDataInfo(i);
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
		} else {
			chartInfo.addInfo(getInfoTimeFromXCoord(x), "-fx-fill: black;");
		}

		// The cursor value.
		if (y >= 0) {
			chartInfo.addInfo(getInfoValue(context.getDataValue(y)), "-fx-fill: red;");
		}

		// Number of visible bars.
		int minIndex = plotData.getMinimumIndex();
		int maxIndex = plotData.getMaximumIndex();
		int numBars = maxIndex - minIndex + 1;
		chartInfo.addInfo(" Bars " + numBars, "-fx-fill: blue;");

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
		b.append(plotData.getDataInfo(0).getInstrument().getId());
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
	 * Returns the time information given an index.
	 * 
	 * @param index The index.
	 * @return The time information.
	 */
	private String getInfoTimeFromIndex(int index) {
		Data data = plotData.getData(0, index);
		long time = data.getTime();
		return getInfoTime(time);
	}

	/**
	 * Returns the time information when the x coordinate is out of range.
	 * 
	 * @param x The mouse x coordinate.
	 * @return The time information.
	 */
	private String getInfoTimeFromXCoord(double x) {
		int index = chartPlotter.getContext().getDataIndex(x);
		Period period = plotData.getPeriod();
		Unit unit = period.getUnit();
		int periods = period.getSize();
		int firstIndex = 0;
		int lastIndex = plotData.getDataSize() - 1;
		long time = 0;
		if (index < firstIndex) {
			Data data = plotData.getData(0, firstIndex);
			Calendar calendar = new Calendar(data.getTime());
			while (index++ < firstIndex) {
				addToCalendar(calendar, unit, -periods);
			}
			time = calendar.getTimeInMillis();
		} else if (index > lastIndex) {
			Data data = plotData.getData(0, lastIndex);
			Calendar calendar = new Calendar(data.getTime());
			while (index-- > lastIndex) {
				addToCalendar(calendar, unit, periods);
			}
			time = calendar.getTimeInMillis();
		}
		return getInfoTime(time);
	}

	/**
	 * Add the proper units to the calendar.
	 * 
	 * @param calendar The calendar.
	 * @param unit The unit.
	 * @param periods The number of units.
	 */
	private void addToCalendar(Calendar calendar, Unit unit, int periods) {
		switch (unit) {
		case MILLISECOND:
			calendar.addMillis(periods);
			break;
		case SECOND:
			calendar.addSeconds(periods);
			break;
		case MINUTE:
			calendar.addMinutes(periods);
			break;
		case HOUR:
			calendar.addHours(periods);
			break;
		case DAY:
			calendar.addDays(periods);
			break;
		case WEEK:
			calendar.addWeeks(periods);
			break;
		case MONTH:
			calendar.addMonths(periods);
			break;
		case YEAR:
			calendar.addYears(periods);
			break;
		}
	}

	/**
	 * Returns the time information given the long timestamp.
	 * 
	 * @param time The timestamp.
	 * @return The time information.
	 */
	private String getInfoTime(long time) {
		StringBuilder b = new StringBuilder();
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
		b.append(Formats.formattedFromDouble(value, tickScale, Session.getSession().getLocale()));
		return b.toString();
	}
}
