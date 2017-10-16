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
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

/**
 * A task that executes a pool of tasks concurrently.
 *
 * @author Miquel Sas
 */
public class TaskPool extends Task {

	/**
	 * Recursive task executor.
	 */
	class Executor extends RecursiveAction {
		@Override
		protected void compute() {
			List<ForkJoinTask<?>> runnables = new ArrayList<>();
			for (Task task : tasks) {
				runnables.add(ForkJoinTask.adapt(task));
			}
			invokeAll(runnables);
		}
	}

	/** The list of tasks to execute. */
	private List<Task> tasks = new ArrayList<>();
	/** The pool to execute tasks concurrently. */
	private ForkJoinPool pool;

	/**
	 * Constructor.
	 */
	public TaskPool() {
		super();
	}

	/**
	 * Add a task.
	 * 
	 * @param task The task to add.
	 */
	public void addTask(Task task) {
		task.setParent(this);
		tasks.add(task);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		for (Task task : tasks) {
			task.cancel(mayInterruptIfRunning);
		}
		return super.cancel(mayInterruptIfRunning);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Void call() throws Exception {
		for (Task task : tasks) {
			task.requestTotalWork();
		}
		pool = new ForkJoinPool(tasks.size());
		pool.invoke(new Executor());
		pool.shutdown();
		return null;
	}
}
