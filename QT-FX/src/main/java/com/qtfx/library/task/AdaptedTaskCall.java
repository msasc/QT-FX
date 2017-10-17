package com.qtfx.library.task;

import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RunnableFuture;

/**
 * Adaptor for tasks to run the main call.
 */
final class AdaptedTaskCall extends ForkJoinTask<Void> implements RunnableFuture<Void> {
	final Task task;

	AdaptedTaskCall(Task task) {
		if (task == null)
			throw new NullPointerException();
		this.task = task;
	}

	public final Void getRawResult() {
		return null;
	}

	public final void setRawResult(Void v) {
	}

	public final boolean exec() {
		task.run();
		return true;
	}

	public final void run() {
		invoke();
	}
}