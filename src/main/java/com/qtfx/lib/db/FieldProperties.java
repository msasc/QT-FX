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

import java.util.Comparator;

import com.qtfx.lib.app.Session;

/**
 * Provides field meta data as fields of the fields properties.
 * 
 * @author Miquel Sas
 */
public class FieldProperties {
	
	/**
	 * Sorter for fields.
	 */
	public static class Sorter implements Comparator<Record> {
		@Override
		public int compare(Record o1, Record o2) {
			int g1 = o1.getValue(GROUP_INDEX).getInteger();
			int g2 = o2.getValue(GROUP_INDEX).getInteger();
			int compare = Integer.compare(g1, g2);
			if (compare == 0) {
				int i1 = o1.getValue(INDEX).getInteger();
				int i2 = o2.getValue(INDEX).getInteger();
				compare = Integer.compare(i1, i2);
			}
			return compare;
		}
	}

	/** Property alias: INDEX. */
	public static final String INDEX = "INDEX";
	/** Property alias: GROUP. */
	public static final String GROUP = "GROUP";
	/** Property alias: GROUP_INDEX. */
	public static final String GROUP_INDEX = "GROUP_INDEX";
	/** Property alias: NAME. */
	public static final String NAME = "NAME";
	/** Property alias: ALIAS. */
	public static final String ALIAS = "ALIAS";
	/** Property alias: HEADER. */
	public static final String HEADER = "HEADER";
	/** Property alias: TITLE. */
	public static final String TITLE = "TITLE";
	/** Property alias: TYPE. */
	public static final String TYPE = "TYPE";
	/** Property alias: LENGTH. */
	public static final String LENGTH = "LENGTH";
	/** Property alias: DECIMALS. */
	public static final String DECIMALS = "DECIMALS";
	/** Property alias: ASC. */
	public static final String ASCENDING = "ASC";

	/** The properties field list. */
	private FieldList fieldList;
	/** The working session. */
	private Session session;

	/**
	 * Constructor.
	 */
	public FieldProperties() {
		this(Session.getSession());
	}

	/**
	 * Constructor.
	 * 
	 * @param session The working session.
	 */
	public FieldProperties(Session session) {
		super();
		this.session = session;
	}

	
	
	
	/**
	 * Return the working session.
	 * 
	 * @return The session.
	 */
	public Session getSession() {
		return session;
	}

	/**
	 * Returns the properties field list.
	 * 
	 * @return The properties field list.
	 */
	public FieldList getFieldList() {
		if (fieldList == null) {
			fieldList = new FieldList();
			fieldList.addField(getFieldGroupIndex());
			fieldList.addField(getFieldIndex());
			fieldList.addField(getFieldGroup());
			fieldList.addField(getFieldName());
			fieldList.addField(getFieldAlias());
			fieldList.addField(getFieldHeader());
			fieldList.addField(getFieldTitle());
			fieldList.addField(getFieldType());
			fieldList.addField(getFieldLength());
			fieldList.addField(getFieldDecimals());
			fieldList.addField(getFieldAscending());
		}
		return fieldList;
	}

	/**
	 * Returns the properties record.
	 * 
	 * @return The properties record.
	 */
	public Record getProperties() {
		return new Record(getFieldList());
	}

	/**
	 * Returns the field corresponding to the property <i>GroupIndex</i>.
	 * 
	 * @return The field.
	 */
	public Field getFieldGroupIndex() {
		Field field = new Field(getFieldAlias());
		field.setName(GROUP_INDEX);
		field.setAlias(GROUP_INDEX);
		field.setTitle(getSession().getString("fieldGroupIndex"));
		field.setLabel(getSession().getString("fieldGroupIndex"));
		field.setHeader(getSession().getString("fieldGroupIndex"));
		field.setType(Types.INTEGER);
		field.setPrimaryKey(true);
		return field;
	}

	/**
	 * Returns the field corresponding to the property <i>Index</i>.
	 * 
	 * @return The field.
	 */
	public Field getFieldIndex() {
		Field field = new Field();
		field.setName(INDEX);
		field.setAlias(INDEX);
		field.setTitle(getSession().getString("fieldIndex"));
		field.setLabel(getSession().getString("fieldIndex"));
		field.setHeader(getSession().getString("fieldIndex"));
		field.setType(Types.INTEGER);
		field.setPrimaryKey(true);
		return field;
	}

	/**
	 * Returns the field corresponding to the property <i>Group</i>.
	 * 
	 * @return The field.
	 */
	public Field getFieldGroup() {
		Field field = new Field();
		field.setName(GROUP);
		field.setAlias(GROUP);
		field.setTitle(getSession().getString("fieldGroup"));
		field.setLabel(getSession().getString("fieldGroup"));
		field.setHeader(getSession().getString("fieldGroup"));
		field.setType(Types.STRING);
		field.setLength(60);
		return field;
	}

	/**
	 * Returns the field corresponding to the property <i>Name</i>.
	 * 
	 * @return The field.
	 */
	public Field getFieldName() {
		Field field = new Field();
		field.setName(NAME);
		field.setAlias(NAME);
		field.setTitle(getSession().getString("fieldName"));
		field.setLabel(getSession().getString("fieldName"));
		field.setHeader(getSession().getString("fieldName"));
		field.setType(Types.STRING);
		field.setLength(30);
		return field;
	}

	/**
	 * Returns the field corresponding to the property <i>Alias</i>.
	 * 
	 * @return The field.
	 */
	public Field getFieldAlias() {
		Field field = new Field();
		field.setName(ALIAS);
		field.setAlias(ALIAS);
		field.setTitle(getSession().getString("fieldAlias"));
		field.setLabel(getSession().getString("fieldAlias"));
		field.setHeader(getSession().getString("fieldAlias"));
		field.setType(Types.STRING);
		field.setLength(30);
		return field;
	}

	/**
	 * Returns the field corresponding to the property <i>Header</i>.
	 * 
	 * @return The field.
	 */
	public Field getFieldHeader() {
		Field field = new Field();
		field.setName(HEADER);
		field.setAlias(HEADER);
		field.setTitle(getSession().getString("fieldHeader"));
		field.setLabel(getSession().getString("fieldHeader"));
		field.setHeader(getSession().getString("fieldHeader"));
		field.setType(Types.STRING);
		field.setLength(60);
		return field;
	}

	/**
	 * Returns the field corresponding to the property <i>Title</i>.
	 * 
	 * @return The field.
	 */
	public Field getFieldTitle() {
		Field field = new Field();
		field.setName(TITLE);
		field.setAlias(TITLE);
		field.setTitle(getSession().getString("fieldTitle"));
		field.setLabel(getSession().getString("fieldTitle"));
		field.setHeader(getSession().getString("fieldTitle"));
		field.setType(Types.STRING);
		field.setLength(60);
		return field;
	}

	/**
	 * Returns the field corresponding to the property <i>Type</i>.
	 * 
	 * @return The field.
	 */
	public Field getFieldType() {
		Field field = new Field();
		field.setName(TYPE);
		field.setAlias(TYPE);
		field.setTitle(getSession().getString("fieldType"));
		field.setLabel(getSession().getString("fieldType"));
		field.setHeader(getSession().getString("fieldType"));
		field.setType(Types.STRING);
		field.setLength(20);
		return field;
	}

	/**
	 * Returns the field corresponding to the property <i>Length</i>.
	 * 
	 * @return The field.
	 */
	public Field getFieldLength() {
		Field field = new Field();
		field.setName(LENGTH);
		field.setAlias(LENGTH);
		field.setTitle(getSession().getString("fieldLength"));
		field.setLabel(getSession().getString("fieldLength"));
		field.setHeader(getSession().getString("fieldLength"));
		field.setType(Types.INTEGER);
		return field;
	}

	/**
	 * Returns the field corresponding to the property <i>Decimals</i>.
	 * 
	 * @return The field.
	 */
	public Field getFieldDecimals() {
		Field field = new Field();
		field.setName(DECIMALS);
		field.setAlias(DECIMALS);
		field.setTitle(getSession().getString("fieldDecimals"));
		field.setLabel(getSession().getString("fieldDecimals"));
		field.setHeader(getSession().getString("fieldDecimals"));
		field.setType(Types.INTEGER);
		return field;
	}

	/**
	 * Returns the field corresponding to the property <i>Ascending/Descending</i>.
	 * 
	 * @return The field.
	 */
	public Field getFieldAscending() {
		Field field = new Field();
		field.setName(ASCENDING);
		field.setAlias(ASCENDING);
		field.setTitle(getSession().getString("fieldAsc"));
		field.setLabel(getSession().getString("fieldAsc"));
		field.setHeader(getSession().getString("fieldAsc"));
		field.setType(Types.STRING);
		field.addPossibleValue(new Value(getSession().getString("tokenAsc")));
		field.addPossibleValue(new Value(getSession().getString("tokenDesc")));
		return field;
	}

	/**
	 * Fill the properties record with field data.
	 * 
	 * @param field Source field.
	 * @param index The index in the list.
	 * @param ascending The ascending flag, not included by default in the field.
	 * @return properties The properties record.
	 */
	public Record getProperties(Field field, int index, boolean ascending) {
		Record properties = getProperties();
		properties.setValue(INDEX, new Value(index));
		properties.setValue(GROUP, new Value(getFieldGroupTitle(field.getFieldGroup())));
		properties.setValue(GROUP_INDEX, new Value(getFieldGroupIndex(field.getFieldGroup())));
		properties.setValue(NAME, new Value(field.getName()));
		properties.setValue(ALIAS, new Value(field.getAlias()));
		properties.setValue(HEADER, new Value(field.getHeader()));
		properties.setValue(TITLE, new Value(field.getTitle()));
		properties.setValue(TYPE, new Value(field.getType().name()));
		properties.setValue(LENGTH, new Value(field.getLength()));
		if (field.getType().isDecimal()) {
			properties.setValue(DECIMALS, new Value(field.getDecimals()));
		} else {
			properties.setNull(DECIMALS);
		}
		// Special property
		String strAscending = getSession().getString(ascending ? "tokenAsc" : "tokenDesc");
		properties.setValue(ASCENDING, new Value(strAscending));
		// Set the source field that gave values to this properties.
		properties.getProperties().setObject("Source", field);

		// Nullify length and decimals if appropriate.
		if (!field.isNumber()) {
			properties.setNull(DECIMALS);
		}
		if (field.getLength() <= 0) {
			properties.setNull(LENGTH);
		}

		// Store group and subgroup as objects.
		properties.getProperties().setObject(GROUP, field.getFieldGroup());

		return properties;
	}

	/**
	 * Returns the field property.
	 * 
	 * @param properties The properties record.
	 * @return The property value.
	 */
	public int getPropertyGroupIndex(Record properties) {
		return properties.getValue(GROUP_INDEX).getInteger();
	}

	/**
	 * Returns the field property.
	 * 
	 * @param properties The properties record.
	 * @return The property value.
	 */
	public int getPropertyIndex(Record properties) {
		return properties.getValue(INDEX).getInteger();
	}

	/**
	 * Returns the field property.
	 * 
	 * @param properties The properties record.
	 * @return The property value.
	 */
	public String getPropertyGroup(Record properties) {
		return properties.getValue(GROUP).getString();
	}

	/**
	 * Returns the field property.
	 * 
	 * @param properties The properties record.
	 * @return The property value.
	 */
	public String getPropertyName(Record properties) {
		return properties.getValue(NAME).getString();
	}

	/**
	 * Returns the field property.
	 * 
	 * @param properties The properties record.
	 * @return The property value.
	 */
	public String getPropertyAlias(Record properties) {
		return properties.getValue(ALIAS).getString();
	}

	/**
	 * Returns the field property.
	 * 
	 * @param properties The properties record.
	 * @return The property value.
	 */
	public String getPropertyHeader(Record properties) {
		return properties.getValue(HEADER).getString();
	}

	/**
	 * Returns the field property.
	 * 
	 * @param properties The properties record.
	 * @return The property value.
	 */
	public String getPropertyTitle(Record properties) {
		return properties.getValue(TITLE).getString();
	}

	/**
	 * Returns the field property.
	 * 
	 * @param properties The properties record.
	 * @return The property value.
	 */
	public Types getPropertyType(Record properties) {
		return Types.parseType(properties.getValue(TYPE).getString());
	}

	/**
	 * Returns the field property.
	 * 
	 * @param properties The properties record.
	 * @return The property value.
	 */
	public int getPropertyLength(Record properties) {
		return properties.getValue(LENGTH).getInteger();
	}

	/**
	 * Returns the field property.
	 * 
	 * @param properties The properties record.
	 * @return The property value.
	 */
	public int getPropertyDecimals(Record properties) {
		return properties.getValue(DECIMALS).getInteger();
	}

	/**
	 * Returns the field property.
	 * 
	 * @param properties The properties record.
	 * @return The property value.
	 */
	public boolean getPropertyAscending(Record properties) {
		return properties.getValue(ASCENDING).equals(getSession().getString("tokenAsc"));
	}

	/**
	 * Set the field property.
	 * 
	 * @param properties The properties record.
	 * @return The property value.
	 */
	public void setPropertyAscending(Record properties, boolean asc) {
		if (asc) {
			properties.setValue(ASCENDING, new Value(getSession().getString("tokenAsc")));
		} else {
			properties.setValue(ASCENDING, new Value(getSession().getString("tokenDesc")));
		}
	}

	/**
	 * Returns the source field that gave values to the properties.
	 * 
	 * @param properties The properties.
	 * @return The source field.
	 */
	public Field getPropertiesSourceField(Record properties) {
		return (Field) properties.getProperties().getObject("Source");
	}

	/**
	 * Returns a valid string for the field group.
	 * 
	 * @param fieldGroup The field group.
	 * @return The title string.
	 */
	private String getFieldGroupTitle(FieldGroup fieldGroup) {
		if (fieldGroup == null) {
			return "";
		}
		if (fieldGroup.getName() == null) {
			throw new IllegalStateException();
		}
		if (fieldGroup.getTitle() == null) {
			return fieldGroup.getName();
		}
		return fieldGroup.getTitle();
	}

	/**
	 * Returns a valid index for the field group.
	 * 
	 * @param fieldGroup The field group.
	 * @return A valid index (NumberUtils.MAX_INTEGER if the group is null)
	 */
	private int getFieldGroupIndex(FieldGroup fieldGroup) {
		if (fieldGroup == null) {
			return FieldGroup.EMPTY_FIELD_GROUP.getIndex();
		}
		return fieldGroup.getIndex();
	}
}
