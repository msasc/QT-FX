package learning.fx.oracle;

import java.math.BigDecimal;

import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Use 4 sliders to manage RGBA of a color and a label to show it.
 *
 * @author Miquel Sas
 */
public class SliderFill extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	DoubleProperty red = new SimpleDoubleProperty(1.0);
	DoubleProperty green = new SimpleDoubleProperty(1.0);
	DoubleProperty blue = new SimpleDoubleProperty(1.0);
	DoubleProperty opacity = new SimpleDoubleProperty(1.0);
	Text color = new Text();

	@Override
	public void start(Stage stage) throws Exception {
		
		Text colorText = new Text();
		colorText.setFont(Font.font("Tahoma", FontWeight.NORMAL, 12));
		
		Slider sr = new Slider(0.0, 1.0, 1.0); // Red
		Slider sg = new Slider(0.0, 1.0, 1.0); // Green
		Slider sb = new Slider(0.0, 1.0, 1.0); // Blue
		Slider so = new Slider(0.0, 1.0, 1.0); // Opacity
		
		sr.setPrefWidth(30);
		sg.setPrefWidth(30);
		sb.setPrefWidth(30);
		so.setPrefWidth(30);
		
		sr.setOrientation(Orientation.VERTICAL);
		sg.setOrientation(Orientation.VERTICAL);
		sb.setOrientation(Orientation.VERTICAL);
		so.setOrientation(Orientation.VERTICAL);
		
		System.out.println(sr.getPrefWidth());

		sr.setMaxHeight(Double.MAX_VALUE);
		sg.setMaxHeight(Double.MAX_VALUE);
		sb.setMaxHeight(Double.MAX_VALUE);
		so.setMaxHeight(Double.MAX_VALUE);

		Label label = new Label();
		label.setOpacity(100);
		label.setMaxWidth(Double.MAX_VALUE);
		label.setMaxHeight(Double.MAX_VALUE);
		label.setBackground(getBackground(colorText));

		// Link sliders.
		red.bind(sr.valueProperty());
		green.bind(sg.valueProperty());
		blue.bind(sb.valueProperty());
		opacity.bind(so.valueProperty());

		// On values changed.
		red.addListener((ov, oldValue, newValue) -> {
			label.setBackground(getBackground(colorText));
		});
		green.addListener((ov, oldValue, newValue) -> {
			label.setBackground(getBackground(colorText));
		});
		blue.addListener((ov, oldValue, newValue) -> {
			label.setBackground(getBackground(colorText));
		});
		opacity.addListener((ov, oldValue, newValue) -> {
			label.setBackground(getBackground(colorText));
		});
		
		// Mouse wheel handlers.
		sr.setOnScroll((ScrollEvent e) -> {
			sr.setValue(sr.getValue() + (e.getDeltaY() / 4000.0));
		});
		sg.setOnScroll((ScrollEvent e) -> {
			sg.setValue(sg.getValue() + (e.getDeltaY() / 4000.0));
		});
		sb.setOnScroll((ScrollEvent e) -> {
			sb.setValue(sb.getValue() + (e.getDeltaY() / 4000.0));
		});
		so.setOnScroll((ScrollEvent e) -> {
			so.setValue(so.getValue() + (e.getDeltaY() / 4000.0));
		});
		
		// Do layout.
		HBox hbox = new HBox(10);
		hbox.setPadding(new Insets(10, 10, 10, 10));
		hbox.setOpacity(100);
		hbox.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
		hbox.getChildren().addAll(sr, sg, sb, so);
		
		BorderPane root = new BorderPane();
		root.setTop(colorText);
		root.setLeft(hbox);
		root.setCenter(label);
		
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle("Color example");
		stage.setMinWidth(400);
		stage.show();
	}

	private Background getBackground(Text colorText) {
		
		StringBuilder b = new StringBuilder();
		b.append("Red: ");
		b.append(new BigDecimal(red.get()).setScale(3, BigDecimal.ROUND_HALF_UP));
		b.append(" Green: ");
		b.append(new BigDecimal(green.get()).setScale(3, BigDecimal.ROUND_HALF_UP));
		b.append(" Blue: ");
		b.append(new BigDecimal(blue.get()).setScale(3, BigDecimal.ROUND_HALF_UP));
		b.append(" Opacity: ");
		b.append(new BigDecimal(opacity.get()).setScale(3, BigDecimal.ROUND_HALF_UP));
		colorText.setText(b.toString());
		
		Color color = Color.color(red.get(), green.get(), blue.get(), opacity.get());
		BackgroundFill fill = new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY);
		return new Background(fill);
	}
}
