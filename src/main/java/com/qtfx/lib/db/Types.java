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
package com.qtfx.lib.db;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.MessageFormat;

/**
 * Types supported by the system.
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
	BYTEARRAY,
	/** Value. */
	VALUE;

	/**
	 * The fixed length to select between VARCHAR/VARBINARY or LONVARCHAR/LONGVARBINARY.
	 */
	public static final int FIXED_LENGTH = 2000;
	
	/**
	 * Validates that the type of the argument value is valid for this field.
	 *
	 * @param value The value to validate.
	 * @param type The required type.
	 */
	public static void validateValueType(Value value, Types type) {
		if ((type.isBoolean() && !value.isBoolean())
			|| (type.isDateTimeOrTimestamp() && !value.isDateTimeOrTimestamp())
			|| (type.isString() && !value.isString())
			|| (type.isNumber() && !value.isNumber())) {
			throw new IllegalArgumentException(
				MessageFormat.format("Invalid value type ({0}) for field type {1}", value.getType(), type));
		}
	}

	/**
	 * Returns the null value for this field.
	 *
	 * @return The null value.
	 */
	public Value getNullValue() {
		if (isBoolean()) {
			return new Value((Boolean) null);
		}
		if (isByteArray()) {
			return new Value((byte[]) null);
		}
		if (isDate()) {
			return new Value((Date) null);
		}
		if (isDecimal()) {
			return new Value((BigDecimal) null);
		}
		if (isDouble()) {
			return new Value((Double) null);
		}
		if (isInteger()) {
			return new Value((Integer) null);
		}
		if (isLong()) {
			return new Value((Long) null);
		}
		if (isString()) {
			return new Value((String) null);
		}
		if (isTime()) {
			return new Value((Time) null);
		}
		if (isTimestamp()) {
			return new Value((Timestamp) null);
		}
		return null;
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
	 * Check if this type is a ByteArray.
	 *
	 * @return A boolean
	 */
	public boolean isByteArray() {
		return equals(BYTEARRAY);
	}

	/**
	 * Converts this type to a JDBV type
	 *
	 * @param length The length for string and binary data.
	 * @return The JDBC type.
	 */
	public int getJDBCType(int length) {
		switch (this) {
		case STRING:
			if (length <= FIXED_LENGTH) {
				return java.sql.Types.VARCHAR;
			}
			return java.sql.Types.LONGVARCHAR;
		case DECIMAL:
			return java.sql.Types.DECIMAL;
		case BOOLEAN:
			return java.sql.Types.CHAR;
		case DOUBLE:
			return java.sql.Types.DOUBLE;
		case INTEGER:
			return java.sql.Types.INTEGER;
		case LONG:
			return java.sql.Types.BIGINT;
		case DATE:
			return java.sql.Types.DATE;
		case TIME:
			return java.sql.Types.TIME;
		case TIMESTAMP:
			return java.sql.Types.TIMESTAMP;
		case BYTEARRAY:
			if (length <= FIXED_LENGTH) {
				return java.sql.Types.VARBINARY;
			}
			return java.sql.Types.LONGVARBINARY;
		default:
			break;
		}
		throw new IllegalArgumentException(MessageFormat.format("Unsupported type conversion to JDBC: {0}", this));
	}

	/**
	 * Returns the type with the given name, not case sensitive.
	 * 
	 * @param typeName The type name.
	 * @return The type.
	 * @throws IllegalArgumentException if the type name is not supported.
	 */
	public static Types parseType(String typeName) {
		Types[] types = values();
		for (Types type : types) {
			if (type.name().toLowerCase().equals(typeName.toLowerCase())) {
				return type;
			}
		}
		throw new IllegalArgumentException(MessageFormat.format("Unsupported type name: {0}", typeName));
	}
}
