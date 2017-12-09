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
import java.util.List;

import com.qtfx.lib.db.Criteria;
import com.qtfx.lib.db.Field;
import com.qtfx.lib.db.Filter;
import com.qtfx.lib.db.Order;
import com.qtfx.lib.db.OrderKey;
import com.qtfx.lib.db.Persistor;
import com.qtfx.lib.db.PersistorDDL;
import com.qtfx.lib.db.PersistorException;
import com.qtfx.lib.db.Record;
import com.qtfx.lib.db.RecordIterator;
import com.qtfx.lib.db.RecordSet;
import com.qtfx.lib.db.Value;
import com.qtfx.lib.db.ValueMap;
import com.qtfx.lib.db.View;
import com.qtfx.lib.db.rdbms.sql.Select;

/**
 * Database persistor.
 * 
 * @author Miquel Sas
 */
public class DBPersistor implements Persistor {

	/**
	 * The underlying <code>DBEngine</code>.
	 */
	private DBEngine dbEngine;
	/**
	 * The underlying <code>View</code>.
	 */
	private View view;

	/**
	 * Constructor.
	 * 
	 * @param dbEngine The <code>DBEngine</code>.
	 * @param view The <code>View</code>.
	 */
	public DBPersistor(DBEngine dbEngine, View view) {
		super();
		this.dbEngine = dbEngine;
		this.view = view;
		this.view.setPersistor(this);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public PersistorDDL getDDL() {
		return new DBPersistorDDL(dbEngine);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public View getView() {
		return view;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Record getDefaultRecord() {
		return view.getDefaultRecord();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Record getRecord(OrderKey primaryKey) throws PersistorException {
		try {
			return dbEngine.executeSelectPrimaryKey(view, primaryKey);
		} catch (SQLException exc) {
			throw new PersistorException(exc.getMessage(), exc);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Record getRecord(List<Value> primaryKeyValues) throws PersistorException {
		return getRecord(new OrderKey(primaryKeyValues));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Record getRecord(Value... primaryKeyValues) throws PersistorException {
		return getRecord(new OrderKey(primaryKeyValues));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getFieldCount() {
		return view.getFieldCount();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field getField(int index) {
		return view.getField(index);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getFieldIndex(String alias) {
		return view.getFieldIndex(alias);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field getField(String alias) {
		return view.getField(alias);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long count(Criteria criteria) throws PersistorException {
		try {
			Filter filter = new Filter(criteria);
			return dbEngine.executeSelectCount(view, filter);
		} catch (SQLException exc) {
			throw new PersistorException(exc.getMessage(), exc);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int delete(Criteria criteria) throws PersistorException {
		try {
			Filter filter = new Filter(criteria);
			return dbEngine.executeDelete(view.getMasterTable(), filter);
		} catch (SQLException exc) {
			throw new PersistorException(exc.getMessage(), exc);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int delete(Record record) throws PersistorException {
		try {
			return dbEngine.executeDelete(view.getMasterTable(), view.getMasterTableRecord(record));
		} catch (SQLException exc) {
			throw new PersistorException(exc.getMessage(), exc);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean exists(Record record) throws PersistorException {
		try {
			return dbEngine.existsRecord(view.getMasterTable(), view.getMasterTableRecord(record));
		} catch (SQLException exc) {
			throw new PersistorException(exc.getMessage(), exc);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean exists(OrderKey primaryKey) throws PersistorException {
		try {
			return dbEngine.existsRecord(view.getMasterTable(), primaryKey);
		} catch (SQLException exc) {
			throw new PersistorException(exc.getMessage(), exc);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean refresh(Record record) throws PersistorException {
		try {
			Record recordView = dbEngine.executeSelectPrimaryKey(view, record.getPrimaryKey());
			if (recordView != null) {
				Record.move(recordView, record);
				return true;
			}
			return false;
		} catch (SQLException exc) {
			throw new PersistorException(exc.getMessage(), exc);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int insert(Record record) throws PersistorException {
		try {
			return dbEngine.executeInsert(view.getMasterTable(), view.getMasterTableRecord(record));
		} catch (SQLException exc) {
			throw new PersistorException(exc.getMessage(), exc);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RecordIterator iterator(Criteria criteria) throws PersistorException {
		return iterator(criteria, null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RecordIterator iterator(Criteria criteria, Order order) throws PersistorException {
		try {
			// Use a copy of the view to change the order without side effects.
			View view = new View(this.view);
			if (order != null) {
				view.setOrderBy(order);
			}
			Filter filter = new Filter(criteria);
			Select select = dbEngine.getDBEngineAdapter().getQuerySelect(view, filter);
			Cursor cursor = dbEngine.executeSelectCursor(select);
			return new DBRecordIterator(cursor);
		} catch (SQLException exc) {
			throw new PersistorException(exc.getMessage(), exc);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ValueMap max(Criteria criteria, int... indexes) throws PersistorException {
		try {
			Filter filter = new Filter(criteria);
			return dbEngine.executeSelectMaxMap(view, filter, indexes);
		} catch (SQLException exc) {
			throw new PersistorException(exc.getMessage(), exc);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ValueMap max(Criteria criteria, String... aliases) throws PersistorException {
		try {
			Filter filter = new Filter(criteria);
			return dbEngine.executeSelectMaxMap(view, filter, aliases);
		} catch (SQLException exc) {
			throw new PersistorException(exc.getMessage(), exc);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ValueMap min(Criteria criteria, int... indexes) throws PersistorException {
		try {
			Filter filter = new Filter(criteria);
			return dbEngine.executeSelectMinMap(view, filter, indexes);
		} catch (SQLException exc) {
			throw new PersistorException(exc.getMessage(), exc);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ValueMap min(Criteria criteria, String... aliases) throws PersistorException {
		try {
			Filter filter = new Filter(criteria);
			return dbEngine.executeSelectMinMap(view, filter, aliases);
		} catch (SQLException exc) {
			throw new PersistorException(exc.getMessage(), exc);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int save(Record record) throws PersistorException {
		try {
			return dbEngine.executeSave(view.getMasterTable(), view.getMasterTableRecord(record));
		} catch (SQLException exc) {
			throw new PersistorException(exc.getMessage(), exc);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RecordSet select(Criteria criteria) throws PersistorException {
		return select(criteria, view.getOrderBy());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RecordSet select(Criteria criteria, Order order) throws PersistorException {
		try {
			View view = new View(this.view);
			view.setOrderBy(order);
			Filter filter = new Filter(criteria);
			Select select = dbEngine.getDBEngineAdapter().getQuerySelect(view, filter);
			return dbEngine.executeSelectRecordSet(select);
		} catch (SQLException exc) {
			throw new PersistorException(exc.getMessage(), exc);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ValueMap sum(Criteria criteria, int... indexes) throws PersistorException {
		try {
			Filter filter = new Filter(criteria);
			return dbEngine.executeSelectSumMap(view, filter, indexes);
		} catch (SQLException exc) {
			throw new PersistorException(exc.getMessage(), exc);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ValueMap sum(Criteria criteria, String... aliases) throws PersistorException {
		try {
			Filter filter = new Filter(criteria);
			return dbEngine.executeSelectSumMap(view, filter, aliases);
		} catch (SQLException exc) {
			throw new PersistorException(exc.getMessage(), exc);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int update(Record record) throws PersistorException {
		try {
			return dbEngine.executeUpdate(view.getMasterTable(), view.getMasterTableRecord(record));
		} catch (SQLException exc) {
			throw new PersistorException(exc.getMessage(), exc);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int update(Criteria criteria, ValueMap map) throws PersistorException {
		try {
			Filter filter = new Filter(criteria);
			return dbEngine.executeUpdate(view.getMasterTable(), filter, map);
		} catch (SQLException exc) {
			throw new PersistorException(exc.getMessage(), exc);
		}
	}
}
