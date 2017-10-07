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

package learning.fx.projavafx8.ch06;

import java.util.Map;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author Miquel Sas
 *
 */
public class ThreadsExample extends Application implements EventHandler<ActionEvent>, 	ChangeListener<Number> {

	static class Model {
		ObservableList<String> threadNames;
		ObservableList<String> stackTraces;

		Model() {
			threadNames = FXCollections.observableArrayList();
			stackTraces = FXCollections.observableArrayList();
			update();
		}

		void update() {
			threadNames.clear();
			stackTraces.clear();
			Map<Thread, StackTraceElement[]> map = Thread.getAllStackTraces();
			for (Map.Entry<Thread, StackTraceElement[]> entry : map.entrySet()) {
				threadNames.add("\"" + entry.getKey().getName() + "\"");
				stackTraces.add(formatStackTrace(entry.getValue()));
			}
		}

		private String formatStackTrace(StackTraceElement[] elements) {
			StringBuilder b = new StringBuilder("Stack trace:\n");
			for (StackTraceElement e : elements) {
				b.append(" at").append(e.toString()).append("\n");
			}
			return b.toString();
		}
	}

	private Model model;
	ListView<String> threadNames;
	TextArea stackTrace;

	@Override
	public void start(Stage stage) throws Exception {

		model = new Model();
		threadNames = new ListView<>(model.threadNames);
		stackTrace = new TextArea();
		Button updateButton = new Button("Update");
		
		updateButton.setOnAction(this);
		threadNames.getSelectionModel().selectedIndexProperty().addListener(this);

		VBox vbox = new VBox(10);
		vbox.setPadding(new Insets(10, 10, 10, 10));
		vbox.getChildren().addAll(threadNames, stackTrace, updateButton);

		Scene scene = new Scene(vbox, 440, 640);
		stage.setScene(scene);
		stage.show();
	}

	@Override
	public void handle(ActionEvent event) {
		model.update();
	}

	@Override
	public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
		int index = newValue.intValue();
		stackTrace.setText(model.stackTraces.get(index));
	}

	public static void main(String[] args) {
		launch(args);
	}
}
