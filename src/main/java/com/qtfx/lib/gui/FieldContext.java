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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.qtfx.lib.db.Field;
import com.qtfx.lib.db.Record;
import com.qtfx.lib.gui.action.ActionLookup;
import com.qtfx.lib.gui.action.ActionSearchAndRefresh;
import com.qtfx.lib.gui.action.ValueAction;
import com.qtfx.lib.gui.controls.CheckBoxField;
import com.qtfx.lib.gui.controls.ChoiceBoxField;
import com.qtfx.lib.gui.controls.ComboBoxField;
import com.qtfx.lib.gui.controls.DatePickerField;
import com.qtfx.lib.gui.controls.LookupComboBoxField;
import com.qtfx.lib.gui.controls.NumberTextField;
import com.qtfx.lib.gui.controls.PasswordTextField;
import com.qtfx.lib.gui.controls.StringTextField;

import javafx.geometry.Pos;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * Field edit context, normally in a {@link com.qtfx.lib.gui.FormRecordPane}.
 *
 * @author Miquel Sas
 */
public class FieldContext {

	/** The locale to use. */
	private Locale locale;
	/** The edit record. */
	private Record record;
	/** Master field. */
	private Field field;
	/** Optional list of value actions. */
	private List<ValueAction> valueActions = new ArrayList<>();
	/** Optional action lookup. */
	private ActionLookup actionLookup;
	/** Optional action search and refresh, that is a value action. */
	private ActionSearchAndRefresh actionSearchAndRefresh;
	/** Optional refresh field. */
	private Field refreshField;

	/**
	 * Constructor.
	 * 
	 * @param locale The locale to use.
	 */
	public FieldContext(Locale locale) {
		super();
		this.locale = locale;
	}

	/**
	 * Return the record.
	 * 
	 * @return The record.
	 */
	public Record getRecord() {
		return record;
	}

	/**
	 * Set the record.
	 * 
	 * @param record The record.
	 */
	public void setRecord(Record record) {
		this.record = record;
	}

	/**
	 * Return the field.
	 * 
	 * @return The field.
	 */
	public Field getField() {
		return field;
	}

	/**
	 * Set the field.
	 * 
	 * @param field The field.
	 */
	public void setField(Field field) {
		this.field = field;
	}

	/**
	 * Return the action lookup.
	 * 
	 * @return The action lookup.
	 */
	public ActionLookup getActionLookup() {
		return actionLookup;
	}

	/**
	 * Set the action lookup.
	 * 
	 * @param actionLookup The action lookup.
	 */
	public void setActionLookup(ActionLookup actionLookup) {
		this.actionLookup = actionLookup;
	}

	/**
	 * Return the search and refresh action.
	 * 
	 * @return The search and refresh action.
	 */
	public ActionSearchAndRefresh getActionSearchAndRefresh() {
		return actionSearchAndRefresh;
	}

	/**
	 * Set the search and refresh action.
	 * 
	 * @param actionSearchAndRefresh The search and refresh action.
	 */
	public void setActionSearchAndRefresh(ActionSearchAndRefresh actionSearchAndRefresh) {
		this.actionSearchAndRefresh = actionSearchAndRefresh;
	}

	/**
	 * Return the refresh field.
	 * 
	 * @return The refresh field.
	 */
	public Field getRefreshField() {
		return refreshField;
	}

	/**
	 * Set the refresh field.
	 * 
	 * @param refreshField The refresh field.
	 */
	public void setRefreshField(Field refreshField) {
		this.refreshField = refreshField;
	}

	/**
	 * Add a value action.
	 * 
	 * @param valueAction The value action.
	 */
	public void addValueAction(ValueAction valueAction) {
		valueActions.add(valueAction);
	}

	/**
	 * Check if the context has a refresh field.
	 * 
	 * @return A boolean.
	 */
	public boolean isRefresh() {
		return refreshField != null;
	}

	/**
	 * Check if the main field should span in case applicable.
	 * 
	 * @return A boolean.
	 */
	public boolean isSpan() {
		if (field.isFixedWidth()) {
			return false;
		}
		if (isRefresh()) {
			return false;
		}
		return true;
	}

	/**
	 * Return the field label.
	 * 
	 * @return The label.
	 */
	public Label getFieldLabel() {
		Label label = new Label(field.getDisplayLabel());
		return label;
	}

	/**
	 * Return a suitable field control.
	 * 
	 * @return A suitable field control.
	 */
	private FieldControl getFieldControl() {
		FieldControl fc = null;
		if (field.isPassword()) {
			fc = getPasswordTextField();
		} else if (field.isBoolean()) {
			if (field.isEditBooleanInCheckBox()) {
				fc = getCheckBoxField();
			} else {
				fc = getChoiceBoxField();
			}
		} else if (field.isDate()) {
			fc = getDatePickerField();
		} else if (field.isPossibleValues()) {
			fc = getComboBoxField();
		} else if (actionLookup != null) {
			fc = getLookupComboBoxField();
		} else if (field.isNumber()) {
			fc = getNumberTextField();
		} else if (field.isString()) {
			fc = getStringTextField();
		}
		return fc;
	}

	/**
	 * Return the control for the main field.
	 * 
	 * @return The control.
	 */
	public Control getControl() {
		return getFieldControl().getControl();
	}

	/**
	 * Return a suitable PasswordTextField.
	 * 
	 * @return The PasswordTextField.
	 */
	private PasswordTextField getPasswordTextField() {
		PasswordTextField fc = new PasswordTextField(field);
		return fc;
	}

	/**
	 * Return a suitable CheckBoxField.
	 * 
	 * @return The CheckBoxField.
	 */
	private CheckBoxField getCheckBoxField() {
		CheckBoxField fc = new CheckBoxField(field);
		return fc;
	}

	/**
	 * Return a suitable ChoiceBoxField.
	 * 
	 * @return The ChoiceBoxField.
	 */
	private ChoiceBoxField getChoiceBoxField() {
		ChoiceBoxField fc = new ChoiceBoxField(field, locale);
		return fc;
	}

	/**
	 * Return a suitable DatePickerField.
	 * 
	 * @return The DatePickerField.
	 */
	private DatePickerField getDatePickerField() {
		DatePickerField fc = new DatePickerField(field);
		return fc;
	}

	/**
	 * Return a suitable ComboBoxField.
	 * 
	 * @return The ComboBoxField.
	 */
	private ComboBoxField getComboBoxField() {
		ComboBoxField fc = new ComboBoxField(field);
		return fc;
	}

	/**
	 * Return a suitable LookupComboBoxField.
	 * 
	 * @return The LookupComboBoxField.
	 */
	private LookupComboBoxField getLookupComboBoxField() {
		LookupComboBoxField fc = new LookupComboBoxField(field, actionLookup);
		return fc;
	}

	/**
	 * Return a suitable NumberTextField.
	 * 
	 * @return The NumberTextField.
	 */
	private NumberTextField getNumberTextField() {
		NumberTextField fc = new NumberTextField(field, locale);
		TextField textField = fc.getTextField();
		textField.setAlignment(Pos.CENTER_RIGHT);
		if (field.isDecimal()) {
			int length = field.getLength();
			int decimals = field.getDecimals();
			int lengthIntegerPart = length - (decimals != 0 ? decimals + 1 : 0);
			int thousandSeparators = lengthIntegerPart / 3;
			int displayLength = lengthIntegerPart + thousandSeparators + (decimals != 0 ? decimals + 1 : 0);
			double avgWidth = FX.getAverageDigitWidth(textField.getFont());
			double width = avgWidth * displayLength * 1.3;
			textField.setPrefWidth(width);
			textField.setMaxWidth(width);
		}
		return fc;
	}

	/**
	 * Return a suitable StringTextField.
	 * 
	 * @return The StringTextField.
	 */
	private StringTextField getStringTextField() {
		StringTextField fc = new StringTextField(field);
		TextField textField = fc.getTextField();
		double avgWidth = FX.getAverageLetterWidth(textField.getFont());
		// Set a width up to a maximum of 80 chars.
		double width = avgWidth * 1.3 * Math.min(field.getDisplayLength(), 80);
		textField.setPrefWidth(width);
		if (getField().isFixedWidth()) {
			textField.setMaxWidth(width);
		}
		return fc;
	}

	/**
	 * Return the control for the refresh field.
	 * 
	 * @return The control for the refresh field.
	 */
	public Control getRefreshControl() {
		if (!isRefresh()) {
			throw new IllegalStateException("Not a refresh context");
		}
		StringTextField fc = new StringTextField(field);
		TextField textField = fc.getTextField();
		double avgWidth = FX.getAverageLetterWidth(textField.getFont());
		// Set a width up to a maximum of 80 chars.
		double width = avgWidth * 1.3 * Math.min(field.getDisplayLength(), 80);
		textField.setPrefWidth(width);
		textField.setStyle("-fx-font-style: italic; -fx-text-fill: gray;");
		textField.setFocusTraversable(false);
		return textField;
	}
}
