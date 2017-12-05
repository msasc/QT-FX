package com.qtfx.gui;

import java.util.Locale;

import com.qtfx.lib.db.Record;
import com.qtfx.lib.db.Value;
import com.qtfx.lib.gui.controls.DatePickerField;
import com.qtfx.util.Util;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TestDatePicker extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}
	
	class ValueListener implements ChangeListener<Value> {
		@Override
		public void changed(ObservableValue<? extends Value> observable, Value oldValue, Value newValue) {
			System.out.println(newValue);
		}
		
	}

	@Override
	public void start(Stage stage) throws Exception {
		Locale.setDefault(Locale.UK);
		
		Record rc = Util.getRandomRecord(Util.getFieldList());
		DatePickerField date = new DatePickerField(rc.getField("TCREATED"));
		date.valueProperty().addListener(new ValueListener());
		date.setValue(rc.getValue("TCREATED"));
		Scene scene = new Scene(date.getControl());
		stage.setScene(scene);
		stage.show();
	}


}
