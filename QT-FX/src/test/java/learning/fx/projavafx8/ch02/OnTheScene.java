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
 * OnTheSceneMain.java - A JavaFX Script example program that demonstrates how to use the Scene class in JavaFX, and
 * displays many of the properties' values as the Scene is manipulated by the user.
 *
 * Developed 2011 by James L. Weaver jim.weaver [at] javafxpert.com as a JavaFX SDK 2.0 example for the Pro JavaFX book.
 */
package learning.fx.projavafx8.ch02;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 * The OnTheSceneMain with my layout.
 *
 * @author Miquel Sas
 */
public class OnTheScene extends Application {

	public static void main(String[] args) {
		Application.launch(args);
	}
	
	@Override
	public void start(Stage stage) {
		
		// The VBox that is the root.
		VBox vboxRoot = new VBox(10);
		vboxRoot.setPadding(new Insets(20, 20, 20, 20));

		// A HBox that handles the vertical slide and a rectangle which gray will be set by the slide.
		HBox hboxBrightness = new HBox(20);
		hboxBrightness.setPadding(new Insets(20, 20, 20, 20));
		Slider sliderBrightness = new Slider(0, 255, 255);
		sliderBrightness.setOrientation(Orientation.VERTICAL);
		Rectangle rectBrightness = new Rectangle(0,0,100,100);
		rectBrightness.setFill(Color.BLACK);
		rectBrightness.setOpacity(100);
		HBox.setHgrow(rectBrightness, Priority.ALWAYS);
//		rectBrightness.widthProperty().bind(hboxBrightness.widthProperty());
//		rectBrightness.heightProperty().bind(hboxBrightness.heightProperty());
		hboxBrightness.getChildren().add(sliderBrightness);
		hboxBrightness.getChildren().add(rectBrightness);
		
		vboxRoot.getChildren().add(hboxBrightness);

		// Mouse choice.
//		ChoiceBox<Cursor> choiceBox = new ChoiceBox<>(getCursors());

		// An HBox to hold together the slider and the choice box.
//		HBox hbox = new HBox(slider, choiceBox);
//		hbox.setSpacing(10);

		// Texts for scene x and y coordinates and width and height.
//		Text textX = new Text();
//		textX.getStyleClass().add("emphasized-text");
//		Text textY = new Text();
//		textY.getStyleClass().add("emphasized-text");
//		Text textWidth = new Text();
//		textWidth.getStyleClass().add("emphasized-text");
//		Text textHeight = new Text();
//		textHeight.getStyleClass().add("emphasized-text");

		// Root flow pane.
//		FlowPane flowPane = new FlowPane(Orientation.VERTICAL, 20, 10);
//		flowPane.setPadding(new Insets(20, 20, 20, 20));
//		flowPane.setColumnHalignment(HPos.LEFT);
//		flowPane.setLayoutX(20);
//		flowPane.setLayoutY(20);
		
		// Add components.
//		flowPane.getChildren().add(hbox);
//		flowPane.getChildren().add(textX);
//		flowPane.getChildren().add(textY);
//		flowPane.getChildren().add(textWidth);
//		flowPane.getChildren().add(textHeight);

		Scene scene = new Scene(vboxRoot, 600, 400);
		stage.setScene(scene);
		stage.setTitle("On the Scene");
		stage.show();
	}
}
