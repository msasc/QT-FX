/*
 * Copyright (C) 2015 Miquel Sas
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

package com.qtfx.library.task;

import java.sql.Timestamp;
import java.util.Locale;

import com.qtfx.library.util.FormatUtils;
import com.qtfx.library.util.TextServer;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;

/**
 * A task extension that exposes some additional and useful properties.
 *
 * @author Miquel Sas
 */
public abstract class Task<V> extends javafx.concurrent.Task<V> {

	/**
	 * The change listener that internally observes state changes.
	 */
	class StateChangeListener implements ChangeListener<Worker.State> {
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void changed(ObservableValue<? extends State> observable, State oldValue, State newValue) {

			// When changing to RUNNING, register the start time.
			if (newValue.equals(State.RUNNING)) {
				timeStart.set(System.currentTimeMillis());
			}
		}
	}

	/** Time start property, set when the task changes to the RUNNING state. */
	private DoubleProperty timeStart = new SimpleDoubleProperty();
	/** Time elapsed property, set on the call to <code>updateTime</code>. */
	private DoubleProperty timeElapsed = new SimpleDoubleProperty();
	/** Time estimated property, set on the call to <code>updateTime</code>. */
	private DoubleProperty timeEstimated = new SimpleDoubleProperty();
	/** Time remaining property, set on the call to <code>updateTime</code>. */
	private DoubleProperty timeRemaining = new SimpleDoubleProperty();
	/** Time progress string property, set on the call to <code>updateTime</code>. */
	private StringProperty timeProgress = new SimpleStringProperty();

	/** The locale to use build messages. */
	private Locale locale;

	/**
	 * Constructor with the default locale for messages.
	 */
	public Task() {
		this(Locale.getDefault());
	}

	/**
	 * Constructor passing the locale for messages.
	 * 
	 * @param locale The locale for messages.
	 */
	public Task(Locale locale) {
		super();
		this.locale = locale;

		// Add the listener to internally observe state changes.
		stateProperty().addListener(new StateChangeListener());
	}

	/**
	 * Return the time start property, set when the task changes to the RUNNING state.
	 * 
	 * @return The time start property.
	 */
	public ReadOnlyDoubleProperty timeStartProperty() {
		return timeStart;
	}

	/**
	 * Return the time elapsed property, set on the call to <code>updateTime</code>.
	 * 
	 * @return The time elapsed property.
	 */
	public ReadOnlyDoubleProperty timeElapsedProperty() {
		return timeElapsed;
	}

	/**
	 * Return the time estimated property, set on the call to <code>updateTime</code>.
	 * 
	 * @return The time estimated property.
	 */
	public ReadOnlyDoubleProperty timeEstimatedProperty() {
		return timeEstimated;
	}

	/**
	 * Return the time remaining property, set on the call to <code>updateTime</code>.
	 * 
	 * @return The time remaining property.
	 */
	public ReadOnlyDoubleProperty timeRemainingProperty() {
		return timeRemaining;
	}

	/**
	 * Return the time progress string property, set on the call to <code>updateTime</code>.
	 * 
	 * @return The time progress string property.
	 */
	public ReadOnlyStringProperty timeProgressProperty() {
		return timeProgress;
	}

	/**
	 * Updates the time properties. Normally it should be called when calling <code>updateProgress</code>.
	 */
	protected void updateTime() {
		double progress = getProgress();
		double currenTime = System.currentTimeMillis();
		double timeElapsed = currenTime - timeStart.get();
		this.timeElapsed.set(timeElapsed);
		if (progress <= 0) {
			return;
		}
		double timeEstimated = timeElapsed / progress;
		double timeRemaining = timeEstimated - timeElapsed;
		this.timeEstimated.set(timeEstimated);
		this.timeRemaining.set(timeRemaining);

		StringBuilder b = new StringBuilder();
		b.append(TextServer.getString("tokenTime", locale));
		b.append(" ");
		b.append(TextServer.getString("tokenElapsed", locale).toLowerCase());
		b.append(" ");
		b.append(getTimeString(timeElapsed));
		b.append(", ");
		b.append(TextServer.getString("tokenEstimated", locale).toLowerCase());
		b.append(" ");
		b.append(getTimeString(timeEstimated));
		b.append(", ");
		b.append(TextServer.getString("tokenRemaining", locale).toLowerCase());
		b.append(" ");
		b.append(getTimeString(timeRemaining));
		b.append(", ");
		b.append(TextServer.getString("tokenTime").toLowerCase());
		b.append(" ");
		b.append(TextServer.getString("tokenStarted").toLowerCase());
		b.append(" ");
		b.append(getTimestampString(timeStart.get()));
		b.append(", ");
		b.append(TextServer.getString("tokenEnd", locale).toLowerCase());
		b.append(" ");
		b.append(TextServer.getString("tokenTime", locale).toLowerCase());
		b.append(" ");
		b.append(getTimestampString(timeStart.get() + timeEstimated));
		timeProgress.set(b.toString());
	}

	/**
	 * Returns the time information string (seconds,minutes or hours).
	 * 
	 * @param time The time in milliseconds.
	 * @return The time info.
	 */
	protected String getTimeString(double time) {
		int decimals = 1;
		double seconds = (time / 1000.0);
		if (seconds < 60) {
			StringBuilder b = new StringBuilder();
			b.append(FormatUtils.formattedFromDouble(seconds, decimals, locale));
			b.append(" ");
			b.append(TextServer.getString("tokenSeconds", locale).toLowerCase());
			return b.toString();
		}
		double minutes = (time / (1000.0 * 60.0));
		if (minutes < 60) {
			StringBuilder b = new StringBuilder();
			b.append(FormatUtils.formattedFromDouble(minutes, decimals, locale));
			b.append(" ");
			b.append(TextServer.getString("tokenMinutes", locale).toLowerCase());
			return b.toString();
		}
		double hours = (time / (1000.0 * 60.0 * 60.0));
		StringBuilder b = new StringBuilder();
		b.append(FormatUtils.formattedFromDouble(hours, decimals, locale));
		b.append(" ");
		b.append(TextServer.getString("tokenHours", locale).toLowerCase());
		return b.toString();
	}

	/**
	 * Returns the formatted timestamp.
	 * 
	 * @param time The time in millis.
	 * @return The formatted timestamp.
	 */
	private String getTimestampString(double time) {
		return FormatUtils.formattedFromTimestamp(new Timestamp((long) time), locale);
	}
}
