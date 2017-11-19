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

import java.util.Locale;

import com.qtfx.lib.db.Record;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

/**
 * A callback to use with fields in a table view.
 *
 * @author Miquel Sas
 */
public class CallbackField implements Callback<CellDataFeatures<Record, String>, ObservableValue<String>> {

	/** Index of the field. */
	private int index = -1;
	/** Alias of the field. */
	private String alias;
	/** Optional locale. */
	private Locale locale;

	/**
	 * Constructor assigning the field index.
	 * 
	 * @param index The field index.
	 */
	public CallbackField(int index) {
		this(index, Locale.getDefault());
	}

	/**
	 * Constructor assigning the field index and the desired locale.
	 * 
	 * @param index The field index.
	 * @param locale The required locale.
	 */
	public CallbackField(int index, Locale locale) {
		super();
		this.index = index;
		this.locale = locale;
	}

	/**
	 * Constructor assigning the field alias.
	 * 
	 * @param alias The field alias.
	 * @param locale The required locale.
	 */
	public CallbackField(String alias) {
		this(alias, Locale.getDefault());
	}

	/**
	 * Constructor assigning the field alias and the desired locale.
	 * 
	 * @param alias The field alias.
	 * @param locale The required locale.
	 */
	public CallbackField(String alias, Locale locale) {
		super();
		this.alias = alias;
		this.locale = locale;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ObservableValue<String> call(CellDataFeatures<Record, String> param) {
		if (index >= 0) {
			return new ReadOnlyObjectWrapper<String>(param.getValue().getValue(index).toStringFormatted(locale));
		}
		return new ReadOnlyObjectWrapper<String>(param.getValue().getValue(alias).toStringFormatted(locale));
	}
}
