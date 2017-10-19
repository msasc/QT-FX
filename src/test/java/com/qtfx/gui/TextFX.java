package com.qtfx.gui;

import java.util.Iterator;
import java.util.Map;

import com.qtfx.library.util.ThreadUtils;

import javafx.application.Application;
import javafx.stage.Stage;

public class TextFX extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		Map<Thread, StackTraceElement[]> map = Thread.getAllStackTraces();
		Iterator<Thread> i = map.keySet().iterator();
		while (i.hasNext()) {
			Thread t = i.next();
			if (t.getName().equals("JavaFX Application Thread")) {
				System.out.println("YYYYYEEEEEAAAAA");
			}
			System.out.println(t);
		}
		
		System.out.println(ThreadUtils.isFxApplicationThreadRunning());
	}

}
