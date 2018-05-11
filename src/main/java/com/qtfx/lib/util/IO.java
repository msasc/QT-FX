/*
 * Copyright (C) 2017 Miquel Sas
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 */

package com.qtfx.lib.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

/**
 * IO utilities to read/write from an input/output stream.
 *
 * @author Miquel Sas
 */
public class IO {
	
	public static BigDecimal readBigDecimal(InputStream is) throws IOException {
		return new BigDecimal(readString(is));
	}

	/**
	 * Read a byte throwing an IOException if an attempt to pas the end of stream has been made.
	 * 
	 * @param is The input stream.
	 * @return The output stream.
	 * @throws IOException
	 */
	public static byte readByte(InputStream is) throws IOException {
		int i = is.read();
		if (i >= 0) {
			return (byte) i;
		}
		throw new IOException("Past end of stream");
	}

	public static byte[] readBytes(InputStream is, int length) throws IOException {
		byte[] bytes = new byte[length];
		for (int i = 0; i < length; i++) {
			bytes[i] = readByte(is);
		}
		return bytes;
	}

	/**
	 * Read a byte buffer.
	 * 
	 * @param is The input stream
	 * @param bytes The number of bytes to read.
	 * @return The byte buffer.
	 * @throws IOException
	 */
	public static ByteBuffer readBuffer(InputStream is, int bytes) throws IOException {
		ByteBuffer b = ByteBuffer.allocate(bytes);
		for (int i = 0; i < bytes; i++) {
			b.put(i, readByte(is));
		}
		return b;

	}

	/**
	 * Read a double.
	 * 
	 * @param is The input stream.
	 * @return A double
	 * @throws IOException
	 */
	public static double readDouble(InputStream is) throws IOException {
		return readBuffer(is, Double.BYTES).getDouble(0);
	}

	/**
	 * Read a double one dimensional array.
	 * 
	 * @param is The input stream.
	 * @return The double one dimensional array.
	 * @throws IOException
	 */
	public static double[] readDouble1A(InputStream is) throws IOException {
		int length = readInt(is);
		double[] value = new double[length];
		for (int i = 0; i < length; i++) {
			value[i] = readDouble(is);
		}
		return value;
	}

	/**
	 * Read a double two dimensional array.
	 * 
	 * @param is The input stream.
	 * @return The double one dimensional array.
	 * @throws IOException
	 */
	public static double[][] readDouble2A(InputStream is) throws IOException {
		int rows = readInt(is);
		int cols = readInt(is);
		double[][] value = new double[rows][cols];
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				value[r][c] = readDouble(is);
			}
		}
		return value;
	}

	/**
	 * Read an integer.
	 * 
	 * @param is The input stream.
	 * @return An integer
	 * @throws IOException
	 */
	public static int readInt(InputStream is) throws IOException {
		return readBuffer(is, Integer.BYTES).getInt(0);
	}

	/**
	 * Read a long.
	 * 
	 * @param is The input stream.
	 * @return An long
	 * @throws IOException
	 */
	public static long readLong(InputStream is) throws IOException {
		return readBuffer(is, Long.BYTES).getLong(0);
	}

	/**
	 * Read a string in UTF-16 character set.
	 * 
	 * @param is The input stream.
	 * @return The string.
	 * @throws IOException
	 */
	public static String readString(InputStream is) throws IOException {
		int length = readInt(is);
		byte[] bytes = readBytes(is, length);
		ByteBuffer bb = ByteBuffer.allocate(bytes.length);
		bb.put(bytes);
		bb.rewind();
		CharBuffer cb = Charset.forName("UTF-16").decode(bb);
		return cb.toString();
	}
	
	public static void writeBigDecimal(OutputStream os, BigDecimal b) throws IOException {
		writeString(os, b.toString());
	}

	/**
	 * Write a byte to the output stream.
	 * 
	 * @param os The output stream.
	 * @param b The byte.
	 * @throws IOException
	 */
	public static void writeByte(OutputStream os, byte b) throws IOException {
		os.write(b);
	}

	/**
	 * Write the bytes to the output stream.
	 * 
	 * @param os The output stream.
	 * @param bytes The array of bytes.
	 * @throws IOException
	 */
	public static void writeBytes(OutputStream os, byte[] bytes) throws IOException {
		for (int i = 0; i < bytes.length; i++) {
			os.write(bytes[i]);
		}
	}

	/**
	 * Write a double.
	 * 
	 * @param os The output steam.
	 * @param value The double.
	 * @throws IOException
	 */
	public static void writeDouble(OutputStream os, double value) throws IOException {
		writeBytes(os, ByteBuffer.allocate(Double.BYTES).putDouble(value).array());
	}

	/**
	 * Write a one dimensional array of doubles
	 * 
	 * @param os The output stream
	 * @param value The one dimensional array of doubles.
	 * @throws IOException
	 */
	public static void writeDouble1A(OutputStream os, double[] value) throws IOException {
		writeInt(os, value.length);
		for (int i = 0; i < value.length; i++) {
			writeDouble(os, value[i]);
		}
	}

	/**
	 * Write a two dimensional array of doubles
	 * 
	 * @param os The output stream
	 * @param value The one dimensional array of doubles.
	 * @throws IOException
	 */
	public static void writeDouble2A(OutputStream os, double[][] value) throws IOException {
		int rows = value.length;
		int cols = rows > 0 ? value[0].length : 0;
		writeInt(os, rows);
		writeInt(os, cols);
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				writeDouble(os, value[r][c]);
			}
		}
	}

	/**
	 * Write an integer.
	 * 
	 * @param os The output steam.
	 * @param value The integer.
	 * @throws IOException
	 */
	public static void writeInt(OutputStream os, int value) throws IOException {
		writeBytes(os, ByteBuffer.allocate(Integer.BYTES).putInt(value).array());
	}

	/**
	 * Write an long.
	 * 
	 * @param os The output steam.
	 * @param value The long.
	 * @throws IOException
	 */
	public static void writeLong(OutputStream os, long value) throws IOException {
		writeBytes(os, ByteBuffer.allocate(Long.BYTES).putLong(value).array());
	}

	/**
	 * Write a string in UTF-16 character set.
	 * 
	 * @param os The output stream.
	 * @param str The string to write.
	 * @throws IOException
	 */
	public static void writeString(OutputStream os, String str) throws IOException {
		ByteBuffer bb = Charset.forName("UTF-16").encode(str);
		writeInt(os, bb.limit());
		bb.rewind();
		for (int i = 0; i < bb.limit(); i++) {
			os.write(bb.get(i));
		}
	}
}
