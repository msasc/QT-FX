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

/**
 * The Derby database adapter.
 *
 * @author Miquel Sas
 */
public class DerbyAdapter extends DBEngineAdapter {

	/**
	 * Default constructor.
	 */
	protected DerbyAdapter() {
		super();
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
		return "CURRENT_DATE";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getCurrentTime() {
		return "CURRENT_TIME";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getCurrentTimestamp() {
		return "CURRENT_TIMESTAMP";
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
			b.append("BLOB");
			b.append("(");
			b.append(field.getLength());
			b.append(")");
		} else if (type == Types.STRING) {
			if (field.getLength() <= Types.FIXED_LENGTH) {
				b.append("VARCHAR");
				b.append("(");
				b.append(Math.min(field.getLength(), Types.FIXED_LENGTH));
				b.append(")");
			} else {
				b.append("CLOB");
			}
		} else if (type == Types.DECIMAL) {
			b.append("DECIMAL");
			b.append("(");
			b.append(field.getLength());
			b.append(",");
			b.append(field.getDecimals());
			b.append(")");
		} else if (type == Types.DOUBLE) {
			b.append("DOUBLE");
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
			throw new IllegalArgumentException("Invalid field type");
		}

		return b.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isExplicitRelation() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toStringSQL(Date date) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toStringSQL(Time time) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toStringSQL(Timestamp timestamp) {
		throw new UnsupportedOperationException();
	}
}
