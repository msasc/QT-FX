package com.qtfx;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;

import com.qtfx.lib.util.IO;

public class TestIO {

	public static void main(String[] args) {
		try {
			File file = new File("resources/test-io.dat");
			FileOutputStream fo = new FileOutputStream(file);
			
			String s_in = "Esto es un poco de texto";
			IO.writeString(fo, s_in);
			
			double[] v_in = new double[] { 1.2, 3.5, 3.333 };
			IO.writeDouble1A(fo, v_in);
			
			double[][] a_in = new double[][] { {1.2, 3.5, 3.333}, {1.2, 3.5, 3.333} };
			IO.writeDouble2A(fo, a_in);
			
			BigDecimal b_in = new BigDecimal("10000.203");
			IO.writeBigDecimal(fo, b_in);
			
			fo.close();
			
			FileInputStream fi = new FileInputStream(file);
			String s_out = IO.readString(fi);
			double[] v_out = IO.readDouble1A(fi);
			double[][] a_out = IO.readDouble2A(fi);
			BigDecimal b_out = IO.readBigDecimal(fi);
			fi.close();
			
			System.out.println(s_in);
			System.out.println(s_out);
			System.out.println(print(v_in));
			System.out.println(print(v_out));
			
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}

	private static String print(double[] v) {
		StringBuilder b = new StringBuilder();
		b.append("[");
		for (int i = 0; i < v.length; i++) {
			if (i > 0) {
				b.append(", ");
			}
			b.append(v[i]);
		}
		b.append("]");
		return b.toString();
	}
}
