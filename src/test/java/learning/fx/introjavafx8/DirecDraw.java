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

package learning.fx.introjavafx8;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Canvas example.
 * 
 * @author Miquel Sas
 */
public class DirecDraw extends Application {

	public static void main(String[] args) {
		launch(args);
	}
	
	class XCanvas extends Canvas {
		
		XCanvas() {
			super();
		}

		XCanvas(double width, double height) {
			super(width, height);
		}

		@Override
		public boolean isResizable() {
			return true;
		}

		@Override
		public void resize(double width, double height) {
//			super.resize(width, height);
			display(this, getGraphicsContext2D());
			System.out.println(width+", "+height);
		}

	}
	
	Color[] colors = { Color.RED, Color.BLUE, Color.GREEN, Color.BLACK };
	int colorIdx = 0;
	boolean small = false;

	@Override
	public void start(Stage stage) throws Exception {

		// Root will be a border pane.
		BorderPane root = new BorderPane();

		// The canvas in the center.
		Pane center = new Pane();
		center.setStyle("-fx-background-color: #336699;");
		root.setCenter(center);
		Canvas canvas = new XCanvas();
		canvas.setOpacity(50);
		center.getChildren().addAll(canvas);
		canvas.widthProperty().bind(Bindings.selectDouble(canvas.parentProperty(), "width"));
		canvas.heightProperty().bind(Bindings.selectDouble(canvas.parentProperty(), "height"));

		// Buttons
		Button btnChangeColor = new Button("Change color");
		Button btnChangeScale = new Button("Change scale");
		FlowPane bottom = new FlowPane(10, 10);
		bottom.getChildren().addAll(btnChangeColor, btnChangeScale);
		root.setBottom(bottom);

		// Scene
		Scene scene = new Scene(root, 480, 480);
		stage.setScene(scene);

		GraphicsContext gc = canvas.getGraphicsContext2D();

		btnChangeColor.setOnAction((ActionEvent e) -> {
			gc.setStroke(colors[colorIdx]);
			gc.setFill(colors[colorIdx]);
			display(canvas, gc);
			colorIdx++;
			if (colorIdx == colors.length)
				colorIdx = 0;
		});

		btnChangeScale.setOnAction((ActionEvent e) -> {
			// Switch between small and large scale.
			if (!small) {
				// Scale to one half size.
				gc.scale(0.5, 0.5);
			} else {
				// Scale to full size.
				gc.scale(2.0, 2.0);
			}
			small = !small;
			display(canvas, gc);
		});

		display(canvas, gc);

		stage.setTitle("Draw directly to a Canvas");
		stage.show();
	}
	
	private void display(Canvas canvas, GraphicsContext gc) {
		double w = canvas.getWidth();
		double h = canvas.getHeight();
		
		gc.clearRect(0, 0, w * (small ? 2.0 : 1.0), h * (small ? 2.0 : 1.0));
		
		gc.strokeLine(0, 0, w / 2.0, h / 2.0);
		gc.strokeOval(w / 4.0, w / 4.0, w / 2.0, h / 2.0);
		// gc.strokeRect(0, h / 2.0, w / 8.0, h / 2.0);
		gc.fillOval(0, 0, w / 20.0, h / 20.0);
		// gc.fillRect(w / 4.0, h / 1.2, w / 2.0, h / 10.0);
		gc.setFont(new Font(h / 20.0));
		gc.fillText("This is drawn on the canvas", w / 8.0, h / 8.0);

		for (double i = 0; i < w; i += 0.5) {
			for (double j = h - 50; j < h; j += 0.5) {
				gc.strokeLine(i, j, i + 0.05, j + 0.05);
			}
		}
	}

}
