package com.qtfx.gui;

import com.qtfx.lib.db.Record;
import com.qtfx.lib.db.Value;

import javafx.application.Application;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class TestFont extends Application {

	public static void main(String[] args) {
		System.out.println(Font.font("System Regular", 10));
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Font.getFamilies().forEach(family -> System.out.println(family));
		System.out.println(new TextFieldTableCell<Record, Value>().getFont());
	}

}
