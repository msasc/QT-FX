package com.qtfx.lib.ml.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * A layer of a network.
 * <p>
 * The layer is enforced to store the input and the output and any internal necessary data for the forward process, like
 * weights, and for the error backward process, like gradients.
 * <p>
 * Note that with this structure, a layer can be at the same time input and output layer.
 *
 * @author Miquel Sas
 */
public abstract class Layer {

	/** Parent network. */
	private Network network;
	/** Input size. */
	private int inputSize;
	/** Output size. */
	private int outputSize;

	/**
	 * Constructor for a layer, assigning the output sizes.
	 * 
	 * @param outputSize Output size.
	 */
	public Layer(int outputSize) {
		super();
		this.outputSize = outputSize;
	}

	/**
	 * Set the input size (the network sets it).
	 * 
	 * @param inputSize The input size.
	 */
	protected void setInputSize(int inputSize) {
		this.inputSize = inputSize;
	}

	/**
	 * Set the output size, mainly used in the restore method.
	 * 
	 * @param outputSize The output size.
	 */
	protected void setOutputSize(int outputSize) {
		this.outputSize = outputSize;
	}

	/**
	 * Set the parent network.
	 * 
	 * @param network The network.
	 */
	void setNetwork(Network network) {
		this.network = network;
	}

	/**
	 * Return the input size.
	 * 
	 * @return The input size.
	 */
	public int getInputSize() {
		return inputSize;
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
	 * Check if this layer is the input layer.
	 * 
	 * @return A boolean.
	 */
	public boolean isInputLayer() {
		return network.isInputLayer(this);
	}

	/**
	 * Check if this layer is the output layer.
	 * 
	 * @return A boolean.
	 */
	public boolean isOutputLayer(Layer layer) {
		return network.isOutputLayer(this);
	}

	/**
	 * Check if this layer is a hidden layer.
	 * 
	 * @param layer The layer.
	 * @return A boolean.
	 */
	public boolean isHiddenLayer(Layer layer) {
		return network.isHiddenLayer(this);
	}

	/**
	 * Create and initialize this layer internal data.
	 */
	public abstract void initialize();

	/////////////////////
	// Forward processing

	/**
	 * Forward process of the input data (input size) producing the output data.
	 * 
	 * @param inputs Input data.
	 * @return Output data.
	 */
	public abstract double[] forward(double[] inputs);

	//////////////////////
	// Backward processing

	/**
	 * Optional backward learning process of an output error producing an input error.
	 * 
	 * @param errors The output error.
	 * @return The input error.
	 */
	public abstract double[] backward(double[] errors);

	///////////////
	// Save/restore

	/**
	 * Save all the layer internal data to an output stream.
	 * 
	 * @param os The output stream.
	 * @throws IOException
	 */
	public abstract void save(OutputStream os) throws IOException;

	/**
	 * Restore all the layer internal data from an input stream.
	 * 
	 * @param is The input stream.
	 * @throws IOException
	 */
	public abstract void restore(InputStream is) throws IOException;
}
