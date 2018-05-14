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

package com.qtfx.lib.ml.function;

import com.qtfx.lib.ml.function.distance.DistanceEuclidean;
import com.qtfx.lib.util.Numbers;

/**
 * Error function for an iteration of the learning process over a pattern source.
 *
 * @author Miquel Sas
 */
public class IterationError {

	/** Distance function. */
	private Distance distance;
	/** Total accumulated error. */
	private double error = 0;
	/** The counter of patterns. */
	private double patterns = 0;

	/**
	 * Constructor with a default Euclidean distance.
	 */
	public IterationError() {
		super();
		this.distance = new DistanceEuclidean();
	}

	/**
	 * Constructor assigning the distance function.
	 * 
	 * @param distance The distance function.
	 */
	public IterationError(Distance distance) {
		super();
		this.distance = distance;
	}

	/**
	 * Add the error of a pattern forward process.
	 * 
	 * @param patternOutputs The pattern or desired outputs.
	 * @param networkOutputs The network outputs.
	 */
	public void add(double[] patternOutputs, double[] networkOutputs) {
		error += distance.distance(patternOutputs, networkOutputs);
		patterns += 1;
	}

	/**
	 * Returns the total current error.
	 * 
	 * @return The error.
	 */
	public double get() {
		if (patterns == 0) {
			return Numbers.MAX_DOUBLE;
		}
		return error / patterns;
	}
	
	/**
	 * Reset the total error setting it to zero.
	 */
	public void reset() {
		error = 0;
		patterns = 0;
	}
}
