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

package com.qtfx.gui;

import com.qtfx.lib.gui.Alert;
import com.qtfx.lib.util.TextServer;

import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * Test different alert options.
 * 
 * @author Miquel Sas
 */
public class TestAlert extends Application {
	
	/** Logger configuration. */
	static {
		System.setProperty("log4j.configurationFile", "resources/LoggerQTPlatform.xml");
	}
	public static void main(String[] args) {
		TextServer.addBaseResource("resources/StringsLibrary.xml");
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		
		Alert alert = new Alert(stage);
		alert.setTitle("Alert");
		alert.setType(Alert.Type.CONFIRMATION);
		alert.addText("Hola mensaje: ");
		alert.addText("EN ROJO", "-fx-fill: red; -fx-font-weight: bold");
		alert.addText(", ");
		alert.addText("Y OBLIQUE", "-fx-fill: red; -fx-font-weight: bold; -fx-font-style: oblique;");
		Button option = alert.show();
		System.out.println(option);
		
		System.out.println(Alert.warning("Warning", "Warning message"));
		
	}
}
