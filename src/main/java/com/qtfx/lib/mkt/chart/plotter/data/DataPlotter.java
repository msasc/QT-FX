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

package com.qtfx.lib.mkt.chart.plotter.data;

import com.qtfx.lib.mkt.chart.plotter.Plotter;
import com.qtfx.lib.mkt.data.Data;
import com.qtfx.lib.mkt.data.DataList;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Base class for data plotters of timed data.
 * 
 * @author Miquel Sas
 */
public abstract class DataPlotter extends Plotter {

	/** Indexes within the data element used by the plotter. */
	private int[] indexes;
	/** Color bearish even. */
	private Color colorBearishEven = Color.color(0.50, 0.06, 0.06);
	/** Color bearish odd. */
	private Color colorBearishOdd = Color.color(0.50, 0.06, 0.06);
	/** Color bullish even. */
	private Color colorBullishEven = Color.color(0.06, 0.38, 0.38);
	/** Color bullish odd. */
	private Color colorBullishOdd = Color.color(0.06, 0.38, 0.38);
	/**
	 * A boolean to control if the plotter should plot, thus allowing to hide/show plot actions.
	 */
	private boolean plot = true;

	/**
	 * Constructor.
	 */
	public DataPlotter() {
	}

	///////////////////
	// Data management.

	/**
	 * Check if the plotter should plot.
	 * 
	 * @return A boolean.
	 */
	public boolean isPlot() {
		return plot;
	}

	/**
	 * Set if the plotter should plot.
	 * 
	 * @param plot A boolean.
	 */
	public void setPlot(boolean plot) {
		this.plot = plot;
	}

	/**
	 * Returns the indexes within the data element used by the plotter.
	 * 
	 * @return The indexes within the data element used by the plotter.
	 */
	public int[] getIndexes() {
		return indexes;
	}

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

	/**
	 * Set the indexes within the data element used by the plotter.
	 * 
	 * @param indexes The indexes within the data element used by the plotter.
	 */
	public void setIndexes(int[] indexes) {
		this.indexes = indexes;
	}

	/**
	 * Return the index (the first).
	 * 
	 * @return The first index.
	 */
	public int getIndex() {
		return indexes[0];
	}

	/**
	 * Set an unique index.
	 * 
	 * @param index The index.
	 */
	public void setIndex(int index) {
		setIndexes(new int[] { index });
	}

	////////////////////
	// Color management.

	/**
	 * Sets the color used for a bearish line/bar/candle is an even period.
	 * 
	 * @param colorBearishEven The color used for a bearish line bar candle is an even period.
	 */
	public void setColorBearishEven(Color colorBearishEven) {
		this.colorBearishEven = colorBearishEven;
	}

	/**
	 * Sets the color used for a bearish line/bar/candle is an odd period.
	 * 
	 * @param colorBearishOdd The color used for a bearish line bar candle is an odd period.
	 */
	public void setColorBearishOdd(Color colorBearishOdd) {
		this.colorBearishOdd = colorBearishOdd;
	}

	/**
	 * Sets the color used for a bullish line/bar/candle in an even period.
	 * 
	 * @param colorBullishEven The color used for a bullish line/bar/candle in an even period.
	 */
	public void setColorBullishEven(Color colorBullishEven) {
		this.colorBullishEven = colorBullishEven;
	}

	/**
	 * Sets the color used for a bullish line/bar/candle in an odd period.
	 * 
	 * @param colorBullishOdd The color used for a bullish line/bar/candle in an odd period.
	 */
	public void setColorBullishOdd(Color colorBullishOdd) {
		this.colorBullishOdd = colorBullishOdd;
	}

	/**
	 * Returns the color used for a bearish line/bar/candle is an even period.
	 * 
	 * @return the colorBearishEven The color used for a bearish line bar candle is an even period.
	 */
	public Color getColorBearishEven() {
		return colorBearishEven;
	}

	/**
	 * Returns the color used for a bearish line/bar/candle is an odd period.
	 * 
	 * @return the colorBearishOdd The color used for a bearish line bar candle is an odd period.
	 */
	public Color getColorBearishOdd() {
		return colorBearishOdd;
	}

	/**
	 * Returns the color used for a bullish line/bar/candle in an even period.
	 * 
	 * @return the colorBullishEven The color used for a bullish line/bar/candle in an even period.
	 */
	public Color getColorBullishEven() {
		return colorBullishEven;
	}

	/**
	 * Returns the color used for a bullish line/bar/candle in an odd period.
	 * 
	 * @return the colorBullishOdd The color used for a bullish line/bar/candle in an odd period.
	 */
	public Color getColorBullishOdd() {
		return colorBullishOdd;
	}

	//////////////////
	// Main data plot.
	
	/**
	 * Do plot.
	 * @param gc The graphics context.
	 * @param dataList The data list.
	 * @param startIndex The start index.
	 * @param endIndex The end index.
	 */
	public abstract void plot(GraphicsContext gc, DataList dataList, int startIndex, int endIndex);
}
