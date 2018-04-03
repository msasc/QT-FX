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
 * The Jensen-Shannon divergence is a popular method of measuring the similarity between two probability distributions.
 * It is also known as information radius or total divergence to the average.
 * <p>
 * The Jensen-Shannon divergence is a symmetrized and smoothed version of the Kullback-Leibler divergence . It is
 * defined by
 * <p>
 * J(P||Q) = (D(P||M) + D(Q||M)) / 2
 * <p>
 * where M = (P+Q)/2 and D(&middot;||&middot;) is KL divergence. Different from the Kullback-Leibler divergence, it is
 * always a finite value.
 * <p>
 * The square root of the Jensen-Shannon divergence is this distance metric.
 *
 * @author Miquel Sas
 */
public class DistanceJensenShannon implements Distance {

	/**
	 * Constructor.
	 */
	public DistanceJensenShannon() {
		super();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double distance(double[] x, double[] y) {
		return Vector.distanceJensenShannon(x, y);
	}

}
