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

/**
 * A task that sequentially executes a list of tasks.
 *
 * @author Miquel Sas
 */
public class TaskList extends Task {

	/** The list of tasks to execute. */
	private List<Task> tasks = new ArrayList<>();

	/**
	 * Default constructor.
	 */
	public TaskList() {
		super();
	}

	/**
	 * Constructor assigning the locale.
	 * 
	 * @param locale The locale.
	 */
	public TaskList(Locale locale) {
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
	 * {@inheritDoc}
	 */
	@Override
	protected Void call() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
