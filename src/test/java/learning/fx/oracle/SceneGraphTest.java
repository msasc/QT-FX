package learning.fx.oracle;

import javafx.animation.FillTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Test the scene graph
 *
 * @author Miquel Sas
 *
 */
public class SceneGraphTest extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		Group root = new Group();
		Scene scene = new Scene(root, 500, 500, Color.BLACK);

		Rectangle rect = new Rectangle(0, 0, 250, 250);
		rect.setFill(Color.BLUE);
		root.getChildren().add(rect);
		
		TranslateTransition translate = new TranslateTransition(Duration.millis(750));
		translate.setToX(390);
		translate.setToY(390);

		FillTransition fill = new FillTransition(Duration.millis(750));
		fill.setToValue(Color.RED);

		RotateTransition rotate = new RotateTransition(Duration.millis(750));
		rotate.setToAngle(360);

		ScaleTransition scale = new ScaleTransition(Duration.millis(750));
		scale.setToX(0.1);
		scale.setToY(0.1);

		ParallelTransition transition = new ParallelTransition(rect, translate, fill, rotate, scale);
		transition.setCycleCount(Timeline.INDEFINITE);
		transition.setAutoReverse(true);
		transition.play();
		stage.setTitle("JavaFX Scene Graph Demo");
		stage.setScene(scene);
		stage.show();
	}

}
