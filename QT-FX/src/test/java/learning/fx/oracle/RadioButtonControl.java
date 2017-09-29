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

package learning.fx.oracle;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * Test radio button controls.
 * 
 * @author Miquel Sas
 */
public class RadioButtonControl extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		GridPane grid = new GridPane();
		grid.setBackground(Background.EMPTY);
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		ToggleGroup group = new ToggleGroup();

		RadioButton rbBrowse = new RadioButton("Browse");
		rbBrowse.setToggleGroup(group);
		rbBrowse.setUserData("browse.png");

		RadioButton rbCancel = new RadioButton("Cancel");
		rbCancel.setToggleGroup(group);
		rbCancel.setUserData("cancel.png");

		RadioButton rbChart = new RadioButton("Chart");
		rbChart.setToggleGroup(group);
		rbChart.setUserData("chart.png");

		grid.add(rbBrowse, 0, 0);
		grid.add(rbCancel, 0, 1);
		grid.add(rbChart, 0, 2);

		ImageView icon = new ImageView();
		grid.add(icon, 1, 0, 1, 3);

		group.selectedToggleProperty().addListener(
			(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) -> {
				if (group.getSelectedToggle() != null) {
					Image image =
						new Image(
							getClass().getResourceAsStream(
								group.getSelectedToggle().getUserData().toString()));
					icon.setImage(image);
				}
			});	
		Scene scene = new Scene(grid, 400, 400);
		primaryStage.setScene(scene);
		primaryStage.setTitle("RadioButton controls");
		primaryStage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}

}
