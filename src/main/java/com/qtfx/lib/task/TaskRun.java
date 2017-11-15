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

package com.qtfx.lib.task;

/**
 * A task that runs in a task pool.
 * 
 * @author Miquel Sas
 */
public abstract class TaskRun extends Task {

	/** The parent task pool. */
	private TaskPool parent;

	/**
	 * A flag that forces the task to act indeterminate during notifications. If any task in the pool is indeterminate,
	 * any notification is managed by the parent task pool.
	 */
	private boolean indeterminate = false;

	/**
	 * Constructor.
	 */
	public TaskRun() {
		super();
	}

	/**
	 * Set the parent pool.
	 * 
	 * @param parent The parent pool.
	 */
	void setParent(TaskPool parent) {
		this.parent = parent;
	}
	
	/**
	 * Request the total work. Called by the parent pool.
	 * @return The total work.
	 */
	protected abstract double requestTotalWork();

	/**
	 * {@inheritDoc}
	 * <p>
	 * This method does nothing when the task is tagged as indeterminate by the parent pool.
	 */
	@Override
	protected void update(String message, double workDone, double totalWork) {
		if (indeterminate) {
			return;
		}
		super.update(message, workDone, totalWork);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This method does nothing when the task is tagged as indeterminate by the parent pool.
	 */
	@Override
	protected void update(String message) {
		if (indeterminate) {
			return;
		}
		super.update(message);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This method does nothing when the task is tagged as indeterminate by the parent pool.
	 */
	@Override
	protected void updateTitle(String title) {
		if (indeterminate) {
			return;
		}
		super.updateTitle(title);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This method does nothing when the task is tagged as indeterminate by the parent pool.
	 */
	@Override
	protected void updateMessage(String message) {
		if (indeterminate) {
			return;
		}
		super.updateMessage(message);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This method does nothing when the task is tagged as indeterminate by the parent pool.
	 */
	@Override
	protected void updateCounting() {
		// TODO Auto-generated method stub
		super.updateCounting();
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This method does nothing when the task is tagged as indeterminate by the parent pool.
	 */
	@Override
	protected void updateProgressMessage() {
		if (indeterminate) {
			return;
		}
		super.updateProgressMessage();
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This method does nothing when the task is tagged as indeterminate by the parent pool.
	 */
	@Override
	protected void updateTimeMessage() {
		if (indeterminate) {
			return;
		}
		super.updateTimeMessage();
	}

	/**
	 * Update the work done by incrementally updating the parent work done.
	 * <p>
	 * When running in a parent pool, progress updates are managed incremental and submitted to the parent. Must block
	 * until done. A special case is when workDone and totalWork are -1.
	 * 
	 * @param workDone The work done.
	 * @param totalWork The total work.
	 */
	@Override
	protected void updateProgress(double workDone, double totalWork) {
		if (indeterminate || workDone < 0 || totalWork < 0) {
			return;
		}
		parent.getLock().lock();
		try {
			// Current task increases.
			double workDoneDelta = (getWorkDone() < 0 ? workDone : workDone - getWorkDone());
			setWorkDone(workDone);
			double totalWorkDelta = (getTotalWork() < 0 ? totalWork : totalWork - getTotalWork());
			setTotalWork(totalWork);
			// Parent workDone and total work to submit.
			double parentWorkDone = (parent.getWorkDone() < 0 ? workDoneDelta : parent.getWorkDone() + workDoneDelta);
			double parentTotalWork = (parent.getTotalWork() < 0 ? totalWorkDelta : parent.getTotalWork() + totalWorkDelta);

			// Do submit.
			parent.updateProgress(parentWorkDone, parentTotalWork);
		} finally {
			parent.getLock().unlock();
		}
	}

	/**
	 * Set the indeterminate flag.
	 * 
	 * @param indeterminate A boolean.
	 */
	void setIndeterminate(boolean indeterminate) {
		this.indeterminate = indeterminate;
	}
}
