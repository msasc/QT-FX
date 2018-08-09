package com.qtfx.lib.ml.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

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

	/**
	 * A convenient class to pack internal layer data. The data necessary data for layers is made of doubles, double
	 * vectors and double matrices. Since values are accessed by key, it is recommended for efficiency to first retrieve
	 * the values and then use them, instead of retrieving and using them in each loop.
	 *
	 * @author Miquel Sas
	 */
	public static class Data {
		/** Map of double values. */
		private HashMap<Object, Double> mapValues = new HashMap<>();
		/** Map of vectors. */
		private HashMap<Object, double[]> mapVectors = new HashMap<>();
		/** Map of matrices. */
		private HashMap<Object, double[][]> mapMatrices = new HashMap<>();

		/**
		 * Default constructor.
		 */
		public Data() {
			super();
		}

		public double getValue(Object key) {
			Double value = mapValues.get(key);
			if (value != null) {
				return value;
			}
			return 0.0;
		}

		public double[] getVector(Object key) {
			return mapVectors.get(key);
		}

		public double[][] getMatrix(Object key) {
			return mapMatrices.get(key);
		}

		public void setValue(Object key, double value) {
			mapValues.put(key, value);
		}

		public void setVector(Object key, double[] vector) {
			mapVectors.put(key, vector);
		}

		public void setMatrix(Object key, double[][] matrix) {
			mapMatrices.put(key, matrix);
		}
	}

	/** Parent network. */
	private Network network;
	/** Input size. */
	private int inputSize;
	/** Output size. */
	private int outputSize;

	/**
	 * A boolean that indicates that this layer admits processing deltas/gradients in batches and then performing a
	 * final update of the weights and internal parameters.
	 */

	/**
	 * Protected constructor.
	 */
	protected Layer() {
	}

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
	protected void setNetwork(Network network) {
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
	 * Return the network.
	 * 
	 * @return The network.
	 */
	public Network getNetwork() {
		return network;
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

	///////////////////////
	// Internal data access

	/**
	 * Return the internal data of the layer.
	 * 
	 * @return The internal data.
	 */
	public abstract Data getData();

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

	/**
	 * Clone this layer and return an exact copy.
	 * 
	 * @return A copy of this layer.
	 */
	@Override
	public abstract Layer clone();
}
