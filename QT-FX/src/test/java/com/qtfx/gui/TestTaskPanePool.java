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

package com.qtfx.gui;

import com.qtfx.library.gui.TaskPane;
import com.qtfx.library.task.TaskPool;
import com.qtfx.library.task.sample.SampleTask;
import com.qtfx.library.util.TextServer;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Test the task pane with a test Task.
 * 
 * @author Miquel Sas
 */
public class TestTaskPanePool extends Application {

	/** Logger configuration. */
	static {
		System.setProperty("log4j.configurationFile", "resources/LoggerQTPlatform.xml");
	}

	public static void main(String[] args) {
		TextServer.addBaseResource("resources/StringsLibrary.xml");
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		
		TaskPool taskPool = new TaskPool();
		taskPool.setTitle("Task pool");
		taskPool.setProgressDecimals(1);
		taskPool.setParallelism(20);
		
		int taskCount = 10;
		for (int i = 0; i < taskCount; i++) {
			SampleTask task = new SampleTask();
			task.setTitle("Title of the task");
			task.setIterations(100000000);
			task.setSleep(-1);
			task.setModule(1000);
			task.setIndeterminate(false);
			taskPool.addTask(task);
		}

		TaskPane taskPane = new TaskPane(taskPool);

		Scene scene = new Scene(taskPane);
		stage.setScene(scene);
		stage.show();
	}

}
