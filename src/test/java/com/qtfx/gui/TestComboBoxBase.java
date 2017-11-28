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

import com.qtfx.lib.gui.Alert;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author Miquel Sas
 *
 */
public class TestComboBoxBase extends Application {
	
	class ComboBase extends ComboBox<String> {
		@Override
		public void show() {
			Alert.warning("Combo", "Hello");
			if (getValue().equals("AAAAA")) {
				setValue("BBBBB");
			} else {
				setValue("AAAAA");
			}
		}
		
	}

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
		
		ComboBase combo = new ComboBase();
		combo.setEditable(true);
		combo.setValue("AAAAA");
		vbox.getChildren().add(combo);
		
		Scene scene = new Scene(vbox);
		stage.setScene(scene);
		stage.show();
	}

}
