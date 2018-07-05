package com.qtfx;

import java.util.ArrayList;
import java.util.List;

import com.qtfx.lib.util.Lists;

public class TestShuffle {

	public static void main(String[] args) {
		List<String> list = new ArrayList<>();
		for (int i = 1; i <= 1000; i++) {
			list.add(Integer.toString(i));
		}

		System.out.println(list);
		for (int i = 0; i < 9; i++) {
			List<String> shuffled = new ArrayList<>(list);
			Lists.shuffle(shuffled,2000);
			System.out.println(shuffled);
		}
	}

}
