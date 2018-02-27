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
 * A pattern, with inputs and optional expected label and outputs.
 *
 * @author Miquel Sas
 */
public interface Pattern {

	/**
	 * Return the pattern inputs.
	 * 
	 * @return The pattern inputs.
	 */
	double[] getInputs();

	/**
	 * Return the optional pattern outputs.
	 * 
	 * @return The pattern outputs.
	 */
	double[] getOutputs();

	/**
	 * Return the optional label.
	 * 
	 * @return The label.
	 */
	String getLabel();
}
