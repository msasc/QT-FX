package other;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

import javafx.concurrent.Task;

public class TestTask {

	public static void main(String[] args) {
		
		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				return null;
			}
		};
		
		ForkJoinPool.commonPool().submit(task);

	}

}
