package com.qtfx.gui;

import java.time.LocalDate;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class TestTextFields extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		
		BorderPane root = new BorderPane();
		root.setPadding(new Insets(20, 20, 20, 20));
		GridPane pane = new GridPane();
		root.setCenter(pane);
		TextField text1 = new TextField("Text 1");
		TextField text2 = new TextField("Text 2");
		text2.setEditable(false);
		text2.setFocusTraversable(false);
//		System.out.println(text1.getFont());
//		System.out.println(text2.getFont());
//		text2.setBackground(new Background(new BackgroundFill(Color.BEIGE, new CornerRadii(4), Insets.EMPTY)));
		text2.setBorder(Border.EMPTY);
		text2.setStyle("-fx-font-style: italic; -fx-text-fill: gray;");
//		text2.setStyle("-fx-font-style: italic; -fx-background-color: lavender;");
		text2.setMaxHeight(Double.MAX_VALUE);
		text2.setMaxWidth(Double.MAX_VALUE);
		TextField text3 = new TextField("Text 3 kjdhaksjhdkasjhd kasjhd kasjhd askjdh");
		pane.add(text1, 0, 0);
		
		HBox hbox = new HBox();
		hbox.getChildren().add(text2);
		hbox.setMaxWidth(Double.MAX_VALUE);
//		hbox.prefWidthProperty().bind(Bindings.selectDouble(hbox.parentProperty(), "width"));
//		text2.prefWidthProperty().bind(Bindings.selectDouble(text2.parentProperty(), "width").subtract(100));
		
		pane.add(hbox, 1, 0);
		pane.add(text3, 0, 1);
		TextField text4 = new TextField("Text 4 kjdhaksjhdkasjhd kasjhd kasjhd askjdh");
		pane.add(text4, 1, 1);

		DatePicker picker = new DatePicker(LocalDate.now());
		pane.add(picker, 0, 2);
		
		Button button = new Button("Border");
		pane.add(button, 0, 3);
		
		System.out.println(text1.getBorder());
		
		button.setOnAction(e -> {
			System.out.println(text1.getFont());
//			System.out.println(text1.getHeight());
//			System.out.println(text1.getBoundsInLocal().getHeight());
//			System.out.println(text1.getBoundsInLocal().getHeight() / FX.getStringHeight("AAAAA", text1.getFont()));
			System.out.println(text1.getBorder());
		});
		
//		pane.setPadding(new Insets(5, 5, 5, 5));
//		for (Node child : pane.getChildren()) {
//			GridPane.setMargin(child, new Insets(2, 5, 2, 5));
//		}
		
		ColumnConstraints c1 = new ColumnConstraints();
		c1.setPercentWidth(-1);
		ColumnConstraints c2 = new ColumnConstraints();
		c2.setPercentWidth(100);
		ColumnConstraints c3 = new ColumnConstraints();
		pane.getColumnConstraints().addAll(c1, c2, c3);

//		pane.prefWidthProperty().bind(Bindings.selectDouble(pane.parentProperty(), "width"));

		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setHeight(400);
		stage.setWidth(500);
		text1.applyCss();
		stage.show();
		
	}
}
