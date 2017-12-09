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

import java.sql.SQLException;

import com.qtfx.lib.db.PersistorException;
import com.qtfx.lib.db.Record;
import com.qtfx.lib.db.RecordIterator;

/**
 * A database record iterator.
 * 
 * @author Miquel Sas
 */
public class DBRecordIterator implements RecordIterator {
	
	/**
	 * The underlying cursor.
	 */
	private Cursor cursor;

	/**
	 * Constructor.
	 * 
	 * @param cursor The underlying cursor.
	 */
	public DBRecordIterator(Cursor cursor) {
		super();
		this.cursor = cursor;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasNext() {
		if (cursor.isClosed()) {
			return false;
		}
		boolean next = false;
		try {
			next = cursor.nextRecord();
		} catch (SQLException e) {
			silentlyClose();
		}
		if (!next) {
			silentlyClose();
		}
		return next;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Record next() {
		return cursor.getRecord();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() throws PersistorException {
		if (!cursor.isClosed()) {
			try {
				cursor.close();
			} catch (SQLException exc) {
				throw new PersistorException(exc.getMessage(), exc);
			}
		}
	}
	
	/**
	 * Silently close this iterator underlying cursor.
	 */
	private void silentlyClose() {
		try {
			close();
		} catch (PersistorException exc) {
			throw new IllegalStateException(exc);
		}
	}
}
