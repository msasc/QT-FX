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

package com.qtfx.gui;

import java.math.BigDecimal;
import java.util.function.UnaryOperator;

import com.qtfx.lib.db.Field;
import com.qtfx.lib.db.Types;
import com.qtfx.lib.db.Value;
import com.qtfx.lib.gui.converters.NumberStringConverter;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Check text formatter for text input controls.
 * @author Miquel Sas
 */
public class TestTextFormatter extends Application {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);

	}
	
	static class Filter implements UnaryOperator<Change> {
		@Override
		public Change apply(Change t) {
			t.setText(t.getText().toUpperCase());
			return t;
		}
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		
		VBox vbox = new VBox(10);
		vbox.setPadding(new Insets(10,10,10,10));
		
		Field field = new Field();
		field.setType(Types.DECIMAL);
		field.setDecimals(0);
		
		TextField textNum = new TextField();
		textNum.setId("id-text");
		NumberStringConverter cnvNum = new NumberStringConverter(field);
		TextFormatter<Value> fmtNum = new TextFormatter<>(cnvNum);
		fmtNum.setValue(new Value(new BigDecimal(80000000000l)));
		textNum.setTextFormatter(fmtNum);
		vbox.getChildren().add(textNum);
		
		TextFormatter<Value> fmtUpper = new TextFormatter<>(new Filter());
		TextField textUpper = new TextField();
		textUpper.setTextFormatter(fmtUpper);
		vbox.getChildren().add(textUpper);
		
		Button button = new Button("Change value");
		vbox.getChildren().add(button);
		
		Scene scene = new Scene(vbox);
		stage.setScene(scene);
		
		button.setOnAction(a -> {
			fmtNum.setValue(new Value(fmtNum.getValue().getBigDecimal().multiply(new BigDecimal(10))));
		});
		
		
		stage.show();
	}

}
