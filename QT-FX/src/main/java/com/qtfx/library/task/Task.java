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
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

import com.qtfx.library.util.FormatUtils;
import com.qtfx.library.util.NumberUtils;
import com.qtfx.library.util.TextServer;

import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;

/**
 * A task extension that exposes some additional and useful properties to monitor it.
 *
 * @author Miquel Sas
 */
public abstract class Task extends javafx.concurrent.Task<Void> {

	/** Time start, set when the task changes to the RUNNING state. */
	private double timeStart = -1;
	/** Time elapsed, set on the call to <code>updateMessageTime()</code>. */
	private double timeElapsed = -1;
	/** Time estimated, set on the call to <code>updateMessageTime()</code>. */
	private double timeEstimated = -1;
	/** Time remaining, set on the call to <code>updateMessageTime()</code>. */
	private double timeRemaining = -1;
	/** Privately register work done, necessary to avoid calls to properties. */
	private double workDone = -1;
	/** Privately register total work, necessary to avoid calls to properties. */
	private double totalWork = -1;

	/**
	 * Progress message string property, set on the call to <code>updateProgress()</code>.
	 */
	private StringProperty messageProgress = new SimpleStringProperty();
	/**
	 * Used to send message updates in a thread-safe manner from the subclass to the FX application thread.
	 * AtomicReference is used so as to coalesce updates such that we don't flood the event queue.
	 */
	private AtomicReference<String> messageProgressUpdate = new AtomicReference<>();
	/**
	 * Time message string property, set on the call to <code>updateProgress()</code>.
	 */
	private StringProperty messageTime = new SimpleStringProperty();
	/**
	 * Used to send message updates in a thread-safe manner from the subclass to the FX application thread.
	 * AtomicReference is used so as to coalesce updates such that we don't flood the event queue.
	 */
	private AtomicReference<String> messageTimeUpdate = new AtomicReference<>();

	/** List of optional additional messages. */
	private ListProperty<String> messages = new SimpleListProperty<>();

	/** Progress decimals for the progress message. */
	private int progressDecimals = 1;

	/** The locale to use build messages. */
	private Locale locale;

	/**
	 * The parent task when this task is submitted to a pool by the parent, to be executed concurrently with a list of
	 * tasks.
	 */
	private Task parent;
	/**
	 * The parent lock.
	 */
	private ReentrantLock lock;

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
		setOnRunning((EventHandler<WorkerStateEvent>) value -> {
			timeStart = System.currentTimeMillis();
		});
	}

	/**
	 * Set the parent task and initialize the parent lock if not already initialized.
	 * 
	 * @param parent The parent task.
	 */
	protected void setParent(Task parent) {
		this.parent = parent;
		if (parent.lock == null) {
			parent.lock = new ReentrantLock();
		}
	}

	/**
	 * Return the start time.
	 * 
	 * @return The start time.
	 */
	public double getTimeStart() {
		return timeStart;
	}

	/**
	 * Return the elapsed time.
	 * 
	 * @return The elapsed time.
	 */
	public double getTimeElapsed() {
		return timeElapsed;
	}

	/**
	 * Return the remaining time.
	 * 
	 * @return The remaining time.
	 */
	public double getTimeRemaining() {
		return timeRemaining;
	}

	/**
	 * Return the estimated time.
	 * 
	 * @return The estimated time.
	 */
	public double getTimeEstimated() {
		return timeEstimated;
	}

	/**
	 * Set the progress message decimals.
	 * 
	 * @param progressDecimals The progress message decimals.
	 */
	public void setProgressDecimals(int progressDecimals) {
		this.progressDecimals = progressDecimals;
	}

	/**
	 * Return the optional list of additional messages.
	 * 
	 * @return The optional list of additional messages.
	 */
	public ReadOnlyListProperty<String> messagesProperty() {
		return messages;
	}

	/**
	 * Return the progress message string property, set on the call to <code>updateTime</code>.
	 * 
	 * @return The time progress string property.
	 */
	public ReadOnlyStringProperty messageProgressProperty() {
		return messageProgress;
	}

	/**
	 * Return the time message string property, set on the call to <code>updateTime</code>.
	 * 
	 * @return The time progress string property.
	 */
	public ReadOnlyStringProperty messageTimeProperty() {
		return messageTime;
	}

	/**
	 * Check if this task is indeterminate.
	 * 
	 * @return A boolean.
	 */
	public boolean isIndeterminate() {
		return workDone < 0 && totalWork < 0;
	}

	/**
	 * Request total work. The implementor, if it knows or can calculate the total work, must call
	 * <code>updateProgress(0, totalWork)</code>.
	 */
	protected void requestTotalWork() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void updateMessage(String message) {
		// When running through a parent any message update is the responsibility of the parent.
		if (parent != null) {
			return;
		}
		super.updateMessage(message);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void updateTitle(String title) {
		// When running through a parent any title update is the responsibility of the parent.
		if (parent != null) {
			return;
		}
		super.updateTitle(title);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void updateProgress(double workDone, double totalWork) {

		// Not running through a parent, do the normal progress update.
		if (parent == null) {
			this.workDone = workDone;
			this.totalWork = totalWork;
			super.updateProgress(workDone, totalWork);
			updateMessageProgress();
			updateMessageTime();
			return;
		}

		// Running through a parent, progress updates are managed incremental and submitted to the parent. Must block
		// until done. A special case is when wrokDone and totalWork are -1, indeterminate, must manage when a task is
		// indeterminate and others not.
		if (workDone < 0 || totalWork < 0) {
			return;
		}
		parent.lock.lock();
		try {
			// Current task increases.
			double workDoneDelta = (this.workDone < 0 ? workDone : workDone - this.workDone);
			this.workDone = workDone;
			double totalWorkDelta = (this.totalWork < 0 ? totalWork : totalWork - this.totalWork);
			this.totalWork = totalWork;
			// Parent workDone and total work to submit.
			double parentWorkDone = parent.workDone + workDoneDelta;
			double parentTotalWork = parent.totalWork + totalWorkDelta;

			// Do submit.
			parent.updateProgress(parentWorkDone, parentTotalWork);

			// Set an automated message.
			StringBuilder b = new StringBuilder();
			b.append(TextServer.getString("tokenProcessed"));
			b.append(" ");
			b.append(FormatUtils.formattedFromLong(Double.valueOf(parentWorkDone + 1).longValue()));
			b.append(" ");
			b.append(TextServer.getString("tokenOf"));
			b.append(" ");
			b.append(FormatUtils.formattedFromLong(Double.valueOf(parentTotalWork + 1).longValue()));
			parent.updateMessage(b.toString());

		} finally {
			parent.lock.unlock();
		}
	}

	/**
	 * An update progress call for indeterminate tasks.
	 */
	protected void updateProgressIndeterminate() {
		updateProgress(-1, -1);
	}

	/**
	 * Updates the progress message property with the token counting, useful for some tasks that spend some time
	 * counting the number of steps.
	 */
	protected void updateCounting() {
		String message = TextServer.getString("tokenCounting", locale) + "...";
		updateMessageProperty(message, messageProgress, messageProgressUpdate);
	}

	/**
	 * Updates the progress message property. In an indeterminate task it should be called per iteration, while in a
	 * determinate task it is called by the update progress methods.
	 */
	protected void updateMessageProgress() {
		updateMessageProperty(getMessageProgress(), messageProgress, messageProgressUpdate);
	}

	/**
	 * Return the progress message.
	 * 
	 * @return The progress message.
	 */
	protected String getMessageProgress() {
		StringBuilder b = new StringBuilder();
		if (isIndeterminate()) {
			b.append(TextServer.getString("tokenProcessing", locale) + "...");
		} else {
			b.append(TextServer.getString("tokenProcessed", locale));
			b.append(" ");
			b.append(NumberUtils.getBigDecimal((workDone / totalWork) * 100.0, progressDecimals));
			b.append(" %");
		}
		return b.toString();
	}

	/**
	 * Updates the time properties and the time message property. In an indeterminate task it should be called per
	 * iteration, while in a determinate task it is called by the update progress methods.
	 */
	protected void updateMessageTime() {
		updateMessageProperty(getMessageTime(), messageTime, messageTimeUpdate);
	}

	protected String getMessageTime() {
		double currenTime = System.currentTimeMillis();
		timeElapsed = currenTime - timeStart;
		StringBuilder b = new StringBuilder();
		b.append(TextServer.getString("tokenTime", locale));
		b.append(" ");
		b.append(TextServer.getString("tokenElapsed", locale).toLowerCase());
		b.append(" ");
		b.append(getTimeString(timeElapsed));
		if (!isIndeterminate()) {
			double progress = workDone / totalWork;
			timeEstimated = timeElapsed / progress;
			timeRemaining = timeEstimated - timeElapsed;
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
			b.append(getTimestampString(timeStart));
			b.append(", ");
			b.append(TextServer.getString("tokenEnd", locale).toLowerCase());
			b.append(" ");
			b.append(TextServer.getString("tokenTime", locale).toLowerCase());
			b.append(" ");
			b.append(getTimestampString(timeStart + timeEstimated));
		}
		return b.toString();
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
	 * Returns the formatted time stamp.
	 * 
	 * @param time The time in milliseconds.
	 * @return The formatted times tamp.
	 */
	private String getTimestampString(double time) {
		return FormatUtils.formattedFromTimestamp(new Timestamp((long) time), locale);
	}

	/**
	 * Publish the update title protected method.
	 * 
	 * @param title The title.
	 */
	public void setTitle(String title) {
		updateTitle(title);
	}

	/**
	 * Publish the update message protected method.
	 * 
	 * @param message The message.
	 */
	public void setMessage(String message) {
		updateMessage(message);
	}

	/**
	 * Safely update a message/text property.
	 * 
	 * @param message The message.
	 * @param property The property.
	 * @param atomicRef The correspondant atomic reference.
	 */
	protected void updateMessageProperty(String message, StringProperty property, AtomicReference<String> atomicRef) {
		if (Platform.isFxApplicationThread()) {
			property.set(message);
		} else {
			// It might be that the background thread will update this message quite frequently, and we need to throttle
			// the updates so as not to completely clobber the event dispatching system.
			if (atomicRef.getAndSet(message) == null) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						final String message = atomicRef.getAndSet(null);
						property.set(message);
					}
				});

			}
		}
	}

}
