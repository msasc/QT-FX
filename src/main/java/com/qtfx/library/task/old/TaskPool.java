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

package com.qtfx.library.task.old;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

import com.qtfx.library.util.TextServer;

/**
 * A task that executes a pool of tasks concurrently.
 *
 * @author Miquel Sas
 */
public class TaskPool extends Task {

	/**
	 * Run adapter.
	 */
	class Run extends TaskAdapter {

		Run(Task task) {
			super(task);
		}

		@Override
		protected boolean exec() {
			getTask().run();
			return true;
		}
	}

	/**
	 * Count adapter.
	 */
	class Count extends TaskAdapter {

		Count(Task task) {
			super(task);
		}

		@Override
		protected boolean exec() {
			getTask().requestTotalWork();
			return true;
		}
	}

	/**
	 * Recursive task runner.
	 */
	class Runner extends RecursiveAction {
		@Override
		protected void compute() {
			List<ForkJoinTask<?>> calls = new ArrayList<>();
			for (Task task : tasks) {
				calls.add(new Run(task));
			}
			invokeAll(calls);
		}
	}

	/**
	 * Recursive task counter.
	 */
	class Counter extends RecursiveAction {
		@Override
		protected void compute() {
			List<ForkJoinTask<?>> calls = new ArrayList<>();
			for (Task task : tasks) {
				calls.add(new Count(task));
			}
			invokeAll(calls);
		}
	}

	/** The list of tasks to execute. */
	private List<Task> tasks = new ArrayList<>();
	/** The pool to execute tasks concurrently. */
	private ForkJoinPool pool;
	/** Parallelism. */
	private int parallelism = 20;

	/**
	 * Constructor.
	 */
	public TaskPool() {
		super();
	}

	/**
	 * Constructor assigning the locale.
	 * 
	 * @param locale The locale.
	 */
	public TaskPool(Locale locale) {
		super(locale);
	}

	/**
	 * Add a task.
	 * 
	 * @param task The task to add.
	 */
	public void addTask(Task task) {
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
	 * Return the list of tasks.
	 * 
	 * @return The list of tasks.
	 */
	public List<Task> getTasks() {
		return tasks;
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
		for (Task task : tasks) {
			flag = task.cancel(mayInterruptIfRunning);
		}
		return flag;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Void call() throws Exception {
		updateCounting();
		pool = new ForkJoinPool(parallelism);
		pool.invoke(new Counter());
		updateMessage(TextServer.getString("taskParallel", getLocale()));
		pool.invoke(new Runner());
		pool.shutdown();
		return null;
	}
}
