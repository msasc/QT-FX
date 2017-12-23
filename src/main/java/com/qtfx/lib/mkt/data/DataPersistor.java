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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qtfx.lib.db.Condition;
import com.qtfx.lib.db.Criteria;
import com.qtfx.lib.db.Field;
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

/**
 * A persistor for elements of timed <tt>Data</tt>. The general contract for a persistor of timed <tt>Data</tt> is that
 * fields must be defined as follows:
 * <ul>
 * <li>The first field is always the index, that starts at 0, primary key. This field value at insert time is managed by
 * this persistor.</li>
 * <li>The second field is a long, the time of the timed data.</li>
 * <li>All subsequent <b>persistent</b> fields of type double and are considered data.</li>
 * </ul>
 * Note that data in a data persistor can not be inserted from different threads, and in most cases it has no sense.
 * 
 * @author Miquel Sas
 */
public class DataPersistor implements Persistor {

	/** Logger instance. */
	private static final Logger LOGGER = LogManager.getLogger();

	/** Underlying persistor. */
	private Persistor persistor;
	/** Map record field indexes to data indexes. Key is the data index and value is the field index. */
	private Map<Integer, Integer> mapDataIndexes;
	/** Map record data indexes to record field indexes. Key is the field index and value is the data index. */
	private Map<Integer, Integer> mapRecordIndexes;

	/** Last index. */
	private long lastIndex = -1;

	/**
	 * Constructor.
	 * 
	 * @param persistor The underlying persistor.
	 */
	public DataPersistor(Persistor persistor) {
		super();
		// First field must be of type LONG
		if (!persistor.getField(0).isLong()) {
			throw new IllegalArgumentException();
		}
		this.persistor = persistor;
	}

	/////////////////////////////////////
	// Data persistor particular methods.

	/**
	 * Return the last index.
	 * 
	 * @return The last index.
	 * @throws PersistorException
	 */
	private long getLastIndex() {
		if (lastIndex == -1) {
			lastIndex = getIndex(getIndexOrder(false));
		}
		return lastIndex;
	}

	/**
	 * Returns the first index with the order.
	 * 
	 * @param order The search order.
	 * @return The first index applying the order.
	 */
	private long getIndex(Order order) {
		Long index = Long.valueOf(-1);
		RecordIterator iter = null;
		try {
			iter = persistor.iterator(null, order);
			if (iter.hasNext()) {
				Record record = iter.next();
				index = getIndex(record);
			}
		} catch (PersistorException exc) {
			LOGGER.catching(exc);
		} finally {
			close(iter);
		}
		return index;
	}

	/**
	 * Returns the underlying index in the record.
	 * 
	 * @param record The source record.
	 * @return The index.
	 */
	private long getIndex(Record record) {
		return record.getValue(0).getLong();
	}

	/**
	 * Returns the order on the index field.
	 * 
	 * @param asc A boolean that indicates ascending/descending order.
	 * @return The order.
	 */
	public Order getIndexOrder(boolean asc) {
		Order order = new Order();
		order.add(persistor.getField(0), asc);
		return order;
	}

	/**
	 * Return the record with the time GE the argument.
	 * 
	 * @param time The time.
	 * @return The record GE the time.
	 * @throws PersistorException
	 */
	public Record getRecord(long index) throws PersistorException {
		Criteria criteria = new Criteria();
		criteria.add(Condition.fieldEQ(getField(0), new Value(index)));
		Record record = null;
		RecordIterator iter = null;
		try {
			iter = persistor.iterator(criteria);
			if (iter.hasNext()) {
				record = iter.next();
			}
		} catch (PersistorException exc) {
			LOGGER.catching(exc);
		} finally {
			close(iter);
		}
		return record;
	}

	/**
	 * Close the iterator.
	 * 
	 * @param iter The record iterator.
	 */
	private void close(RecordIterator iter) {
		try {
			if (iter != null) {
				iter.close();
			}
		} catch (PersistorException exc) {
			LOGGER.catching(exc);
		}
	}

	/**
	 * Return the first record.
	 * 
	 * @return The first record.
	 * @throws PersistorException
	 */
	public Record getFirstRecord() throws PersistorException {
		return getRecord(0);
	}

	/**
	 * Return the last record.
	 * 
	 * @return The last record.
	 * @throws PersistorException
	 */
	public Record getLastRecord() throws PersistorException {
		Order order = new Order(getView().getMasterTable().getPrimaryKey());
		order.invertAsc();
		RecordIterator i = iterator(new Criteria(), order);
		Record record = null;
		if (i.hasNext()) {
			record = i.next();
		}
		i.close();
		return record;
	}

	/**
	 * Returns the size or number of record in the persistor.
	 * 
	 * @return The size.
	 */
	public long size() {
		return getLastIndex() + 1;
	}

	/**
	 * Returns the <tt>Data</tt> in the record or null if out of range.
	 * 
	 * @param record The source record.
	 * @return The <tt>Data</tt>.
	 */
	public Data getData(Record record) {
		if (record == null) {
			return null;
		}
		Data data = new Data(getDataIndexesMap().size());
		data.setTime(record.getValue(1).getLong());
		for (int i = 2; i < record.size(); i++) {
			Field field = record.getField(i);
			if (field.isDouble() && field.isPersistent()) {
				int dataIndex = getRecordIndexesMap().get(i);
				data.setValue(dataIndex, record.getValue(i).getDouble());
			}
		}
		return data;
	}

	/**
	 * Returns the record given a data element.
	 * 
	 * @param data The element.
	 * @return The record.
	 */
	public Record getRecord(Data data) {
		Record record = getDefaultRecord();
		// First index (0) is reserved.
		record.setValue(1, new Value(data.getTime()));
		// Index of data.
		int index = 0;
		for (int i = 2; i < record.size(); i++) {
			if (record.getField(i).isPersistent()) {
				record.setValue(i, new Value(data.getValue(index++)));
			}
		}
		return record;
	}

	/**
	 * Returns the data indexes map properly filled.
	 * 
	 * @return The data indexes map.
	 */
	private Map<Integer, Integer> getDataIndexesMap() {
		checkIndexesMaps();
		return mapDataIndexes;
	}

	/**
	 * Returns the record indexes map properly filled.
	 * 
	 * @return The record indexes map.
	 */
	private Map<Integer, Integer> getRecordIndexesMap() {
		checkIndexesMaps();
		return mapRecordIndexes;
	}

	/**
	 * Check that the indexes maps are filled.
	 */
	private void checkIndexesMaps() {
		if (mapDataIndexes == null || mapRecordIndexes == null) {
			mapDataIndexes = new HashMap<>();
			mapRecordIndexes = new HashMap<>();
			Record record = getDefaultRecord();
			int dataIndex = 0;
			for (int recordIndex = 2; recordIndex < record.size(); recordIndex++) {
				Field field = record.getField(recordIndex);
				if (field.isDouble() && field.isPersistent()) {
					mapDataIndexes.put(dataIndex, recordIndex);
					mapRecordIndexes.put(recordIndex, dataIndex);
					dataIndex++;
				}
			}
		}
	}

	////////////////////////////
	// Persistor implementation.

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PersistorDDL getDDL() {
		return persistor.getDDL();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public View getView() {
		return persistor.getView();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Record getDefaultRecord() {
		return persistor.getDefaultRecord();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Record getRecord(OrderKey primaryKey) throws PersistorException {
		return persistor.getRecord(primaryKey);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Record getRecord(List<Value> primaryKeyValues) throws PersistorException {
		return persistor.getRecord(primaryKeyValues);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Record getRecord(Value... primaryKeyValues) throws PersistorException {
		return persistor.getRecord(primaryKeyValues);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getFieldCount() {
		return persistor.getFieldCount();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field getField(int index) {
		return persistor.getField(index);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field getField(String alias) {
		return persistor.getField(alias);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getFieldIndex(String alias) {
		return persistor.getFieldIndex(alias);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long count(Criteria criteria) throws PersistorException {
		return persistor.count(criteria);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int delete(Criteria criteria) throws PersistorException {
		return persistor.delete(criteria);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int delete(Record record) throws PersistorException {
		return persistor.delete(record);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean exists(Record record) throws PersistorException {
		return persistor.exists(record);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean exists(OrderKey primaryKey) throws PersistorException {
		return persistor.exists(primaryKey);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean refresh(Record record) throws PersistorException {
		return persistor.refresh(record);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int insert(Record record) throws PersistorException {
		long last = getLastIndex() + 1;
		record.setValue(0, new Value(last));
		lastIndex = last;
		return persistor.insert(record);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public RecordIterator iterator(Criteria criteria) throws PersistorException {
		return persistor.iterator(criteria);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RecordIterator iterator(Criteria criteria, Order order) throws PersistorException {
		return persistor.iterator(criteria, order);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ValueMap max(Criteria criteria, int... indexes) throws PersistorException {
		return persistor.max(criteria, indexes);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ValueMap max(Criteria criteria, String... aliases) throws PersistorException {
		return persistor.max(criteria, aliases);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ValueMap min(Criteria criteria, int... indexes) throws PersistorException {
		return persistor.min(criteria, indexes);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ValueMap min(Criteria criteria, String... aliases) throws PersistorException {
		return persistor.min(criteria, aliases);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int save(Record record) throws PersistorException {
		return persistor.save(record);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RecordSet select(Criteria criteria) throws PersistorException {
		return persistor.select(criteria);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RecordSet select(Criteria criteria, Order order) throws PersistorException {
		return persistor.select(criteria, order);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ValueMap sum(Criteria criteria, int... indexes) throws PersistorException {
		return persistor.sum(criteria, indexes);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ValueMap sum(Criteria criteria, String... aliases) throws PersistorException {
		return persistor.sum(criteria, aliases);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int update(Record record) throws PersistorException {
		return persistor.update(record);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int update(Criteria criteria, ValueMap map) throws PersistorException {
		return persistor.update(criteria, map);
	}

}
