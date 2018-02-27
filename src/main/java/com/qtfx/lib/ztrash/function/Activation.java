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

package com.qtfx.lib.ztrash.function;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Activation functions interface.
 *
 * @author Miquel Sas
 */
public interface Activation {
	
	/**
	 * Returns the output values of the function given the trigger values.
	 * 
	 * @param triggers The trigger (weighted sum plus bias) values.
	 * @param outputs The outputs to set.
	 * @return The output values.
	 */
	void activations(double[] triggers, double[] outputs);

	/**
	 * Returns the first derivatives of the function, given the triggers and the outputs. Some activations require the
	 * output and some the trigger.
	 * 
	 * @param triggers The triggers applied to <i>activations</i>.
	 * @param outputs The outputs obtained applying the triggers to <i>activations</i>.
	 * @param derivatives The derivatives to set.
	 * @return The first derivatives.
	 */
	void derivatives(double[] triggers, double[] outputs, double[] derivatives);
}
