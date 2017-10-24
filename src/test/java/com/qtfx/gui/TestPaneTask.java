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
public class TestPaneTask extends Application {

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

		SampleTask task = new SampleTask();
		task.setTitle("Title of the task");
		task.setIterations(1000000000);
		task.setSleep(-1);
		task.setModule(10000);
		task.setIndeterminate(false);
//		task.setThrowException(true);
//		task.setThrowAfterIterations(200000000);

		TaskPane taskPane = new TaskPane(task);

		Scene scene = new Scene(taskPane);
		stage.setScene(scene);
		stage.show();
	}

}
