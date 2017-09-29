/*
 * Copyright (c) 2011, Pro JavaFX Authors All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met: 1. Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer. 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation and/or other materials provided with the
 * distribution. 3. Neither the name of JFXtras nor the names of its contributors may be used to endorse or promote
 * products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * StageCoachMain.java - A JavaFX example program that demonstrates how to use the Stage class in JavaFX, and displays
 * many of the properties' values as the Stage is manipulated by the user. It also demonstrates how to get arguments
 * passed into the program.
 *
 * Developed 2011 by James L. Weaver jim.weaver [at] javafxpert.com as a JavaFX SDK 2.0 example for the Pro JavaFX book.
 */
package learning.fx.projavafx8.ch02;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

/**
 * StageCoach modified by me, laying in a grid and commenting features.
 *
 * @author Miquel Sas
 *
 */
public class StageCoachGrid extends Application {

	public static void main(String[] args) {
		Application.launch(args);
	}

	double dragAnchorX = 0;
	double dragAnchorY = 0;

	@Override
	public void start(Stage stage) {

		// Stage style
		stage.initStyle(StageStyle.DECORATED);

		// Layout grid
		GridPane grid = new GridPane();
		grid.setBackground(Background.EMPTY);
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		// Title property that will be edited and binded to the state title. Define a label and a text field, and bind
		// the text field.
		StringProperty title = new SimpleStringProperty();
		Label titleLabel = new Label("State title");
		TextField titleTextField = new TextField("Stage Coach");
		title.bind(titleTextField.textProperty());
		stage.titleProperty().bind(title);
		grid.add(titleLabel, 0, 0);
		grid.add(titleTextField, 1, 0);

		// Stage position. Set a label and a text and bind the text with x,y state positions.
		Label positionLabel = new Label("Position");
		Text positionText = new Text();
		positionText.textProperty().bind(
			new SimpleStringProperty("x = ")
				.concat(stage.xProperty().asString())
				.concat("  y = ")
				.concat(stage.yProperty().asString()));
		grid.add(positionLabel, 0, 1);
		grid.add(positionText, 1, 1);

		// Stage size. Set a label and a text and bind the text with the state size.
		Label sizeLabel = new Label("Size");
		Text sizeText = new Text();
		sizeText.textProperty().bind(
			new SimpleStringProperty("width = ")
				.concat(stage.widthProperty().asString())
				.concat("  height = ")
				.concat(stage.heightProperty().asString()));
		grid.add(sizeLabel, 0, 2);
		grid.add(sizeText, 1, 2);

		// Focused property.
		Label focusLabel = new Label("Focused");
		Text focusText = new Text();
		focusText.textProperty().bind(new SimpleStringProperty("").concat(stage.focusedProperty().asString()));
		grid.add(focusLabel, 0, 3);
		grid.add(focusText, 1, 3);

		// Resizable property.
		Label resizeblaLabel = new Label("Resizable");
		CheckBox resizableCheckBox = new CheckBox();
		resizableCheckBox.selectedProperty().bindBidirectional(stage.resizableProperty());
		grid.add(resizeblaLabel, 0, 4);
		grid.add(resizableCheckBox, 1, 4);

		// Full screen property
		Label fullScreenLabel = new Label("Full screen");
		CheckBox fullScreenCheckBox = new CheckBox();
		fullScreenCheckBox.selectedProperty().addListener((ov, oldValue, newValue) -> {
			stage.setFullScreen(fullScreenCheckBox.selectedProperty().getValue());
		});

		grid.add(fullScreenLabel, 0, 5);
		grid.add(fullScreenCheckBox, 1, 5);

		Rectangle blue = new Rectangle();
		blue.setFill(Color.SKYBLUE);
		blue.setArcHeight(50);
		blue.setArcWidth(50);
		Group rootGroup = new Group(blue, grid);

		// Respond when mouse is dragged on the root.
		rootGroup.setOnMousePressed((MouseEvent me) -> {
			dragAnchorX = me.getScreenX() - stage.getX();
			dragAnchorY = me.getScreenY() - stage.getY();
		});
		rootGroup.setOnMouseDragged((MouseEvent me) -> {
			stage.setX(me.getScreenX() - dragAnchorX);
			stage.setY(me.getScreenY() - dragAnchorY);
		});
		
		// Main scene
		Scene scene = new Scene(rootGroup, 400, 400);
		scene.setFill(Color.TRANSPARENT);
		stage.setScene(scene);
		stage.setOnCloseRequest((WindowEvent we) -> {
			System.out.println("Stage is closing");
		});
		
		blue.widthProperty().bind(scene.widthProperty());
		blue.heightProperty().bind(scene.heightProperty());
		
		stage.show();
	}

}
