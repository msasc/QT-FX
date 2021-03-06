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
package com.qtfx.lib.db.rdbms;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import com.qtfx.lib.db.Field;
import com.qtfx.lib.db.FieldList;
import com.qtfx.lib.db.Record;
import com.qtfx.lib.db.Types;
import com.qtfx.lib.db.Value;
import com.qtfx.lib.util.Calendar;

/**
 * Database utilities to write to a prepared statement or to read from a resultset.
 *
 * @author Miquel Sas
 */
public class DBUtils {

	/**
	 * Return a date from a local date.
	 * 
	 * @param d The local date.
	 * @return The lava.sql.Date
	 */
	@SuppressWarnings("deprecation")
	public static Date toDate(LocalDate d) {
		return new Date(d.getYear() - 1900, d.getMonthValue() - 1, d.getDayOfMonth());
	}

	/**
	 * Return the time from a local time.
	 * 
	 * @param t The local time.
	 * @return The java.sql.Time
	 */
	@SuppressWarnings("deprecation")
	public static Time toTime(LocalTime t) {
		return new Time(t.getHour(), t.getMinute(), t.getSecond());
	}

	/**
	 * Return the time from a local date-time.
	 * 
	 * @param t The local date-time.
	 * @return The java.sql.Timestamp
	 */
	@SuppressWarnings("deprecation")
	public static Timestamp toTimestamp(LocalDateTime t) {
		return new Timestamp(t.getYear() - 199, t.getMonthValue() - 1, t.getDayOfMonth(), t.getHour(), t.getMinute(),
			t.getSecond(), t.getNano());
	}

	/**
	 * Read a record from a ResultSet.
	 *
	 * @param fieldList The field list
	 * @param rs The source result set
	 * @return The record.
	 * @throws SQLException If such an error occurs.
	 */
	public static Record readRecord(FieldList fieldList, ResultSet rs) throws SQLException {
		Record record = new Record(fieldList);
		List<Field> fields = fieldList.getFields();
		int index = 1;
		for (int i = 0; i < fields.size(); i++) {
			Field field = fields.get(i);
			Types type = field.getType();
			int decimals = field.getDecimals();
			Value value;
			if (field.isPersistent() || field.isVirtual()) {
				value = DBUtils.fromResultSet(type, decimals, index++, rs);
			} else {
				value = field.getDefaultValue();
			}
			record.setValue(i, value, false);
		}
		return record;
	}

	/**
	 * Reads a value from a result set.
	 *
	 * @param type The type
	 * @param decimals The scale
	 * @param index The index in the result set
	 * @param resultSet The result set
	 * @return The appropriate value.
	 * @throws SQLException If such an error occurs.
	 */
	public static Value fromResultSet(Types type, int decimals, int index, ResultSet resultSet) throws SQLException {
		Value value = null;
		if (type == Types.BOOLEAN) {
			String s = resultSet.getString(index);
			boolean b = (s != null && s.equals("Y"));
			value = new Value(b);
		} else if (type == Types.DECIMAL) {
			BigDecimal bd = resultSet.getBigDecimal(index);
			if (bd != null) {
				bd = bd.setScale(decimals, BigDecimal.ROUND_HALF_UP);
				value = new Value(bd);
			}
		} else if (type == Types.INTEGER) {
			value = new Value(resultSet.getInt(index));
		} else if (type == Types.LONG) {
			value = new Value(resultSet.getLong(index));
		} else if (type == Types.DOUBLE) {
			value = new Value(resultSet.getDouble(index));
		} else if (type == Types.DATE) {
			java.sql.Date date = resultSet.getDate(index);
			if (date == null) {
				value = new Value((Date) null);
			} else {
				Calendar calendar = new Calendar(date.getTime());
				value = new Value(calendar.toDate());
			}
		} else if (type == Types.TIME) {
			java.sql.Time time = resultSet.getTime(index);
			if (time == null) {
				value = new Value((Time) null);
			} else {
				Calendar calendar = new Calendar(time.getTime());
				value = new Value(calendar.toTime());
			}
		} else if (type == Types.TIMESTAMP) {
			java.sql.Timestamp timestamp = resultSet.getTimestamp(index);
			if (timestamp == null) {
				value = new Value((Timestamp) timestamp);
			} else {
				value = new Value(new Timestamp(timestamp.getTime()));
			}
		} else if (type == Types.STRING) {
			value = new Value(resultSet.getString(index));
		} else if (type == Types.BYTEARRAY) {
			value = new Value(resultSet.getBytes(index));
		}
		if (!type.isNumber() && resultSet.wasNull()) {
			value = type.getNullValue();
		}
		return value;
	}

	/**
	 * Set the value to a <code>PreparedStatement</code> parameter at the specified index.
	 *
	 * @param value The value to set to the prepared statement.
	 * @param index The parameter index.
	 * @param ps The <code>PreparedStatement</code>.
	 * @throws SQLException If such an error occurs.
	 */
	public static void toPreparedStatement(Value value, int index, PreparedStatement ps) throws SQLException {
		Types type = value.getType();
		if (value.isNull()) {
			ps.setNull(index, type.getJDBCType(0));
		} else if (type == Types.BOOLEAN) {
			ps.setString(index, (value.getBoolean() ? "Y" : "N"));
		} else if (type == Types.DECIMAL) {
			ps.setBigDecimal(index, value.getBigDecimal());
		} else if (type == Types.DOUBLE) {
			ps.setDouble(index, value.getDouble());
		} else if (type == Types.INTEGER) {
			ps.setInt(index, value.getInteger());
		} else if (type == Types.LONG) {
			ps.setLong(index, value.getLong());
		} else if (type == Types.STRING) {
			int length = value.getString().length();
			if (length <= Types.FIXED_LENGTH) {
				ps.setString(index, value.getString());
			} else {
				String string = value.getString();
				ps.setCharacterStream(index, new StringReader(string), string.length());
			}
		} else if (type == Types.BYTEARRAY) {
			byte[] bytes = value.getByteArray();
			if (bytes.length <= Types.FIXED_LENGTH) {
				ps.setBytes(index, bytes);
			} else {
				ps.setBinaryStream(index, new ByteArrayInputStream(bytes), bytes.length);
			}
		} else if (type == Types.DATE) {
			ps.setDate(index, toDate(value.getLocalDate()));
		} else if (type == Types.TIME) {
			ps.setTime(index, toTime(value.getLocalTime()));
		} else if (type == Types.TIMESTAMP) {
			ps.setTimestamp(index, toTimestamp(value.getLocalDateTime()));
		}
	}
}
