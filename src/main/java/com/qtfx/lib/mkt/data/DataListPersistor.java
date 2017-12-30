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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qtfx.lib.db.Persistor;
import com.qtfx.lib.db.PersistorException;
import com.qtfx.lib.mkt.data.info.DataInfo;
import com.qtfx.lib.util.CacheMap;

/**
 * A data list that retrieves data from a data persistor.
 *
 * @author Miquel Sas
 */
public class DataListPersistor extends DataList {

	/** Logger instance. */
	private static final Logger LOGGER = LogManager.getLogger();

	/** Underlying data persistor. */
	private DataPersistor persistor;
	/** Cache map. */
	private CacheMap<Integer, Data> map = new CacheMap<>(-1);

	/**
	 * Constructor.
	 * 
	 * @param dataInfo The data info.
	 */
	public DataListPersistor(DataInfo dataInfo, Persistor persistor) {
		super(dataInfo);
		this.persistor = new DataPersistor(persistor);
	}

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
	public boolean isEmpty() {
		return persistor.size() == 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void add(Data data) {
		// TODO Pending to implement
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Data get(int index) {
		Data data = map.get(index);
		if (data == null) {
			try {
				data = persistor.getData(persistor.getRecord(index));
				map.put(index, data);
			} catch (PersistorException exc) {
				LOGGER.catching(exc);
			}
		}
		return data;
	}

	/**
	 * Not supported.
	 */
	@Override
	public Data remove(int index) {
		throw new UnsupportedOperationException();
	}

}
