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

import com.qtfx.lib.db.Field;
import com.qtfx.lib.db.Types;
import com.qtfx.lib.db.Value;
import com.qtfx.lib.gui.FX;
import com.qtfx.lib.gui.converters.NumberStringConverter;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
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
	
	@Override
	public void start(Stage stage) throws Exception {
		
		VBox vbox = new VBox(10);
		vbox.setPadding(new Insets(10,10,10,10));
		
		Field field = new Field();
		field.setType(Types.DECIMAL);
		field.setDecimals(0);
		
		TextField text = new TextField();
		text.setId("id-text");
		NumberStringConverter cnv = new NumberStringConverter(field);
		TextFormatter<Value> fmt = new TextFormatter<>(cnv);
		fmt.setValue(new Value(new BigDecimal(80000000000l)));
		text.setTextFormatter(fmt);
		vbox.getChildren().add(text);
		
		Button button = new Button("Change value");
		vbox.getChildren().add(button);
		
		Scene scene = new Scene(vbox);
		stage.setScene(scene);
		
		button.setOnAction(a -> {
			fmt.setValue(new Value(fmt.getValue().getBigDecimal().multiply(new BigDecimal(10))));
			System.out.println(FX.lookup(text.getId(), button));
		});
		
		
		stage.show();
	}

}
