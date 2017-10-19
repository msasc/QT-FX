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

/**
 * A task that runs in a task pool.
 * 
 * @author Miquel Sas
 */
public abstract class TaskRun extends Task {

	/** The parent task pool. */
	private TaskPool parent;

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
		if (workDone < 0 || totalWork < 0) {
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

}
