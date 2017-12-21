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

package com.qtfx.lib.gui.observables;

import com.qtfx.lib.db.Record;
import com.qtfx.lib.db.RecordSet;

import javafx.collections.ModifiableObservableListBase;

/**
 * An observable and modifiable record set.
 *
 * @author Miquel Sas
 */
public class ObservableRecordSet extends ModifiableObservableListBase<Record> {
	
	/** Internal record set. */
	private RecordSet recordSet;

	/**
	 * Constructor.
	 * 
	 * @param recordSet The record set.
	 */
	public ObservableRecordSet(RecordSet recordSet) {
		super();
		this.recordSet = recordSet;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Record get(int index) {
		return recordSet.get(index);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size() {
		return recordSet.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doAdd(int index, Record record) {
		recordSet.add(index, record);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Record doSet(int index, Record record) {
		Record current = recordSet.get(index);
		recordSet.set(index, record);
		return current;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Record doRemove(int index) {
		return recordSet.remove(index);
	}
}
