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
import java.util.List;

import com.qtfx.lib.util.Lists;
import com.qtfx.lib.util.Numbers;

/**
 * An immutable value of supported types.
 *
 * @author Miquel Sas
 */
public class Value implements Comparable<Object> {

	/** The value type. */
	private Types type;
	/** The value itself. */
	private Object value;

	/**
	 * Private constructor for internal usage.
	 */
	private Value() {
	}

	/**
	 * Copy constructor.
	 *
	 * @param v A value
	 */
	public Value(Value v) {
		super();
		if (v == null) {
			throw new NullPointerException();
		}
		type = v.type;
		value = v.value;
	}

	/**
	 * Constructor assigning a boolean.
	 *
	 * @param b A boolean
	 */
	public Value(boolean b) {
		super();
		value = b;
		type = Types.BOOLEAN;
	}

	/**
	 * Constructor assigning a boolean.
	 *
	 * @param b A boolean
	 */
	public Value(Boolean b) {
		super();
		value = b;
		type = Types.BOOLEAN;
	}

	/**
	 * Constructor assigning a string.
	 *
	 * @param s A string
	 */
	public Value(String s) {
		super();
		value = s;
		type = Types.STRING;
	}

	/**
	 * Constructor assigning a number with precision. Note that for a null big decimal the precision is set to 0.
	 *
	 * @param b A big decimal
	 */
	public Value(BigDecimal b) {
		super();
		value = b;
		type = Types.DECIMAL;
	}

	/**
	 * Constructor assigning a double.
	 *
	 * @param d A double
	 */
	public Value(Double d) {
		super();
		value = d;
		type = Types.DOUBLE;
	}

	/**
	 * Constructor assigning a double.
	 *
	 * @param d A double
	 */
	public Value(double d) {
		super();
		value = d;
		type = Types.DOUBLE;
	}

	/**
	 * Constructor assigning an integer.
	 *
	 * @param i An integer
	 */
	public Value(Integer i) {
		super();
		value = i;
		type = Types.INTEGER;
	}

	/**
	 * Constructor assigning an integer.
	 *
	 * @param i An integer
	 */
	public Value(int i) {
		super();
		value = i;
		type = Types.INTEGER;
	}

	/**
	 * Constructor assigning a long.
	 *
	 * @param l A long
	 */
	public Value(Long l) {
		super();
		value = l;
		type = Types.LONG;
	}

	/**
	 * Constructor assigning a long.
	 *
	 * @param l A long
	 */
	public Value(long l) {
		super();
		value = l;
		type = Types.LONG;
	}

	/**
	 * Constructor assigning a ByteArray
	 *
	 * @param byteArray The ByteArray
	 */
	public Value(byte[] byteArray) {
		super();
		value = byteArray;
		type = Types.BYTEARRAY;
	}

	/**
	 * Constructor assigning a date.
	 *
	 * @param d The date
	 */
	public Value(Date d) {
		super();
		value = d;
		type = Types.DATE;
	}

	/**
	 * Constructor assigning a time.
	 *
	 * @param t The time
	 */
	public Value(Time t) {
		super();
		value = t;
		type = Types.TIME;
	}

	/**
	 * Constructor assigning a time-stamp.
	 *
	 * @param t The time-stamp
	 */
	public Value(Timestamp t) {
		super();
		value = t;
		type = Types.TIMESTAMP;
	}

	/**
	 * Returns a copy of this value.
	 * 
	 * @return The copy.
	 */
	public Value getCopy() {
		Value v = new Value();
		v.type = type;
		if (isNull()) {
			return v;
		}
		switch (type) {
		case BOOLEAN:
			v.value = new Boolean(getBoolean());
			break;
		case BYTEARRAY:
			v.value = Lists.copy(getByteArray());
			break;
		case DATE:
			v.value = new Date(getDate().getTime());
			break;
		case DECIMAL:
			BigDecimal b = (BigDecimal) value;
			v.value = new BigDecimal(b.doubleValue()).setScale(b.scale(), BigDecimal.ROUND_HALF_UP);
			break;
		case DOUBLE:
			v.value = new Double(getDouble());
			break;
		case INTEGER:
			v.value = new Integer(getInteger());
			break;
		case LONG:
			v.value = new Long(getLong());
			break;
		case STRING:
			v.value = new String(getString());
			break;
		case TIME:
			v.value = new Time(getTime().getTime());
			break;
		case TIMESTAMP:
			v.value = new Timestamp(getTimestamp().getTime());
			break;
		default:
			v.value = value;
			break;
		}
		return v;
	}

	/**
	 * Returns a negative integer, zero, or a positive integer as this value is less than, equal to, or greater than the
	 * specified value.
	 *
	 * @param o The object to compare.
	 * @return The comparison integer.
	 */
	@Override
	public int compareTo(Object o) {
		// Argument can not be null
		if (o == null) {
			throw new NullPointerException();
		}
		// Must be the same type
		Value v = null;
		try {
			v = (Value) o;
		} catch (ClassCastException exc) {
			throw new UnsupportedOperationException(MessageFormat.format("Not comparable type: {0}", o.getClass().getName()));
		}

		// Null types
		if (isNull() && v.isNull()) {
			return 0;
		}
		if (isNull() && !v.isNull()) {
			return -1;
		}
		if (!isNull() && v.isNull()) {
			return 1;
		}
		// Compare only if comparable
		if (isBoolean()) {
			if (!v.isBoolean()) {
				throw new UnsupportedOperationException(MessageFormat.format("Not comparable type: {0}", o.getClass().getName()));
			}
			boolean b1 = getBoolean();
			boolean b2 = v.getBoolean();
			return (!b1 && b2 ? -1 : (b1 && !b2 ? 1 : 0));
		}
		if (isNumber()) {
			if (!v.isNumber()) {
				throw new UnsupportedOperationException(MessageFormat.format("Not comparable type: {0}", o.getClass().getName()));
			}
			double d1 = getDouble();
			double d2 = v.getDouble();
			return Double.compare(d1, d2);
		}
		if (isString()) {
			if (!v.isString()) {
				throw new UnsupportedOperationException(MessageFormat.format("Not comparable type: {0}", o.getClass().getName()));
			}
			return getString().compareTo(v.getString());
		}
		if (isDate()) {
			if (!v.isDate()) {
				throw new UnsupportedOperationException(MessageFormat.format("Not comparable type: {0}", o.getClass().getName()));
			}
			return getDate().compareTo(v.getDate());
		}
		if (isTime()) {
			if (!v.isTime()) {
				throw new UnsupportedOperationException(MessageFormat.format("Not comparable type: {0}", o.getClass().getName()));
			}
			return getTime().compareTo(v.getTime());
		}
		if (isTimestamp()) {
			if (!v.isTimestamp()) {
				throw new UnsupportedOperationException(MessageFormat.format("Not comparable type: {0}", o.getClass().getName()));
			}
			return getTimestamp().compareTo(v.getTimestamp());
		}
		if (isByteArray()) {
			if (!v.isByteArray()) {
				throw new UnsupportedOperationException(MessageFormat.format("Not comparable type: {0}", o.getClass().getName()));
			}
			return Lists.compare(getByteArray(), v.getByteArray());
		}
		throw new IllegalArgumentException(MessageFormat.format("Value {0} is not comparable", toString()));
	}

	/**
	 * Indicates whether some other object is "equal to" this one.
	 *
	 * @return A boolean.
	 * @param o The object to compare with.
	 */
	@Override
	public boolean equals(Object o) {
		// Null
		if (o == null) {
			return isNull();
		}
		if (isNull()) {
			return false;
		}
		// Boolean
		if (o instanceof Boolean) {
			if (!isBoolean()) {
				return false;
			}
			Boolean b = (Boolean) o;
			return getBoolean().equals(b);
		}
		// String
		if (o instanceof String) {
			if (!isString()) {
				return false;
			}
			String s = (String) o;
			return getString().equals(s);
		}
		// Decimal, Double, Integer, Long
		if (o instanceof Number) {
			if (!isNumber()) {
				return false;
			}
			Number n = (Number) o;
			return getNumber().equals(n);
		}
		// Date
		if (o instanceof Date) {
			if (!isDate()) {
				return false;
			}
			Date d = (Date) o;
			return getDate().equals(d);
		}
		// Time
		if (o instanceof Time) {
			if (!isTime()) {
				return false;
			}
			Time t = (Time) o;
			return getTime().equals(t);
		}
		// Timestamp
		if (o instanceof Timestamp) {
			if (!isTimestamp()) {
				return false;
			}
			Timestamp t = (Timestamp) o;
			return getTimestamp().equals(t);
		}
		// ByteArray
		if (o instanceof byte[]) {
			if (!isByteArray()) {
				return false;
			}
			byte[] byteArray = (byte[]) o;
			return Lists.equals(getByteArray(), byteArray);
		}
		// Value
		if (o instanceof Value) {
			Value v = (Value) o;
			// Types must be the same except for numbers where the number must be the same
			if ((isBoolean() && !v.isBoolean()) || (!isBoolean() && v.isBoolean())) {
				return false;
			}
			if ((isString() && !v.isString()) || (!isString() && v.isString())) {
				return false;
			}
			if ((isDateTimeOrTimestamp() && !v.isDateTimeOrTimestamp()) || (!isDateTimeOrTimestamp() && v.isDateTimeOrTimestamp())) {
				return false;
			}
			if ((isNumber() && !v.isNumber()) || (!isNumber() && v.isNumber())) {
				return false;
			}
			if ((isByteArray() && !v.isByteArray()) || (!isByteArray() && v.isByteArray())) {
				return false;
			}
			if (isBoolean()) {
				return getBoolean().equals(v.getBoolean());
			}
			if (isString()) {
				return getString().equals(v.getString());
			}
			if (isDateTimeOrTimestamp()) {
				return getTimestamp().equals(v.getTimestamp());
			}
			if (isNumber()) {
				return getNumber().equals(v.getNumber());
			}
			if (isByteArray()) {
				return Lists.equals(getByteArray(), v.getByteArray());
			}
		}
		return false;
	}

	/**
	 * Returns the hash code for this value.
	 *
	 * @return The hash code
	 */
	@Override
	public int hashCode() {
		if (!isNull()) {
			if (isBoolean()) {
				Boolean b = getBoolean();
				return b.hashCode();
			}
			if (isByteArray()) {
				return getByteArray().hashCode();
			}
			if (isNumber()) {
				Double d = getDouble();
				return d.hashCode();
			}
			if (isString()) {
				return getString().hashCode();
			}
			if (isDate()) {
				return getDate().hashCode();
			}
			if (isTime()) {
				return getTime().hashCode();
			}
			if (isTimestamp()) {
				return getTimestamp().hashCode();
			}
		}
		return Numbers.MIN_INTEGER;
	}

	/**
	 * Returns this value type.
	 *
	 * @return The type.
	 */
	public Types getType() {
		return type;
	}

	/**
	 * Get the value as a <code>boolean</code>.
	 *
	 * @return A boolean
	 */
	public Boolean getBoolean() {
		if (isBoolean()) {
			if (isNull()) {
				return false;
			}
			return ((Boolean) value);
		}
		throw new UnsupportedOperationException(MessageFormat.format("Value {0} is not a boolean", value));
	}

	/**
	 * Get the value as a <code>String</code>.
	 *
	 * @return A String
	 */
	public String getString() {
		if (!isString()) {
			throw new UnsupportedOperationException(MessageFormat.format("Value {0} is not a string", value));
		}
		return (String) value;
	}

	/**
	 * Get the value as a <code>Date</code>.
	 *
	 * @return A Date
	 */
	public Date getDate() {
		if (isDate()) {
			return (Date) value;
		}
		if (isNull()) {
			return null;
		}
		if (isTimestamp()) {
			return new Date(getTimestamp().getTime());
		}
		throw new UnsupportedOperationException(MessageFormat.format("Value {0} is not a date", value));
	}

	/**
	 * Get the value as a <code>Time</code>.
	 *
	 * @return A Time
	 */
	public Time getTime() {
		if (isTime()) {
			return (Time) value;
		}
		if (isNull()) {
			return null;
		}
		if (isTimestamp()) {
			return new Time(getTimestamp().getTime());
		}
		throw new UnsupportedOperationException(MessageFormat.format("Value {0} is not a time", value));
	}

	/**
	 * Get the value as a <code>Timestamp</code>.
	 *
	 * @return A Timestamp
	 */
	public Timestamp getTimestamp() {
		if (isTimestamp()) {
			return (Timestamp) value;
		}
		if (isNull()) {
			return null;
		}
		if (isDate()) {
			return new Timestamp(getDate().getTime());
		}
		if (isTime()) {
			return new Timestamp(getTime().getTime());
		}
		throw new UnsupportedOperationException(MessageFormat.format("Value {0} is not a date, time or time-stamp", value));
	}

	/**
	 * Get the value as a ByteArray.
	 *
	 * @return A ByteArray
	 */
	public byte[] getByteArray() {
		if (isByteArray()) {
			return (byte[]) value;
		}
		throw new UnsupportedOperationException(MessageFormat.format("Value {0} is not a byte array", value));
	}

	/**
	 * Get the value as a number if it is so.
	 *
	 * @return The number.
	 */
	public Number getNumber() {
		if (isNumber()) {
			return (Number) value;
		}
		throw new UnsupportedOperationException(MessageFormat.format("Value {0} is not a number", value));
	}

	/**
	 * Check if this value is boolean.
	 *
	 * @return A boolean.
	 */
	public boolean isBoolean() {
		return getType().isBoolean();
	}

	/**
	 * Check if this value is a string.
	 *
	 * @return A boolean.
	 */
	public boolean isString() {
		return getType().isString();
	}

	/**
	 * Check if this value is a number (decimal) with fixed precision.
	 *
	 * @return A boolean.
	 */
	public boolean isDecimal() {
		return getType().isDecimal();
	}

	/**
	 * Check if this value is a double.
	 *
	 * @return A boolean.
	 */
	public boolean isDouble() {
		return getType().isDouble();
	}

	/**
	 * Check if this value is an integer.
	 *
	 * @return A boolean.
	 */
	public boolean isInteger() {
		return getType().isInteger();
	}

	/**
	 * Check if this value is a long.
	 *
	 * @return A boolean.
	 */
	public boolean isLong() {
		return getType().isLong();
	}

	/**
	 * Check if this value is a number (decimal, double or integer).
	 *
	 * @return A boolean.
	 */
	public boolean isNumber() {
		return getType().isNumber();
	}

	/**
	 * Check if this value is a floating point number.
	 *
	 * @return A boolean.
	 */
	public boolean isFloatingPoint() {
		return getType().isFloatingPoint();
	}

	/**
	 * Check if this value is a date.
	 *
	 * @return A boolean.
	 */
	public boolean isDate() {
		return getType().isDate();
	}

	/**
	 * Check if this value is a time.
	 *
	 * @return A boolean.
	 */
	public boolean isTime() {
		return getType().isTime();
	}

	/**
	 * Check if this value is a time.
	 *
	 * @return A boolean.
	 */
	public boolean isTimestamp() {
		return getType().isTimestamp();
	}

	/**
	 * Check if this value is a date, time or time-stamp.
	 *
	 * @return A boolean.
	 */
	public boolean isDateTimeOrTimestamp() {
		return getType().isDateTimeOrTimestamp();
	}

	/**
	 * Check if this value is binary (byte[]).
	 *
	 * @return A boolean.
	 */
	public boolean isByteArray() {
		return getType().isByteArray();
	}

	/**
	 * Check if this value is null. Null is not a type, but a value can be null if the holder object its so.
	 *
	 * @return A boolean indicating if the value is null.
	 */
	public boolean isNull() {
		return (value == null);
	}

	/**
	 * Check if the value is empty, that is, null, empty string or zero if is number.
	 *
	 * @return A boolean
	 */
	public boolean isEmpty() {
		if (isNull()) {
			return true;
		}
		if (isString() && getString().length() == 0) {
			return true;
		}
		return isNumber() && getDouble() == 0;
	}

	/**
	 * Check if this value is empty or a blank string (only spaces).
	 *
	 * @return A boolean
	 */
	public boolean isBlank() {
		return isEmpty() || (isString() && getString().trim().length() == 0);
	}

	/**
	 * Get the value as a BigDecimal it it's a number, otherwise throw an exception.
	 *
	 * @return A BigDecimal
	 */
	public BigDecimal getBigDecimal() {
		if (isDecimal()) {
			return (BigDecimal) value;
		}
		if (isNumber()) {
			if (isDouble()) {
				return new BigDecimal(getDouble());
			}
			return new BigDecimal(getLong());
		}
		throw new UnsupportedOperationException(MessageFormat.format("Value {0} is not a number", value));
	}

	/**
	 * Get the value as a double it it's a number, otherwise throw an exception.
	 *
	 * @return A double
	 */
	public Double getDouble() {
		if (isNumber()) {
			if (isNull()) {
				return (double) 0;
			}
			return ((Number) value).doubleValue();
		}
		throw new UnsupportedOperationException(MessageFormat.format("Value {0} is not a number", value));
	}

	/**
	 * Get the value as an <code>int</code>.
	 *
	 * @return An integer
	 */
	public Integer getInteger() {
		if (isNumber()) {
			if (isNull()) {
				return (int) 0;
			}
			return ((Number) value).intValue();
		}
		throw new UnsupportedOperationException(MessageFormat.format("Value {0} is not a number", value));
	}

	/**
	 * Get the value as an <code>long</code>.
	 *
	 * @return A long
	 */
	public Long getLong() {
		if (isNumber()) {
			if (isNull()) {
				return (long) 0;
			}
			return ((Number) value).longValue();
		}
		throw new UnsupportedOperationException(MessageFormat.format("Value {0} is not a number", value));
	}

	/**
	 * Privately access the value
	 *
	 * @return The value
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Returns a string representation of this value.
	 *
	 * @return A string
	 */
	@Override
	public String toString() {
		if (isNull()) {
			return "null";
		}
		if (isByteArray()) {
			return new String((byte[]) value);
		}
		return value.toString();
	}

	/**
	 * Returns true if this value is in the list, false otherwise.
	 *
	 * @param values The list of values to check.
	 * @return True if this value is in the list.
	 */
	public boolean in(Value... values) {
		return Lists.in(this, values);
	}

	/**
	 * Returns true if this value is in the list, false otherwise.
	 *
	 * @param values The list of values to check.
	 * @return True if this value is in the list.
	 */
	public boolean in(List<Value> values) {
		return Lists.in(this, values);
	}

	/**
	 * Returns true if this value is not in the list, false otherwise.
	 *
	 * @param values The list of values to check.
	 * @return True if this value is not in the list.
	 */
	public boolean notIn(Value... values) {
		return !Lists.in(this, values);
	}

	/**
	 * Returns true if this value is not in the list, false otherwise.
	 *
	 * @param values The list of values to check.
	 * @return True if this value is not in the list.
	 */
	public boolean notIn(List<Value> values) {
		return !Lists.in(this, values);
	}
}
