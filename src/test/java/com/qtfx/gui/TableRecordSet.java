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

import com.qtfx.lib.db.Field;
import com.qtfx.lib.db.Record;
import com.qtfx.lib.db.RecordSet;
import com.qtfx.lib.gui.CallbackField;
import com.qtfx.lib.gui.ObservableRecordSet;
import com.qtfx.util.Util;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Test table record set possibilities.
 *
 * @author Miquel Sas
 */
public class TableRecordSet extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		RecordSet rs = Util.getRandomRecordSet(2000, Util.getFieldList());
		ObservableRecordSet obsRs = new ObservableRecordSet(rs);

		Field fCARTICLE = rs.getField("CARTICLE");
		TableColumn<Record, String> colCARTICLE = new TableColumn<>(fCARTICLE.getHeader());
		colCARTICLE.setMinWidth(100);
		colCARTICLE.setCellValueFactory(new CallbackField("CARTICLE"));
		colCARTICLE.setSortable(false);

		TableView<Record> table = new TableView<>();
		table.setItems(obsRs);
		table.getColumns().add(colCARTICLE);

		BorderPane root = new BorderPane();
		table.setPadding(new Insets(10, 10, 10, 10));
		root.setCenter(table);

		Button button = new Button("Sort");
		button.setOnAction(a -> {
			obsRs.sort();
		});
		root.setBottom(button);

		Scene scene = new Scene(root);
		stage.setTitle("Table RecordSet Sample");
		stage.setWidth(450);
		stage.setHeight(500);

		stage.setScene(scene);
		stage.show();
	}
}
