package com.qtfx.gui;

import java.util.ArrayList;
import java.util.List;

import com.qtfx.lib.gui.FX;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TestTextFieldsSizes extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		
		List<TextField> fields = new ArrayList<>();
		int count = 100;
		int size = 8;
		for (int i = 0; i < count; i++) {
//			TextField field = new TextField("font size: " + size);
			TextField field = new TextField("font size");
			String style = "-fx-font-size: " + size + ";";
			field.setStyle(style);
			size += 1;
			fields.add(field);
		}
		
		BorderPane root = new BorderPane();
		root.setPadding(new Insets(20, 20, 20, 20));
		
		VBox vbox = new VBox(5);
		vbox.getChildren().addAll(fields);
		root.setCenter(new ScrollPane(vbox));
		
		Button button = new Button("Info");
//		button.setFocusTraversable(false);
		root.setBottom(button);
		
		button.setOnAction(e -> {
			double total = 0;
			for (TextField field : fields) {
				double fieldHeight = field.getBoundsInLocal().getHeight();
				double textHeight = FX.getStringHeight(field.getFont());
				StringBuilder b = new StringBuilder();
				b.append(" Font size: " + field.getFont().getSize());
				b.append(" Field : " + fieldHeight);
				b.append(" Text : " + textHeight);
				double factor = fieldHeight / textHeight;
				b.append(" Factor: " + factor);
				System.out.println(b);
				total += factor;
			}
			System.out.println(" Average: " + (total / fields.size()));
		});
		
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		
	}
}
