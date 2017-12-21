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

package com.qtfx.lib.mkt.data;

import java.util.Comparator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qtfx.lib.db.Order;
import com.qtfx.lib.db.PersistorException;
import com.qtfx.lib.db.Record;
import com.qtfx.lib.db.RecordSet;
import com.qtfx.lib.util.CacheMap;

/**
 * A recordset of timed data.
 * 
 * @author Miquel Sas
 */
public class DataRecordSet extends RecordSet {
	
	/** Logger instance. */
	private static final Logger LOGGER = LogManager.getLogger();

	/** Underlying data persistor. */
	private DataPersistor persistor;
	/** Cache map. */
	private CacheMap<Integer, Record> map = new CacheMap<>(5000);

	/**
	 * Constructor.
	 * 
	 * @param persistor The underlying data persistor.
	 */
	public DataRecordSet(DataPersistor persistor) {
		super();
		this.persistor = persistor;
	}

	/////////////////////
	// Recordset methods.

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size() {
		return Long.valueOf(persistor.size()).intValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Record get(int index) {
		Record record = map.get(index);
		if (record == null) {
			try {
				record = persistor.getRecord(index);
				map.put(index, record);
			} catch (PersistorException exc) {
				LOGGER.catching(exc);
			}
		}
		return record;
	}

	/**
	 * Not supported.
	 */
	@Override
	public void sort() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Not supported.
	 */
	@Override
	public void sort(Order order) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Not supported.
	 */
	@Override
	public void sort(Comparator<Record> comparator) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSortable() {
		return false;
	}

}
