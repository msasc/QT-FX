package com.qtfx.library.task.old;

import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RunnableFuture;

/**
 * Adapter for tasks to be a <code>ForkJoinTask<Void></code>. The <code>exec()</code> method must be overwritten.
 * 
 * @author Miquel Sas
 */
public abstract class TaskAdapter extends ForkJoinTask<Void> implements RunnableFuture<Void> {

	/** The task. */
	private Task task;

	/**
	 * Constructor.
	 * 
	 * @param task The task.
	 */
	public TaskAdapter(Task task) {
		if (task == null) {
			throw new NullPointerException();
		}
		this.task = task;
	}

	/**
	 * Return the internal task.
	 * 
	 * @return The task.
	 */
	protected Task getTask() {
		return task;
	}

	/**
	 * {@inheritDoc}
	 */
	public final Void getRawResult() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public final void setRawResult(Void v) {
	}

	/**
	 * {@inheritDoc}
	 */
	public final void run() {
		invoke();
	}
}