package com.qtfx.gui;

import javafx.application.Application;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class TestTextproperties extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		
		Text text = new Text("Hola cocacola");
		System.out.println(text.getFont());
		System.out.println(text.getBoundsInLocal());
		System.exit(0);
	}
}
