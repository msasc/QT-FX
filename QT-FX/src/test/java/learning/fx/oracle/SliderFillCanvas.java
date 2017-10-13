package learning.fx.oracle;

import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Use 4 sliders to manage RGBA of a color and a canvas to show it.
 *
 * @author Miquel Sas
 */
public class SliderFillCanvas extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	DoubleProperty red = new SimpleDoubleProperty(1.0);
	DoubleProperty green = new SimpleDoubleProperty(1.0);
	DoubleProperty blue = new SimpleDoubleProperty(1.0);
	DoubleProperty opacity = new SimpleDoubleProperty(1.0);

	@Override
	public void start(Stage stage) throws Exception {

		Slider sr = new Slider(0.0, 1.0, 1.0); // Red
		Slider sg = new Slider(0.0, 1.0, 1.0); // Green
		Slider sb = new Slider(0.0, 1.0, 1.0); // Blue
		Slider so = new Slider(0.0, 1.0, 1.0); // Opacity
		
		sr.setOrientation(Orientation.VERTICAL);
		sg.setOrientation(Orientation.VERTICAL);
		sb.setOrientation(Orientation.VERTICAL);
		so.setOrientation(Orientation.VERTICAL);

		sr.setMaxHeight(Double.MAX_VALUE);
		sg.setMaxHeight(Double.MAX_VALUE);
		sb.setMaxHeight(Double.MAX_VALUE);
		so.setMaxHeight(Double.MAX_VALUE);

		// Link sliders.
		red.bind(sr.valueProperty());
		green.bind(sg.valueProperty());
		blue.bind(sb.valueProperty());
		opacity.bind(so.valueProperty());
		
		// Region.
		Region region = new Region();
		region.setOpacity(100);
		region.setMinWidth(100);
		region.setMaxWidth(Double.MAX_VALUE);
		region.setMaxHeight(Double.MAX_VALUE);

		// On values changed.
		red.addListener((ov, oldValue, newValue) -> {
			region.setBackground(getBackground());
		});
		green.addListener((ov, oldValue, newValue) -> {
			region.setBackground(getBackground());
		});
		blue.addListener((ov, oldValue, newValue) -> {
			region.setBackground(getBackground());
		});
		opacity.addListener((ov, oldValue, newValue) -> {
			region.setBackground(getBackground());
		});
		
		// Do layout.
		HBox hbox = new HBox(10);
		hbox.setPadding(new Insets(10, 10, 10, 10));
		hbox.getChildren().addAll(sr, sg, sb, so, region);
		hbox.setMinWidth(400);
		
		Scene scene = new Scene(hbox);
		stage.setScene(scene);
		stage.setTitle("Layout Buttons");
		stage.show();
	}

	private Background getBackground() {
		Color color = Color.color(red.get(), green.get(), blue.get(), opacity.get());
		BackgroundFill fill = new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY);
		return new Background(fill);
	}
}
