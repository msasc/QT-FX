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

import com.qtfx.library.gui.Dialog;
import com.qtfx.library.gui.Option;
import com.qtfx.library.util.NumberUtils;
import com.qtfx.library.util.Random;
import com.qtfx.library.util.StringUtils;
import com.qtfx.library.util.TextServer;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

/**
 * Test different dialog configurations.
 * 
 * @author Miquel Sas
 */
public class TestDialog extends Application {
	
	public static void main(String[] args) {
		TextServer.addBaseResource("resources/StringsLibrary.xml");
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {

		Option result;
		
		result = getDialogEmpty(stage).show();
		System.out.println(result);
		
		result = getDialogBottomPlain(stage).show();
		System.out.println(result);
		
		for (int i = 0; i < 5; i++) {
			result = getDialogBottomPlainRandom(stage).show();
			System.out.println(result);
		}
		
		result = getDialogLeftPlain(stage).show();
		System.out.println(result);
		
		result = getDialogBottomGroups(stage).show();
		System.out.println(result);
		
		result = getDialogLeftGroups(stage).show();
		System.out.println(result);
	}

	private Dialog getDialogBottomPlain(Stage stage) {
		Dialog dialog = new Dialog(stage);
		dialog.setButtonsBottom();
		dialog.setTitle("First dialog");
		dialog.setCenter(new Text("Text content"));
		dialog.getOptionPane().getOptions().addAll(getOptionsPlain());
		return dialog;
	}
	
	private Dialog getDialogBottomPlainRandom(Stage stage) {
		Dialog dialog = new Dialog(stage);
		dialog.setButtonsBottom();
		dialog.setTitle("First dialog");
		dialog.setCenter(new Text("Text content"));
		
		Option[] options = getOptionsPlain();
		int digits = NumberUtils.getDigits(options.length);
		for (Option option : options) {
			int k = Random.nextInt(options.length);
			String groupKey = StringUtils.leftPad(Integer.toString(k), digits, "0");
			option.setOrder(groupKey);
		}
		
		dialog.getOptionPane().getOptions().addAll(options);
		return dialog;
	}
	
	private Dialog getDialogBottomGroups(Stage stage) {
		Dialog dialog = new Dialog(stage);
		dialog.setButtonsBottom();
		dialog.setTitle("First dialog");
		dialog.setCenter(new Text("Text content"));
		dialog.getOptionPane().getOptions().addAll(getOptionsGroups());
		return dialog;
	}
	
	private Dialog getDialogLeftPlain(Stage stage) {
		Dialog dialog = new Dialog(stage);
		dialog.setButtonsLeft();
		dialog.setTitle("First dialog");
		dialog.setCenter(new Text("Text content"));
		dialog.getOptionPane().getOptions().addAll(getOptionsPlain());
		return dialog;
	}
	
	private Dialog getDialogEmpty(Stage stage) {
		Dialog dialog = new Dialog(stage);
		dialog.setTitle("First dialog");
		Text text = new Text("Text content ashd askljdh askjdhaskjsdh askjsdhask kdjah");
		TextFlow flow = new TextFlow(text);
		flow.setPadding(new Insets(20, 20, 20, 20));
		dialog.setCenter(flow);
		return dialog;
	}
	
	private Dialog getDialogLeftGroups(Stage stage) {
		Dialog dialog = new Dialog(stage);
		dialog.setButtonsLeft();
		dialog.setTitle("First dialog");
		dialog.setCenter(new Text("Text content"));
		dialog.getOptionPane().getOptions().addAll(getOptionsGroups());
		return dialog;
	}
	
	private Option[] getOptionsPlain() {
		Option accept = Option.accept();
		Option apply = Option.apply();
		Option cancel = Option.cancel();
		Option close = Option.close();
		Option no = Option.no();
		Option finish = Option.finish();
		Option ignore = Option.ignore();
		Option next = Option.next();
		Option ok = Option.ok();
		Option open = Option.open();
		Option previous = Option.previous();
		Option retry = Option.retry();
		Option yes = Option.yes();
		return new Option[] { accept, apply, cancel, close, no, finish, ignore, next, ok, open, previous, retry, yes};
	}
	private Option[] getOptionsGroups() {
		Option accept = Option.accept();
		Option apply = Option.apply();
		Option cancel = Option.cancel();
		Option close = Option.close();
		Option no = Option.no();
		Option finish = Option.finish();
		Option ignore = Option.ignore();
		Option next = Option.next();
		Option ok = Option.ok();
		Option open = Option.open();
		Option previous = Option.previous();
		Option retry = Option.retry();
		Option yes = Option.yes();
		
		accept.setGroup("00");
		accept.setOrder("00");
		apply.setGroup("00");
		apply.setOrder("01");
		
		ok.setGroup("01");
		ok.setOrder("00");
		cancel.setGroup("01");
		cancel.setOrder("01");
		
		open.setGroup("02");
		open.setOrder("00");
		close.setGroup("02");
		close.setOrder("01");
		
		yes.setGroup("03");
		yes.setOrder("00");
		no.setGroup("03");
		no.setOrder("01");
		
		next.setGroup("04");
		next.setOrder("00");
		previous.setGroup("04");
		previous.setOrder("01");
		
		finish.setGroup("05");
		finish.setOrder("00");
		ignore.setGroup("05");
		ignore.setOrder("01");
		retry.setGroup("05");
		retry.setOrder("02");
		
		return new Option[] { accept, apply, cancel, close, no, finish, ignore, next, ok, open, previous, retry, yes};
	}
}
