package com.qtfx.gui;

import java.time.Instant;
import java.time.temporal.ChronoField;
import java.time.temporal.UnsupportedTemporalTypeException;

public class TestInstant {

	public static void main(String[] args) {
		Instant now = Instant.now();
		long millis = System.currentTimeMillis();
		ChronoField[] fields = ChronoField.values();
		for (ChronoField field : fields) {
			try {
				System.out.println(field.name() + ": " + now.getLong(field));
			} catch (UnsupportedTemporalTypeException exc) {
//				System.out.println(field.name() + ": not supported");
			}
		}
		System.out.println("System: " + millis);
		
		System.out.println();
		for (int i = 0; i < 100; i++) {
			System.out.println(Instant.now().get(ChronoField.MICRO_OF_SECOND));
		}
	}

}
