package com.qtfx.gui;

import com.qtfx.lib.gui.FX;
import com.qtfx.lib.util.Numbers;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.Stage;

public class TestCanvas extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}
	
	class SizeListener implements ChangeListener<Number> {
		@Override
		public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
			display();
		}
	}

	Canvas canvas;
	
	@Override
	public void start(Stage stage) throws Exception {
		
		BorderPane root = new BorderPane();
		
		Scene scene = new Scene(root, 450, 480);
		stage.setScene(scene);
		
		this.canvas = new Canvas();

		Button increase = new Button("Increase");
		increase.setOnAction(e -> {
			lineWidth++;
			display();
		});
		Button decrease = new Button("Decrease");
		decrease.setOnAction(e -> {
			lineWidth--;
			display();
		});
		Button reset = new Button("Reset");
		reset.setOnAction(e -> {
			lineWidth = 1;
			display();
		});
		
		root.setCenter(new Pane(canvas));
		canvas.widthProperty().bind(Bindings.selectDouble(canvas.parentProperty(), "width"));
		canvas.heightProperty().bind(Bindings.selectDouble(canvas.parentProperty(), "height"));
		canvas.widthProperty().addListener(new SizeListener());
		canvas.heightProperty().addListener(new SizeListener());
		
		root.setBottom(new HBox(increase, decrease, reset));

		stage.show();

	}
	
	double lineWidth = 1;
	
	private void display() {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		double w = canvas.getWidth();
		double h = canvas.getHeight();
		
		gc.clearRect(0, 0, w, h);
		gc.setLineWidth(lineWidth);
		FX.strokeLine(gc, 0, h / 2.0, w, h / 2.0);
		FX.strokeLine(gc, w / 2.0, 0, w / 2.0, h);
		
//		System.out.println(w + ", " + h);
//		System.out.println(magic(w / 2.0) + ", " + magic(h / 2.0));

	}
	
}
