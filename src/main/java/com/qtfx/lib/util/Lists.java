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
package com.qtfx.lib.util;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import com.qtfx.lib.db.Value;

/**
 * Array and list utility functions.
 *
 * @author Miquel Sas
 */
public class Lists {

	/**
	 * Add the element to the first position of a list.
	 *
	 * @param <T> The type.
	 * @param e The element to add.
	 * @param list The list.
	 */
	public static <T> void addFirst(T e, List<T> list) {
		list.add(0, e);
	}

	/**
	 * Add the element to the last position of a list.
	 *
	 * @param <T> The type.
	 * @param e The element to add.
	 * @param list The list.
	 */
	public static <T> void addLast(T e, List<T> list) {
		list.add(e);
	}

	/**
	 * Returns a list given the argument array.
	 * 
	 * @param array The array.
	 * @return The list.
	 */
	public static List<Double> asList(double... array) {
		List<Double> list = new ArrayList<>();
		for (double element : array) {
			list.add(element);
		}
		return list;
	}

	/**
	 * Returns a list given the argument array.
	 * 
	 * @param array The array.
	 * @return The list.
	 */
	public static List<Integer> asList(int... array) {
		List<Integer> list = new ArrayList<>();
		for (int element : array) {
			list.add(element);
		}
		return list;
	}

	/**
	 * Returns a list given the argument array.
	 * 
	 * @param <T> The type.
	 * @param array The array.
	 * @return The list.
	 */
	@SafeVarargs
	public static <T> List<T> asList(T... array) {
		List<T> list = new ArrayList<>();
		for (T element : array) {
			list.add(element);
		}
		return list;
	}

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
	 * Compares an array list of comparable objects to the argument object. Returns a negative integer, zero, or a
	 * positive integer as this list is less than, equal to, or greater than the specified argument list. Throws an
	 * UnsupportedOperationException if the argument is not an
	 *
	 * @param list The source array list of comparable objects.
	 * @param o The object to compare.
	 * @return The comparison integer.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static int compare(List list, Object o) {
		List<Comparable> comparable = null;
		try {
			comparable = ((ArrayList) o);
		} catch (ClassCastException exc) {
			throw new UnsupportedOperationException(
				MessageFormat.format("Not comparable type: {0}", o.getClass().getName()));
		}
		if (list.isEmpty() && comparable.size() > 0) {
			return -1;
		}
		if (list.size() > 0 && comparable.isEmpty()) {
			return 1;
		}
		for (int i = 0; i < list.size(); i++) {
			Comparable c1 = (Comparable) list.get(i);
			Comparable c2 = comparable.get(i);
			int compare = c1.compareTo(c2);
			if (compare == 0) {
				if (i == list.size() - 1 && i < comparable.size() - 1) {
					return -1;
				}
				if (i < list.size() - 1 && i == comparable.size() - 1) {
					return 1;
				}
				continue;
			}
			return compare;
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
		byte[] b = new byte[a.length];
		for (int i = 0; i < a.length; i++) {
			b[i] = a[i];
		}
		return b;
	}

	/**
	 * Return a copy of the source array.
	 * 
	 * @param a The source array.
	 * @return The copy.
	 */
	public static Value[] copy(Value[] a) {
		Value[] b = new Value[a.length];
		for (int i = 0; i < a.length; i++) {
			b[i] = new Value(a[i]);
		}
		return b;
	}

	/**
	 * Check if both arrays are equal.
	 * 
	 * @param a The first array.
	 * @param b The second array.
	 * @return
	 */
	public static boolean equals(byte[] a, byte[] b) {
		if (a == b) {
			return true;
		}
		if (a == null || b == null) {
			return false;
		}
		int length = a.length;
		if (b.length != length) {
			return false;
		}
		for (int i = 0; i < length; i++) {
			if (a[i] != b[i]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Check whether two lists are equal.
	 * 
	 * @param l1 List 1.
	 * @param l2 List 2.
	 * @return A boolean.
	 */
	public static boolean equals(List<?> l1, List<?> l2) {
		if (l1.size() != l2.size()) {
			return false;
		}
		for (int i = 0; i < l1.size(); i++) {
			if (l1.get(i) == null && l2.get(i) != null) {
				return false;
			}
			if (l1.get(i) != null && l2.get(i) == null) {
				return false;
			}
			if (l1.get(i) == null && l2.get(i) == null) {
				continue;
			}
			if (!l1.get(i).equals(l2.get(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns the first element of a list.
	 * 
	 * @param <T> The type.
	 * @param list The list.
	 * @return The first element.
	 */
	public static <T> T getFirst(List<T> list) {
		if (list == null || list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}

	/**
	 * Returns the last element of a list.
	 * 
	 * @param <T> The type.
	 * @param list The list.
	 * @return The last element.
	 */
	public static <T> T getLast(List<T> list) {
		if (list == null || list.isEmpty()) {
			return null;
		}
		return list.get(list.size() - 1);
	}

	/**
	 * Check in the list.
	 * 
	 * @param chr The check char.
	 * @param chars The list of chars.
	 * @return A boolean.
	 */
	public static boolean in(char chr, char... chars) {
		for (char c : chars) {
			if (c == chr) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Check in the list.
	 * 
	 * @param value The value to check.
	 * @param values The list of values.
	 * @return A boolean.
	 */
	@SafeVarargs
	public static <T> boolean in(T value, T... values) {
		for (T v : values) {
			if (v.equals(value)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Check in the list.
	 * 
	 * @param value The value to check.
	 * @param values The list of values.
	 * @return A boolean.
	 */
	public static <T> boolean in(T value, List<T> values) {
		for (T v : values) {
			if (v.equals(value)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the index of this value in a list of values, or -1 if this value is not in the list.
	 *
	 * @param value The value to check.
	 * @param values The list of values.
	 * @return The index of this value in the list or -1.
	 */
	@SafeVarargs
	public static <T> int indexOf(T value, T... values) {
		for (int index = 0; index < values.length; index++) {
			if (values[index].equals(value)) {
				return index;
			}
		}
		return -1;
	}

	/**
	 * Returns the index of this value in a list of values, or -1 if this value is not in the list.
	 *
	 * @param value The value to check.
	 * @param values The list of values.
	 * @return The index of this value in the list or -1.
	 */
	public static <T> int indexOf(T value, List<T> values) {
		return values.indexOf(value);
	}

	/**
	 * Randomly get an element of the list.
	 * 
	 * @param <T> The type.
	 * @param list The list.
	 * @return The selected element.
	 */
	public static <T> T randomGet(List<T> list) {
		return list.get(Random.nextInt(list.size()));
	}

	/**
	 * Remove the first element in the list.
	 * 
	 * @param <T> The type.
	 * @param list The list.
	 * @return The removed element.
	 */
	public static <T> T removeFirst(List<T> list) {
		return list.remove(0);
	}

	/**
	 * Remove the last element in the list.
	 * 
	 * @param <T> The type.
	 * @param list The list.
	 * @return The removed element.
	 */
	public static <T> T removeLast(List<T> list) {
		return list.remove(list.size() - 1);
	}

	/**
	 * Returns the array of double values given the list.
	 * 
	 * @param list The list of doubles.
	 * @return The array.
	 */
	public static double[] toDoubleArray(List<Double> list) {
		double[] values = new double[list.size()];
		for (int i = 0; i < list.size(); i++) {
			values[i] = list.get(i);
		}
		return values;
	}

	/**
	 * Returns the array of int values given the list.
	 * 
	 * @param list The list of doubles.
	 * @return The array.
	 */
	public static int[] toIntegerArray(List<Integer> list) {
		int[] values = new int[list.size()];
		for (int i = 0; i < list.size(); i++) {
			values[i] = list.get(i);
		}
		return values;
	}
}
