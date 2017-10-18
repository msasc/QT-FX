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
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.transform.TransformChangedEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Miquel Sas
 *
 */
public class CanvasTest extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Drawing Operations Test");
		Group root = new Group();
		Canvas canvas = new Canvas();

		GraphicsContext gc = canvas.getGraphicsContext2D();
		
		// Clear away portions as the user drags the mouse
		canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, (MouseEvent e) -> {
			gc.clearRect(e.getX() - 10, e.getY() - 10, 20, 20);
		});
		
		
		root.getChildren().add(canvas);

		Scene scene = new Scene(root);
		
		canvas.widthProperty().bind(scene.widthProperty());
		canvas.heightProperty().bind(scene.heightProperty());
		
		primaryStage.addEventHandler(WindowEvent.WINDOW_SHOWN, (WindowEvent e) -> {
			drawShapes(gc);
		});
		
		canvas.addEventHandler(TransformChangedEvent.TRANSFORM_CHANGED, (TransformChangedEvent e) -> {
			drawShapes(gc);
		});
		
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private void drawShapes(GraphicsContext gc) {
		gc.setFill(Color.GREEN);
		gc.setStroke(Color.BLUE);
		gc.setLineWidth(5);
		gc.strokeLine(40, 10, 10, 40);
		gc.fillOval(10, 60, 30, 30);
		gc.strokeOval(60, 60, 30, 30);
		gc.fillRoundRect(110, 60, 30, 30, 10, 10);
		gc.strokeRoundRect(160, 60, 30, 30, 10, 10);
		gc.fillArc(10, 110, 30, 30, 45, 240, ArcType.OPEN);
		gc.fillArc(60, 110, 30, 30, 45, 240, ArcType.CHORD);
		gc.fillArc(110, 110, 30, 30, 45, 240, ArcType.ROUND);
		gc.strokeArc(10, 160, 30, 30, 45, 240, ArcType.OPEN);
		gc.strokeArc(60, 160, 30, 30, 45, 240, ArcType.CHORD);
		gc.strokeArc(110, 160, 30, 30, 45, 240, ArcType.ROUND);
		gc.fillPolygon(
			new double[] { 10, 40, 10, 40 },
			new double[] { 210, 210, 240, 240 },
			4);
		gc.strokePolygon(
			new double[] { 60, 90, 60, 90 },
			new double[] { 210, 210, 240, 240 },
			4);
		gc.strokePolyline(
			new double[] { 110, 140, 110, 140 },
			new double[] { 210, 210, 240, 240 },
			4);
	}
}
