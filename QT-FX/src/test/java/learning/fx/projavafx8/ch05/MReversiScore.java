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

package learning.fx.projavafx8.ch05;

import java.util.ArrayDeque;
import java.util.Deque;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.effect.Reflection;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * My implementation of the reversi example.
 *
 * @author Miquel Sas
 */
public class MReversiScore extends Application {

	/**
	 * The piece.
	 */
	static class Piece extends Region {

		private ObjectProperty<Owner> ownerProperty = new SimpleObjectProperty<>(this, "owner", Owner.NONE);

		public ObjectProperty<Owner> ownerProperty() {
			return ownerProperty;
		}

		public Owner getOwner() {
			return ownerProperty.get();
		}

		public void setOwner(Owner owner) {
			ownerProperty.set(owner);
		}

		Piece() {
			styleProperty().bind(
				Bindings
					.when(ownerProperty.isEqualTo(Owner.NONE))
					.then("radius: 0")
					.otherwise(
						Bindings
							.when(ownerProperty.isEqualTo(Owner.WHITE))
							.then("-fx-background-color: radial-gradient(radius 100%, white .4, gray .9, darkgray 1)")
							.otherwise("-fx-background-color: radial-gradient(radius 100%, white 0, black .6)"))
					.concat("; -fx-background-radius: 1000em; -fx-background-insets: 5"));
			Reflection reflection = new Reflection();
			reflection.setFraction(1);
			reflection.topOffsetProperty().bind(heightProperty().multiply(-.75));
			setEffect(reflection);
			setPrefSize(180, 180);
			setMouseTransparent(true);
		}

		Piece(Owner owner) {
			this();
			ownerProperty.setValue(owner);
		}
	}

	/**
	 * The square region.
	 */
	static class Square extends Region {

		Region highlight;
		FadeTransition highlightTransition;

		Square(Model model, int x, int y) {

			highlight = new Region();
			highlight.setOpacity(0);
			highlight.setStyle("-fx-border-width: 3; -fx-border-color: dodgerblue;");

			highlightTransition = new FadeTransition(Duration.millis(200), highlight);
			highlightTransition.setFromValue(0);
			highlightTransition.setToValue(1);

			styleProperty().bind(
				Bindings
					.when(model.legalMove(x, y))
					.then("-fx-background-color: derive(dodgerblue, -60%)")
					.otherwise("-fx-background-color: burlywood"));
			Light.Distant light = new Light.Distant();
			light.setAzimuth(-135);
			light.setElevation(30);
			setEffect(new Lighting(light));
			getChildren().add(highlight);

			addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET, t -> {
				if (model.legalMove(x, y).get()) {
					highlightTransition.setRate(1);
					highlightTransition.play();
				}
			});

			addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, t -> {
				if (model.legalMove(x, y).get()) {
					highlightTransition.setRate(-1);
					highlightTransition.play();
				}
			});

			setOnMouseClicked(e -> {
				if (e.getButton().equals(MouseButton.PRIMARY)) {
					if (model.legalMove(x, y).get()) {
						model.play(x, y);
						highlightTransition.setRate(-1);
						highlightTransition.play();
					}
				}
			});
		}

		@Override
		protected void layoutChildren() {
			layoutInArea(highlight, 0, 0, getWidth(), getHeight(), getBaselineOffset(), HPos.CENTER, VPos.CENTER);
		}
	}

	/**
	 * An owner of a cell.
	 */
	static enum Owner {

		NONE,
		WHITE,
		BLACK;

		Owner opposite() {
			return this == WHITE ? BLACK : this == BLACK ? WHITE : NONE;
		}

		Color getColor() {
			return this == Owner.WHITE ? Color.WHITE : Color.BLACK;
		}

		String getColorStyle() {
			return this == Owner.WHITE ? "white" : "black";
		}

	}

	/**
	 * The model.
	 */
	static class Model {

		/** Can flip boolean binding. */
		class CanFlip extends BooleanBinding {
			int cellX, cellY, dirX, dirY;

			CanFlip(int cellX, int cellY, int dirX, int dirY) {
				this.cellX = cellX;
				this.cellY = cellY;
				this.dirX = dirX;
				this.dirY = dirY;
				bind(turn);
				int x = cellX + dirX;
				int y = cellY + dirY;
				while (x >= 0 && x < boardSize && y >= 0 && y < boardSize) {
					bind(board[x][y]);
					x += dirX;
					y += dirY;
				}
			}

			@Override
			protected boolean computeValue() {
				Owner turnVal = turn.get();
				int x = cellX + dirX;
				int y = cellY + dirY;
				boolean first = true;
				while (x >= 0 && x < boardSize && y >= 0 && y < boardSize && board[x][y].get() != Owner.NONE) {
					if (board[x][y].get() == turnVal) {
						return !first;
					}
					first = false;
					x += dirX;
					y += dirY;
				}
				return false;
			}

		}

		int boardSize = 8;

		ObjectProperty<Owner>[][] board;
		ObjectProperty<Owner> turn = new SimpleObjectProperty<>(Owner.BLACK);

		SimpleIntegerProperty scoreBlack;
		SimpleIntegerProperty remainingBlack;
		SimpleIntegerProperty scoreWhite;
		SimpleIntegerProperty remainingWhite;

		Deque<ObjectProperty<Owner>[][]> undoStack = new ArrayDeque<>();

		@SuppressWarnings("unchecked")
		Model(int boardSize) {
			if (boardSize % 2 != 0) {
				throw new IllegalArgumentException();
			}
			this.boardSize = boardSize;
			board = new ObjectProperty[boardSize][boardSize];
			for (int i = 0; i < boardSize; i++) {
				for (int j = 0; j < boardSize; j++) {
					board[i][j] = new SimpleObjectProperty<>(Owner.NONE);
				}
			}

			scoreBlack = new SimpleIntegerProperty();
			remainingBlack = new SimpleIntegerProperty();
			scoreWhite = new SimpleIntegerProperty();
			remainingWhite = new SimpleIntegerProperty();

			initBoard();
			setScores(false);
		}

		void initBoard() {
			int center1 = boardSize / 2 - 1;
			int center2 = boardSize / 2;
			board[center1][center1].setValue(Owner.WHITE);
			board[center1][center2].setValue(Owner.BLACK);
			board[center2][center1].setValue(Owner.BLACK);
			board[center2][center2].setValue(Owner.WHITE);
		}

		void restart() {
			undoStack.clear();
			for (int i = 0; i < boardSize; i++) {
				for (int j = 0; j < boardSize; j++) {
					board[i][j].setValue(Owner.NONE);
				}
			}
			initBoard();
			turn.setValue(Owner.BLACK);
			setScores(false);
		}

		@SuppressWarnings("unchecked")
		void save() {
			ObjectProperty<Owner>[][] undo = new ObjectProperty[boardSize][boardSize];
			for (int i = 0; i < boardSize; i++) {
				for (int j = 0; j < boardSize; j++) {
					undo[i][j] = new SimpleObjectProperty<>(board[i][j].get());
				}
			}
			undoStack.addLast(undo);
		}

		void undo() {
			if (!undoStack.isEmpty()) {
				ObjectProperty<Owner>[][] undo = undoStack.removeLast();
				for (int i = 0; i < boardSize; i++) {
					for (int j = 0; j < boardSize; j++) {
						board[i][j].set(undo[i][j].get());
					}
				}
				turn.setValue(turn.getValue().opposite());
				setScores(true);
			}
		}

		int getNumCells(Owner owner) {
			int score = 0;
			for (int i = 0; i < boardSize; i++) {
				for (int j = 0; j < boardSize; j++) {
					score += board[i][j].isEqualTo(owner).get() ? 1 : 0;
				}
			}
			return score;
		}

		void setScores(boolean play) {
			this.scoreBlack.set(getNumCells(Owner.BLACK));
			this.scoreWhite.set(getNumCells(Owner.WHITE));

			int emptyCells = getNumCells(Owner.NONE);
			int remainingBlack, remainingWhite;
			if (play) {
				remainingBlack = turn.get().equals(Owner.BLACK) ? (emptyCells + 1) / 2 : emptyCells / 2;
				remainingWhite = turn.get().equals(Owner.WHITE) ? (emptyCells + 1) / 2 : emptyCells / 2;
			} else {
				remainingBlack = emptyCells / 2;
				remainingWhite = emptyCells / 2;
			}
			this.remainingBlack.set(remainingBlack);
			this.remainingWhite.set(remainingWhite);
		}

		BooleanBinding canFlip(int cellX, int cellY, int dirX, int dirY) {
			return new CanFlip(cellX, cellY, dirX, dirY);
		}

		BooleanBinding legalMove(int x, int y) {
			BooleanBinding canFlip;
			canFlip = canFlip(x, y, 0, -1);
			canFlip = canFlip.or(canFlip(x, y, -1, -1));
			canFlip = canFlip.or(canFlip(x, y, -1, 0));
			canFlip = canFlip.or(canFlip(x, y, -1, 1));
			canFlip = canFlip.or(canFlip(x, y, 0, 1));
			canFlip = canFlip.or(canFlip(x, y, 1, 1));
			canFlip = canFlip.or(canFlip(x, y, 1, 0));
			canFlip = canFlip.or(canFlip(x, y, 1, -1));
			return board[x][y].isEqualTo(Owner.NONE).and(canFlip);
		}

		void flip(int cellX, int cellY, int directionX, int directionY) {
			if (canFlip(cellX, cellY, directionX, directionY).get()) {
				int x = cellX + directionX;
				int y = cellY + directionY;
				while (x >= 0 && x < boardSize && y >= 0 && y < boardSize && board[x][y].get() != turn.get()) {
					board[x][y].setValue(turn.get());
					x += directionX;
					y += directionY;
				}
			}
		}

		void play(int cellX, int cellY) {
			if (legalMove(cellX, cellY).get()) {
				save();
				board[cellX][cellY].setValue(turn.get());
				flip(cellX, cellY, 0, -1);
				flip(cellX, cellY, -1, -1);
				flip(cellX, cellY, -1, 0);
				flip(cellX, cellY, -1, 1);
				flip(cellX, cellY, 0, 1);
				flip(cellX, cellY, 1, 1);
				flip(cellX, cellY, 1, 0);
				flip(cellX, cellY, 1, -1);
				turn.setValue(turn.getValue().opposite());
				setScores(true);
			}
		}
	}

	Model model;

	@Override
	public void start(Stage stage) throws Exception {

		// Start with and 8x8 model.
		model = new Model(8);

		// Root is border pane.
		BorderPane game = new BorderPane();

		// Title: a tile pane on the top.
		TilePane title = getTitle();
		game.setTop(title);
		title.prefTileWidthProperty().bind(Bindings.selectDouble(title.parentProperty(), "width").divide(2));

		// Center tiles.
		StackPane centerTiles = getCenterTiles();
		game.setCenter(centerTiles);
		centerTiles.prefWidthProperty().bind(Bindings.selectDouble(title.parentProperty(), "width"));
		centerTiles.prefHeightProperty().bind(Bindings.selectDouble(title.parentProperty(), "height"));

		// Bottom score.
		TilePane bottomScore = getBottomScore();
		game.setBottom(bottomScore);
		bottomScore.prefTileWidthProperty().bind(
			Bindings.selectDouble(bottomScore.parentProperty(), "width").divide(2));

		// The scene.
		Scene scene = new Scene(game);
		stage.setScene(scene);

		// Right mouse button to restart and undo.
		scene.setOnMouseClicked(e -> {
			if (e.getButton().equals(MouseButton.SECONDARY)) {
				ContextMenu menu = new ContextMenu();
				MenuItem restart = new MenuItem("Restart");
				restart.setOnAction(a -> {
					model.restart();
				});
				MenuItem undo = new MenuItem("Undo");
				undo.setOnAction(a -> {
					model.undo();
				});
				menu.getItems().addAll(restart, undo);
				menu.setOnShowing(w -> {
					if (model.undoStack.isEmpty()) {
						undo.disableProperty().set(true);
					} else {
						undo.disableProperty().set(false);
					}
				});
				menu.show(stage, e.getScreenX(), e.getScreenY());
			}
		});

		scene.setOnKeyPressed(k -> {
			if (k.getCode().equals(KeyCode.Z) && k.isControlDown()) {
				model.undo();
			}
		});

		// Do show.
		stage.show();
	}

	private StackPane getCenterTiles() {
		GridPane board = new GridPane();
		for (int i = 0; i < model.boardSize; i++) {
			for (int j = 0; j < model.boardSize; j++) {
				Square square = new Square(model, i, j);
				Piece piece = new Piece();
				piece.ownerProperty().bind(model.board[i][j]);
				StackPane tile = new StackPane(square, piece);
				tile.setMinSize(60, 60);
				board.add(tile, i, j);
			}
		}
		Region region = new Region();
		region.setStyle("-fx-background-color: radial-gradient(radius 100%, white, gray)");
		return new StackPane(region, board);
	}

	private TilePane getTitle() {

		// Left stack pane
		StackPane left = new StackPane();
		left.setStyle("-fx-background-color: black");
		Text textLeft = new Text("JavaFX");
		textLeft.setFont(Font.font(null, FontWeight.BOLD, 18));
		textLeft.setFill(Color.WHITE);
		StackPane.setAlignment(textLeft, Pos.CENTER_RIGHT);
		StackPane.setMargin(textLeft, new Insets(0, 5, 0, 0));
		left.getChildren().add(textLeft);

		// Right stack pane
		StackPane right = new StackPane();
		right.setStyle("-fx-background-color: white");
		Text textRight = new Text("Reversi");
		textRight.setFont(Font.font(null, FontWeight.BOLD, 18));
		textRight.setFill(Color.BLACK);
		StackPane.setAlignment(textRight, Pos.CENTER_LEFT);
		StackPane.setMargin(textRight, new Insets(0, 0, 0, 5));
		right.getChildren().add(textRight);

		TilePane title = new TilePane();
		title.setSnapToPixel(false);
		title.getChildren().addAll(left, right);
		title.setPrefTileHeight(40);

		return title;
	}

	private TilePane getBottomScore() {
		TilePane tiles = new TilePane(getBottomScore(Owner.BLACK), getBottomScore(Owner.WHITE));
		tiles.setSnapToPixel(false);
		tiles.setPrefColumns(2);
		return tiles;
	}

	private Node getBottomScore(Owner owner) {

		Region background = new Region();
		background.setStyle("-fx-background-color: " + owner.opposite().getColorStyle());

		Ellipse piece = new Ellipse(32, 20);
		piece.setFill(owner.getColor());

		Text score = new Text();
		score.setFont(Font.font(null, FontWeight.BOLD, 100));
		score.setFill(owner.getColor());

		Text remaining = new Text();
		remaining.setFont(Font.font(null, FontWeight.BOLD, 12));
		remaining.setFill(owner.getColor());

		VBox vbox = new VBox(5.0, piece, remaining);
		vbox.setAlignment(Pos.CENTER);

		FlowPane flowPane = new FlowPane(score, vbox);
		flowPane.setHgap(5);
		flowPane.setVgap(5);
		flowPane.setAlignment(Pos.CENTER);

		StackPane stack = new StackPane(background, flowPane);

		InnerShadow innerShadow = new InnerShadow();
		innerShadow.setColor(Color.DODGERBLUE);
		innerShadow.setChoke(0.5);

		background.effectProperty().bind(
			Bindings
				.when(model.turn.isEqualTo(owner))
				.then(innerShadow)
				.otherwise((InnerShadow) null));

		DropShadow dropShadow = new DropShadow();
		dropShadow.setColor(Color.DODGERBLUE);
		dropShadow.setSpread(0.2);
		piece.setEffect(dropShadow);
		piece.effectProperty().bind(
			Bindings
				.when(model.turn.isEqualTo(owner))
				.then(dropShadow)
				.otherwise((DropShadow) null));

		if (owner.equals(Owner.BLACK)) {
			score.textProperty().bind(model.scoreBlack.asString());
			remaining.textProperty().bind(model.remainingBlack.asString().concat(" turns remaining"));
		} else {
			score.textProperty().bind(model.scoreWhite.asString());
			remaining.textProperty().bind(model.remainingWhite.asString().concat(" turns remaining"));
		}

		return stack;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
