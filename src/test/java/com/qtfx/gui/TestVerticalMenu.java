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

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.PopupWindow.AnchorLocation;
import javafx.stage.Stage;

/**
 * @author Miquel Sas
 */
public class TestVerticalMenu extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		BorderPane root = new BorderPane();
		VBox menuBox = new VBox();
		menuBox.getChildren().add(getMenu("Hola 1"));
		menuBox.getChildren().add(getMenu("Hola 2"));
		root.setLeft(menuBox);
		
		Scene scene = new Scene(root, 450, 480);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private MenuButton getMenu(String text) {
		MenuButton menu = new MenuButton(text);
		MenuItem item1 = new MenuItem("Item 1");
		MenuItem item2 = new MenuItem("Item 2");
		MenuItem item3 = new MenuItem("Item 3");
		MenuItem item4 = new MenuItem("Item 5");
		MenuItem item5 = new MenuItem("Item 6");
		
		ContextMenu ctxMenu = new ContextMenu();
		ctxMenu.setAnchorLocation(AnchorLocation.WINDOW_BOTTOM_LEFT);
		ctxMenu.getItems().add(item1);
		
		menu.setContextMenu(ctxMenu);
		return menu;
	}
}
