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

import com.qtfx.lib.db.Order;
import com.qtfx.lib.db.Record;
import com.qtfx.lib.db.RecordComparator;
import com.qtfx.lib.db.RecordSet;
import com.qtfx.lib.gui.table.TableColumns;
import com.qtfx.lib.util.TextServer;
import com.qtfx.util.Util;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Test table record set possibilities.
 *
 * @author Miquel Sas
 */
public class TableRecordSet extends Application {
	
	
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
		RecordSet rs = Util.getRandomRecordSet(10000, Util.getFieldList());

		TableView<Record> table = new TableView<>();
		
		ObservableList<Record> list = rs.getObservableList();
		
		table.setItems(list);
		table.getColumns().add(TableColumns.getColumn(rs.getField("ICHECKED"),false));
		table.getColumns().add(TableColumns.getColumn(rs.getField("CARTICLE"),true));
		table.getColumns().add(TableColumns.getColumn(rs.getField("DARTICLE"),false));
		table.getColumns().add(TableColumns.getColumn(rs.getField("CBUSINESS"),false));
		table.getColumns().add(TableColumns.getColumn(rs.getField("TCREATED"),false));
		table.getColumns().add(TableColumns.getColumn(rs.getField("QSALES"),false));
		table.getColumns().add(TableColumns.getColumn(rs.getField("IREQUIRED"),false));
		table.getColumns().add(TableColumns.getColumn(rs.getField("ISTATUS"),false));
		
		table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		BorderPane root = new BorderPane();
		table.setPadding(new Insets(10, 10, 10, 10));
		root.setCenter(table);
		
		Order order = new Order();
		order.add(rs.getField("ICHECKED"), false);
		order.add(rs.getField("CARTICLE"), true);

		Button button = new Button("Sort");
		button.setOnAction(a -> {
			order.invertAsc();
			list.sort(new RecordComparator(order));
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
