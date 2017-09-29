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

package learning.fx.oracle;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Test VBox with buttons.
 *
 * @author Miquel Sas
 */
public class LayoutEx extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		Button btnAdd = new Button("Add");
		Button btnDelete = new Button("Delete");
		Button btnMoveUp = new Button("Move Up");
		Button btnMoveDown = new Button("Move Down");
		
		btnAdd.setMaxWidth(Double.MAX_VALUE);
		btnDelete.setMaxWidth(Double.MAX_VALUE);
		btnMoveUp.setMaxWidth(Double.MAX_VALUE);
		btnMoveDown.setMaxWidth(Double.MAX_VALUE);
		
		VBox vbButtons = new VBox();
		vbButtons.setSpacing(10);
		vbButtons.setPadding(new Insets(0, 20, 10, 20)); 
		vbButtons.getChildren().addAll(btnAdd, btnDelete, btnMoveUp, btnMoveDown);
		
		Scene scene = new Scene(vbButtons);
		stage.setScene(scene);
		stage.setTitle("Layout Buttons");
		stage.show();
	}

}
