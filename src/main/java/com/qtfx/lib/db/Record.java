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

import java.util.ArrayList;
import java.util.List;

import com.qtfx.lib.util.Properties;

/**
 * A record packs a list of values and their corresponding field definitions.
 *
 * @author Miquel Sas
 */
public class Record implements Comparable<Object> {

	/**
	 * Key used to set the total property.
	 */
	public static final String TOTAL = "Total";

	/**
	 * Move values from a source record to a destination record, by coincident alias and type.
	 * 
	 * @param source The source record.
	 * @param destination The destination record.
	 */
	public static void move(Record source, Record destination) {
		for (int i = 0; i < source.size(); i++) {
			String alias = source.getField(i).getAlias();
			Value value = source.getValue(i);
			Types type = source.getField(i).getType();
			int index = destination.getFieldList().getFieldIndex(alias);
			if (index >= 0 && destination.getField(index).getType().equals(type)) {
				destination.setValue(index, value.getCopy());
			}
		}
	}

	/**
	 * Returns a copy of the argument source record.
	 * 
	 * @param source The source record.
	 * @return A copy of the argument source record.
	 */
	public static Record copy(Record source) {
		Record destination = new Record(source.getFieldList());
		copy(source, destination);
		return destination;
	}

	/**
	 * Returns a copy of the argument source record, with a copy of data and fields.
	 * 
	 * @param source The source record.
	 * @return A copy of the argument source record.
	 */
	public static Record copyDataAndFields(Record source) {
		Record destination = new Record(new FieldList(source.fields));
		copy(source, destination);
		return destination;
	}

	/**
	 * Copy date from source to destination.
	 * 
	 * @param source The source record.
	 * @param destination The destination record.
	 */
	public static void copy(Record source, Record destination) {
		destination.persistor = source.persistor;
		destination.properties = source.properties;
		move(source, destination);
	}

	/** The list of fields. */
	private FieldList fields;
	/** The list of values. */
	private Value[] values;
	/** List of modified flags. */
	private boolean[] modifieds;

	/** An arbitrary map of properties. */
	private Properties properties;
	/** The persistor. */
	private Persistor persistor;
	/**
	 * Constructor assigning the list of fields.
	 * 
	 * @param fields The list of fields.
	 */
	public Record(FieldList fields) {
		super();
		this.fields = fields;
		this.values = fields.getDefaultValues();
		this.modifieds = new boolean[size()];
	}

	/**
	 * Returns the number of fields.
	 *
	 * @return The number of fields.
	 */
	public int size() {
		return fields.size();
	}

	/**
	 * Returns the field list.
	 *
	 * @return The field list.
	 */
	public FieldList getFieldList() {
		return fields;
	}

	/**
	 * Get the field at the given index.
	 *
	 * @param index The index of the field.
	 * @return The field.
	 */
	public Field getField(int index) {
		return fields.getField(index);
	}

	/**
	 * Get a field by alias.
	 *
	 * @param alias The field alias.
	 * @return The field or null if not found.
	 */
	public Field getField(String alias) {
		return fields.getField(alias);
	}

	/**
	 * Clears this record fields to their default values.
	 */
	public void clear() {
		for (int i = 0; i < size(); i++) {
			values[i] = getField(i).getDefaultValue();
		}
	}

	/**
	 * Returns the value at a given index.
	 *
	 * @param index The index
	 * @return The value at the given index.
	 */
	public Value getValue(int index) {
		Calculator calculator = getField(index).getCalculator();
		if (calculator != null) {
			return calculator.getValue(this);
		}
		return values[index];
	}

	/**
	 * Get a value by field alias.
	 *
	 * @param alias The field alias
	 * @return The value.
	 */
	public Value getValue(String alias) {
		int index = fields.getFieldIndex(alias);
		return (index == -1 ? null : getValue(index));
	}

	/**
	 * Returns the list of persistent values.
	 *
	 * @return The list of persistent values.
	 */
	public List<Value> getPersistentValues() {
		List<Field> persistentFields = getPersistentFields();
		List<Value> persistentValues = new ArrayList<>(persistentFields.size());
		for (Field field : persistentFields) {
			persistentValues.add(getValue(field.getAlias()));
		}
		return persistentValues;
	}

	/**
	 * Returns the order key for the given order. The order must contain fields of the record.
	 * 
	 * @param order The order.
	 * @return The key.
	 */
	public OrderKey getOrderKey(Order order) {
		OrderKey key = new OrderKey();
		for (int i = 0; i < order.size(); i++) {
			Order.Segment segment = order.get(i);
			Field field = segment.getField();
			boolean asc = segment.isAsc();
			Value value = getValue(field.getAlias());
			if (value == null) {
				throw new IllegalArgumentException();
			}
			key.add(value, asc);
		}
		return key;
	}

	/**
	 * Returns the list of persistent fields.
	 *
	 * @return the list of persistent fields.
	 */
	public List<Field> getPersistentFields() {
		return fields.getPersistentFields();
	}

	/**
	 * Set the value at the given index.
	 *
	 * @param index The index of the value.
	 * @param value The value to set.
	 */
	public void setValue(int index, Value value) {
		values[index] = value;
		setModified(index, true);
	}

	/**
	 * Set the value at the given index.
	 *
	 * @param index The index of the value.
	 * @param value The value to set.
	 * @param modified A boolean.
	 */
	public void setValue(int index, Value value, boolean modified) {
		values[index] = value;
		setModified(index, modified);
	}

	/**
	 * Set the value at the given index.
	 *
	 * @param alias The alias of the field.
	 * @param value The value to set.
	 */
	public void setValue(String alias, Value value) {
		int index = fields.getFieldIndex(alias);
		setValue(index, value);
	}

	/**
	 * Tag the value as modified.
	 * 
	 * @param index The index.
	 * @param modified A boolean.
	 */
	public void setModified(int index, boolean modified) {
		modifieds[index] = modified;
	}

	/**
	 * Tag the value as modified.
	 * 
	 * @param alias The alias of the field.
	 * @param modified A boolean.
	 */
	public void setModified(String alias, boolean modified) {
		int index = fields.getFieldIndex(alias);
		setModified(index, modified);
	}

	/**
	 * Check if the value is modified.
	 * 
	 * @param index The index.
	 * @return The value.
	 */
	public boolean isModified(int index) {
		return modifieds[index];
	}

	/**
	 * Check if the value is modified.
	 * 
	 * @param alias The alias of the field.
	 * @return The value.
	 */
	public boolean isModified(String alias) {
		int index = fields.getFieldIndex(alias);
		return isModified(index);
	}

	/**
	 * Nullify the value.
	 * 
	 * @param alias The alias of the field.
	 */
	public void setNull(String alias) {
		int index = fields.getFieldIndex(alias);
		setNull(index);
	}

	/**
	 * Nullify the value.
	 * 
	 * @param index The index.
	 */
	public void setNull(int index) {
		values[index] = fields.getField(index).getNullValue();
		setModified(index, true);
	}

	/**
	 * Get the list of primary key fields.
	 * 
	 * @return The list of primary key fields.
	 */
	public List<Field> getPrimaryKeyFields() {
		return fields.getPrimaryKeyFields();
	}

	/**
	 * Returns the primary order.
	 * 
	 * @return The primary order.
	 */
	public Order getPrimaryOrder() {
		return fields.getPrimaryOrder();
	}

	/**
	 * Get the primary key.
	 *
	 * @return An <code>IndexKey</code>
	 */
	public OrderKey getPrimaryKey() {
		List<Field> primaryKeyFields = getPrimaryKeyFields();
		OrderKey orderKey = new OrderKey();
		for (Field field : primaryKeyFields) {
			orderKey.add(getValue(field.getAlias()), true);
		}
		return orderKey;
	}

	/**
	 * Returns the persistor associated with this record if any.
	 * 
	 * @return The persistor.
	 */
	public Persistor getPersistor() {
		return persistor;
	}

	/**
	 * Sets the persistor associated with this record if any.
	 * 
	 * @param persistor The persistor.
	 */
	public void setPersistor(Persistor persistor) {
		this.persistor = persistor;
	}

	/**
	 * Check if the record has been modified.
	 *
	 * @return A boolean.
	 */
	public boolean isModified() {
		for (int i = 0; i < modifieds.length; i++) {
			if (modifieds[i]) {
				return true;
			}
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compareTo(Object o) {
		Record record = null;
		try {
			record = (Record) o;
		} catch (ClassCastException exc) {
			throw new UnsupportedOperationException("Not comparable type: " + o.getClass().getName());
		}
		// Compare using the primary key pointers.
		RecordComparator comparator = new RecordComparator(getPrimaryOrder());
		return comparator.compare(this, record);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Record) {
			Record record = (Record) obj;
			return getPrimaryKey().equals(record.getPrimaryKey());
		}
		return super.equals(obj);
	}

	/**
	 * Returns the additional properties.
	 * 
	 * @return The additional properties.
	 */
	public Properties getProperties() {
		if (properties == null) {
			properties = new Properties();
		}
		return properties;
	}

	/**
	 * Check if the record has a main description field or at least a non fixed width field.
	 * 
	 * @return A boolean.
	 */
	public boolean hasMainDescription() {
		boolean hasMainDescription = false;
		boolean hasNonFixedWidthFields = false;
		for (int i = 0; i < size(); i++) {
			Field field = getField(i);
			if (field.isMainDescription()) {
				hasMainDescription = true;
				break;
			}
			if (!field.isFixedWidth()) {
				hasNonFixedWidthFields = true;
			}
		}
		return (hasMainDescription || hasNonFixedWidthFields);
	}

	/**
	 * Return the main description field or if non exists, the first non fixed width field.
	 * 
	 * @return The main description field or if non exists, the first non fixed width field.
	 */
	public Field getMainDescription() {
		Field mainDescription = null;
		Field firstNonFixedWidth = null;
		for (int i = 0; i < size(); i++) {
			Field field = getField(i);
			if (field.isMainDescription()) {
				mainDescription = field;
				break;
			}
			if (!field.isFixedWidth() && firstNonFixedWidth == null) {
				firstNonFixedWidth = field;
			}
		}
		return (mainDescription != null ? mainDescription : firstNonFixedWidth);
	}

	/**
	 * Return the list of all fields tagged for lookup.
	 * 
	 * @return The list of all lookup fields.
	 */
	public List<Field> getLookupFields() {
		List<Field> lookupFields = new ArrayList<>();
		for (int i = 0; i < size(); i++) {
			Field field = getField(i);
			if (field.isLookup()) {
				lookupFields.add(field);
			}
		}
		return lookupFields;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		for (int i = 0; i < size(); i++) {
			if (i > 0) {
				b.append(", ");
			}
			Value value = getValue(i);
			Types type = getField(i).getType();
			switch (type) {
			case BOOLEAN:
				b.append(value.getBoolean());
				break;
			case BYTEARRAY:
				b.append(value.getByteArray());
				break;
			case DECIMAL:
			case DOUBLE:
			case INTEGER:
			case LONG:
				b.append(value.getNumber());
				break;
			case STRING:
			case DATE:
			case TIME:
			case TIMESTAMP:
				b.append("'" + value.toString() + "'");
				break;
			case VALUE:
				b.append(value.toString());
				break;
			default:
				throw new IllegalArgumentException();
			}
		}
		return b.toString();
	}

}
