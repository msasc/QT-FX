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

package com.qtfx.library.task.sample;

import com.qtfx.library.task.Task;

/**
 * Sample task useful to test the task and task pane usages.
 *
 * @author Miquel Sas
 */
public class SampleTask extends Task {

	/** Total number of iterations. */
	private long iterations;
	/** Iteration sleep. */
	private long sleep = 0;
	/** Module. */
	private long module = 0;
	/** Indeterminate flag. */
	private boolean indeterminate = false;
	/** Throw exception flag. */
	private boolean throwException = false;
	/** Throw after iterations. */
	private long throwAfterIterations;
	/** Count seconds. */
	private long countSeconds = 3;
	/** Check previously counted. */
	private boolean preCounted = false;

	/**
	 * Constructor.
	 */
	public SampleTask() {
		super();
	}

	/**
	 * @param iterations the iterations to set
	 */
	public void setIterations(long iterations) {
		this.iterations = iterations;
	}

	/**
	 * @param sleep the sleep to set
	 */
	public void setSleep(long sleep) {
		this.sleep = sleep;
	}

	/**
	 * @param module the module to set
	 */
	public void setModule(long module) {
		this.module = module;
	}

	/**
	 * @param indeterminate the indeterminate to set
	 */
	public void setIndeterminate(boolean indeterminate) {
		this.indeterminate = indeterminate;
	}

	/**
	 * @param throwException the throwException to set
	 */
	public void setThrowException(boolean throwException) {
		this.throwException = throwException;
	}

	/**
	 * @param throwAfterIterations the throwAfterIterations to set
	 */
	public void setThrowAfterIterations(long throwAfterIterations) {
		this.throwAfterIterations = throwAfterIterations;
	}

	/**
	 * @param countSeconds the countSeconds to set
	 */
	public void setCountSeconds(long countSeconds) {
		this.countSeconds = countSeconds;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void requestTotalWork() {
		if (countSeconds > 0) {
			try {
				Thread.sleep(countSeconds * 1000);
				updateProgress(0, iterations);
			} catch (InterruptedException interrupted) {
				if (isCancelled()) {
					return;
				}
			}
		}
		preCounted = true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Void call() throws Exception {
		if (!preCounted && countSeconds > 0) {
			updateCounting();
			try {
				Thread.sleep(countSeconds * 1000);
			} catch (InterruptedException interrupted) {
				if (isCancelled()) {
					return null;
				}
			}
		}
		for (long i = 0; i < iterations; i++) {
			if (isCancelled()) {
				break;
			}
			if (i == 0 || module <= 0 || ((i + 1) % module == 0 || i == (iterations - 1))) {
				updateMessage("Doing the work...");
				if (indeterminate) {
					updateProgressIndeterminate();
				} else {
					updateProgress(i + 1, iterations);
				}
			}
			if (sleep > 0) {
				try {
					Thread.sleep(sleep);
				} catch (InterruptedException interrupted) {
					if (isCancelled()) {
						break;
					}
				}
			}
			if (throwException) {
				if (i >= throwAfterIterations) {
					throw new Exception("Error occurred after " + i + " iterations.");
				}
			}
		}
		return null;
	}

}
