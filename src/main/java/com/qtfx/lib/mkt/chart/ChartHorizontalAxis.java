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
import com.qtfx.lib.mkt.data.DataList;
import com.qtfx.lib.mkt.data.PlotData;
import com.qtfx.lib.util.Calendar;
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
 * An horizontal axis in a chart view. The horizontal axis contains one or two lines and shows time information
 * depending on the period shown.
 * 
 * @author Miquel Sas
 */
public class ChartHorizontalAxis {
	/**
	 * An enumeration of the time periods to show with their approximate millis.
	 */
	enum TimePeriod {
		FIVE_MINUTES(1000L * 60L * 5L, "00:00"),
		FIFTEEN_MINUTES(1000L * 60L * 15L, "00:00"),
		THIRTY_MINUTES(1000L * 60L * 30L, "00:00"),
		ONE_HOUR(1000L * 60L * 60L, "00:00"),
		THREE_HOURS(1000L * 60L * 60L * 3L, "00:00"),
		SIX_HOURS(1000L * 60L * 60L * 6L, "00:00"),
		TWELVE_HOURS(1000L * 60L * 60L * 12L, "00:00"),
		DAY(1000L * 60L * 60L * 24L, "0000-00-00"),
		WEEK(1000L * 60L * 60L * 24L * 7L, "0000-00-00"),
		MONTH(1000L * 60L * 60L * 24L * 30L, "0000-00"),
		QUARTER(1000L * 60L * 60L * 24L * 90L, "0000-00"),
		YEAR(1000L * 60L * 60L * 24L * 365L, "0000"),
		QUINQUENIUM(1000L * 60L * 60L * 24L * 365L * 5L, "0000"),
		DECADE(1000L * 60L * 60L * 24L * 365L * 10L, "0000");

		private long millis;
		private String string;

		TimePeriod(long millis, String string) {
			this.millis = millis;
			this.string = string;
		}

		long getMillis() {
			return millis;
		}

		String getString() {
			return string;
		}

	}

	/** Chart. */
	private Chart chart;
	/** Effective pane. */
	private Pane pane;
	/** Plot canvas. */
	private Canvas canvas;

	/** Horizontal axis font. */
	private Font textFont = Font.font("System Regular", 12);
	/** Horizontal axis: the line and text color. */
	private Color textColor = Color.BLACK;
	/** Horizontal axis: insets of the text. */
	private Insets textInsets = new Insets(3, 2, 3, 2);
	/** Vertical axis: the length of the small line before each value. */
	private double lineLength = 5;
	/** Horizontal axis height. */
	private double axisHeight = 40;

	/**
	 * Constructor.
	 * 
	 * @param chart The parent chart.
	 */
	public ChartHorizontalAxis(Chart chart) {
		this.chart = chart;

		pane = new Pane();
		canvas = new Canvas();
		pane.getChildren().add(canvas);
		canvas.widthProperty().bind(Bindings.selectDouble(canvas.parentProperty(), "width"));
		canvas.heightProperty().bind(Bindings.selectDouble(canvas.parentProperty(), "height"));
		
		pane.setMinHeight(axisHeight);
		pane.setMaxHeight(axisHeight);
		pane.setPrefHeight(axisHeight);
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
	 * Do plot the axis.
	 */
	public void plot() {
		
		if (chart.getContainers().isEmpty()) {
			return;
		}
		
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.save();
		
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		FX.strokeLine(gc, 0, 0, canvas.getWidth(), 0);
		
		// The first data list of the plot data is enough to get the list of times.
		PlotData plotData = chart.getContainers().get(0).getPlotData();
		PlotterContext context = chart.getContainers().get(0).getChartPlotter().getContext();
		DataList dataList = plotData.get(0);
		
		// Set start and end time indexes.
		int startTimeIndex = plotData.getStartIndex();
		if (startTimeIndex < 0) {
			startTimeIndex = 0;
		}
		int endTimeIndex = plotData.getEndIndex();
		if (endTimeIndex >= dataList.size()) {
			endTimeIndex = dataList.size() - 1;
		}
		if (startTimeIndex >= endTimeIndex) {
			return;
		}
		
		// Calculate the available width to plot.
		double startX = context.getCoordinateX(startTimeIndex);
		double endX = context.getCoordinateX(endTimeIndex);
		double availableWidth = endX - startX + 1;

		// Start and end time.
		long startTime = dataList.get(startTimeIndex).getTime();
		long endTime = dataList.get(endTimeIndex).getTime();
		long timeElapsed = endTime - startTime;
		
		// The time period to plot.
		TimePeriod timePeriod = getTimePeriodThatFits(gc, timeElapsed, availableWidth);
		
		// Set drawing parameters.
		gc.setFont(textFont);
		gc.setFill(textColor);
		
		// Necessary width to check overlaps.
		Bounds bounds = FX.getStringBounds(timePeriod.getString(), textFont);
		double necessaryWidth = textInsets.getLeft() + bounds.getWidth() + textInsets.getRight() + 1;
		
		// Iterate from start index to end index.
		double lastX = 0;
		for (int index = startTimeIndex + 1; index <= endTimeIndex; index++) {

			// Current and previous times.
			long timeCurrent = dataList.get(index).getTime();
			long timePrevious = dataList.get(index - 1).getTime();

			// Check if the index is the start of the time period and if not do nothing.
			boolean startPeriod = isStartTimePeriod(timeCurrent, timePrevious, timePeriod);
			if (!startPeriod) {
				continue;
			}

			// Get the string and plot it.
			String stringToPlot = getStringToPlot(timeCurrent, timePeriod);
			double x = context.getCoordinateX(index) + textInsets.getLeft();

			// Check overlap.
			if (x - lastX < necessaryWidth) {
				continue;
			}
			lastX = x;

			// Draw the vertical line.
			double lineX = x + bounds.getWidth() / 2.0;
			FX.strokeLine(gc, lineX, 0, lineX, lineLength);

			// Draw the string.
			double y = lineLength + textInsets.getTop() + bounds.getHeight() / 2.0;
			FX.fillText(gc, stringToPlot, x, y);
		}
		
		gc.restore();
	}
	
	/**
	 * Returns the time string to plot.
	 * 
	 * @param time The time in millis.
	 * @param timePeriod The time period.
	 * @return The string to plot.
	 */
	private String getStringToPlot(long time, TimePeriod timePeriod) {

		boolean year;
		boolean month;
		boolean day;
		boolean hour;
		boolean minute;
		boolean second = false;
		boolean millis = false;
		boolean separators = true;

		switch (timePeriod) {
		case FIVE_MINUTES:
		case FIFTEEN_MINUTES:
		case THIRTY_MINUTES:
		case ONE_HOUR:
		case THREE_HOURS:
		case SIX_HOURS:
		case TWELVE_HOURS:
			year = false;
			month = false;
			day = false;
			hour = true;
			minute = true;
			return getStringToPlot(time, year, month, day, hour, minute, second, millis, separators);
		case DAY:
		case WEEK:
			year = true;
			month = true;
			day = true;
			hour = false;
			minute = false;
			return getStringToPlot(time, year, month, day, hour, minute, second, millis, separators);
		case MONTH:
		case QUARTER:
			year = true;
			month = true;
			day = false;
			hour = false;
			minute = false;
			return getStringToPlot(time, year, month, day, hour, minute, second, millis, separators);
		case YEAR:
		case QUINQUENIUM:
		case DECADE:
		default:
			year = true;
			month = false;
			day = false;
			hour = false;
			minute = false;
			return getStringToPlot(time, year, month, day, hour, minute, second, millis, separators);
		}
	}

	/**
	 * Returns the string to plot.
	 * 
	 * @param time The time.
	 * @param year Year flag
	 * @param month Month flag.
	 * @param day Day flag.
	 * @param hour Hour flag.
	 * @param minute Minute flag.
	 * @param second Second flag.
	 * @param millis Millis flag.
	 * @param separators Separators flag.
	 * @return The string to plot.
	 */
	private static String getStringToPlot(
		long time,
		boolean year,
		boolean month,
		boolean day,
		boolean hour,
		boolean minute,
		boolean second,
		boolean millis,
		boolean separators) {
		return Formats.unformattedFromTimestamp(
			new Timestamp(time),
			year,
			month,
			day,
			hour,
			minute,
			second,
			millis,
			separators);
	}

	/**
	 * Check if the given time is the start time of the time period.
	 * 
	 * @param timeCurrent The current time to check.
	 * @param timePrevious The previous time to check.
	 * @param timePeriod The reference time period.
	 * @return A boolean that indicates if the given time is the start time of the time period.
	 */
	private boolean isStartTimePeriod(long timeCurrent, long timePrevious, TimePeriod timePeriod) {

		// The necessary calendars.
		Calendar calendarCurrent = new Calendar(timeCurrent);
		Calendar calendarPrevious = new Calendar(timePrevious);

		// Do check.
		switch (timePeriod) {
		case FIVE_MINUTES:
			if (calendarCurrent.getMinute() == calendarPrevious.getMinute()) {
				return false;
			}
			if (Numbers.remainder(calendarCurrent.getMinute(), 5) == 0) {
				return true;
			}
			return false;
		case FIFTEEN_MINUTES:
			if (calendarCurrent.getMinute() == calendarPrevious.getMinute()) {
				return false;
			}
			if (Numbers.remainder(calendarCurrent.getMinute(), 15) == 0) {
				return true;
			}
			return false;
		case THIRTY_MINUTES:
			if (calendarCurrent.getMinute() == calendarPrevious.getMinute()) {
				return false;
			}
			if (Numbers.remainder(calendarCurrent.getMinute(), 30) == 0) {
				return true;
			}
			return false;
		case ONE_HOUR:
			if (calendarCurrent.getHour() == calendarPrevious.getHour()) {
				return false;
			}
			return true;
		case THREE_HOURS:
			if (calendarCurrent.getHour() == calendarPrevious.getHour()) {
				return false;
			}
			if (Numbers.remainder(calendarCurrent.getHour(), 3) == 0) {
				return true;
			}
			return false;
		case SIX_HOURS:
			if (calendarCurrent.getHour() == calendarPrevious.getHour()) {
				return false;
			}
			if (Numbers.remainder(calendarCurrent.getHour(), 6) == 0) {
				return true;
			}
			return false;
		case TWELVE_HOURS:
			if (calendarCurrent.getHour() == calendarPrevious.getHour()) {
				return false;
			}
			if (Numbers.remainder(calendarCurrent.getHour(), 12) == 0) {
				return true;
			}
			return false;
		case DAY:
			if (calendarCurrent.getDay() == calendarPrevious.getDay()) {
				return false;
			}
			return true;
		case WEEK:
			if (calendarCurrent.getWeek() == calendarPrevious.getWeek()) {
				return false;
			}
			return true;
		case MONTH:
			if (calendarCurrent.getMonth() == calendarPrevious.getMonth()) {
				return false;
			}
			return true;
		case QUARTER:
			if (calendarCurrent.getMonth() == calendarPrevious.getMonth()) {
				return false;
			}
			if (calendarCurrent.getMonth() == 1) {
				return true;
			}
			if (calendarCurrent.getMonth() == 4) {
				return true;
			}
			if (calendarCurrent.getMonth() == 7) {
				return true;
			}
			if (calendarCurrent.getMonth() == 10) {
				return true;
			}
			return false;
		case YEAR:
			if (calendarCurrent.getYear() == calendarPrevious.getYear()) {
				return false;
			}
			return true;
		case QUINQUENIUM:
			if (calendarCurrent.getYear() == calendarPrevious.getYear()) {
				return false;
			}
			if (Numbers.remainder(calendarCurrent.getYear(), 5) == 0) {
				return true;
			}
			return false;
		case DECADE:
			if (calendarCurrent.getYear() == calendarPrevious.getYear()) {
				return false;
			}
			if (Numbers.remainder(calendarCurrent.getYear(), 10) == 0) {
				return true;
			}
			return false;
		default:
			return false;
		}
	}

	/**
	 * Returns the time period to plot that fits in the available width.
	 * 
	 * @param gc The graphics context.
	 * @param timeElapsed The total time elapsed.
	 * @param availableWidth The available width.
	 * @return The time period that fits.
	 */
	private TimePeriod getTimePeriodThatFits(GraphicsContext gc, long timeElapsed, double availableWidth) {

		// Iterate time periods.
		TimePeriod[] timePeriods = TimePeriod.values();
		for (TimePeriod timePeriod : timePeriods) {
			// The millis of the period.
			long millisPeriod = timePeriod.getMillis();
			// The number of periods within the time elapsed.
			double periods = timeElapsed / millisPeriod;
			// Available width per period.
			double availableWidthPerPeriod = (availableWidth / periods);
			// The string to show the period.
			String string = timePeriod.getString();
			double stringWidth = FX.getStringWidth(string, textFont);
			// The necessary width to show the period.
			double necessaryWidthPerPeriod = textInsets.getLeft() + stringWidth + textInsets.getRight() + 1;
			// If the available width per period is greater than the necessary width per period, we are done.
			if (availableWidthPerPeriod > necessaryWidthPerPeriod) {
				return timePeriod;
			}
		}

		// Return a decade.
		return TimePeriod.DECADE;
	}
}
