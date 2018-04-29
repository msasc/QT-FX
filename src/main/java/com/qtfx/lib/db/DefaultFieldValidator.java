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

import java.text.MessageFormat;
import java.util.ArrayList;

import com.qtfx.lib.app.Session;

/**
 * Default field validator for type, maximum, minimum and possible values, required and nullable.
 *
 * @author Miquel Sas
 */
public class DefaultFieldValidator extends Validator<Value> {
	
	/** The field. */
	private Field field;
	/** The session. */
	private Session session;

	/**
	 * Constructor.
	 * 
	 * @param field The field to validate.
	 */
	public DefaultFieldValidator(Field field) {
		this(Session.getSession(), field);
	}

	/**
	 * Constructor.
	 * 
	 * @param session The working session for string literals.
	 * @param field The field to validate.
	 */
	public DefaultFieldValidator(Session session, Field field) {
		super();
		this.session = session;
		this.field = field;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean validate(Value value, Object operation) {

		// Strict type
		if (!value.getType().equals(field.getType())) {
			return false;
		}

		// Maximum value
		if (field.getMaximumValue() != null) {
			if (value.compareTo(field.getMaximumValue()) > 0) {
				return false;
			}
		}

		// Minimum value
		if (field.getMinimumValue() != null) {
			if (value.compareTo(field.getMinimumValue()) < 0) {
				return false;
			}
		}

		// Possible values
		if (!field.getPossibleValues().isEmpty()) {
			if (!value.in(new ArrayList<>(field.getPossibleValues()))) {
				return false;
			}
		}

		// Non empty required
		if (field.isRequired() && value.isEmpty()) {
			return false;
		}

		// Nullable
		if (!field.isNullable() && value.isNull()) {
			return false;
		}
		
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getMessage(Value value, Object operation) {

		// Strict type
		if (!value.getType().equals(field.getType())) {
			return MessageFormat.format(session.getString("fieldValidType"), value.getType(), field.getType());
		}

		// Maximum value
		if (field.getMaximumValue() != null) {
			if (value.compareTo(field.getMaximumValue()) > 0) {
				return MessageFormat.format(session.getString("fieldValidMax"), value, field.getMaximumValue());
			}
		}

		// Minimum value
		if (field.getMinimumValue() != null) {
			if (value.compareTo(field.getMinimumValue()) < 0) {
				return MessageFormat.format(session.getString("fieldValidMin"), value, field.getMinimumValue());
			}
		}

		// Possible values
		if (!field.getPossibleValues().isEmpty()) {
			if (!value.in(new ArrayList<>(field.getPossibleValues()))) {
				return MessageFormat.format(session.getString("fieldValidPossible"), value);
			}
		}

		// Non empty required
		if (field.isRequired() && value.isEmpty()) {
			return session.getString("fieldValidEmpy");
		}

		// Nullable
		if (!field.isNullable() && value.isNull()) {
			return session.getString("fieldValidEmpy");
		}

		return null;
	}

}
