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

package com.qtfx.lib.mkt.data;

/**
 * Base class for data plotters of timed data.
 * 
 * @author Miquel Sas
 */
public abstract class DataPlotter {
	
	/** Indexes within the data element used by the plotter. */
	private int[] indexes;
	
	/**
	 * Returns the indexes to apply to the data item. By default, all data values.
	 * 
	 * @param data The data item.
	 * @return The indexes.
	 */
	public int[] getIndexes(Data data) {
		if (indexes == null) {
			indexes = new int[data.size()];
			for (int i = 0; i < data.size(); i++) {
				indexes[i] = i;
			}
		}
		return indexes;
	}
	
	/**
	 * Returns the list of values given the data element.
	 * 
	 * @param data The data item.
	 * @return The list of values.
	 */
	public double[] getValues(Data data) {
		int[] indexes = getIndexes(data);
		double[] values = new double[indexes.length];
		for (int i = 0; i < indexes.length; i++) {
			values[i] = data.getValue(indexes[i]);
		}
		return values;
	}
}
