package com.qtfx.lib.gui.action;

import com.qtfx.lib.db.Value;
import com.qtfx.lib.gui.action.handlers.ActionEventHandler;

import javafx.scene.Node;

/**
 * An action launched when the value of the control changes. Simply, a value change listener with a reference to the
 * field control.
 *
 * @author Miquel Sas
 */
public abstract class ValueAction extends ActionEventHandler {

	/** Old value. */
	private Value oldValue;
	/** New value. */
	private Value newValue;

	/**
	 * Constructor.
	 * 
	 * @param node The reference node.
	 */
	public ValueAction(Node node) {
		super(node);
	}

	/**
	 * Return the old value.
	 * 
	 * @return The old value.
	 */
	public Value getOldValue() {
		return oldValue;
	}

	/**
	 * Set the old value.
	 * 
	 * @param oldValue The old value.
	 */
	public void setOldValue(Value oldValue) {
		this.oldValue = oldValue;
	}

	/**
	 * Return the new value.
	 * 
	 * @return The new value.
	 */
	public Value getNewValue() {
		return newValue;
	}

	/**
	 * Set the new value.
	 * 
	 * @param newValue The new value.
	 */
	public void setNewValue(Value newValue) {
		this.newValue = newValue;
	}

}
