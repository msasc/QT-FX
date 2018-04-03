package com.qtfx;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.qtfx.lib.util.Numbers;

public class TestMath {

	public static void main(String[] args) {
		System.out.println(Math.exp(1));
		System.out.println(Math.log(Math.exp(1)));
		System.out.println(Math.exp(0.00000001));
		System.out.println(Math.log(Math.exp(0.00000001)));
		System.out.println(round(Math.log(Math.exp(0.00000001)),9));
		System.out.println(round(1.4567,8));
		System.out.println(Numbers.round(1.4567,8));
	}

	private static double round(double x, int d) {
		BigDecimal b = new BigDecimal(x).setScale(d, RoundingMode.HALF_UP);
		return b.doubleValue();
	}
}
