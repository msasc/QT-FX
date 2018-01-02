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

package com.qtfx.lib.ml;

/**
 * An activation function, ReLU, Sigmoid, TANH...
 *
 * @author Miquel Sas
 */
public interface Activation {

	/**
	 * Returns the output value of the function given the signal (trigger) value.
	 * 
	 * @param trigger The signal, sometimes a weighted sum plus a bias.
	 * @return The output value.
	 */
	double getOutput(double signal);

	/**
	 * Returns the first derivative of the function, given the signals and the outputs. Some activations require the
	 * output and some the signal to calculate the derivative.
	 * 
	 * @param signal The signal applied to {@link com.qtfx.lib.ml.Activation#getOutput(double)}.
	 * @param output The output obtained applying the signal to {@link com.qtfx.lib.ml.Activation#getOutput(double)}.
	 * @return The first derivative.
	 */
	double getDerivative(double signal, double output);
}
