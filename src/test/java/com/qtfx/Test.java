package com.qtfx;

import java.math.BigDecimal;

public class Test {

	public static void main(String[] args) {
		printNumber(10);
		printNumber(10.10);
		printNumber(new BigDecimal(12.2));
	}

	private static void printNumber(Number n) {
		System.out.println(n);
	}
}
