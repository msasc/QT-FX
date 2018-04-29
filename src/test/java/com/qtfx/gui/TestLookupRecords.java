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

import java.util.List;

import com.qtfx.lib.app.Session;
import com.qtfx.lib.db.Record;
import com.qtfx.lib.gui.LookupRecords;
import com.qtfx.lib.util.Random;
import com.qtfx.util.Util;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

/**
 * Test table record set possibilities.
 *
 * @author Miquel Sas
 */
public class TestLookupRecords extends Application {

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

		LookupRecords lookup = new LookupRecords(new Record(Util.getFieldList()));
		lookup.addColumn("ICHECKED", false);
		lookup.addColumn("CARTICLE", true);
		lookup.addColumn("DARTICLE", false);
		lookup.addColumn("CBUSINESS", false);
		lookup.addColumn("TCREATED", true);
		lookup.addColumn("QSALES", false);
		lookup.addColumn("IREQUIRED", false);
		lookup.addColumn("ISTATUS", false);

		ObservableList<Record> records = Util.getRandomRecordSet(50000, Util.getFieldList()).getObservableList();
		lookup.setRecords(records);

		Record record = lookup.lookupRecord(stage);
		System.out.println(record);

		int[] rows = new int[10];
		for (int i = 0; i < rows.length; i++) {
			rows[i] = Random.nextInt(rows.length * 5);
		}
		lookup.clearSelection();
		lookup.selectedIndices(rows[0], rows);
		List<Record> selected = lookup.lookupRecords(stage);
		selected.forEach(rc -> System.out.println(rc));
	}
}
