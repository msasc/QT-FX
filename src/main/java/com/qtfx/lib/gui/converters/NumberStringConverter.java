package com.qtfx.lib.gui.converters;

import java.math.BigDecimal;
import java.util.Locale;

import com.qtfx.lib.db.Field;
import com.qtfx.lib.db.Value;
import com.qtfx.lib.util.Formats;

import javafx.util.StringConverter;

/**
 * String converter for values of type number
 * 
 * @author Miquel Sas
 */
public class NumberStringConverter extends StringConverter<Value> {

	/** Field. */
	private Field field;
	/** Locale. */
	private Locale locale;
	
	/**
	 * Constructor.
	 * 
	 * @param field Field.
	 * @param locale Locale.
	 */
	public NumberStringConverter(Field field, Locale locale) {
		super();
		if (!field.isNumber()) {
			throw new IllegalArgumentException("Field is not a number");
		}
		this.field = field;
		this.locale = locale;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString(Value value) {
		if (value == null) {
			return toString(field.getDefaultValue());
		}
		return Formats.getNumberFormat(field.getDisplayDecimals(), locale).format(value.getDouble());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Value fromString(String string) {
		try {
			if (field.isDecimal()) {
				BigDecimal value = Formats.formattedToBigDecimal(string, locale);
				value = value.setScale(field.getDecimals(), BigDecimal.ROUND_HALF_UP);
				return new Value(value);
			}
			if (field.isDouble()) {
				double value = Formats.formattedToDouble(string, locale);
				return new Value(value);
			}
			if (field.isInteger()) {
				int value = Formats.formattedToInteger(string, locale);
				return new Value(value);
			}
			if (field.isLong()) {
				long value = Formats.formattedToLong(string, locale);
				return new Value(value);
			}
		} catch (java.text.ParseException exc) {
			exc.printStackTrace();
		}
		return null;
	}
}