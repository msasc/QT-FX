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

package other;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

/**
 * Test grid pane with column constraints.
 * 
 * @author Miquel Sas
 */
public class TestGridPane extends Application {

	public static void main(String[] args) {
		launch(args);
	}
	
	boolean status = true;

	@Override
	public void start(Stage stage) throws Exception {
		GridPane grid = new GridPane();
		
		ColumnConstraints c0 = new ColumnConstraints();
		c0.setHgrow(Priority.ALWAYS);
		c0.setMaxWidth(Double.MAX_VALUE);
		grid.getColumnConstraints().add(c0);
		
		Label label = new Label("Label");
		HBox hbox = new HBox();
		hbox.setPadding(new Insets(0, 10, 0, 10));
		hbox.getChildren().add(label);
		grid.add(hbox, 0, 0);
		
		Button button = new Button("Button");
		grid.add(button, 1, 0);
		
		button.setOnAction((EventHandler<ActionEvent>) e -> {
			if (status) {
				grid.getColumnConstraints().get(0).setHgrow(Priority.NEVER);
				grid.getColumnConstraints().get(0).setMaxWidth(-1);
			} else {
				grid.getColumnConstraints().get(0).setHgrow(Priority.ALWAYS);
				grid.getColumnConstraints().get(0).setMaxWidth(Double.MAX_VALUE);
			}
			status = !status;
			grid.requestLayout();
		});
		
		Pane pane = new Pane();
		pane.getChildren().add(grid);
		grid.prefWidthProperty().bind(Bindings.selectDouble(grid.parentProperty(), "width"));
		grid.prefHeightProperty().bind(Bindings.selectDouble(grid.parentProperty(), "height"));
		
		Scene scene = new Scene(pane);
		stage.setScene(scene);
		stage.show();

	}

}
