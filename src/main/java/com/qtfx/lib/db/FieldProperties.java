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

import com.qtfx.lib.util.TextServer;

/**
 * Provides field metadata as fields of the fields properties.
 * 
 * @author Miquel Sas
 */
public class FieldProperties {

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

	/**
	 * The properties field list.
	 */
	private FieldList fieldList;

	/**
	 * Constructor.
	 */
	public FieldProperties() {
		super();
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
		Field field = new Field();
		field.setName(GROUP_INDEX);
		field.setAlias(GROUP_INDEX);
		field.setTitle(TextServer.getString("fieldGroupIndex"));
		field.setLabel(TextServer.getString("fieldGroupIndex"));
		field.setHeader(TextServer.getString("fieldGroupIndex"));
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
		field.setTitle(TextServer.getString("fieldIndex"));
		field.setLabel(TextServer.getString("fieldIndex"));
		field.setHeader(TextServer.getString("fieldIndex"));
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
		field.setTitle(TextServer.getString("fieldGroup"));
		field.setLabel(TextServer.getString("fieldGroup"));
		field.setHeader(TextServer.getString("fieldGroup"));
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
		field.setTitle(TextServer.getString("fieldName"));
		field.setLabel(TextServer.getString("fieldName"));
		field.setHeader(TextServer.getString("fieldName"));
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
		field.setTitle(TextServer.getString("fieldAlias"));
		field.setLabel(TextServer.getString("fieldAlias"));
		field.setHeader(TextServer.getString("fieldAlias"));
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
		field.setTitle(TextServer.getString("fieldHeader"));
		field.setLabel(TextServer.getString("fieldHeader"));
		field.setHeader(TextServer.getString("fieldHeader"));
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
		field.setTitle(TextServer.getString("fieldTitle"));
		field.setLabel(TextServer.getString("fieldTitle"));
		field.setHeader(TextServer.getString("fieldTitle"));
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
		field.setTitle(TextServer.getString("fieldType"));
		field.setLabel(TextServer.getString("fieldType"));
		field.setHeader(TextServer.getString("fieldType"));
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
		field.setTitle(TextServer.getString("fieldLength"));
		field.setLabel(TextServer.getString("fieldLength"));
		field.setHeader(TextServer.getString("fieldLength"));
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
		field.setTitle(TextServer.getString("fieldDecimals"));
		field.setLabel(TextServer.getString("fieldDecimals"));
		field.setHeader(TextServer.getString("fieldDecimals"));
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
		field.setTitle(TextServer.getString("fieldAsc"));
		field.setLabel(TextServer.getString("fieldAsc"));
		field.setHeader(TextServer.getString("fieldAsc"));
		field.setType(Types.STRING);
		field.addPossibleValue(new Value(TextServer.getString("tokenAsc")));
		field.addPossibleValue(new Value(TextServer.getString("tokenDesc")));
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
		properties.setValue(DECIMALS, new Value(field.getDecimals()));
		// Special property
		String strAscending = TextServer.getString(ascending ? "tokenAsc" : "tokenDesc");
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
		return properties.getValue(ASCENDING).equals(TextServer.getString("tokenAsc"));
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
			return FieldGroup.emptyFieldGroup.getIndex();
		}
		return fieldGroup.getIndex();
	}
}
