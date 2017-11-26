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

package com.qtfx.lib.db.rdbms.adapters;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import javax.sql.DataSource;

import org.postgresql.jdbc3.Jdbc3PoolingDataSource;

import com.qtfx.lib.db.Field;
import com.qtfx.lib.db.Types;
import com.qtfx.lib.db.rdbms.DBEngineAdapter;
import com.qtfx.lib.db.rdbms.DataSourceInfo;

/**
 * PostgreSQL database adapter.
 * 
 * @author Miquel Sas
 */
public class PostgreSQLAdapter extends DBEngineAdapter {

	/**
	 * Default constructor.
	 */
	public PostgreSQLAdapter() {
		super();
		setDriverClassName("org.postgresql.Driver");
	}

	/**
	 * Returns a appropriate data source.
	 * 
	 * @param info The data source info.
	 * @return The data source.
	 */
	@Override
	public DataSource getDataSource(DataSourceInfo info) {
		Jdbc3PoolingDataSource source = new Jdbc3PoolingDataSource();
		source.setDataSourceName(info.getDataSourceName());
		source.setServerName(info.getServerName());
		source.setPortNumber(info.getPortNumber());
		source.setDatabaseName(info.getDatabase());
		source.setUser(info.getUser());
		source.setPassword(info.getPassword());
		source.setMaxConnections(10);
		return source;
	}

	/**
	 * Returns the CURRENT DATE function as a string.
	 *
	 * @return The CURRENT DATE function as a string.
	 */
	@Override
	public String getCurrentDate() {
		return "current_date";
	}

	/**
	 * Returns the CURRENT TIME function as a string.
	 *
	 * @return The CURRENT TIME function as a string.
	 */
	@Override
	public String getCurrentTime() {
		return "current_time";
	}

	/**
	 * Returns the CURRENT TIMESTAMP function as a string.
	 *
	 * @return The CURRENT TIMESTAMP function as a string.
	 */
	@Override
	public String getCurrentTimestamp() {
		return "current_timestamp";
	}

	/**
	 * Gets the field definition to use in a <code>CREATE TABLE</code> statement, given a field.
	 *
	 * @return The field definition.
	 * @param field The field.
	 */
	@Override
	public String getFieldDefinition(Field field) {
		StringBuilder b = new StringBuilder();

		b.append(field.getNameCreate());
		b.append(" ");

		Types type = field.getType();
		if (type == Types.BOOLEAN) {
			b.append("CHAR(1)");
		} else if (type == Types.BYTEARRAY) {
			b.append("BYTEA");
		} else if (type == Types.STRING) {
			if (field.getLength() <= Types.FIXED_LENGTH) {
				b.append("CHARACTER VARYING");
				b.append("(");
				b.append(Math.min(field.getLength(), Types.FIXED_LENGTH));
				b.append(")");
			} else {
				b.append("TEXT");
			}
		} else if (type == Types.DECIMAL) {
			b.append("DECIMAL");
			b.append("(");
			b.append(field.getLength());
			b.append(",");
			b.append(field.getDecimals());
			b.append(")");
		} else if (type == Types.DOUBLE) {
			b.append("DOUBLE PRECISION");
		} else if (type == Types.LONG) {
			b.append("BIGINT");
		} else if (type == Types.INTEGER) {
			b.append("INTEGER");
		} else if (type == Types.DATE) {
			b.append("DATE");
		} else if (type == Types.TIME) {
			b.append("TIME");
		} else if (type == Types.TIMESTAMP) {
			b.append("TIMESTAMP");
		} else {
			throw new IllegalArgumentException("Invalid field type to create the field");
		}

		return b.toString();
	}

	/**
	 * Check if the underlying database accepts explicit relations.
	 *
	 * @return A boolean.
	 */
	@Override
	public boolean isExplicitRelation() {
		return true;
	}

	/**
	 * Return a string representation of the date, valid to be used in an SQL statement.
	 * 
	 * @param date The date.
	 * @return The representation.
	 */
	@Override
	public String toStringSQL(Date date) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Return a string representation of the time, valid to be used in an SQL statement.
	 * 
	 * @param time The time.
	 * @return The representation.
	 */
	@Override
	public String toStringSQL(Time time) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Return a string representation of the time-stamp, valid to be used in an SQL statement.
	 * <p>
	 * 
	 * @param time-stamp The time-stamp.
	 * @return The representation.
	 */
	@Override
	public String toStringSQL(Timestamp timestamp) {
		throw new UnsupportedOperationException();
	}

}
