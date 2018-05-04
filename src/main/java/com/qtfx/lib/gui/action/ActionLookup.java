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

package com.qtfx.lib.gui.action;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qtfx.lib.db.Condition;
import com.qtfx.lib.db.Criteria;
import com.qtfx.lib.db.Field;
import com.qtfx.lib.db.Order;
import com.qtfx.lib.db.PersistorException;
import com.qtfx.lib.db.Record;
import com.qtfx.lib.db.RecordSet;
import com.qtfx.lib.db.RecordSetCustomizer;
import com.qtfx.lib.db.Value;
import com.qtfx.lib.gui.FX;
import com.qtfx.lib.gui.FieldControl;
import com.qtfx.lib.gui.FormRecordPane;
import com.qtfx.lib.gui.LookupRecords;
import com.qtfx.lib.gui.action.handlers.ActionEventHandler;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Control;

/**
 * An action to lookup a list of records, select one of them and update the referred controls.
 * <p>
 * Normally to define an action lookup it is required to complete the following steps:
 * <ul>
 * <li>1. Define the master {@link com.qtfx.lib.db.Record}, used to display the possible values.
 * <li>2. Define the columns to be displayed.
 * <li>3. Define the link between the the screen context (using the field keys) and the fields of the displayed records.
 * <li>4. Define the way to populate data:
 * <ul>
 * <li>Letting the action to automatically populate data. In this case you can create an optional
 * {@link com.qtfx.lib.db.Criteria} based on special search criteria. When collecting data also can be set the order and
 * distinct flag.
 * <li>Setting the {@link com.qtfx.lib.db.RecordSet}.
 * </ul>
 * </ul>
 * 
 * @author Miquel Sas
 */
public class ActionLookup extends ActionEventHandler {
	
	/** Logger instance. */
	private static final Logger LOGGER = LogManager.getLogger();

	/////////////////////////
	// Decoration properties.

	/** An optional title for this lookup. */
	private String title;

	/////////////////////////
	// Definition properties.

	/** The master record. */
	private Record masterRecord;
	/** List of local key fields. */
	private List<Field> localKeyFields = new ArrayList<>();
	/** List of foreign key fields. */
	private List<Field> foreignKeyFields = new ArrayList<>();

	/////////////////////////////
	// Data retrieval properties.

	/**
	 * An optional {@link com.qtfx.lib.db.Criteria} to further filter the {@link com.qtfx.lib.db.RecordSet}, either when
	 * retrieved from a database or when set directly.
	 */
	private Criteria criteria;
	/** The optional {@link com.qtfx.lib.db.RecordSet}, when set no lookup in a database is performed. */
	private RecordSet recordSet;
	/**
	 * An optional {@link com.qtfx.lib.db.RecordSetCustomizer} to further adapt the {@link com.qtfx.lib.db.RecordSet},
	 * either when retrieved or set directly. Normally, when set directly, data would come already customized.
	 */
	private RecordSetCustomizer recordSetCustomizer;
	/** Optional {@link com.qtfx.lib.db.Order}. */
	private Order order;

	/**
	 * Constructor.
	 * 
	 * @param node The reference node.
	 */
	public ActionLookup(Node node) {
		super(node);
	}

	/////////////////////////
	// Decoration properties.

	/**
	 * Set the title.
	 * 
	 * @param title The title.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/////////////////////////
	// Definition properties.

	/**
	 * Set the master record.
	 * 
	 * @param masterRecord The master record.
	 */
	public void setMasterRecord(Record masterRecord) {
		this.masterRecord = masterRecord;
	}

	/**
	 * Add a local-foreign key field pair.
	 * 
	 * @param localKeyField The local key field.
	 * @param foreignKeyField The foreign key field.
	 */
	public void addKeyFields(Field localKeyField, Field foreignKeyField) {
		localKeyFields.add(localKeyField);
		foreignKeyFields.add(foreignKeyField);
	}

	/////////////////////////////
	// Data retrieval properties.

	/**
	 * Set the criteria.
	 * 
	 * @param criteria The criteria.
	 */
	public void setCriteria(Criteria criteria) {
		this.criteria = criteria;
	}

	/**
	 * Set the record set.
	 * 
	 * @param recordSet The record set.
	 */
	public void setRecordSet(RecordSet recordSet) {
		this.recordSet = recordSet;
	}

	/**
	 * Set the record set customizer.
	 * 
	 * @param recordSetCustomizer The record set customizer.
	 */
	public void setRecordSetCustomizer(RecordSetCustomizer recordSetCustomizer) {
		this.recordSetCustomizer = recordSetCustomizer;
	}

	/**
	 * Set the order.
	 * 
	 * @param order The order.
	 */
	public void setOrder(Order order) {
		this.order = order;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handle(ActionEvent event) {

		// Check initial state.

		if (masterRecord == null) {
			throw new IllegalStateException("Master record required.");
		}
		if (localKeyFields.isEmpty()) {
			throw new IllegalStateException("Local-foreign key fields required.");
		}
		// Validate that foreign key fields are the master record primary key fields.
		if (!masterRecord.getPrimaryKeyFields().equals(foreignKeyFields)) {
			throw new IllegalStateException("Foreign key fields must be the master record primary key fields.");
		}

		// Add the field aliases to be displayed for the master record, these are the foreign key fields and fields from
		// the foreign table that are main description and lookup.

		List<String> lookupAliases = new ArrayList<>();
		for (Field field : foreignKeyFields) {
			lookupAliases.add(field.getAlias());
		}
		Field mainDescription = masterRecord.getMainDescription();
		if (mainDescription != null && !lookupAliases.contains(mainDescription.getAlias())) {
			lookupAliases.add(mainDescription.getAlias());
		}
		List<Field> lookupFields = masterRecord.getLookupFields();
		for (Field field : lookupFields) {
			if (lookupAliases.contains(field.getAlias())) {
				lookupAliases.add(field.getAlias());
			}
		}

		// Source node where field controls are installed, and list of all controls.

		if (!(event.getTarget() instanceof Node)) {
			throw new IllegalStateException("Action not lauched from an FX node.");
		}
		Node node = (Node) event.getTarget();
		FormRecordPane pane = FX.getFormRecordPane(node);
		if (pane == null) {
			throw new IllegalStateException("Target not installed in a FormRecordPane.");
		}
		List<Control> controls = FX.getControls(pane.getPane());
		if (controls.isEmpty()) {
			throw new IllegalStateException("No controls found in the FormRecordPane.");
		}

		// Validate that all local key fields have a related control.

		for (Field field : localKeyFields) {
			if (FX.getFieldControl(field.getAlias(), controls) == null) {
				throw new IllegalStateException("Controls not found for field " + field);
			}
		}

		// Build the list of records if not already built.

		RecordSet workingRecordSet = null;
		if (recordSet != null) {
			// The record set has been set, filter it if applicable.
			workingRecordSet = recordSet.getRecordSet(criteria);
		} else {
			// The record set has not been set. First check if there is a persistor to retrieve the data.
			if (masterRecord.getPersistor() == null) {
				throw new IllegalStateException("A Persistor is required if the record set is not previously set.");
			}
			// Build a working criteria to retrieve the data.
			Criteria workingCriteria = new Criteria(Criteria.AND);
			// Append the optional criteria if present.
			if (criteria != null) {
				workingCriteria.add(criteria);
			}
			// Append the criteria for the local key fields. If the field is string, then a partial value is admitted
			// through a LikeLeft condition.
			for (Field field : localKeyFields) {
				FieldControl control = FX.getFieldControl(field.getAlias(), controls);
				Value value = control.getValue();
				if (field.isString()) {
					workingCriteria.add(Condition.likeLeft(field, value));
				} else {
					workingCriteria.add(Condition.fieldEQ(field, value));
				}
			}
			// The selection order. If not set, build it with the key fields.
			Order selectionOrder;
			if (order != null) {
				selectionOrder = order;
			} else {
				if (masterRecord.getPersistor().getView().getOrderBy() != null) {
					selectionOrder = masterRecord.getPersistor().getView().getOrderBy();
				} else {
					selectionOrder = masterRecord.getPrimaryOrder();
				}
			}
			// Try filling the working record set.
			try {
				workingRecordSet = masterRecord.getPersistor().select(workingCriteria, selectionOrder);
			} catch (PersistorException exc) {
				LOGGER.catching(exc);
				return;
			}
		}
		// Apply the customizer if set.
		if (recordSetCustomizer != null) {
			recordSetCustomizer.customize(workingRecordSet);
		}

		// Configure the lookup.
		LookupRecords lookup = new LookupRecords(masterRecord);
		lookup.setTitle(title);
		lookupAliases.forEach(alias -> lookup.addColumn(alias, true));
		
		// Select a record.
		Record selectedRecord = lookup.lookupRecord(getNode().getScene().getWindow());
		if (selectedRecord == null) {
			return;
		}

		// Assign key values to related controls.
		for (int i = 0; i < localKeyFields.size(); i++) {
			Field localField = localKeyFields.get(i);
			Field foreignField = foreignKeyFields.get(i);
			FieldControl control = FX.getFieldControl(localField.getAlias(), controls);
			Value value = selectedRecord.getValue(foreignField.getAlias());
			control.setValue(value);
		}
		
		// Assign values of any not foreign key field that has a control, e.g. the main description.
		for (Field field : lookupFields) {
			if (!foreignKeyFields.contains(field)) {
				FieldControl control = FX.getFieldControl(field.getAlias(), controls);
				if (control != null) {
					Value value = selectedRecord.getValue(field.getAlias());
					control.setValue(value);
				}
			}
		}
	}
}
