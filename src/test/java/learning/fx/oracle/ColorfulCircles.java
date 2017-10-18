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
import static java.lang.Math.random;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.BoxBlur;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Colorful javafx example.
 * 
 * @author Miquel Sas
 */
public class ColorfulCircles extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Group root = new Group();
		Scene scene = new Scene(root, 600, 800, Color.BLACK);
		primaryStage.setScene(scene);

		// Color behind circles
		Rectangle colors =
			new Rectangle(
				scene.getWidth(),
				scene.getHeight(),
				new LinearGradient(
					0f,
					1f,
					1f,
					0f,
					true,
					CycleMethod.NO_CYCLE,
					new Stop[] {
						new Stop(0, Color.web("#f8bd55")),
						new Stop(0.14, Color.web("#c0fe56")),
						new Stop(0.28, Color.web("#5dfbc1")),
						new Stop(0.43, Color.web("#64c2f8")),
						new Stop(0.57, Color.web("#be4af7")),
						new Stop(0.71, Color.web("#ed5fc2")),
						new Stop(0.85, Color.web("#ef504c")),
						new Stop(1, Color.web("#f2660f")), }));
		colors.widthProperty().bind(scene.widthProperty());
		colors.heightProperty().bind(scene.heightProperty());

		// Circles
		Group circles = new Group();
		for (int i = 0; i < 30; i++) {
			Circle circle = new Circle(150, Color.web("white", 0.05));
			circle.setStrokeType(StrokeType.OUTSIDE);
			circle.setStroke(Color.web("white", 0.16));
			circle.setStrokeWidth(4);
			circles.getChildren().add(circle);
		}
		circles.setEffect(new BoxBlur(10, 10, 3));

//		root.getChildren().add(colors);
//		root.getChildren().add(circles);

		Group blendModeGroup =
			new Group(
				new Group(
					new Rectangle(
						scene.getWidth(),
						scene.getHeight(),
						Color.BLACK),
					circles),
				colors);
		colors.setBlendMode(BlendMode.OVERLAY);
		root.getChildren().add(blendModeGroup);
		
		Timeline timeline = new Timeline();
		for (Node circle: circles.getChildren()) {
		    timeline.getKeyFrames().addAll(
		        new KeyFrame(Duration.ZERO, // set start position at 0
		            new KeyValue(circle.translateXProperty(), random() * 800),
		            new KeyValue(circle.translateYProperty(), random() * 600)
		        ),
		        new KeyFrame(new Duration(40000), // set end position at 40s
		            new KeyValue(circle.translateXProperty(), random() * 800),
		            new KeyValue(circle.translateYProperty(), random() * 600)
		        )
		    );
		}
		// play 40s of animation
		timeline.play();		
		
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
