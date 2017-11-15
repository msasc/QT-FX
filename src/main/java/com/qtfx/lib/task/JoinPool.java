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

import java.util.Collection;
import java.util.concurrent.ForkJoinPool;

/**
 * An extension of the <em>ForkJoinPool</em> with helpers to work with tasks.
 *
 * @author Miquel Sas
 */
public class JoinPool extends ForkJoinPool {

	/**
	 * Runner.
	 */
	public static class Runner extends TaskAdapter {
		Collection<? extends Task> tasks;

		public Runner(Collection<? extends Task> tasks) {
			super();
			this.tasks = tasks;
		}

		@Override
		protected void compute() {
			invokeAll(tasks);
		}
	}

	/**
	 * Default constructor.
	 */
	public JoinPool() {
		super();
	}

	/**
	 * Constructor assigning the degree of parallelism.
	 * 
	 * @param parallelism The degree of parallelism.
	 */
	public JoinPool(int parallelism) {
		super(parallelism);
	}

	/**
	 * Invoke a collection of tasks.
	 * 
	 * @param tasks The collection of tasks.
	 */
	public void invokeTasks(Collection<? extends Task> tasks) {
		invoke(new Runner(tasks));
	}
}
