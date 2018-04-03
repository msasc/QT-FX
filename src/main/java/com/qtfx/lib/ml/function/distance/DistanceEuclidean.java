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

package com.qtfx.lib.ml.function.distance;

import com.qtfx.lib.math.Vector;
import com.qtfx.lib.ml.function.Distance;

/**
 * Weighted Euclidean distance.
 *
 * @author Miquel Sas
 */
public class DistanceEuclidean implements Distance {

	/**
	 * The weights used in weighted distance.
	 */
	private double[] weights = null;

	/**
	 * Constructor without weights.
	 */
	public DistanceEuclidean() {
		super();
	}

	/**
	 * Constructor assigning the weights.
	 * 
	 * @param weights The weights.
	 */
	public DistanceEuclidean(double[] weights) {
		super();
		this.weights = weights;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double distance(double[] x, double[] y) {
		
		// Initialize weights if not done.
		if (weights == null) {
			weights = new double[x.length];
			for (int i = 0; i < weights.length; i++) {
				weights[i] = 1.0;
			}
		}
		
		return Vector.distanceEuclidean(x, y, weights);
	}

}
