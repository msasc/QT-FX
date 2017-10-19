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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A task that executes other tasks in concurrent pool.
 *
 * @author Miquel Sas
 */
public class TaskPool extends Task {

	/** List of tasks to run concurrently. */
	private List<TaskRun> tasks = new ArrayList<>();
	/** Lock used by tasks that run in this parent pool. */
	private ReentrantLock lock;

	/**
	 * Default constructor.
	 */
	public TaskPool() {
		super();
	}

	/**
	 * Constructor assigning the locale for messages.
	 * 
	 * @param locale The locale.
	 */
	public TaskPool(Locale locale) {
		super(locale);
	}

	/**
	 * Add a task to the list of tasks to execute.
	 * 
	 * @param task The task.
	 */
	public void addTask(TaskRun task) {
		task.setParent(this);
		tasks.add(task);

		// Set the listener that will handle FAILURES on tasks.
		task.stateProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.equals(State.FAILED)) {
				cancel();
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void compute() throws Exception {
	}

	/**
	 * Return this pool lock.
	 * 
	 * @return The lock
	 */
	ReentrantLock getLock() {
		return lock;
	}
}
