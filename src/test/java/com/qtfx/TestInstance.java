package com.qtfx;

public class TestInstance {

	public static void main(String[] args) {
		byte[] bytes = new byte[10];
		if (bytes instanceof byte[]) {
			System.out.println("Byte array");
		}
	}

}
