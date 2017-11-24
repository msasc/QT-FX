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

package com.qtfx.lib.gui.table;

import java.lang.ref.WeakReference;

import com.qtfx.lib.db.Field;
import com.qtfx.lib.db.Record;
import com.qtfx.lib.db.Value;
import com.qtfx.lib.gui.ExpressionHelper;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

/**
 * A table cell value factory that relies on fields.
 *
 * @author Miquel Sas
 */
public class CellValueFactory implements Callback<CellDataFeatures<Record, Value>, ObservableValue<Value>> {

	/**
	 * Change and invalidation listener.
	 */
	class ValueProperty extends ObjectProperty<Value> {

		private ObservableValue<? extends Value> observable = null;
		private InvalidationListener listener = null;
		private boolean valid = true;
		private ExpressionHelper<Value> helper = null;

		/**
		 * Constructor.
		 */
		public ValueProperty() {
			super();
			this.helper = new ExpressionHelper<>(this);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void bind(ObservableValue<? extends Value> newObservable) {
			if (newObservable == null) {
				throw new NullPointerException("Cannot bind to null");
			}
			if (!newObservable.equals(observable)) {
				unbind();
				observable = newObservable;
				if (listener == null) {
					listener = new Listener(this);
				}
				observable.addListener(listener);
				markInvalid();
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void unbind() {
			if (observable != null) {
				observable.removeListener(listener);
				observable = null;
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isBound() {
			return observable != null;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Object getBean() {
			return null;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String getName() {
			return null;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void addListener(ChangeListener<? super Value> listener) {
			helper.addListener(listener);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void removeListener(ChangeListener<? super Value> listener) {
			helper.removeListener(listener);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void addListener(InvalidationListener listener) {
			helper.addListener(listener);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void removeListener(InvalidationListener listener) {
			helper.removeListener(listener);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Value get() {
			valid = true;
			Value value = record.getValue(field.getAlias());
			if (field.isPossibleValues()) {
				String label = field.getPossibleValueLabel(value);
				if (label != null) {
					value = new Value(label);
				}
			}
			return observable == null ? value : observable.getValue();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void set(Value newValue) {
			if (isBound()) {
				throw new java.lang.RuntimeException((getBean() != null && getName() != null ? getBean().getClass().getSimpleName() + "." + getName() + " : " : "") + "A bound value cannot be set.");
			}
			Value value = record.getValue(field.getAlias());
			if ((value == null) ? newValue != null : !value.equals(newValue)) {
				value = newValue;
				markInvalid();
			}
		}

		/**
		 * Mark the property as invalid and fire changed event.
		 */
		private void markInvalid() {
			if (valid) {
				valid = false;
				helper.fireValueChangedEvent();
			}
		}

		/**
		 * Invalidation listener when the value is bound.
		 */
		private class Listener implements InvalidationListener {

			private final WeakReference<ValueProperty> wref;

			public Listener(ValueProperty ref) {
				this.wref = new WeakReference<>(ref);
			}

			@Override
			public void invalidated(Observable observable) {
				ValueProperty ref = wref.get();
				if (ref == null) {
					observable.removeListener(this);
				} else {
					ref.markInvalid();
				}
			}
		}
	}

	/** The field. */
	private Field field;
	/** The value property. */
	private ValueProperty valueProperty;
	/** The call back record. */
	private transient Record record;

	/**
	 * Constructor assigning the field alias.
	 * 
	 * @param field The field.
	 * @param locale The required locale.
	 */
	public CellValueFactory(Field field) {
		super();
		this.field = field;
		this.valueProperty = new ValueProperty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ObservableValue<Value> call(CellDataFeatures<Record, Value> param) {
		this.record = param.getValue();
		return valueProperty;
	}
}
