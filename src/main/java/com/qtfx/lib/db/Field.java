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

package com.qtfx.lib.db;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import com.qtfx.lib.util.Strings;

/**
 * Definition a an item usually of tabular data.
 *
 * @author Miquel Sas
 */
public class Field implements Comparable<Object> {

	/**
	 * A possible value is a pair value-label.
	 */
	public static class PossibleValue {
		private Value value;
		private String label;

		public PossibleValue(Value value, String label) {
			this.value = value;
			this.label = label;
		}

		public Value getValue() {
			return value;
		}

		public String getLabel() {
			return label;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof PossibleValue) {
				PossibleValue pv = (PossibleValue) obj;
				return this.value.equals(pv.value);
			}
			return false;
		}
	}

	///////////////////
	// Main properties.

	/** Field name. */
	private String name;
	/** Optional field alias, if not set the name is used. */
	private String alias;
	/** Length if applicable, otherwise -1. */
	private int length = -1;
	/** Decimals if applicable, otherwise -1. */
	private int decimals = -1;
	/** Type. */
	private Types type;

	//////////////////////////////////////
	// Description and display properties.

	/** Description, normally the longer description. */
	private String description;
	/** Label on forms. */
	private String label;
	/** Header on grids. */
	private String header;
	/** Title or short description. */
	private String title;
	/** Adjusted display length. */
	private int displayLength;
	/** Field group. */
	private FieldGroup fieldGroup = FieldGroup.emptyFieldGroup;

	////////////////////////////////////////
	// Validation and formatting properties.

	/** Initial value. */
	private Value initialValue;
	/** Maximum value. */
	private Value maximumValue;
	/** Minimum value. */
	private Value minimumValue;
	/** List of possible values. */
	private List<PossibleValue> possibleValues = new ArrayList<>();
	/** A flag indicating whether a non empty value is required for this field. */
	private boolean required;
	/** Upper case flag. */
	private boolean uppercase;
	/** Optional calculator. */
	private Calculator calculator;

	///////////////////////////////
	// Database related properties.

	/**
	 * A boolean that indicates if the field, when not present in an insert clause (DEFAULT) should be initialized with
	 * the database function for a datte, time or time stamp.
	 */
	private boolean currentDateTimeOrTimestamp = false;
	/** A flag that indicates whether this field is persistent. */
	private boolean persistent = true;
	/** A flag that indicates whether this field can be null. */
	private boolean nullable = true;
	/** A flag that indicates whether this field is a primary key field. */
	private boolean primaryKey = false;
	/** A supported database function if the column is virtual or calculated. */
	private String function;
	/** Optional parent table. */
	private Table table;
	/** Optional parent view. */
	private View view;

	/**
	 * Default constructor.
	 */
	public Field() {
		super();
	}

	/**
	 * Copy constructor.
	 * 
	 * @param field
	 */
	public Field(Field field) {
		super();

		// Main properties.
		this.name = field.name;
		this.alias = field.alias;
		this.length = field.length;
		this.decimals = field.decimals;
		this.type = field.type;

		// Description and display properties.
		this.description = field.description;
		this.label = field.label;
		this.header = field.header;
		this.title = field.title;
		this.displayLength = field.displayLength;

		// Validation and formatting properties.
		this.initialValue = field.initialValue;
		this.maximumValue = field.maximumValue;
		this.minimumValue = field.minimumValue;
		this.possibleValues = new ArrayList<>(field.possibleValues);
		this.required = field.required;
		this.uppercase = field.uppercase;

		// Database related properties.
		this.persistent = field.persistent;
		this.nullable = field.nullable;
		this.primaryKey = field.primaryKey;
		this.function = field.function;
		this.table = field.table;
		this.view = field.view;
	}

	///////////////////
	// Main properties.

	/**
	 * Get the name.
	 *
	 * @return The name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name.
	 *
	 * @param name The name of the field.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get the field alias.
	 *
	 * @return The field alias.
	 */
	public String getAlias() {
		if (alias == null) {
			return getName();
		}
		return alias;
	}

	/**
	 * Set the field alias.
	 *
	 * @param alias The field alias.
	 */
	public void setAlias(String alias) {
		this.alias = alias;
	}

	/**
	 * Get the length if applicable, otherwise -1.
	 *
	 * @return The field length if applicable, otherwise -1.
	 */
	public int getLength() {
		return length;
	}

	/**
	 * Set the field length.
	 *
	 * @param length The field length.
	 */
	public void setLength(int length) {
		this.length = length;
	}

	/**
	 * Get the number of decimal places if applicable.
	 *
	 * @return The number of decimal places.
	 */
	public int getDecimals() {
		if (!isNumber()) {
			return 0;
		}
		if (isInteger() || isLong()) {
			return 0;
		}
		return decimals;
	}

	/**
	 * Set the number of decimal places.
	 *
	 * @param decimals The number of decimal places.
	 */
	public void setDecimals(int decimals) {
		this.decimals = decimals;
	}

	/**
	 * Get the type.
	 *
	 * @return The type.
	 */
	public Types getType() {
		return type;
	}

	/**
	 * Set the type.
	 *
	 * @param type The type.
	 */
	public void setType(Types type) {
		this.type = type;
	}

	//////////////////////////////////////
	// Description and display properties.

	/**
	 * Get the long description.
	 *
	 * @return The long description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Set the long description.
	 *
	 * @param description The long description.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Get the label used in forms.
	 *
	 * @return The label used in forms.
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Set the label used in forms.
	 *
	 * @param label The label used in forms.
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * Get the header used in tables.
	 *
	 * @return The header used in tables.
	 */
	public String getHeader() {
		return header;
	}

	/**
	 * Set the header used in tables.
	 *
	 * @param header The header used in tables.
	 */
	public void setHeader(String header) {
		this.header = header;
	}

	/**
	 * Get the title or short description.
	 *
	 * @return The title or short description.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Set the title or short description.
	 *
	 * @param title The title or short description.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Get the display length.
	 *
	 * @return The display length.
	 */
	public int getDisplayLength() {
		if (displayLength > 0) {
			return displayLength;
		}
		return getLength();
	}

	/**
	 * Set the display length.
	 *
	 * @param displayLength The display length.
	 */
	public void setDisplayLength(int displayLength) {
		this.displayLength = displayLength;
	}

	/**
	 * Returns the field group if any.
	 * 
	 * @return The field group if any.
	 */
	public FieldGroup getFieldGroup() {
		return fieldGroup;
	}

	/**
	 * Sets the field group.
	 * 
	 * @param fieldGroup The field group.
	 */
	public void setFieldGroup(FieldGroup fieldGroup) {
		this.fieldGroup = fieldGroup;
	}

	///////////////////////////////
	// Safe description properties.

	/**
	 * Returns a display description of this field using the description, title, label, header or something not null.
	 * 
	 * @return A not null description.
	 */
	public String getDisplayDescription() {
		return Strings.getFirstNotNull(getDescription(), getTitle(), getLabel(), getHeader());
	}

	/**
	 * Returns a display label of this field using the label, header, title, description or something not null.
	 * 
	 * @return A not null header.
	 */
	public String getDisplayLabel() {
		return Strings.getFirstNotNull(getLabel(), getHeader(), getTitle(), getDescription());
	}

	/**
	 * Returns a display header of this field using the header, label, title, description or something not null.
	 * 
	 * @return A not null header.
	 */
	public String getDisplayHeader() {
		return Strings.getFirstNotNull(getHeader(), getLabel(), getTitle(), getDescription());
	}

	/**
	 * Returns a display title of this field using the title, label, header, description or something not null.
	 * 
	 * @return A not null title.
	 */
	public String getDisplayTitle() {
		return Strings.getFirstNotNull(getTitle(), getLabel(), getHeader(), getDescription());
	}

	////////////////////////////////////////
	// Validation and formatting properties.

	/**
	 * Get the initial value.
	 *
	 * @return The initial value.
	 */
	public Value getInitialValue() {
		return initialValue;
	}

	/**
	 * Set the initial value.
	 *
	 * @param initialValue The initial value.
	 */
	public void setInitialValue(Value initialValue) {
		Types.validateValueType(initialValue, getType());
		this.initialValue = initialValue;
	}

	/**
	 * Get the maximum value.
	 *
	 * @return The maximum value.
	 */
	public Value getMaximumValue() {
		return maximumValue;
	}

	/**
	 * Set the maximum value.
	 *
	 * @param maximumValue The maximum value.
	 */
	public void setMaximumValue(Value maximumValue) {
		Types.validateValueType(maximumValue, getType());
		this.maximumValue = maximumValue;
	}

	/**
	 * Get the minimum value.
	 *
	 * @return The minimum value.
	 */
	public Value getMinimumValue() {
		return minimumValue;
	}

	/**
	 * Set the minimum value.
	 *
	 * @param minimumValue The minimum value.
	 */
	public void setMinimumValue(Value minimumValue) {
		Types.validateValueType(minimumValue, getType());
		this.minimumValue = minimumValue;
	}

	/**
	 * Check whether a non empty value is required for this field.
	 *
	 * @return A boolean.
	 */
	public boolean isRequired() {
		return required;
	}

	/**
	 * Set whether a non empty value is required for this field.
	 *
	 * @param required A boolean.
	 */
	public void setRequired(boolean required) {
		this.required = required;
	}

	/**
	 * Check whether edition is upper case.
	 *
	 * @return A boolean
	 */
	public boolean isUppercase() {
		return uppercase;
	}

	/**
	 * Set whether edition is upper case.
	 *
	 * @param uppercase A boolean indicating that the value is uppercase.
	 */
	public void setUppercase(boolean uppercase) {
		this.uppercase = uppercase;
	}

	/**
	 * Add a possible value.
	 * 
	 * @param value The value.
	 */
	public void addPossibleValue(Value value) {
		addPossibleValue(value, value.toString());
	}

	/**
	 * Add a possible value.
	 * 
	 * @param value The value.
	 * @param label The label.
	 */
	public void addPossibleValue(Value value, String label) {
		PossibleValue possibleValue = new PossibleValue(value, label);
		if (!possibleValues.contains(possibleValue)) {
			possibleValues.add(possibleValue);
		}
	}

	/**
	 * Clear the list of possible values.
	 */
	public void clearPossibleValues() {
		possibleValues.clear();
	}

	/**
	 * Return the list of possible values.
	 * 
	 * @return The list of possible values.
	 */
	public List<PossibleValue> getPossibleValues() {
		return new ArrayList<>(possibleValues);
	}

	/**
	 * Returns the possible value label or null if not applicable or not found.
	 * 
	 * @param value The target value.
	 * @return The possible value label.
	 */
	public String getPossibleValueLabel(Object value) {
		for (PossibleValue possibleValue : possibleValues) {
			if (possibleValue.getValue().equals(value)) {
				return possibleValue.getLabel();
			}
		}
		return "";
	}

	/**
	 * Return the optional calculator.
	 * 
	 * @return The optional calculator.
	 */
	public Calculator getCalculator() {
		return calculator;
	}

	/**
	 * Set the optional calculator.
	 * 
	 * @param calculator The optional calculator.
	 */
	public void setCalculator(Calculator calculator) {
		this.calculator = calculator;
	}

	///////////////////////////////
	// Database related properties.

	/**
	 * Check the current date, time or time stamp flag.
	 * 
	 * @return A boolean.
	 */
	public boolean isCurrentDateTimeOrTimestamp() {
		return currentDateTimeOrTimestamp;
	}

	/**
	 * Check if his field should initialize to the current date.
	 * 
	 * @return A boolean indicating that it should be initialized to the current date.
	 */
	public boolean isCurrentDate() {
		return isDate() && isCurrentDateTimeOrTimestamp();
	}

	/**
	 * Check if his field should initialize to the current time.
	 * 
	 * @return A boolean indicating that it should be initialized to the current time.
	 */
	public boolean isCurrentTime() {
		return isTime() && isCurrentDateTimeOrTimestamp();
	}

	/**
	 * Check if his field should initialize to the current time stamp.
	 * 
	 * @return A boolean indicating that it should be initialized to the current time stamp.
	 */
	public boolean isCurrentTimestamp() {
		return isTimestamp() && isCurrentDateTimeOrTimestamp();
	}

	/**
	 * Set the current date, time or time stamp flag.
	 * 
	 * @param currentDateTimeOrTimestamp A boolean.
	 */
	public void setCurrentDateTimeOrTimestamp(boolean currentDateTimeOrTimestamp) {
		this.currentDateTimeOrTimestamp = currentDateTimeOrTimestamp;
	}

	/**
	 * Check whether this field is persistent.
	 *
	 * @return A boolean
	 */
	public boolean isPersistent() {
		return persistent;
	}

	/**
	 * Sets whether this field is persistent.
	 *
	 * @param persistent A boolean.
	 */
	public void setPersistent(boolean persistent) {
		this.persistent = persistent;
	}

	/**
	 * Check whether this field can be null.
	 *
	 * @return A boolean
	 */
	public boolean isNullable() {
		return nullable;
	}

	/**
	 * Set whether this field can be null.
	 *
	 * @param nullable A boolean
	 */
	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

	/**
	 * Check whether this field is a primary key field.
	 *
	 * @return A boolean
	 */
	public boolean isPrimaryKey() {
		return primaryKey;
	}

	/**
	 * Set whether this field is a primary key field.
	 *
	 * @param primaryKey A boolean.
	 */
	public void setPrimaryKey(boolean primaryKey) {
		this.primaryKey = primaryKey;
		if (primaryKey) {
			setNullable(false);
		}
	}

	/**
	 * Gets the function or formula.
	 *
	 * @return The function.
	 */
	public String getFunction() {
		return function;
	}

	/**
	 * Sets the function or formula.
	 *
	 * @param function The function.
	 */
	public void setFunction(String function) {
		this.function = function;
	}

	/**
	 * Return this field parent table.
	 *
	 * @return The parent table.
	 */
	public Table getTable() {
		return table;
	}

	/**
	 * Set this field parent table.
	 *
	 * @param table The parent table.
	 */
	public void setTable(Table table) {
		this.table = table;
	}

	/**
	 * Return this field parent view.
	 *
	 * @return The parent view.
	 */
	public View getView() {
		return view;
	}

	/**
	 * Set this field parent view.
	 *
	 * @param view The parent view.
	 */
	public void setView(View view) {
		this.view = view;
	}

	/**
	 * Gets the name to use in a <code>CREATE TABLE</code> or <code>ALTER TABLE</code> statement.
	 *
	 * @return The name.
	 */
	public String getNameCreate() {
		return getName();
	}

	/**
	 * Returns the name to use in an <code>DELETE</code> statement.
	 *
	 * @return The name.
	 */
	public String getNameDelete() {
		return getNameUpdate();
	}

	/**
	 * Returns the name of the field in the database, qualified with the parent table or view alias if it exists.
	 *
	 * @return The name.
	 */
	public String getNameParent() {
		String name = getName();
		String tableAlias = (getTable() != null ? getTable().getAlias() : null);
		if (tableAlias != null) {
			return tableAlias + "." + name;
		}
		return name;
	}

	/**
	 * Returns the name to use in a relation of a select statement.
	 *
	 * @return The name.
	 */
	public String getNameRelate() {
		return getNameParent();
	}

	/**
	 * Returns the name to use in the column list of a <code>SELECT</code> query.
	 *
	 * @return The name.
	 */
	public String getNameSelect() {
		StringBuilder name = new StringBuilder();
		if (isVirtual()) {
			name.append("(");
			name.append(getFunction());
			name.append(")");
		} else {
			name.append(getNameParent());
		}
		if (getAlias() != null) {
			name.append(" AS ");
			name.append(getAlias());
		}
		return name.toString();
	}

	/**
	 * Returns the name to use in an <code>UPDATE</code> statement.
	 *
	 * @return The name.
	 */
	public String getNameUpdate() {
		String name = getName();
		String parentName = (getTable() != null ? getTable().getName() : null);
		if (parentName != null) {
			return parentName + "." + name;
		}
		return name;
	}

	/**
	 * Returns the name to use in a <code>WHERE</code> clause.
	 *
	 * @return The name.
	 */
	public String getNameWhere() {
		if (isVirtual()) {
			return "(" + getFunction() + ")";
		}
		return getNameParent();
	}

	/**
	 * Returns the name to use in a <code>GROUP BY</code> clause of a <code>SELECT</code> query.
	 *
	 * @return The name.
	 */
	public String getNameGroupBy() {
		StringBuilder name = new StringBuilder();
		if (isVirtual()) {
			name.append("(");
			name.append(getFunction());
			name.append(")");
		} else {
			name.append(getNameParent());
		}
		return name.toString();
	}

	/**
	 * Returns the name to use in an <code>ORDER BY</code> clause of a select query.
	 *
	 * @return The name.
	 */
	public String getNameOrderBy() {
		StringBuilder name = new StringBuilder();
		if (isVirtual()) {
			name.append("(");
			name.append(getFunction());
			name.append(")");
		} else {
			name.append(getNameParent());
		}
		return name.toString();
	}

	/////////////////
	// Type checking.

	/**
	 * Check if this field is boolean.
	 *
	 * @return A boolean.
	 */
	public boolean isBoolean() {
		return getType().isBoolean();
	}

	/**
	 * Check if this field is a string.
	 *
	 * @return A boolean.
	 */
	public boolean isString() {
		return getType().isString();
	}

	/**
	 * Check if this field is a number (decimal) with fixed precision.
	 *
	 * @return A boolean.
	 */
	public boolean isDecimal() {
		return getType().isDecimal();
	}

	/**
	 * Check if this field is a double.
	 *
	 * @return A boolean.
	 */
	public boolean isDouble() {
		return getType().isDouble();
	}

	/**
	 * Check if this field is an integer.
	 *
	 * @return A boolean.
	 */
	public boolean isInteger() {
		return getType().isInteger();
	}

	/**
	 * Check if this field is a long.
	 *
	 * @return A boolean.
	 */
	public boolean isLong() {
		return getType().isLong();
	}

	/**
	 * Check if this field is a number (decimal, double or integer).
	 *
	 * @return A boolean.
	 */
	public boolean isNumber() {
		return getType().isNumber();
	}

	/**
	 * Check if this field is a floating point number.
	 *
	 * @return A boolean.
	 */
	public boolean isFloatingPoint() {
		return getType().isFloatingPoint();
	}

	/**
	 * Check if this field is a date.
	 *
	 * @return A boolean.
	 */
	public boolean isDate() {
		return getType().isDate();
	}

	/**
	 * Check if this field is a time.
	 *
	 * @return A boolean.
	 */
	public boolean isTime() {
		return getType().isTime();
	}

	/**
	 * Check if this field is a time.
	 *
	 * @return A boolean.
	 */
	public boolean isTimestamp() {
		return getType().isTimestamp();
	}

	/**
	 * Check if this field is a date, time or time stamp.
	 *
	 * @return A boolean.
	 */
	public boolean isDateTimeOrTimestamp() {
		return getType().isDateTimeOrTimestamp();
	}

	/**
	 * Check if this field is binary (byte[]).
	 *
	 * @return A boolean.
	 */
	public boolean isByteArray() {
		return getType().isTimestamp();
	}

	/**
	 * Returns the default value padded with characters if it is string.
	 *
	 * @return The default blank value.
	 */
	public Value getBlankValue() {
		if (isString()) {
			return new Value(Strings.repeat(" ", getLength()));
		}
		return getDefaultValue();
	}

	/**
	 * Returns the default value for this field.
	 *
	 * @return The default value.
	 */
	public Value getDefaultValue() {
		if (isBoolean()) {
			return new Value(false);
		}
		if (isByteArray()) {
			return new Value(new byte[0]);
		}
		if (isDate()) {
			return new Value((Date) null);
		}
		if (isDecimal()) {
			return new Value(new BigDecimal(0).setScale(getDecimals(), BigDecimal.ROUND_HALF_UP));
		}
		if (isDouble()) {
			return new Value((double) 0);
		}
		if (isInteger()) {
			return new Value(0);
		}
		if (isLong()) {
			return new Value((long) 0);
		}
		if (isString()) {
			return new Value("");
		}
		if (isTime()) {
			return new Value((Time) null);
		}
		if (isTimestamp()) {
			return new Value((Timestamp) null);
		}
		return null;
	}

	///////////////////////////////////
	// Comparable and object overrides.

	/**
	 * Returns a negative integer, zero, or a positive integer as this value is less than, equal to, or greater than the
	 * specified value.
	 * <p>
	 * A field is considered to be equal to another field if the alias, type, length and decimals are the same.
	 *
	 * @param o The object to compare.
	 * @return The comparison integer.
	 */
	@Override
	public int compareTo(Object o) {
		Field field = null;
		try {
			field = (Field) o;
		} catch (ClassCastException exc) {
			throw new UnsupportedOperationException(MessageFormat.format("Not comparable type: {0}", o.getClass().getName()));
		}
		if (getAlias().equals(field.getAlias())) {
			if (getType().equals(field.getType())) {
				if (getLength() == field.getLength()) {
					if (getDecimals() == field.getDecimals()) {
						return 0;
					}
				}
			}
		}
		return getAlias().compareTo(field.getAlias());
	}

	/**
	 * Returns the hash code for this field.
	 *
	 * @return The hash code
	 */
	@Override
	public int hashCode() {
		int hash = 3;
		return hash;
	}

	/**
	 * Check if this field is a foreign field, that is, belongs to a foreign table in the list of relations of the
	 * parent view.
	 * 
	 * @return A boolean that indicates if this field is a foreign field
	 */
	public boolean isForeign() {
		// Parent table null and parent view not null can not be. Or both null, or parent view or none.
		if (getTable() == null) {
			return false;
		}
		if (getView() == null) {
			return false;
		}
		if (getView().getMasterTable().equals(getTable())) {
			return false;
		}
		List<Relation> relations = getView().getRelations();
		for (Relation relation : relations) {
			if (relation.getForeignTable().equals(getTable())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Check if this field is a local field. Has no parent table or belongs to the parent table.
	 * 
	 * @return A boolean that indicates if this field is a local field.
	 */
	public boolean isLocal() {
		return !isForeign();
	}

	/**
	 * Check if this column is virtual. A column is virtual is it has a function but not a name.
	 *
	 * @return A <code>boolean</code>.
	 */
	public boolean isVirtual() {
		return (getFunction() != null);
	}
}
