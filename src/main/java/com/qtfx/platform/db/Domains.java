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

package com.qtfx.platform.db;

import com.qtfx.library.db.Field;
import com.qtfx.library.db.Types;
import com.qtfx.library.util.StringUtils;

/**
 * Centralizes master field definitions (domains). These definitions do not include table attributes like primary key.
 * 
 * @author Miquel Sas
 */
public class Domains {

	/**
	 * Returns field definition for a double value.
	 * 
	 * @param name Field name.
	 * @return The field definition.
	 */
	public static Field getDouble(String name) {
		String header = StringUtils.parseCapitalize(name, "_", " ");
		return getDouble(name, name, header, header, header);
	}

	/**
	 * Returns field definition for a double value.
	 * 
	 * @param name Field name.
	 * @param header The field header.
	 * @param label The field label.
	 * @return The field definition.
	 */
	public static Field getDouble(String name, String header, String label) {
		return getDouble(name, name, header, label, label);
	}

	/**
	 * Returns field definition for a double value.
	 * 
	 * @param name Field name.
	 * @param alias The field alias.
	 * @param header The header.
	 * @param label The label.
	 * @param title The title.
	 * @return The field definition.
	 */
	public static Field getDouble(
		String name,
		String alias,
		String header,
		String label,
		String title) {

		Field field = new Field();
		field.setName(name);
		field.setAlias(alias);
		field.setType(Types.DOUBLE);
		field.setHeader(header);
		field.setLabel(label);
		field.setTitle(title);

		return field;
	}

	/**
	 * Returns field definition for a string value. Header and label are set capitalizing.
	 * 
	 * @param name Field name.
	 * @param length Field length.
	 * @return The field definition.
	 */
	public static Field getString(String name, int length) {
		String header = StringUtils.parseCapitalize(name, "_", " ");
		return getString(name, name, length, header, header, header);
	}

	/**
	 * Returns field definition for a string value.
	 * 
	 * @param name Field name.
	 * @param length Field length.
	 * @param header The field header.
	 * @param label The field label.
	 * @return The field definition.
	 */
	public static Field getString(String name, int length, String header, String label) {
		return getString(name, name, length, header, label, label);
	}

	/**
	 * Returns field definition for a string value.
	 * 
	 * @param name Field name.
	 * @param alias The field alias.
	 * @param length The field length.
	 * @param header The header.
	 * @param label The label.
	 * @param title The title.
	 * @return The field.
	 */
	public static Field getString(
		String name,
		String alias,
		int length,
		String header,
		String label,
		String title) {

		Field field = new Field();
		field.setName(name);
		field.setAlias(alias);
		field.setType(Types.STRING);
		field.setLength(length);
		field.setHeader(header);
		field.setLabel(label);
		field.setTitle(title);

		return field;
	}

	/**
	 * Returns field definition for an integer value.
	 * 
	 * @param name Field name.
	 * @return The field definition.
	 */
	public static Field getInteger(String name) {
		String header = StringUtils.parseCapitalize(name, "_", " ");
		return getInteger(name, name, header, header, header);
	}

	/**
	 * Returns field definition for an integer value.
	 * 
	 * @param name Field name.
	 * @param header The field header.
	 * @param label The field label.
	 * @return The field definition.
	 */
	public static Field getInteger(String name, String header, String label) {
		return getInteger(name, name, header, label, label);
	}

	/**
	 * Returns field definition for an integer value.
	 * 
	 * @param name Field name.
	 * @param alias The field alias.
	 * @param header The header.
	 * @param label The label.
	 * @param title The title.
	 * @return The field.
	 */
	public static Field getInteger(
		String name,
		String alias,
		String header,
		String label,
		String title) {

		Field field = new Field();
		field.setName(name);
		field.setAlias(alias);
		field.setType(Types.INTEGER);

		field.setHeader(header);
		field.setLabel(label);
		field.setTitle(title);

		return field;
	}

	/**
	 * Returns field definition for an long value.
	 * 
	 * @param name Field name.
	 * @return The field definition.
	 */
	public static Field getLong(String name) {
		String header = StringUtils.parseCapitalize(name, "_", " ");
		return getLong(name, name, header, header, header);
	}

	/**
	 * Returns field definition for an long value.
	 * 
	 * @param name Field name.
	 * @param header The field header.
	 * @param label The field label.
	 * @return The field definition.
	 */
	public static Field getLong(String name, String header, String label) {
		return getLong(name, name, header, label, label);
	}

	/**
	 * Returns field definition for an long value.
	 * 
	 * @param name Field name.
	 * @param alias The field alias.
	 * @param header The header.
	 * @param label The label.
	 * @param title The title.
	 * @return The field.
	 */
	public static Field getLong(
		String name,
		String alias,
		String header,
		String label,
		String title) {

		Field field = new Field();
		field.setName(name);
		field.setAlias(alias);
		field.setType(Types.LONG);

		field.setHeader(header);
		field.setLabel(label);
		field.setTitle(title);

		return field;
	}

}
