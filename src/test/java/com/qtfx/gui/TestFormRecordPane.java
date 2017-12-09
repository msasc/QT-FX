package com.qtfx.gui;

import java.util.List;
import java.util.Locale;

import com.qtfx.lib.db.FieldList;
import com.qtfx.lib.db.Record;
import com.qtfx.lib.gui.FX;
import com.qtfx.lib.gui.FormRecordPane;
import com.qtfx.lib.util.TextServer;
import com.qtfx.util.Util;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class TestFormRecordPane extends Application {

	/** Logger configuration. */
	static {
		System.setProperty("log4j.configurationFile", "resources/LoggerQTPlatform.xml");
	}

	public static void main(String[] args) {
		Locale.setDefault(Locale.UK);
		TextServer.addBaseResource("resources/StringsLibrary.xml");
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {

		FieldList fields = Util.getFieldList();
//		fields.forEach(field -> field.setFieldGroup(FieldGroup.emptyFieldGroup));
		Record record = Util.getRandomRecord(fields);
		FormRecordPane form = new FormRecordPane();
		form.setRecord(record);
		form.addField("CARTICLE", 0, 0);
		form.addField("DARTICLE", 0, 0);
		form.addField("TCREATED", 0, 0);
		form.addField("ICHECKED", 1, 0);
		form.addField("IREQUIRED", 1, 0);
		form.addField("ISTATUS", 1, 0);
		form.addField("QSALES", 2, 0);
		form.addField("QPROD", 2, 0);
		form.addField("QPURCH", 2, 0);
		form.layoutFields();
		form.updateFieldControls();
		
		List<Control> controls = FX.getControls(form.getNode());

		BorderPane root = new BorderPane();
		root.setCenter(form.getNode());
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

}
