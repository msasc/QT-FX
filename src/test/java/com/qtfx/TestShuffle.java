package com.qtfx;

import java.util.ArrayList;
import java.util.List;

import com.qtfx.lib.util.Lists;

public class TestShuffle {

	public static void main(String[] args) {
		List<String> list = new ArrayList<>();
		list.add("A");
		list.add("B");
		list.add("C");
		list.add("D");
		list.add("E");
		list.add("F");
		list.add("G");
		list.add("H");
		list.add("I");
		list.add("J");

		System.out.println(list);
		for (int i = 0; i < 9; i++) {
			Lists.shuffle(list);
			System.out.println(list);
		}
	}

}
