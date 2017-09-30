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

package com.qtfx.library.ai.mnist;

import java.io.File;
import java.util.List;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 *
 * @author Miquel Sas
 *
 */
public class NumberImageViewer extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	private List<NumberImage> numberImages;
	private int currentImage = 0;
	private Label[][] pixelLabels;
	private Label numberLabel;
	private int pageSize = 100;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void start(Stage stage) throws Exception {

		// Read images
		File fileImg = new File("resources/mnist/train-images.idx3-ubyte");
		File fileLbl = new File("resources/mnist/train-labels.idx1-ubyte");
		NumberImageReader reader = new NumberImageReader(fileLbl, fileImg);
		numberImages = reader.read();

		// Pixel labels and grid
		GridPane grid = new GridPane();
		pixelLabels = new Label[NumberImage.ROWS][NumberImage.COLUMNS];
//		Border pixelBorder =
//			new Border(
//				new BorderStroke(Color.LIGHTGRAY.brighter(), BorderStrokeStyle.DOTTED, CornerRadii.EMPTY, BorderStroke.THIN));
		for (int row = 0; row < NumberImage.ROWS; row++) {
			for (int column = 0; column < NumberImage.COLUMNS; column++) {
				Label l = new Label();
//				l.setBorder(pixelBorder);
				l.setMaxWidth(Double.MAX_VALUE);
				l.setMaxHeight(Double.MAX_VALUE);
				l.setBackground(getBackground(255));
				l.setMinWidth(5);
				l.setMinHeight(5);
				grid.add(l, column, row);
				l.prefWidthProperty().bind(Bindings.selectDouble(l.parentProperty(), "width"));
				l.prefHeightProperty().bind(Bindings.selectDouble(l.parentProperty(), "height"));
				pixelLabels[row][column] = l;
			}
		}

		// Number label
		numberLabel = new Label();
		numberLabel.setMaxWidth(Double.MAX_VALUE);
		numberLabel.setAlignment(Pos.CENTER);
		numberLabel.setMinHeight(40);

		// First image
		showImage(0);

		// Root border pane.
		BorderPane root = new BorderPane();
		root.setCenter(grid);
		grid.prefWidthProperty().bind(Bindings.selectDouble(grid.parentProperty(), "width"));
		grid.prefHeightProperty().bind(Bindings.selectDouble(grid.parentProperty(), "height"));
		root.setBottom(numberLabel);

		// Scene and show
		Scene scene = new Scene(root, 300, 300);

		scene.setOnScroll((ScrollEvent e) -> {
			showImage(e.getDeltaY() > 0 ? -1 : 1);
		});

		scene.setOnKeyPressed((KeyEvent e) -> {
			switch (e.getCode()) {
			case DOWN:
			case KP_DOWN:
			case RIGHT:
			case KP_RIGHT:
				showImage(1);
				break;
			case UP:
			case KP_UP:
			case LEFT:
			case KP_LEFT:
				showImage(-1);
				break;
			case PAGE_DOWN:
				showImage(pageSize);
				break;
			case PAGE_UP:
				showImage(-pageSize);
				break;
			case HOME:
				showImage(-numberImages.size());
				break;
			case END:
				showImage(numberImages.size());
				break;
			default:
			}
		});

		stage.setScene(scene);
		stage.setTitle("Number image viewer");
		stage.show();
	}

	private void showImage(int move) {
		currentImage += move;
		if (currentImage >= numberImages.size()) {
			currentImage = numberImages.size() - 1;
		}
		if (currentImage < 0) {
			currentImage = 0;
		}
		NumberImage image = numberImages.get(currentImage);
		int number = image.getNumber();
		numberLabel.setText(Integer.toString(number) + " (" + (currentImage + 1) + "/" + numberImages.size() + ")");
		int[][] bytes = image.getImage();
		for (int row = 0; row < NumberImage.ROWS; row++) {
			for (int column = 0; column < NumberImage.COLUMNS; column++) {
				int b = bytes[row][column];
				pixelLabels[row][column].setBackground(getBackground(b));
			}
		}
	}

	private Background getBackground(int b) {
		Color color = Color.rgb(b, b, b);
		BackgroundFill fill = new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY);
		return new Background(fill);
	}
}
