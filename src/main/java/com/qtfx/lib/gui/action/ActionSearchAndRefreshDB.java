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

package com.qtfx.lib.gui.action;

import java.util.ArrayList;
import java.util.List;

import com.qtfx.lib.db.Condition;
import com.qtfx.lib.db.Criteria;
import com.qtfx.lib.db.Field;
import com.qtfx.lib.db.Persistor;
import com.qtfx.lib.db.PersistorException;
import com.qtfx.lib.db.Record;
import com.qtfx.lib.db.RecordSet;
import com.qtfx.lib.db.Value;
import com.qtfx.lib.gui.FieldControl;

/**
 * Search values in a database.
 *
 * @author Miquel Sas
 */
public class ActionSearchAndRefreshDB extends ActionSearchAndRefresh {

	/** The search record, must have a persistor and belong to the search table. */
	private Record searchRecord;
	/** The list of aliases of key fields in the search record (table). */
	private List<String> searchKeyAliases = new ArrayList<>();
	/** The list of aliases of refresh fields in the search record (table). */
	private List<String> searchRefreshAliases = new ArrayList<>();

	/**
	 * Constructor.
	 */
	public ActionSearchAndRefreshDB() {
		super();
	}

	/**
	 * Constructor.
	 * 
	 * @param searchRecord The search record.
	 */
	public ActionSearchAndRefreshDB(Record searchRecord) {
		super();
		this.searchRecord = searchRecord;
	}

	/**
	 * Add a search key alias.
	 * 
	 * @param alias The alias.
	 */
	public void addSearchKeyAlias(String alias) {
		searchKeyAliases.add(alias);
	}

	/**
	 * Add a search refresh alias.
	 * 
	 * @param alias The alias.
	 */
	public void addSearchRefreshAlias(String alias) {
		searchRefreshAliases.add(alias);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected List<Value> getRefreshValues(List<FieldControl> keyControls, List<FieldControl> refreshControls) {
		
		// If the search persistor has not been set, try to find a valid one from the refresh controls.
		if (searchRecord == null) {
			for (FieldControl control : refreshControls) {
				Field field = control.getField();
				if (field.getTable() != null && field.getTable().getPersistor() != null) {
					searchRecord = field.getTable().getDefaultRecord();
					break;
				}
			}
			if (searchRecord == null) {
				throw new IllegalStateException("No valid search record has been set.");
			}
			if (searchRecord.getPersistor() == null) {
				throw new IllegalStateException("Search record without persistor.");
			}
		}
		// Check lengths of lists.
		if (searchKeyAliases.size() != keyControls.size()) {
			throw new IllegalStateException("The number of key aliases and key controls must be the same.");
		}
		if (searchRefreshAliases.size() != refreshControls.size()) {
			throw new IllegalStateException("The number of refresh aliases and refresh controls must be the same.");
		}
		
		// Build a criteria with the search entity and the key components.
		Criteria criteria = new Criteria(Criteria.AND);
		for (int i = 0; i < keyControls.size(); i++) {
			String alias = searchKeyAliases.get(i);
			Field field = searchRecord.getField(alias);
			Value value = keyControls.get(i).getValue();
			criteria.add(Condition.fieldEQ(field, value));
		}
		
		// Retrieve the possible list of records.
		RecordSet recordSet = null;
		try {
			Persistor persistor = searchRecord.getPersistor();
			recordSet = persistor.select(criteria);
		} catch (PersistorException exc) {
			exc.printStackTrace();
		}
		if (recordSet == null || recordSet.isEmpty()) {
			return null;
		}
		Record refreshRecord = recordSet.get(0);
		List<Value> values = new ArrayList<>();
		for (String alias : searchRefreshAliases) {
			values.add(refreshRecord.getValue(alias));
		}

		return values;
	}

}
