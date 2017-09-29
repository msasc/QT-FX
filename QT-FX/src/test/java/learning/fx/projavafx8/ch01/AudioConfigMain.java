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
 * HelloEarthRiseMain.java - A JavaFX "Hello World" style example
 *
 * Developed 2011 by James L. Weaver jim.weaver [at] javafxpert.com as a JavaFX SDK 2.0 example for the Pro JavaFX book.
 */
package learning.fx.projavafx8.ch01;

import javafx.application.Application;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AudioConfigMain extends Application {

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage stage) {

		// Title text
		Text title = new Text(65, 12, "Audio Configuration");
		title.setTextOrigin(VPos.TOP);
		title.setFill(Color.WHITE);
		title.setFont(Font.font("SansSerif", FontWeight.BOLD, 20));

		// Text to show the decibels
		Text textDb = new Text();
		textDb.setLayoutX(18);
		textDb.setLayoutY(69);
		textDb.setTextOrigin(VPos.TOP);
		textDb.setFill(Color.web("#131021"));
		textDb.setFont(Font.font("SansSerif", FontWeight.BOLD, 18));

		// Slider
		Slider slider = new Slider();
		slider.setLayoutX(135);
		slider.setLayoutY(69);
		slider.setPrefWidth(162);
		slider.setMin(0);
		slider.setMax(160);

		// Muting text
		Text mutingText = new Text(18, 113, "Muting");
		mutingText.setTextOrigin(VPos.TOP);
		mutingText.setFont(Font.font("SanSerif", FontWeight.BOLD, 18));
		mutingText.setFill(Color.web("#131021"));

		// Muting check box
		CheckBox mutingCheckBox = new CheckBox();
		mutingCheckBox.setLayoutX(280);
		mutingCheckBox.setLayoutY(113);

		// Genre text
		Text genreText = new Text(18, 154, "Genre");
		genreText.setTextOrigin(VPos.TOP);
		genreText.setFill(Color.web("#131021"));
		genreText.setFont(Font.font("SanSerif", FontWeight.BOLD, 18));

		// Genre choice box
		ChoiceBox<String> genreChoiceBox = new ChoiceBox<>();
		genreChoiceBox.setLayoutX(204);
		genreChoiceBox.setLayoutY(154);
		genreChoiceBox.setPrefWidth(93);
		ObservableList<String> genres =
			FXCollections.observableArrayList(
				"Chamber",
				"Country",
				"Cowbell",
				"Metal",
				"Polka",
				"Rock");
		genreChoiceBox.setItems(genres);

		Stop[] stops = new Stop[] { new Stop(0, Color.web("0xAEBBCC")), new Stop(1, Color.web("0x6D84A3")) };
		LinearGradient linearGradient = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops);
		
		Rectangle rectangle = new Rectangle(0, 0, 320, 45);
		rectangle.setFill(linearGradient);

		Rectangle rectangle2 = new Rectangle(0, 43, 320, 300);
		rectangle2.setFill(Color.rgb(199, 206, 213));

		Rectangle rectangle3 = new Rectangle(8, 54, 300, 140);
		rectangle3.setArcHeight(20);
		rectangle3.setArcWidth(20);
		rectangle3.setFill(Color.WHITE);
		rectangle3.setStroke(Color.color(0.66, 0.67, 0.69));

		Line line1 = new Line(9, 97, 309, 97);
		line1.setStroke(Color.color(0.66, 0.67, 0.69));

		Line line2 = new Line(9, 141, 309, 141);
		line2.setFill(Color.color(0.66, 0.67, 0.69));

		Group group =
			new Group(
				rectangle,
				title,
				rectangle2,
				rectangle3,
				textDb,
				slider,
				line1,
				mutingText,
				mutingCheckBox,
				line2,
				genreText,
				genreChoiceBox);
		Scene scene = new Scene(group, 320, 343);

		// Bind slider and text
		IntegerProperty selectedDBs = new SimpleIntegerProperty(0);
		textDb.textProperty().bind(selectedDBs.asString().concat(" dB"));

		// Bind slider and muting.
		BooleanProperty muting = new SimpleBooleanProperty(false);
		slider.disableProperty().bind(muting);
		mutingCheckBox.selectedProperty().bindBidirectional(muting);

		// Bind the genre selection and the slice with a lamba listener.
		genreChoiceBox.getSelectionModel().selectedIndexProperty().addListener((Observable o) -> {
			int selectedIndex = genreChoiceBox.getSelectionModel().selectedIndexProperty().getValue();
			switch (selectedIndex) {
			case 0:
				selectedDBs.setValue(80);
				break;
			case 1:
				selectedDBs.setValue(100);
				break;
			case 2:
				selectedDBs.setValue(150);
				break;
			case 3:
				selectedDBs.setValue(140);
				break;
			case 4:
				selectedDBs.setValue(120);
				break;
			case 5:
				selectedDBs.setValue(130);
			}
		});
		slider.valueProperty().bindBidirectional(selectedDBs);

		genreChoiceBox.getSelectionModel().selectFirst();

		stage.setScene(scene);
		stage.setTitle("Audio Configuration");
		stage.show();
	}
}
