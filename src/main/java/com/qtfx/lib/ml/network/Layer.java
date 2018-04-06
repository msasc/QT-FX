package com.qtfx.lib.ml.network;

/**
 * A layer of a network. A layer makes no assumptions on how is managed internal data.
 * <p>
 * The layer is enforced to store the input and the output. The input is set via the call to <em>setInput()</em> and the
 * output is retrieved via the call to <em>getOutput()</em>.
 * <p>
 * So, a forward calculations will look like this.
 * <ul>
 * <li><em>layer.setInput(double[] input)</em></li>
 * <li><em>layer.forward()</em></li>
 * <li><em>double[] output = layer.getOutput()</em></li>
 * </ul>
 * The general contract to process an error and allow processing different lists of the same pattern in a batch mode, is
 * to pass the output error, apply and store any gradients and internal data, and the update this internal data to
 * produce the input error.
 * <ul>
 * <li><em>layer.setOutputError(double[] error)</em></li>
 * <li><em>layer.updateGradients()</em></li>
 * <li><em>double[] inputError = layer.getInputError()</em></li>
 * </ul>
 * Note that with this structure, a layer can be at the same time input and output layer.
 *
 * @author Miquel Sas
 */
public abstract class Layer {

	/** Parent network. */
	Network network;
	/** Input size. */
	int inputSize;
	/** Output size. */
	private int outputSize;

	/**
	 * Constructor for a layer, assigning the output sizes.
	 * 
	 * @param outputSize Output size.
	 */
	public Layer(int inputSize, int outputSize) {
		super();
		this.outputSize = outputSize;
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
	boolean isOutputLayer(Layer layer) {
		return network.isOutputLayer(this);
	}

	/**
	 * Check if this layer is a hidden layer.
	 * 
	 * @param layer The layer.
	 * @return A boolean.
	 */
	boolean isHiddenLayer(Layer layer) {
		return network.isHiddenLayer(this);
	}

	/**
	 * Create and initialize this layer internal data.
	 */
	public abstract void initialize();
}
