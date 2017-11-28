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
import com.qtfx.lib.gui.Buttons;
import com.qtfx.lib.gui.Dialog;
import com.qtfx.lib.gui.Nodes;
import com.qtfx.lib.gui.TableRecordPane;
import com.qtfx.lib.util.TextServer;
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

		TableRecordPane table = new TableRecordPane(new Record(Util.getFieldList()));
		table.setRecordSet(Util.getRandomRecordSet(50000, Util.getFieldList()));

		table.addColumn("ICHECKED", false);
		table.addColumn("CARTICLE", true);
		table.addColumn("DARTICLE", false);
		table.addColumn("CBUSINESS", false);
		table.addColumn("TCREATED", true);
		table.addColumn("QSALES", false);
		table.addColumn("IREQUIRED", false);
		table.addColumn("ISTATUS", false);

		table.setSelectionMode(SelectionMode.MULTIPLE);
		table.setPadding(new Insets(10, 10, 0, 10));

		Dialog dialog = new Dialog(stage);
		dialog.setTitle("Table RecordSet Sample");
		dialog.setCenter(table.getNode());
		dialog.addPropertySetter(table.getPropertySetter());
		dialog.setWidth(800);
		dialog.setHeight(800);
		Button buttonSort = new Button("Sort");
		dialog.getButtonPane().getButtons().add(buttonSort);
		dialog.getButtonPane().getButtons().add(Buttons.buttonClose());
		dialog.getButtonPane().setPadding(new Insets(10, 10, 10, 10));

		Order order = new Order();
		order.add(table.getField("ICHECKED"), false);
		order.add(table.getField("CARTICLE"), false);

		buttonSort.setOnAction(actionEvent -> {
			TableRecordPane t = Nodes.getTableRecordPane(buttonSort);
			order.invertAsc();
			t.getRecords().sort(new RecordComparator(order));
		});

		dialog.show();
	}
}
