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

import java.awt.EventQueue;
import java.sql.Timestamp;
import java.util.Locale;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.atomic.AtomicReference;

import com.qtfx.library.util.FormatUtils;
import com.qtfx.library.util.NumberUtils;
import com.qtfx.library.util.TextServer;
import com.qtfx.library.util.ThreadUtils;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * An observable recursive action that is not forced to be observed from the FX application thread.
 * <p>
 * For the sake of simplicity in the class architecture, only the properties are published, no <code>setOn...</code>
 * methods are provided, enforcing to set listeners directly to the properties.
 * <p>
 * Four string messages are published:
 * <ul>
 * <li><em>title</em>, a user task title.</li>
 * <li><em>message</em>, a user message.</li>
 * <li><em>progressMessage</em>, an internally and standard updated progress message.</li>
 * <li><em>timeMessage</em>, an internally and standard updated time message.</li>
 * </ul>
 *
 * @author Miquel Sas
 */
public abstract class Task extends ForkJoinTask<Void> {

	/**
	 * A small class to manage published strings.
	 */
	private static final class Message {
		StringProperty property;
		AtomicReference<String> reference;

		Message(Object bean, String name, String initialValue) {
			property = new SimpleStringProperty(bean, name, initialValue);
			reference = new AtomicReference<>();
		}
	}

	/**
	 * A small class to manage the published progress.
	 */
	private static final class Progress {
		DoubleProperty workDone;
		DoubleProperty totalWork;
		DoubleProperty progress;
		AtomicReference<Progress> reference;

		Progress(Object bean) {
			workDone = new SimpleDoubleProperty(bean, "workDone", -1);
			totalWork = new SimpleDoubleProperty(bean, "totalWork", -1);
			progress = new SimpleDoubleProperty(bean, "progress", -1);
			reference = new AtomicReference<>();
		}
	}

	/** State property. */
	private ObjectProperty<State> state = new SimpleObjectProperty<>(this, "state", State.READY);
	/** Title string property. */
	private Message title = new Message(this, "title", "");
	/** User message string property. */
	private Message message = new Message(this, "message", "");
	/** Progress message string property. */
	private Message progressMessage = new Message(this, "progressMessage", "");
	/** Time message string property. */
	private Message timeMessage = new Message(this, "timeMessage", "");

	/** Progress properties. */
	private Progress progress = new Progress(this);

	/** Time start, set when the task changes to the RUNNING state. */
	private double timeStart = -1;
	/** Time elapsed, set on the call to <code>updateMessageTime()</code>. */
	private double timeElapsed = -1;
	/** Time estimated, set on the call to <code>updateMessageTime()</code>. */
	private double timeEstimated = -1;
	/** Time remaining, set on the call to <code>updateMessageTime()</code>. */
	private double timeRemaining = -1;
	/** Work done, internal use. */
	private double workDone = -1;
	/** Total work, internal use. */
	private double totalWork = -1;

	/** Progress decimals for the progress message. */
	private int progressDecimals = 1;

	/** The locale to use to build messages. */
	private Locale locale;

	/**
	 * A boolean that indicates whether when this task has been instantiated the FX application thread was running. This
	 * is important in the moment to update observable properties. It is assumed that the FX application thread will
	 * never be launched after the class has been instantiated.
	 */
	private boolean fxApplicationThread;

	/**
	 * Default constructor.
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

		// Check if the FX application thread is running.
		fxApplicationThread = ThreadUtils.isFxApplicationThreadRunning();

		// Add the listener to register the start time when the state changes to RUNNING.
		state.addListener((observable, oldValue, newValue) -> {
			if (newValue.equals(State.RUNNING)) {
				timeStart = System.currentTimeMillis();
			}
		});
	}

	/**
	 * Publish the state property.
	 * 
	 * @return The state property.
	 */
	public ReadOnlyObjectProperty<State> stateProperty() {
		return state;
	}

	/**
	 * Publish the title property.
	 * 
	 * @return The title property.
	 */
	public ReadOnlyStringProperty titleProperty() {
		return title.property;
	}

	/**
	 * Publish the message property.
	 * 
	 * @return The message property.
	 */
	public ReadOnlyStringProperty messageProperty() {
		return message.property;
	}

	/**
	 * Publish the progress message property.
	 * 
	 * @return The progress message property.
	 */
	public ReadOnlyStringProperty progressMessageProperty() {
		return progressMessage.property;
	}

	/**
	 * Publish the time message property.
	 * 
	 * @return The time message property.
	 */
	public ReadOnlyStringProperty timeMessageProperty() {
		return timeMessage.property;
	}

	/**
	 * Publish the work done property.
	 * 
	 * @return The work done property.
	 */
	public ReadOnlyDoubleProperty workDoneProperty() {
		return progress.workDone;
	}

	/**
	 * Publish the total work property.
	 * 
	 * @return The total work property.
	 */
	public ReadOnlyDoubleProperty totalWorkProperty() {
		return progress.totalWork;
	}

	/**
	 * Publish the progress property.
	 * 
	 * @return The progress property.
	 */
	public ReadOnlyDoubleProperty progressProperty() {
		return progress.progress;
	}

	/**
	 * Request total work. The implementor, if it knows or can calculate the total work, must call
	 * <code>updateProgress(0, totalWork)</code>, if it does not know the total work must call
	 * <code>updateProgress(-1, -1)</code> to set the task as indeterminate.
	 */
	protected void requestTotalWork() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void reinitialize() {

		// Reset state variables.
		timeStart = -1;
		timeElapsed = -1;
		timeEstimated = -1;
		timeRemaining = -1;
		workDone = -1;
		totalWork = -1;

		message.property.set("");
		progressMessage.property.set("");
		timeMessage.property.set("");
		progress.workDone.set(-1);
		progress.totalWork.set(-1);
		progress.progress.set(-1);
		state.set(State.READY);

		super.reinitialize();
	}

	/**
	 * Compute the task.
	 */
	protected abstract void compute() throws Exception;

	/**
	 * Return the working locale.
	 * 
	 * @return The working locale.
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * Return the start time.
	 * 
	 * @return The start time.
	 */
	protected double getTimeStart() {
		return timeStart;
	}

	/**
	 * Return the elapsed time.
	 * 
	 * @return The elapsed time.
	 */
	protected double getTimeElapsed() {
		return timeElapsed;
	}

	/**
	 * Return the remaining time.
	 * 
	 * @return The remaining time.
	 */
	protected double getTimeRemaining() {
		return timeRemaining;
	}

	/**
	 * Return the estimated time.
	 * 
	 * @return The estimated time.
	 */
	protected double getTimeEstimated() {
		return timeEstimated;
	}

	/**
	 * Return the internal work done value.
	 * 
	 * @return The internal work done value.
	 */
	protected double getWorkDone() {
		return workDone;
	}

	/**
	 * Set the work done internal value.
	 * 
	 * @param workDone The work done.
	 */
	protected void setWorkDone(double workDone) {
		this.workDone = workDone;
	}

	/**
	 * Return the internal total work value.
	 * 
	 * @return The internal total work value.
	 */
	protected double getTotalWork() {
		return totalWork;
	}

	/**
	 * Set the internal total work.
	 * 
	 * @param totalWork The total work.
	 */
	protected void setTotalWork(double totalWork) {
		this.totalWork = totalWork;
	}

	/**
	 * Check if this task is indeterminate.
	 * 
	 * @return A boolean.
	 */
	public boolean isIndeterminate() {
		return workDone == -1;
	}

	/**
	 * Updates the user message, the work done and total, the time and the progress messages.
	 * 
	 * @param message The user message.
	 * @param workDone The work done.
	 * @param totalWork The total work.
	 */
	protected void update(String message, double workDone, double totalWork) {
		updateProgress(workDone, totalWork);
		updateMessage(message);
		updateString(getMessageProgress(), progressMessage);
		updateString(getMessageTime(), timeMessage);
	}

	/**
	 * Updates the user message, the time and the progress messages for an indeterminate task.
	 * 
	 * @param message The user message.
	 */
	protected void update(String message) {
		updateProgress(-1, -1);
		updateMessage(message);
		updateString(getMessageProgress(), progressMessage);
		updateString(getMessageTime(), timeMessage);
	}

	/**
	 * Updates the <em>workDone</em>, <em>totalWork</em> and <em>progress</em> properties.
	 * 
	 * @param workDone Work done.
	 * @param totalWork Total work.
	 */
	protected void updateProgress(double workDone, double totalWork) {

		// Adjustments.
		if (Double.isInfinite(workDone) || Double.isNaN(workDone)) {
			workDone = -1;
		}
		if (Double.isInfinite(totalWork) || Double.isNaN(totalWork)) {
			totalWork = -1;
		}
		if (workDone < 0) {
			workDone = -1;
		}
		if (totalWork < 0) {
			totalWork = -1;
		}
		if (workDone > totalWork) {
			workDone = totalWork;
		}

		// Register.
		final double workDoneFinal = workDone;
		final double totalWorkFinal = totalWork;
		this.workDone = workDoneFinal;
		this.totalWork = totalWorkFinal;

		if (progress.reference.getAndSet(progress) == null) {
			runLater(() -> {
				final Progress p = progress.reference.getAndSet(null);
				p.workDone.set(workDoneFinal);
				p.totalWork.set(totalWorkFinal);
				if (workDoneFinal == -1) {
					p.progress.set(-1);
				} else {
					p.progress.set(workDoneFinal / totalWorkFinal);
				}
			});
		}
	}

	/**
	 * Update the title property.
	 * 
	 * @param title The title string.
	 */
	protected void updateTitle(String title) {
		updateString(title, this.title);
	}

	/**
	 * Update the message property.
	 * 
	 * @param message The message string.
	 */
	protected void updateMessage(String message) {
		updateString(message, this.message);
	}

	/**
	 * Updates the progress message property with the token counting, useful for some tasks that spend some time
	 * counting the number of steps.
	 */
	protected void updateCounting() {
		String message = TextServer.getString("tokenCounting", locale) + "...";
		updateMessage(message);
	}

	/**
	 * Update a string property.
	 * 
	 * @param str The string.
	 * @param update The update string structure.
	 */
	private void updateString(String str, Message update) {
		if (update.reference.getAndSet(str) == null) {
			runLater(() -> {
				update.property.set(update.reference.getAndSet(null));
			});
		}
	}

	/**
	 * Set the title.
	 * 
	 * @param title The title.
	 */
	public void setTitle(String title) {
		updateTitle(title);
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
			double percentage = (workDone <= 0 ? 0.0 : (workDone / totalWork) * 100.0);
			b.append(NumberUtils.getBigDecimal(percentage, progressDecimals));
			b.append("% (");
			b.append(FormatUtils.formattedFromLong(Double.valueOf(workDone).longValue()));
			b.append(" ");
			b.append(TextServer.getString("tokenOf"));
			b.append(" ");
			b.append(FormatUtils.formattedFromLong(Double.valueOf(totalWork).longValue()));
			b.append(")");
		}
		return b.toString();
	}

	/**
	 * Return the time message.
	 * 
	 * @return The time message.
	 */
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
	 * @return The formatted time stamp.
	 */
	private String getTimestampString(double time) {
		return FormatUtils.formattedFromTimestamp(new Timestamp((long) time), locale);
	}

	/**
	 * Always returns {@code null}.
	 *
	 * @return {@code null} always
	 */
	@Override
	public Void getRawResult() {
		return null;
	}

	/**
	 * Requires null completion value.
	 */
	@Override
	protected void setRawResult(Void mustBeNull) {
	}

	/**
	 * Request termination of this task.
	 * 
	 * @return A boolean if the cancel was successful.
	 */
	public final boolean cancel() {
		return cancel(true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		boolean flag = super.cancel(mayInterruptIfRunning);
		if (flag) {
			// Set the state to CANCELLED.
			runLater(() -> {
				state.set(State.CANCELLED);
			});
		}
		return flag;
	}

	/**
	 * Implements execution convention.
	 */
	@Override
	protected final boolean exec() {

		// Set state to RUNNING.
		runLater(() -> {
			state.set(State.RUNNING);
		});

		// Perform computation.
		try {
			compute();
		} catch (Throwable ex) {
			completeExceptionally(ex);
			// Set state to FAILED.
			runLater(() -> {
				state.set(State.FAILED);
			});
		}

		// If was not can cancelled, set the state to SUCCEDED.
		if (!isCancelled() && !isCompletedAbnormally()) {
			runLater(() -> {
				state.set(State.SUCCEEDED);
			});
		}
		return true;
	}

	/**
	 * Run later taking into account the platform.
	 * 
	 * @param r The runnable.
	 */
	private void runLater(Runnable r) {
		if (fxApplicationThread) {
			Platform.runLater(r);
		} else {
			EventQueue.invokeLater(r);
		}
	}
}
