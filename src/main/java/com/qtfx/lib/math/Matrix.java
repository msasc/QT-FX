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

package com.qtfx.lib.math;

/**
 * Operations on matrices.
 *
 * @author Miquel Sas
 */
public class Matrix {

	/**
	 * Return the cumulative average.
	 * 
	 * @param a The source vector.
	 * @return The cumulative average.
	 */
	public static double[] avgCumulative(double[] a) {
		double avg = 0;
		double[] c = new double[a.length];
		for (int i = 0; i < a.length; i++) {
			avg += a[i];
			c[i] = avg / (i + 1);
		}
		return c;
	}

	/**
	 * Returns the number of columns of a matrix.
	 * 
	 * @param matrix The argument matrix.
	 * @return The number of columns.
	 */
	public static int columns(double[][] matrix) {
		if (rows(matrix) != 0) {
			return matrix[0].length;
		}
		return 0;
	}

	/**
	 * Cumulate the source array into the destination. Both must have the same dimensions.
	 * 
	 * @param src The source.
	 * @param dst The destination.
	 */
	public static void cumulate(double[][] src, double[][] dst) {
		int rows = rows(src);
		int cols = columns(src);
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				dst[row][col] += src[row][col];
			}
		}
	}

	/**
	 * Calculate the Hadamard product of matrices a and b.
	 * 
	 * @param a Matrix a.
	 * @param b Matrix b.
	 */
	public static double[][] hadamard(double[][] a, double[][] b) {
		int rows = rows(a);
		int cols = columns(a);
		double[][] h = new double[rows][cols];
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				h[r][c] = a[r][c] * b[r][c];
			}
		}
		return h;
	}

	/**
	 * Returns the number of rows of a matrix.
	 * 
	 * @param matrix The argument matrix.
	 * @return The number of rows.
	 */
	public static int rows(double[][] matrix) {
		return matrix.length;
	}

	/**
	 * Set the matrix with a scalar value.
	 * 
	 * @param matrix The matrix to initialize.
	 * @param value The value to assign.
	 */
	public static void set(double[][] matrix, double value) {
		int rows = rows(matrix);
		int columns = columns(matrix);
		for (int row = 0; row < rows; row++) {
			for (int column = 0; column < columns; column++) {
				matrix[row][column] = value;
			}
		}
	}

}
