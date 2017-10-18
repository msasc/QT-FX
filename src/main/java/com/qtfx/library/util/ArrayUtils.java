/*
 * Copyright (C) 2015 Miquel Sas
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

package com.qtfx.library.util;

import java.util.Arrays;

/**
 * Array utilities that wrap <code>Arrays</code> and add several new ones.
 * 
 * @author Miquel Sas
 */
public class ArrayUtils {

	/**
	 * Compare two byte arrays.
	 * 
	 * @param a The first array.
	 * @param b The second array.
	 * @return -1, 0 or 1 if a is less, equal or greater than b.
	 */
	public static int compare(byte[] a, byte[] b) {
		int length = Math.max(a.length, b.length);
		for (int i = 0; i < length; i++) {
			int ia = (i < a.length ? a[i] : 0);
			int ib = (i < b.length ? b[i] : 0);
			if (ia < ib) {
				return -1;
			}
			if (ia > ib) {
				return 1;
			}
		}
		return 0;
	}

	/**
	 * Return a copy of the source array.
	 * 
	 * @param a The source array.
	 * @return The copy.
	 */
	public static byte[] copy(byte[] a) {
		return Arrays.copyOf(a, a.length);
	}

	/**
	 * Check if both arrays are equal.
	 * 
	 * @param a The first array.
	 * @param b The second array.
	 * @return
	 */
	public static boolean equals(byte[] a, byte[] b) {
		return Arrays.equals(a, b);

	}
}
