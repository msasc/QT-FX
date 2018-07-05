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

package com.qtfx.lib.math;

import java.util.List;

import com.qtfx.lib.util.Numbers;

/**
 * Operations on vectors.
 *
 * @author Miquel Sas
 */
public class Vector {

	///////////////////////////////////
	// Functions in alphabetical order.

	/**
	 * Add the values of vectors a and b (must have the same length).
	 * 
	 * @param x Vector x.
	 * @param y Vector y.
	 * @return The result of adding the values.
	 */
	public static double[] add(double[] x, double[] y) {
		int length = x.length;
		double[] r = new double[length];
		for (int i = 0; i < length; i++) {
			r[i] = x[i] + y[i];
		}
		return r;
	}

	/**
	 * Check if two vectors are equal rounding the values at the argument precision.
	 * 
	 * @param a Vector a
	 * @param b Vector b
	 * @param precision Rounding precision.
	 * @return A boolean
	 */
	public static boolean areEqual(double[] a, double[] b, int precision) {
		if (a.length != b.length) {
			return false;
		}
		for (int i = 0; i < a.length; i++) {
			double value_a = Numbers.round(a[i], precision);
			double value_b = Numbers.round(b[i], precision);
			if (value_a != value_b) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns the centroid of a list of vectors.
	 * 
	 * @param vectors The list of vectors.
	 * @return The centroid.
	 */
	public static double[] centroid(List<double[]> vectors) {
		int size = vectors.get(0).length;
		double divisor = vectors.size();
		double[] centroid = new double[size];
		for (double[] vector : vectors) {
			for (int i = 0; i < size; i++) {
				centroid[i] += (vector[i] / divisor);
			}
		}
		return centroid;
	}

	/**
	 * Return a copy of the source vector.
	 * 
	 * @param src The source vector.
	 * @return The copy.
	 */
	public static double[] copy(double[] src) {
		double[] dst = new double[src.length];
		copy(src, dst);
		return dst;
	}

	/**
	 * Copy the source array into the destination array. Both must have the same length.
	 * 
	 * @param src The source array.
	 * @param dst The destination array.
	 */
	public static void copy(double[] src, double[] dst) {
		for (int i = 0; i < src.length; i++) {
			dst[i] = src[i];
		}
	}

	/**
	 * Returns the correlation coefficient between two vectors.
	 * 
	 * @param x Vector x.
	 * @param y Vector y.
	 */
	public static double correlation(double[] x, double[] y) {
		checkLengths(x, y);
		checkMinLength(x, 3);

		double xy = covariance(x, y);
		double xx = variance(x);
		double yy = variance(y);

		if (xx == 0 || yy == 0) {
			return Double.NaN;
		}

		return xy / Math.sqrt(xx * yy);
	}

	/**
	 * Returns the covariance between two vectors.
	 * 
	 * @param x Vector x.
	 * @param y Vector y.
	 */
	public static double covariance(double[] x, double[] y) {
		checkLengths(x, y);
		checkMinLength(x, 3);

		double mx = mean(x);
		double my = mean(y);

		double sxy = 0.0;
		for (int i = 0; i < x.length; i++) {
			double dx = x[i] - mx;
			double dy = y[i] - my;
			sxy += dx * dy;
		}

		return sxy / (x.length - 1);
	}

	/**
	 * Correlation distance is defined as 1 - correlation coefficient.
	 * 
	 * @param x Vector x.
	 * @param y Vector y.
	 * @return The distance.
	 */
	public static double distanceCorrelation(double[] x, double[] y) {
		return 1.0 - correlation(x, y);
	}

	/**
	 * Cosine distance is defined as 1 - cosine similarity.
	 * 
	 * @param x Vector x.
	 * @param y Vector y.
	 * @return The cosine distance.
	 */
	public static double distanceCosine(double[] x, double[] y) {
		double similarity = similarityCosine(x, y);
		return 1.0 - similarity;
	}

	/**
	 * Weighted Euclidean distance.
	 * 
	 * @param x Vector x.
	 * @param y Vector y.
	 * @return The distance.
	 */
	public static double distanceEuclidean(double[] x, double[] y) {

		// Check vector lengths
		Vector.checkLengths(x, y);

		// Calculate weighted distance.
		double distance = 0.0;
		for (int i = 0; i < x.length; i++) {
			double d = x[i] - y[i];
			distance += (d * d);
		}
		distance = Math.sqrt(distance);

		return distance;
	}

	/**
	 * The distance Jensen-Shannon is the square root of its divergence.
	 * 
	 * @param x Vector x.
	 * @param y Vector y.
	 * @return The distance.
	 */
	public static double distanceJensenShannon(double[] x, double[] y) {
		return Math.sqrt(divergenceJensenShannon(x, y));
	}

	/**
	 * Jensen-Shannon divergence JS(P||Q) = (KL(P||M) + KL(Q||M)) / 2, where M = (P+Q)/2. The Jensen-Shannon divergence
	 * is a popular method of measuring the similarity between two probability distributions. It is also known as
	 * information radius or total divergence to the average. It is based on the Kullback-Leibler divergence, with the
	 * difference that it is always a finite value. The square root of the Jensen-Shannon divergence is a metric.
	 */
	public static double divergenceJensenShannon(double[] x, double[] y) {
		checkLengths(x, y);
		double[] m = new double[x.length];
		for (int i = 0; i < m.length; i++) {
			m[i] = (x[i] + y[i]) / 2;
		}
		return (divergenceKullbackLeibler(x, m) + divergenceKullbackLeibler(y, m)) / 2;
	}

	/**
	 * Kullback-Leibler divergence. The Kullback-Leibler divergence (also information divergence, information gain,
	 * relative entropy, or KLIC) is a non-symmetric measure of the difference between two probability distributions P
	 * and Q. KL measures the expected number of extra bits required to code samples from P when using a code based on
	 * Q, rather than using a code based on P. Typically P represents the "true" distribution of data, observations, or
	 * a precise calculated theoretical distribution. The measure Q typically represents a theory, model, description,
	 * or approximation of P.
	 * <p>
	 * Although it is often intuited as a distance metric, the KL divergence is not a true metric - for example, the KL
	 * from P to Q is not necessarily the same as the KL from Q to P.
	 */
	public static double divergenceKullbackLeibler(double[] x, double[] y) {
		checkLengths(x, y);
		boolean intersection = false;
		double kl = 0.0;
		for (int i = 0; i < x.length; i++) {
			if (x[i] != 0.0 && y[i] != 0.0) {
				intersection = true;
				kl += x[i] * Math.log(x[i] / y[i]);
			}
		}
		if (intersection) {
			return kl;
		} else {
			return Double.POSITIVE_INFINITY;
		}
	}

	/**
	 * Returns the mean of a vector.
	 * 
	 * @param x The vector.
	 */
	public static double mean(double[] x) {
		if (x.length == 0) {
			return 0;
		}
		return sum(x) / x.length;
	}

	/**
	 * Returns the cosine similarity between two vectors.
	 * 
	 * @param x Vector x.
	 * @param y Vector y.
	 * @return The cosine similarity.
	 */
	public static double similarityCosine(double[] x, double[] y) {
		double ab = 0;
		double a2 = 0;
		double b2 = 0;
		for (int i = 0; i < x.length; i++) {
			ab += (x[i] * y[i]);
			a2 += (x[i] * x[i]);
			b2 += (y[i] * y[i]);
		}
		if (a2 == 0 && b2 == 0) {
			return 1;
		}
		if (a2 == 0 || b2 == 0) {
			return -1;
		}
		double sqrt = Math.sqrt(a2 * b2);
		return ab / sqrt;
	}

	/**
	 * Returns the sum of a vector.
	 * 
	 * @param x The vector.
	 */
	public static double sum(double[] x) {
		double sum = 0.0;
		for (double n : x) {
			sum += n;
		}
		return sum;
	}

	/**
	 * Subtract the values of vector b from vector a (must have the same length).
	 * 
	 * @param x Vector x.
	 * @param y Vector y.
	 * @return The result of subtracting the values.
	 */
	public static double[] subtract(double[] x, double[] y) {
		double[] r = new double[x.length];
		for (int i = 0; i < x.length; i++) {
			r[i] = x[i] - y[i];
		}
		return r;
	}

	/**
	 * Returns the variance of a vector.
	 *
	 * @param x The vector.
	 */
	public static double variance(double[] x) {
		checkMinLength(x, 2);

		double sum = 0.0;
		double sumsq = 0.0;
		for (double xi : x) {
			sum += xi;
			sumsq += xi * xi;
		}

		int n = x.length - 1;
		return sumsq / n - (sum / x.length) * (sum / n);
	}

	//////////////////////////
	// Validations and checks.

	/**
	 * Check that two vectors have the same length. Throws an IllegalArgumentException.
	 * 
	 * @param x Vector x.
	 * @param y Vector y.
	 */
	public static void checkLengths(double[] x, double[] y) {
		if (x.length != y.length) {
			throw new IllegalArgumentException("Vectors have different length.");
		}
	}

	/**
	 * Check that a vector has a minimum length. Throws an IllegalArgumentException.
	 * 
	 * @param x Vector x.
	 * @param minLength Minimum length.
	 */
	public static void checkMinLength(double[] x, int minLength) {
		if (x.length < minLength) {
			throw new IllegalArgumentException("Vector length has to be at least " + minLength);
		}
	}
}
