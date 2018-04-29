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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import com.qtfx.lib.app.Session;

/**
 * A task that executes other tasks in a concurrent pool. If one of the tasks of the pool is indeterminate, then all are
 * considered indeterminate.
 *
 * @author Miquel Sas
 */
public class TaskPool extends Task {

	/**
	 * Counter task.
	 */
	class Counter extends Task {
		TaskRun task;

		Counter(TaskRun task) {
			super(task.getSession());
			this.task = task;
		}

		@Override
		protected void compute() {
			task.setTotalWork(task.requestTotalWork());
			getLock().lock();
			try {
				TaskPool.this.setTotalWork(TaskPool.this.getTotalWork() + task.getTotalWork());
				TaskPool.this.updateProgressMessage();
				TaskPool.this.updateTimeMessage(true);
			} finally {
				getLock().unlock();
			}
		}

		@Override
		public boolean isIndeterminate() {
			return false;
		}
	}

	/** List of tasks to run concurrently. */
	private List<TaskRun> tasks = new ArrayList<>();
	/** Lock used by tasks that run in this parent pool. */
	private ReentrantLock lock = new ReentrantLock();
	/** Parallelism. */
	private int parallelism = Runtime.getRuntime().availableProcessors();
	/** Pool. */
	private JoinPool pool;
	/** Indeterminate flag. */
	private Boolean indeterminate = null;

	/**
	 * Constructor assigning the locale for messages.
	 * 
	 * @param session The working session.
	 */
	public TaskPool(Session session) {
		super(session);
		setStateChangeListener();
	}

	/**
	 * Set the state change listener. When a task fails with an exception, the task pool issues a cancel. this listener
	 * scans the tasks and if one has failed sets the state of this parent pool to failed also.
	 */
	private void setStateChangeListener() {
		stateProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.equals(State.CANCELLED)) {
				for (TaskRun task : tasks) {
					if (task.stateProperty().get().equals(State.FAILED)) {
						setState(State.FAILED);
					}
				}
			}
		});
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
	 * Return the parallelism or number of concurrent threads.
	 * 
	 * @return The parallelism.
	 */
	public int getParallelism() {
		return parallelism;
	}

	/**
	 * Set the parallelism or number of concurrent threads.
	 * 
	 * @param parallelism The parallelism.
	 */
	public void setParallelism(int parallelism) {
		this.parallelism = parallelism;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		boolean flag = super.cancel(mayInterruptIfRunning);
		for (TaskRun task : tasks) {
			flag = task.cancel(mayInterruptIfRunning);
		}
		pool.shutdown();
		return flag;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void reinitialize() {
		indeterminate = null;
		tasks.forEach(t -> t.reinitialize());
		super.reinitialize();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isIndeterminate() {
		if (indeterminate == null) {
			indeterminate = false;
			for (Task taskTest : tasks) {
				if (taskTest.isIndeterminate()) {
					indeterminate = true;
					break;
				}
			}
		}
		return indeterminate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void updateProgress(double workDone, double totalWork) {
		super.updateProgress(workDone, totalWork);
		updateProgressMessage();
		updateTimeMessage();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void compute() {

		// Instantiate the pool.
		pool = new JoinPool(parallelism);

		// Reinitialize all tasks.
		reinitialize();

		// Check indeterminate.
		if (isIndeterminate()) {
			// Tag all to act as indeterminate.
			tasks.forEach(task -> task.setIndeterminate(true));
		} else {
			setWorkDone(0);
			setTotalWork(0);
			// Count.
			List<Counter> counters = new ArrayList<>();
			tasks.forEach(task -> counters.add(new Counter(task)));
			updateCounting();
			pool.invokeTasks(counters);
		}

		// Do process tasks.
		updateMessage(getSession().getString("taskParallel"));
		pool.invokeTasks(tasks);
		pool.shutdown();
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
