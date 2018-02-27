package com.qtfx;

import java.util.HashMap;
import java.util.Map;

public class TestMap {

	public static void main(String[] args) {
		Map<String, Object> map = new HashMap<>();

		map.put("obool", true);
		map.put("vbool", new boolean[] { true, false });
		map.put("oint", 10);
		map.put("vint", new int[] { 10, 20 });
		map.put("aint", new int[][] { { 10, 20 }, { 30, 40 } });

		Object obool = map.get("obool");
		if (obool instanceof Boolean) {
			System.out.println("boolean OK");
		}
		Object vbool = map.get("vbool");
		if (vbool instanceof boolean[]) {
			System.out.println("boolean[] OK");
		}
		Object oint = map.get("oint");
		if (oint instanceof Integer) {
			System.out.println("int OK");
		}
		Object vint = map.get("vint");
		if (vint instanceof int[]) {
			System.out.println("int[] OK");
		}
		Object aint = map.get("aint");
		if (aint instanceof int[]) {
			System.out.println("int[][] Uff");
		}
		if (aint instanceof int[][]) {
			System.out.println("int[][] OK");
		}
	}

}
