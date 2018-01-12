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

package com.qtfx.lib.ml.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Root of network layers.
 *
 * @author Miquel Sas
 */
public abstract class Layer {

	/** Number of input values. */
	private int inputSize = -1;
	/** Number of output values. */
	private int outputSize = -1;

	/**
	 * Constructor.
	 * 
	 * @param inputSize The input size.
	 * @param outputSize
	 */
	public Layer(int inputSize, int outputSize) {
		super();
		this.inputSize = inputSize;
		this.outputSize = outputSize;
		initialize();
	}

	/**
	 * Initialize all the internal state and calculation parameters for the given input and output sizes. If necessary,
	 * randomize where required.
	 */
	protected abstract void initialize();

	/**
	 * Forward process of the input data (input size) producing the output data.
	 * 
	 * @param inputs Input data.
	 * @return Output data.
	 */
	public abstract double[] forward(double[] inputs);

	/**
	 * Optional backward learning process of an output error producing an input error.
	 * 
	 * @param errors The output error.
	 * @return The input error.
	 */
	public abstract double[] backward(double[] errors);
	
	/**
	 * Save this layer to an output stream.
	 * 
	 * @param os The output stream.
	 */
	public abstract void save(OutputStream os) throws IOException;

	/**
	 * Save the layer from an input stream.
	 * 
	 * @param is The input stream.
	 */
	public abstract void restore(InputStream is) throws IOException;

	/**
	 * Return the input size.
	 * 
	 * @return The input size.
	 */
	public int getInputSize() {
		return inputSize;
	}

	/**
	 * Set the input size.
	 * 
	 * @param inputSize The input size.
	 */
	protected void setInputSize(int inputSize) {
		this.inputSize = inputSize;
	}

	/**
	 * Return the output size.
	 * 
	 * @return The output size.
	 */
	public int getOutputSize() {
		return outputSize;
	}

	/**
	 * Set the output size.
	 * 
	 * @param outputSize The output size.
	 */
	protected void setOutputSize(int outputSize) {
		this.outputSize = outputSize;
	}

}
