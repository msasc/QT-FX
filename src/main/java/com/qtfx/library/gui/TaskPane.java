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

package com.qtfx.library.gui;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.ForkJoinPool;

import com.qtfx.library.task.State;
import com.qtfx.library.task.Task;
import com.qtfx.library.util.Icons;
import com.qtfx.library.util.TextServer;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

/**
 * A pane to show the progress of a task
 * 
 * @author Miquel Sas
 */
public class TaskPane extends Pane {

	/** The task. */
	private Task task;

	/**
	 * Constructor assigning the task.
	 * 
	 * @param task The task.
	 */
	public TaskPane(Task task) {
		super();
		this.task = task;
		layoutComponents();
	}

	private void layoutComponents() {

		// Remove any component.
		while (!getChildren().isEmpty()) {
			getChildren().remove(0);
		}

		// The grid pane that will hold all the component.
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(10, 10, 10, 10));

		// Column 0 will expand.
		ColumnConstraints c0 = new ColumnConstraints();
		c0.setHgrow(Priority.ALWAYS);
		c0.setMaxWidth(Double.MAX_VALUE);
		grid.getColumnConstraints().add(c0);

		// Row.
		int row = 0;

		// Title label.
		Label labelTitle = new Label();
		labelTitle.setStyle("-fx-font-weight: bold;");
		labelTitle.textProperty().bind(task.titleProperty());
		grid.add(labelTitle, 0, row);

		// Node 1, 0: buttons in an HBox.
		HBox hboxButtons = new HBox(5);

		// Execute button.
		Button buttonAction = new Button();
		buttonAction.setDefaultButton(false);
		buttonAction.setCancelButton(false);
		buttonAction.setGraphic(Icons.get(Icons.FLAT_24x24_EXECUTE));
		buttonAction.setPadding(new Insets(0, 0, 0, 0));
		buttonAction.setTooltip(new Tooltip(TextServer.getString("tooltipStart")));
		buttonAction.setStyle("-fx-content-display: graphic-only;");
		hboxButtons.getChildren().add(buttonAction);

		// Info button.
		Button buttonInfo = new Button();
		buttonInfo.setDefaultButton(false);
		buttonInfo.setCancelButton(false);
		buttonInfo.setGraphic(Icons.get(Icons.FLAT_24x24_INFO));
		buttonInfo.setPadding(new Insets(0, 0, 0, 0));
		buttonInfo.setTooltip(new Tooltip(TextServer.getString("tooltipErrorInfo")));
		buttonInfo.setStyle("-fx-content-display: graphic-only;");
		buttonInfo.setDisable(true);
		hboxButtons.getChildren().add(buttonInfo);

		// Add the buttons box.
		grid.add(hboxButtons, 1, row++);

		// Task message.
		Label labelMessage = new Label();
		labelMessage.setMaxWidth(Double.MAX_VALUE);
		labelMessage.textProperty().bind(task.messageProperty());
		grid.add(labelMessage, 0, row++, 2, 1);

		// Progress message.
		Label labelMessageProgress = new Label("Hello message progress");
		labelMessageProgress.setMaxWidth(Double.MAX_VALUE);
		labelMessageProgress.textProperty().bind(task.progressMessageProperty());
		grid.add(labelMessageProgress, 0, row++, 2, 1);

		// Time message.
		Label labelMessageTime = new Label();
		labelMessageTime.setMaxWidth(Double.MAX_VALUE);
		labelMessageTime.textProperty().bind(task.timeMessageProperty());
		grid.add(labelMessageTime, 0, row++, 2, 1);

		// State message.
		Label labelState = new Label();
		labelState.setMaxWidth(Double.MAX_VALUE);
		labelState.textProperty().bind(new SimpleStringProperty("State: ").concat(task.stateProperty().asString()));
		grid.add(labelState, 0, row++, 2, 1);

		// Fifth line is for the progress bar.
		ProgressBar progressBar = new ProgressBar();
		progressBar.setMaxWidth(Double.MAX_VALUE);
		progressBar.setProgress(0);
		grid.add(progressBar, 0, row++, 2, 1);

		// Setup button action listener.
		buttonAction.setOnAction((EventHandler<ActionEvent>) e -> {

			// When ready the button action submits the task and changes to cancel.
			if (!task.stateProperty().get().equals(State.RUNNING)) {
				progressBar.progressProperty().bind(task.progressProperty());
				buttonAction.setGraphic(Icons.get(Icons.FLAT_24x24_CANCEL));
				buttonAction.setTooltip(new Tooltip(TextServer.getString("tooltipCancel")));

				task.reinitialize();
				ForkJoinPool.commonPool().submit(task);
			}

			// When running, the button action cancels the current task.
			if (task.stateProperty().get().equals(State.RUNNING)) {
				task.cancel();
			}

		});

		// Setup button info listener.
		buttonInfo.setOnAction((EventHandler<ActionEvent>) e -> {
			showException();
		});

		// Setup task state listener.
		task.stateProperty().addListener((observable, oldValue, newValue) -> {

			// Leaving the running state.
			if (oldValue.equals(State.RUNNING)) {

				// If the task is indeterminate, unbind the progress bar to set the value to zero and stop flowing.
				if (task.isIndeterminate()) {
					progressBar.progressProperty().unbind();
					progressBar.setProgress(0);
				}

				// Failed throwing an exception, enable the info button.
				if (newValue.equals(State.FAILED)) {
					buttonInfo.setDisable(false);
				}

				// Set the action button to restart.
				buttonAction.setGraphic(Icons.get(Icons.FLAT_24x24_EXECUTE));
				buttonAction.setTooltip(new Tooltip(TextServer.getString("tooltipStart")));

			}

		});

		// Add the grid to this pane and bind its width and height.
		getChildren().add(grid);
		grid.prefWidthProperty().bind(Bindings.selectDouble(grid.parentProperty(), "width"));
		grid.prefHeightProperty().bind(Bindings.selectDouble(grid.parentProperty(), "height"));

		// Preferred pane width.
		setPrefWidth(800);
	}

	/**
	 * Show the exception if any.
	 */
	private void showException() {

		Dialog dialog = new Dialog(getScene().getWindow());
		dialog.setButtonsBottom();
		dialog.setTitle(TextServer.getString("taskException"));
		dialog.getOptionPane().getOptions().add(Option.ok());

		dialog.show();
	}

	/**
	 * Return all the stack trace of the throwable.
	 * 
	 * @param e The throwable.
	 * @return The stack trace.
	 */
	private String getStackTrace(Throwable e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		return sw.toString();
	}
}
