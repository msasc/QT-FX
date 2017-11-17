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

package com.qtfx.lib.gui;

import com.qtfx.lib.db.Record;
import com.qtfx.lib.db.Value;

import javafx.util.Callback;

/**
 * A callback implementation for records.
 *
 * @author Miquel Sas
 */
public class CallbackRecord implements Callback<Record, Value> {

	/** Field index. */
	private int index = -1;
	/** Field alias. */
	private String alias;

	/**
	 * Constructor.
	 * 
	 * @param index The field index.
	 */
	public CallbackRecord(int index) {
		super();
		this.index = index;
	}

	/**
	 * Constructor.
	 * 
	 * @param alias The field alias.
	 */
	public CallbackRecord(String alias) {
		super();
		this.alias = alias;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Value call(Record record) {
		if (alias != null) {
			return record.getValue(alias);
		}
		return record.getValue(index);
	}

}
