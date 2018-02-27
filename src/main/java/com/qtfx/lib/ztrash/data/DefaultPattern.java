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
package com.qtfx.lib.ztrash.data;

/**
 * Default pattern implementation..
 * 
 * @author Miquel Sas
 */
public class DefaultPattern implements Pattern {

	/** Patter inputs. */
	private double[] inputs;
	/** Pattern outputs. */
	private double[] outputs;
	/** Pattern label. */
	private String label;

	/**
	 * Default constructor.
	 */
	public DefaultPattern() {
		super();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double[] getInputs() {
		return inputs;
	}

	/**
	 * Set the pattern inputs.
	 * 
	 * @param inputs The pattern inputs.
	 */
	public void setInputs(double[] inputs) {
		this.inputs = inputs;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double[] getOutputs() {
		return outputs;
	}

	/**
	 * Set the pattern outputs.
	 * 
	 * @param outputs The pattern outputs.
	 */
	public void setOutputs(double[] outputs) {
		this.outputs = outputs;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLabel() {
		return label;
	}

	/**
	 * Set the label.
	 * 
	 * @param label The label.
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * Check if the pattern has a label.
	 * 
	 * @return A boolean.
	 */
	public boolean isLabel() {
		return getLabel() != null;
	}

	/**
	 * Return a string representation.
	 * 
	 * @return A string.
	 */
	@Override
	public String toString() {
		if (isLabel()) {
			return getLabel();
		}
		return super.toString();
	}
}
