package com.qtfx;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

import com.qtfx.lib.util.IO;
import com.qtfx.lib.util.Random;

public class TestIOZip {

	public static void main(String[] args) {
		try {
			File file = new File("resources/test-io.dat");
			File file_zip = new File("resources/test-io.zip");
			FileOutputStream fo = new FileOutputStream(file);
			OutputStream fo_zip = new DeflaterOutputStream(new FileOutputStream(file_zip));

			int length = 100000;
			double[] v_in = new double[length];
			for (int i = 0; i < length; i++) {
				v_in[i] = Random.nextDouble();
			}
			IO.writeDouble1A(fo, v_in);
			IO.writeDouble1A(fo_zip, v_in);
			
			fo.close();
			fo_zip.close();
			
			FileInputStream fi = new FileInputStream(file);
			InputStream fi_zip = new InflaterInputStream(new FileInputStream(file_zip));
			double[] v_out = IO.readDouble1A(fi);
			double[] v_out_zip = IO.readDouble1A(fi_zip);
			fi.close();
			
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
