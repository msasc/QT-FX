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

import com.qtfx.lib.db.Field;
import com.qtfx.lib.db.Types;
import com.qtfx.lib.db.rdbms.DBEngineAdapter;
import com.qtfx.lib.db.rdbms.DataSourceInfo;
import com.qtfx.lib.db.rdbms.adapters.sql.OracleCreateSchema;
import com.qtfx.lib.db.rdbms.adapters.sql.OracleDropSchema;
import com.qtfx.lib.db.rdbms.sql.CreateSchema;
import com.qtfx.lib.db.rdbms.sql.DropSchema;
import com.qtfx.lib.util.Formats;

/**
 * The Oracle database adapter.
 * 
 * @author Miquel Sas
 */
public class OracleAdapter extends DBEngineAdapter {

	/**
	 * Default constructor.
	 */
	public OracleAdapter() {
		super();
		setDriverClassName("oracle.jdbc.driver.OracleDriver");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DataSource getDataSource(DataSourceInfo info) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getCurrentDate() {
		return "SYSDATE";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getCurrentTime() {
		return "SYSDATE";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getCurrentTimestamp() {
		return "SYSDATE";
	}

	/**
	 * {@inheritDoc}
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
			if (field.getLength() <= Types.FIXED_LENGTH) {
				b.append("RAW");
				b.append("(");
				b.append(field.getLength());
				b.append(")");
			} else {
				b.append("LONG RAW");
			}
		} else if (type == Types.STRING) {
			if (field.getLength() <= Types.FIXED_LENGTH) {
				b.append("VARCHAR2");
				b.append("(");
				b.append(Math.min(field.getLength(), Types.FIXED_LENGTH));
				b.append(")");
			} else {
				b.append("LONG");
			}
		} else if (type == Types.DECIMAL) {
			b.append("NUMBER");
			b.append("(");
			b.append(field.getLength());
			b.append(",");
			b.append(field.getDecimals());
			b.append(")");
		} else if (type == Types.DOUBLE) {
			b.append("NUMBER");
		} else if (type == Types.LONG) {
			b.append("NUMBER");
		} else if (type == Types.INTEGER) {
			b.append("NUMBER");
		} else if (type == Types.DATE) {
			b.append("DATE");
		} else if (type == Types.TIME) {
			b.append("DATE");
		} else if (type == Types.TIMESTAMP) {
			b.append("DATE");
		} else {
			throw new IllegalArgumentException("Invalid field type to create the field");
		}

		return b.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isExplicitRelation() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toStringSQL(Date date) {
		String sdate = Formats.unformattedFromDate(date);
		return "TO_DATE('" + sdate + "','SYYYYMMDD')";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toStringSQL(Time time) {
		String stime = Formats.unformattedFromTime(time);
		return "TO_DATE('" + stime + "','HH24MISS')";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toStringSQL(Timestamp timestamp) {
		String stime = Formats.unformattedFromTimestamp(timestamp, false);
		return "TO_DATE('" + stime + "','YYYYMMDDHH24MISS')";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CreateSchema getStatementCreateSchema(String schema) {
		OracleCreateSchema createSchema = new OracleCreateSchema();
		createSchema.setDBEngineAdapter(this);
		createSchema.setSchema(schema);
		return createSchema;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DropSchema getStatementDropSchema(String schema) {
		OracleDropSchema dropSchema = new OracleDropSchema();
		dropSchema.setDBEngineAdapter(this);
		dropSchema.setSchema(schema);
		return dropSchema;
	}

}
