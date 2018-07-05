/*
 * Copyright (C) 2017 Miquel Sas
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

package com.qtfx.app.mnist;

import java.io.File;
import java.util.List;

import com.qtfx.lib.ml.data.mnist.NumberImage;
import com.qtfx.lib.ml.data.mnist.NumberImageReader;
import com.qtfx.lib.ml.data.mnist.NumberImageUtils;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

/**
 * A simple number image viewer.
 * 
 * @author Miquel Sas
 */
public class NumberImageViewer extends Application {

	/**
	 * Launch.
	 * 
	 * @param args Arguments.
	 */
	public static void main(String[] args) {
		launch(args);
	}
	
	/**
	 * Width and height change listener to respond to size events.
	 */
	class SizeListener implements ChangeListener<Number> {
		@Override
		public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
			drawImage();
		}
	}

	private int rows = NumberImage.ROWS;
	private int cols = NumberImage.COLUMNS;
	private Canvas canvas;
	private Label label;
	private BorderPane root;
	private List<NumberImage> images;
	private int imageIndex = 0;
	private int pageSize = 100;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void start(Stage stage) throws Exception {

		File fileImg = new File(NumberImageUtils.TRAIN_IMAGES);
		File fileLbl = new File(NumberImageUtils.TRAIN_LABELS);
		NumberImageReader reader = new NumberImageReader(fileLbl, fileImg);
		images = reader.read();

		root = new BorderPane();

		canvas = new Canvas();
		Pane paneCanvas = new Pane(canvas);
		paneCanvas.setPrefWidth(15 * cols);
		paneCanvas.setPrefHeight(15 * rows);
		canvas.widthProperty().bind(paneCanvas.widthProperty());
		canvas.heightProperty().bind(paneCanvas.heightProperty());

		SizeListener sizeListener = new SizeListener();
		canvas.widthProperty().addListener(sizeListener);
		canvas.heightProperty().addListener(sizeListener);
		
		root.setCenter(paneCanvas);

		label = new Label();
		label.setAlignment(Pos.CENTER);
		label.setMaxWidth(Double.MAX_VALUE);

		root.setBottom(label);

		Scene scene = new Scene(root);

		// Key handler.
		scene.setOnKeyPressed(e -> {
			KeyCode keyCode = e.getCode();
			if (keyCode == KeyCode.DOWN) {
				if (imageIndex < images.size() - 1) {
					imageIndex++;
				}
				drawImage();
			}
			if (keyCode == KeyCode.UP) {
				if (imageIndex > 0) {
					imageIndex--;
				}
				drawImage();
			}
			if (keyCode == KeyCode.LEFT) {
				if (imageIndex > 0) {
					imageIndex--;
				}
				drawImage();
			}
			if (keyCode == KeyCode.RIGHT) {
				if (imageIndex < images.size() - 1) {
					imageIndex++;
				}
				drawImage();
			}
			if (keyCode == KeyCode.PAGE_UP) {
				if (imageIndex > 0) {
					imageIndex -= (imageIndex == 0 ? pageSize - 1 : pageSize);
					if (imageIndex < 0) {
						imageIndex = 0;
					}
					drawImage();
				}
			}
			if (keyCode == KeyCode.PAGE_DOWN) {
				if (imageIndex < images.size() - 1) {
					imageIndex += (imageIndex == 0 ? pageSize - 1 : pageSize);
					if (imageIndex >= images.size()) {
						imageIndex = images.size() - 1;
					}
					drawImage();
				}
			}
			if (keyCode == KeyCode.HOME) {
				imageIndex = 0;
				drawImage();
			}
			if (keyCode == KeyCode.END) {
				imageIndex = images.size() - 1;
				drawImage();
			}
		});

		// Mouse wheel handler.
		scene.setOnScroll(e -> {
			int scroll = (e.getTextDeltaY() < 0 ? 1 : -1);
			if (e.isControlDown()) {
				scroll *= (imageIndex == 0 ? pageSize - 1 : pageSize);
			}
			imageIndex += scroll;
			if (imageIndex < 0) {
				imageIndex = 0;
			}
			if (imageIndex >= images.size()) {
				imageIndex = images.size() - 1;
			}
			drawImage();
		});

		stage.setTitle("Number image viewer");
		stage.setScene(scene);
		
		drawImage();
		
		stage.show();
	}

	private void drawImage() {
		Platform.runLater(() -> {
			NumberImage image = images.get(imageIndex);
			double x = 0;
			double y = 0;
			double width = canvas.getWidth();
			double height = canvas.getHeight();

			double pixelWidth = width / cols;
			double pixelHeight = height / rows;
			if (pixelWidth > pixelHeight) {
				pixelWidth = pixelHeight;
			}
			if (pixelHeight > pixelWidth) {
				pixelHeight = pixelWidth;
			}
			width = pixelWidth * cols;
			height = pixelHeight * rows;

			int[][] bytes = image.getImage();

			GraphicsContext gc = canvas.getGraphicsContext2D();
			gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
			
			// Save gc properties
			Paint saveFill = gc.getFill();
			double[] saveDashes = gc.getLineDashes();
			Paint saveStroke = gc.getStroke();
			gc.setLineDashes(1.0, 3.0);
			gc.setStroke(Color.LIGHTGRAY);

			for (int r = 0; r < rows; r++) {
				gc.strokeLine(0, y, width, y);
				x = 0;
				for (int c = 0; c < cols; c++) {
					double b = ((double) bytes[r][c]) / 255d;
					Color color = new Color(b, b, b, 1);
					gc.setFill(color);
					gc.fillRect(x, y, pixelWidth, pixelHeight);
					x += pixelWidth;
				}
				y += pixelHeight;
			}
			
			x = 0;
			y = 0;
			for (int r = 0; r < rows; r++) {
				gc.strokeLine(0, y, width, y);
				y += pixelHeight;
			}
			for (int c = 0; c < cols; c++) {
				gc.strokeLine(x, 0, x, height);
				x += pixelWidth;
			}

			// Restore gc properties
			gc.setFill(saveFill);
			gc.setLineDashes(saveDashes);
			gc.setStroke(saveStroke);

			StringBuilder text = new StringBuilder();
			text.append(image.getNumber());
			text.append(" (");
			text.append(imageIndex + 1);
			text.append("/");
			text.append(images.size());
			text.append(")");
			label.setText(text.toString());
		});
	}
}
