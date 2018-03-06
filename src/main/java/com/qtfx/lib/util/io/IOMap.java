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

package com.qtfx.lib.util.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * A map to save/restore string keyed primitive types.
 *
 * @author Miquel Sas
 */
public class IOMap {

	/////////////////////////////
	// Internal type identifiers.

	private static final byte TYPE_INTEGER = 1;
	private static final byte TYPE_DOUBLE = 2;
	private static final byte TYPE_DOUBLE1A = 3;
	private static final byte TYPE_DOUBLE2A = 4;
	private static final byte TYPE_STRING = 5;
	
	///////////////////////////
	// Internal data container.

	/**
	 * Data container.
	 */
	static class Data {
		byte type;
		Object data;

		Data(byte type, Object data) {
			this.type = type;
			this.data = data;
			switch (type) {
			case TYPE_INTEGER:
				this.data = (Integer) data;
			case TYPE_DOUBLE:
				this.data = (int[]) data;
			case TYPE_DOUBLE1A:
			case TYPE_DOUBLE2A:
			case TYPE_STRING:
			}
		}
	}

	/** Underlying map. */
	private Map<String, Data> map = new HashMap<>();

	/**
	 * Default constructor.
	 */
	public IOMap() {
		super();
	}

	//////////////////
	// Put operations.

	public void putInteger(String key, int value) {
		map.put(key, new Data(TYPE_INTEGER, value));
	}

	public void putDouble(String key, double value) {
		map.put(key, new Data(TYPE_DOUBLE, value));
	}

	public void putDouble1A(String key, double[] value) {
		map.put(key, new Data(TYPE_DOUBLE1A, value));
	}

	public void putDouble2A(String key, double[][] value) {
		map.put(key, new Data(TYPE_DOUBLE2A, value));
	}

	public void putString(String key, String value) {
		map.put(key, new Data(TYPE_STRING, value));
	}

	//////////////////
	// Get operations.

	public int getInt(String key) {
		Data data = map.get(key);
		if (data == null || data.type == TYPE_INTEGER) {
			return (Integer) data.data;
		}
		throw new IllegalArgumentException("Keyed value is not an int.");
	}

	public double getDouble(String key) {
		Data data = map.get(key);
		if (data == null || data.type == TYPE_DOUBLE) {
			return (Double) data.data;
		}
		throw new IllegalArgumentException("Keyed value is not a double.");
	}

	public double[] getDouble1A(String key) {
		Data data = map.get(key);
		if (data == null || data.type == TYPE_DOUBLE1A) {
			return (double[]) data.data;
		}
		throw new IllegalArgumentException("Keyed value is not a double[].");
	}

	public double[][] getDouble2A(String key) {
		Data data = map.get(key);
		if (data == null || data.type == TYPE_DOUBLE2A) {
			return (double[][]) data.data;
		}
		throw new IllegalArgumentException("Keyed value is not a double[][].");
	}

	public String getString(String key) {
		Data data = map.get(key);
		if (data == null || data.type == TYPE_STRING) {
			return (String) data.data;
		}
		throw new IllegalArgumentException("Keyed value is not a String.");
	}

	//////////////////
	// Save operation.

	public void save(OutputStream os) throws IOException {
		IO.writeInt(os, map.size());
		Iterator<String> keys = map.keySet().iterator();
		while (keys.hasNext()) {
			String key = keys.next();
			Data data = map.get(key);
			Byte type = data.type;
			IO.writeString(os, key);
			switch (type) {
			case TYPE_INTEGER:
				IO.writeInt(os, getInt(key));
				break;
			case TYPE_DOUBLE:
				IO.writeDouble(os, getDouble(key));
				break;
			case TYPE_DOUBLE1A:
				IO.writeDouble1A(os, getDouble1A(key));
				break;
			case TYPE_DOUBLE2A:
				IO.writeDouble2A(os, getDouble2A(key));
				break;
			case TYPE_STRING:
				IO.writeString(os, getString(key));
				break;
			}
		}
	}

	/////////////////////
	// Restore operation.

	public void restore(InputStream is) throws IOException {
		map.clear();
		int size = IO.readInt(is);
		for (int i = 0; i < size; i++) {
			String key = IO.readString(is);
			byte type = IO.readByte(is);
			Object value = null;
			switch (type) {
			case TYPE_INTEGER:
				value = IO.readInt(is);
				break;
			case TYPE_DOUBLE:
				value = IO.readDouble(is);
				break;
			case TYPE_DOUBLE1A:
				value = IO.readDouble1A(is);
				break;
			case TYPE_DOUBLE2A:
				value = IO.readDouble2A(is);
				break;
			case TYPE_STRING:
				value = IO.readString(is);
				break;
			}

			map.put(key, new Data(type, value));
		}
	}
}
