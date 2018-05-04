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

import com.qtfx.lib.app.Session;
import com.qtfx.lib.db.Record;
import com.qtfx.lib.db.RecordSet;
import com.qtfx.lib.gui.Buttons;
import com.qtfx.lib.gui.ColumnsPane;
import com.qtfx.lib.gui.ColumnsPane.Operation;
import com.qtfx.lib.gui.Dialog;
import com.qtfx.lib.gui.FX;
import com.qtfx.lib.gui.TableRecordPane;
import com.qtfx.lib.util.Random;
import com.qtfx.util.Util;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;

/**
 * Test table record set possibilities.
 *
 * @author Miquel Sas
 */
public class TestTableRecordSet extends Application {

	/** Logger configuration. */
	static {
		System.setProperty("log4j.configurationFile", "resources/LoggerQTPlatform.xml");
	}

	public static void main(String[] args) {
		Session.addBaseResource("resources/StringsLibrary.xml");
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {

		TableRecordPane table = new TableRecordPane(new Record(Util.getFieldList()));

		table.addColumn("ICHECKED", false);
		table.addColumn("CARTICLE", false);
		table.addColumn("DARTICLE", false);
		table.addColumn("CBUSINESS", false);
		table.addColumn("TCREATED", false);
		table.addColumn("QSALES", false);
		table.addColumn("IREQUIRED", false);
		table.addColumn("ISTATUS", false);

		table.setSelectionMode(SelectionMode.MULTIPLE);
		table.setPadding(new Insets(10, 10, 0, 10));
		
		RecordSet rs = Util.getRandomRecordSet(50000, Util.getFieldList());
		rs.sort();
		table.setRecordSet(rs);

		Dialog dialog = new Dialog(stage);
		dialog.setTitle("Table RecordSet Sample");
		dialog.setCenter(table.getPane());
		dialog.addPropertySetter(table.getPropertySetter());
		dialog.setWidth(800);
		dialog.setHeight(800);
		
		Button buttonScroll = new Button("Scroll");
		Button buttonSelect = new Button("Select");
		Button buttonClear = new Button("Clear");
		Button buttonSort = new Button("Sort");
		Button buttonColumns = new Button("Columns");
		
		
		dialog.getButtonPane().getButtons().add(buttonScroll);
		dialog.getButtonPane().getButtons().add(buttonSelect);
		dialog.getButtonPane().getButtons().add(buttonClear);
		dialog.getButtonPane().getButtons().add(buttonSort);
		dialog.getButtonPane().getButtons().add(buttonColumns);
		dialog.getButtonPane().getButtons().add(Buttons.CLOSE);
		dialog.getButtonPane().setPadding(new Insets(10, 10, 10, 10));

		int scrollIndex = 999;
		buttonScroll.setOnAction(actionEvent -> {
			TableRecordPane t = FX.getTableRecordPane(buttonSort);
			t.scrollTo(scrollIndex);
		});

		buttonSelect.setOnAction(actionEvent -> {
			TableRecordPane t = FX.getTableRecordPane(buttonSort);
			int [] rows = new int[10];
			for (int i = 0; i < rows.length; i++) {
				rows[i] = Random.nextInt(rows.length * 5);
			}
			t.selectIndices(rows[0], rows);
		});

		buttonClear.setOnAction(actionEvent -> {
			TableRecordPane t = FX.getTableRecordPane(buttonSort);
			t.clearSelection();
		});

		buttonSort.setOnAction(actionEvent -> {
			TableRecordPane t = FX.getTableRecordPane(buttonSort);
			ColumnsPane colsPane = new ColumnsPane(Operation.ORDER,t);
			colsPane.show(stage);
		});

		buttonColumns.setOnAction(actionEvent -> {
			TableRecordPane t = FX.getTableRecordPane(buttonColumns);
			ColumnsPane colsPane = new ColumnsPane(Operation.SELECTION,t);
			colsPane.show(stage);
		});

		table.requestFocus();
		dialog.show();
	}
}
