package com.qtfx;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class TestMath {

	public static void main(String[] args) {
//		System.out.println(Math.exp(1));
//		System.out.println(Math.log(Math.exp(1)));
//		System.out.println(Math.exp(0.00000001));
//		System.out.println(Math.log(Math.exp(0.00000001)));
//		System.out.println(round(Math.log(Math.exp(0.00000001)),9));
//		System.out.println(round(1.4567,8));
//		System.out.println(Numbers.round(1.4567,8));
		System.out.println(Math.log(2.2250738585072014E-308));
		System.out.println(Math.log(1E-323));
		System.out.println(log(1E-308));
	}

	private static double round(double x, int d) {
		BigDecimal b = new BigDecimal(x).setScale(d, RoundingMode.HALF_UP);
		return b.doubleValue();
	}
    private static double log(double x) {
        double y = 0.0;
        if (x < 1E-300) {
            y = -690.7755;
        } else {
            y = Math.log(x);
        }
        return y;
    }
}
