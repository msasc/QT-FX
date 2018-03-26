package com.qtfx.lib.gui;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import com.qtfx.lib.task.JoinPool;
import com.qtfx.lib.task.State;
import com.qtfx.lib.task.Task;
import com.qtfx.lib.util.Icons;
import com.qtfx.lib.util.TextServer;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * A task pane to execute several tasks.
 *
 * @author Miquel Sas
 */
public class TaskPane {

	/**
	 * Return the task pane given the node (border pane of the task pane.)
	 * 
	 * @param node The node.
	 * @return The task pane.
	 */
	public static TaskPane getTaskPane(Node node) {
		return (TaskPane) FX.getObject(node, "task-pane");
	}

	/** Border pane. */
	private BorderPane borderPane;
	/** Buttons pane. */
	private ButtonPane buttonPane;
	/** Master vertical box. */
	private VBox vbox;
	/** Execution pool. */
	private JoinPool pool;

	/**
	 * Constructor.
	 */
	public TaskPane() {
		super();
		borderPane = new BorderPane();
		FX.setObject(borderPane, "task-pane", this);
		
		vbox = new VBox(10);
		ScrollPane scrollPane = new ScrollPane(vbox);
		borderPane.setCenter(scrollPane);
		
		scrollPane.prefWidthProperty().bind(Bindings.selectDouble(scrollPane.parentProperty(), "width"));
//		vbox.prefWidthProperty().bind(Bindings.selectDouble(vbox.parentProperty(), "width"));
		vbox.prefWidthProperty().bind(scrollPane.prefWidthProperty().subtract(20));
		
		buttonPane = new ButtonPane();
		buttonPane.setPadding(new Insets(10));
		borderPane.setBottom(buttonPane.getNode());

		Button remove = new Button(TextServer.getString("taskRemoveInactive"));
		remove.setOnAction(e -> {
			List<Task> tasks = getTasks();
			for (Task task : tasks) {
				if (!task.stateProperty().get().equals(State.RUNNING)) {
					removeTask(task);
				}
			}
		});
		buttonPane.getButtons().add(remove);
		
		buttonPane.layoutButtons();

		pool = new JoinPool();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void finalize() throws Throwable {
		pool.shutdown();
		super.finalize();
	}

	/**
	 * Return the node to install on the scene.
	 * 
	 * @return The node.
	 */
	public Node getNode() {
		return borderPane;
	}

	/**
	 * Add a task to be executed.
	 * 
	 * @param task The task.
	 */
	public void addTask(Task task) {
		vbox.getChildren().add(getTaskPane(task));
		vbox.getChildren().add(new Separator());
	}

	/**
	 * Return a list with all first level tasks.
	 * 
	 * @return The list of tasks.
	 */
	public List<Task> getTasks() {
		List<Task> tasks = new ArrayList<>();
		for (int i = 0; i < vbox.getChildren().size(); i++) {
			Node node = vbox.getChildren().get(i);
			if (node instanceof Separator) {
				continue;
			}
			Task task = (Task) FX.getObject(node, "task");
			tasks.add(task);
		}
		return tasks;
	}

	/**
	 * Remove the task.
	 * 
	 * @param task The task.
	 */
	public void removeTask(Task task) {
		for (int i = 0; i < vbox.getChildren().size(); i++) {
			Node node = vbox.getChildren().get(i);
			if (node instanceof Separator) {
				continue;
			}
			Task scan = (Task) FX.getObject(node, "task");
			if (scan.equals(task)) {
				vbox.getChildren().remove(i);
				break;
			}
		}
		List<Task> tasks = getTasks();
		vbox.getChildren().clear();
		for (Task t : tasks) {
			addTask(t);
		}
	}

	/**
	 * Return the grid pane that shows the task progress.
	 * 
	 * @param task The task.
	 * @return The pane.
	 */
	private GridPane getTaskPane(Task task) {

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
		buttonAction.setTooltip(new Tooltip(TextServer.getString("tooltipStartTask")));
		buttonAction.setStyle("-fx-content-display: graphic-only;");
		hboxButtons.getChildren().add(buttonAction);

		// Info button.
		Button buttonInfo = new Button();
		buttonInfo.setDefaultButton(false);
		buttonInfo.setCancelButton(false);
		buttonInfo.setGraphic(Icons.get(Icons.FLAT_24x24_INFO));
		buttonInfo.setPadding(new Insets(0, 0, 0, 0));
		buttonInfo.setTooltip(new Tooltip(TextServer.getString("tooltipErrorInfoTask")));
		buttonInfo.setStyle("-fx-content-display: graphic-only;");
		buttonInfo.setDisable(true);
		hboxButtons.getChildren().add(buttonInfo);

		// Info button.
		Button buttonClose = new Button();
		buttonClose.setDefaultButton(false);
		buttonClose.setCancelButton(false);
		buttonClose.setGraphic(Icons.get(Icons.FLAT_24x24_CLOSE));
		buttonClose.setPadding(new Insets(0, 0, 0, 0));
		buttonClose.setTooltip(new Tooltip(TextServer.getString("tooltipCloseTask")));
		buttonClose.setStyle("-fx-content-display: graphic-only;");
		buttonClose.setDisable(false);
		hboxButtons.getChildren().add(buttonClose);

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
				Platform.runLater(() -> {
					progressBar.progressProperty().bind(task.progressProperty());
					buttonAction.setGraphic(Icons.get(Icons.FLAT_24x24_CANCEL));
					buttonAction.setTooltip(new Tooltip(TextServer.getString("tooltipCancel")));
				});

				task.reinitialize();
				pool.submit(task);
			}

			// When running, the button action cancels the current task.
			if (task.stateProperty().get().equals(State.RUNNING)) {
				task.cancel();
			}

		});

		// Setup button info listener.
		buttonInfo.setOnAction((EventHandler<ActionEvent>) e -> {
			showException(task);
		});

		// Setup button close.
		buttonClose.setOnAction((EventHandler<ActionEvent>) e -> {
			if (task.stateProperty().get().equals(State.RUNNING)) {
				return;
			}
			removeTask(task);
		});

		// Setup task state listener.
		task.stateProperty().addListener((observable, oldValue, newValue) -> {
			
			// Entering the running state.
			if (newValue.equals(State.RUNNING)) {
				buttonClose.setDisable(true);
			}

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
				
				buttonClose.setDisable(false);
			}

		});

		FX.setObject(grid, "task", task);
		return grid;
	}

	///////////
	// Helpers.

	/**
	 * Show the exception if any.
	 */
	private void showException(Task task) {

		Throwable exc = task.getException();
		if (exc == null) {
			return;
		}

		Scene scene = vbox.getScene();
		Dialog dialog = new Dialog(scene != null ? scene.getWindow() : null);
		dialog.setButtonsBottom();
		dialog.setTitle(TextServer.getString("taskException"));
		dialog.getButtonPane().setPadding(new Insets(0, 10, 10, 10));
		dialog.getButtonPane().getButtons().add(Buttons.buttonOk());

		VBox vbox = new VBox(10);
		vbox.setPadding(new Insets(10, 10, 10, 10));
		dialog.setCenter(vbox);

		TextFlow message = new TextFlow();
		Text text = new Text(exc.getLocalizedMessage());
		text.setStyle("-fx-font-weight: bold");
		message.getChildren().add(text);
		vbox.getChildren().add(message);

		ScrollPane scrollPane = new ScrollPane(new TextFlow(new Text(getStackTrace(exc))));
		vbox.getChildren().add(scrollPane);

		dialog.getStage().setMaxHeight(600);
		dialog.getStage().setMaxWidth(800);
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
