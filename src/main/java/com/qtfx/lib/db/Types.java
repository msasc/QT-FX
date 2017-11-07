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

package com.qtfx.lib.db;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

/**
 * Supported types.
 *
 * @author Miquel Sas
 */
public enum Types {
	/** Boolean. */
	BOOLEAN,
	/** String. */
	STRING,
	/** Decimal. */
	DECIMAL,
	/** Double. */
	DOUBLE,
	/** Integer. */
	INTEGER,
	/** Long. */
	LONG,
	/** Date. */
	DATE,
	/** Time. */
	TIME,
	/** Time-stamp. */
	TIMESTAMP,
	/** Binary (byte array). */
	BYTEARRAY;

	/**
	 * The fixed length to select between VARCHAR/VARBINARY or LONVARCHAR/LONGVARBINARY.
	 */
	public static final int FIXED_LENGTH = 2000;

	/**
	 * Return the type of the argument value.
	 * 
	 * @param value The value.
	 * @return The type.
	 */
	public static Types getType(Object value) {
		if (value instanceof Boolean) {
			return BOOLEAN;
		}
		if (value instanceof String) {
			return STRING;
		}
		if (value instanceof BigDecimal) {
			return DECIMAL;
		}
		if (value instanceof Double) {
			return DOUBLE;
		}
		if (value instanceof Integer) {
			return INTEGER;
		}
		if (value instanceof Long) {
			return LONG;
		}
		if (value instanceof Date) {
			return DATE;
		}
		if (value instanceof Time) {
			return TIME;
		}
		if (value instanceof Timestamp) {
			return Types.TIMESTAMP;
		}
		if (value instanceof byte[]) {
			return Types.BYTEARRAY;
		}
		throw new IllegalArgumentException("Not supported type: ");
	}

	/**
	 * Check that the value has the proper type.
	 * 
	 * @param value The value.
	 * @param type The required type.
	 * @throws IllegalArgumentException If the value has not the argument type.
	 */
	public static void check(Object value, Types type) {
		if (getType(value) != type) {
			throw new IllegalArgumentException("Invalid value type.");
		}
	}

	/**
	 * Check if this value is a boolean.
	 *
	 * @param value The value hose type is checked.
	 * @return A boolean
	 */
	public static boolean isBoolean(Object value) {
		return getType(value).isBoolean();
	}

	/**
	 * Check if this value is a string.
	 *
	 * @param value The value hose type is checked.
	 * @return A boolean
	 */
	public static boolean isString(Object value) {
		return getType(value).isString();
	}

	/**
	 * Check if this value is a number with fixed precision (decimal)
	 *
	 * @param value The value hose type is checked.
	 * @return A boolean
	 */
	public static boolean isDecimal(Object value) {
		return getType(value).isDecimal();
	}

	/**
	 * Check if this value is a double.
	 *
	 * @param value The value hose type is checked.
	 * @return A boolean
	 */
	public static boolean isDouble(Object value) {
		return getType(value).isDouble();
	}

	/**
	 * Check if this value is an integer.
	 *
	 * @param value The value hose type is checked.
	 * @return A boolean
	 */
	public static boolean isInteger(Object value) {
		return getType(value).isInteger();
	}

	/**
	 * Check if this value is a long.
	 *
	 * @param value The value hose type is checked.
	 * @return A boolean
	 */
	public static boolean isLong(Object value) {
		return getType(value).isLong();
	}

	/**
	 * Check if this value is a number value of value (decimal, double or integer)
	 *
	 * @param value The value hose type is checked.
	 * @return A boolean
	 */
	public static boolean isNumber(Object value) {
		return getType(value).isNumber();
	}

	/**
	 * Check if this value is a numeric floating point.
	 *
	 * @param value The value hose type is checked.
	 * @return A boolean.
	 */
	public static boolean isFloatingPoint(Object value) {
		return getType(value).isFloatingPoint();
	}

	/**
	 * Check if this value is a date.
	 *
	 * @param value The value hose type is checked.
	 * @return A boolean
	 */
	public static boolean isDate(Object value) {
		return getType(value).isDate();
	}

	/**
	 * Check if this value is a time.
	 *
	 * @param value The value hose type is checked.
	 * @return A boolean
	 */
	public static boolean isTime(Object value) {
		return getType(value).isTime();
	}

	/**
	 * Check if this value is a timestamp.
	 *
	 * @param value The value hose type is checked.
	 * @return A boolean
	 */
	public static boolean isTimestamp(Object value) {
		return getType(value).isTimestamp();
	}

	/**
	 * Check if this value is date, time or timestamp.
	 *
	 * @param value The value hose type is checked.
	 * @return A boolean
	 */
	public static boolean isDateTimeOrTimestamp(Object value) {
		return getType(value).isDateTimeOrTimestamp();
	}

	/**
	 * Check if this value is a byte array.
	 *
	 * @param value The value hose type is checked.
	 * @return A boolean
	 */
	public static boolean isByteArray(Object value) {
		return getType(value).isByteArray();
	}

	/**
	 * Check if this type is a boolean.
	 *
	 * @return A boolean
	 */
	public boolean isBoolean() {
		return equals(BOOLEAN);
	}

	/**
	 * Check if this type is a string.
	 *
	 * @return A boolean
	 */
	public boolean isString() {
		return equals(STRING);
	}

	/**
	 * Check if this type is a number with fixed precision (decimal)
	 *
	 * @return A boolean
	 */
	public boolean isDecimal() {
		return equals(DECIMAL);
	}

	/**
	 * Check if this type is a double.
	 *
	 * @return A boolean
	 */
	public boolean isDouble() {
		return equals(DOUBLE);
	}

	/**
	 * Check if this type is an integer.
	 *
	 * @return A boolean
	 */
	public boolean isInteger() {
		return equals(INTEGER);
	}

	/**
	 * Check if this type is a long.
	 *
	 * @return A boolean
	 */
	public boolean isLong() {
		return equals(LONG);
	}

	/**
	 * Check if this value is a number type of value (decimal, double or integer)
	 *
	 * @return A boolean
	 */
	public boolean isNumber() {
		return isDecimal() || isDouble() || isInteger() || isLong();
	}

	/**
	 * Check if this type is a numeric floating point.
	 *
	 * @return A boolean.
	 */
	public boolean isFloatingPoint() {
		return isDouble();
	}

	/**
	 * Check if this type is a date.
	 *
	 * @return A boolean
	 */
	public boolean isDate() {
		return equals(DATE);
	}

	/**
	 * Check if this type is a time.
	 *
	 * @return A boolean
	 */
	public boolean isTime() {
		return equals(TIME);
	}

	/**
	 * Check if this type is a timestamp.
	 *
	 * @return A boolean
	 */
	public boolean isTimestamp() {
		return equals(TIMESTAMP);
	}

	/**
	 * Check if this type is date, time or timestamp.
	 *
	 * @return A boolean
	 */
	public boolean isDateTimeOrTimestamp() {
		return isDate() || isTime() || isTimestamp();
	}

	/**
	 * Check if this type is a byte array.
	 *
	 * @return A boolean
	 */
	public boolean isByteArray() {
		return equals(BYTEARRAY);
	}
}
