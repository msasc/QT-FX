package com.qtfx;

public class TestVarArg {

	public static void main(String[] args) {
		test("Hola");
		test("Hola", 1, 2, 3);
		test("Hola", null);
	}

	private static void test(String string, int... values) {
		System.out.println(string);
		if (values != null) {
			System.out.println(values.length);
		}
	}
}
