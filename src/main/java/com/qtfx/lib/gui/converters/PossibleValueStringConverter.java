package com.qtfx.lib.gui.converters;

import com.qtfx.lib.db.Field;
import com.qtfx.lib.db.Value;

import javafx.util.StringConverter;

/**
 * String converter for a possible value.
 * 
 * @author Miquel Sas
 */
public class PossibleValueStringConverter extends StringConverter<Value> {
	
	/** Field. */
	private Field field;
	
	/**
	 * Constructor.
	 * @param field The field
	 */
	public PossibleValueStringConverter(Field field) {
		super();
		this.field = field;
		if (!this.field.isPossibleValues()) {
			throw new IllegalArgumentException("Field is not possible values");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString(Value value) {
		if (value instanceof Field.PossibleValue) {
			Field.PossibleValue pv = (Field.PossibleValue) value;
			return pv.getLabel();
		}
		return value.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Value fromString(String string) {
		return field.getPossibleValue(string);
	}
}