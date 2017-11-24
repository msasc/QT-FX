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

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 * Infrastructure support for listeners.
 *
 * @author Miquel Sas
 */
public class ExpressionHelper<T> {

	/** Observable value. */
	private ObservableValue<T> observable;
	/** List of change listeners. */
	private List<ChangeListener<? super T>> changeListeners = new ArrayList<>();
	/** List of invalidation listeners. */
	private List<InvalidationListener> invalidationListeners = new ArrayList<>();
	/** Current value. */
	private T currentValue;
	
	/**
	 * Constructor.
	 * 
	 * @param observable The observable.
	 */
	public ExpressionHelper(ObservableValue<T> observable) {
		super();
		this.observable = observable;
	}

	/**
	 * Add an invalidation listener.
	 * 
	 * @param listener The listener.
	 */
	public void addListener(InvalidationListener listener) {
		invalidationListeners.add(listener);
	}

	/**
	 * Remove the invalidation listener.
	 * 
	 * @param listener The listener to remove.
	 */
	public void removeListener(InvalidationListener listener) {
		invalidationListeners.remove(listener);
	}
	/**
	 * Add a change listener.
	 * 
	 * @param listener The listener.
	 */
	public void addListener(ChangeListener<? super T> listener) {
		changeListeners.add(listener);
	}

	/**
	 * Remove the change listener.
	 * 
	 * @param listener The listener to remove.
	 */
	public void removeListener(ChangeListener<? super T> listener) {
		changeListeners.remove(listener);
	}
	
	/**
	 * Fire a value changed event.
	 */
	public void fireValueChangedEvent() {
		for (InvalidationListener listener : invalidationListeners) {
			try {
				listener.invalidated(observable);
			} catch (Exception e) {
				Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
			}
		}
		if (!changeListeners.isEmpty()) {
			T oldValue = currentValue;
			currentValue = observable.getValue();
			boolean changed = (currentValue == null) ? (oldValue != null) : !currentValue.equals(oldValue);
			if (changed) {
				for (ChangeListener<? super T> listener : changeListeners) {
					try {
						listener.changed(observable, oldValue, currentValue);
					} catch (Exception e) {
						Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
					}
				}
			}
		}
	}
}
