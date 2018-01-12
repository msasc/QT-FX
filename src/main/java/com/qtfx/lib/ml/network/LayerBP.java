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
import java.util.Random;

import com.qtfx.lib.ml.function.Activation;
import com.qtfx.lib.util.Matrix;

/**
 * Back propagation layer without momentum.
 *
 * @author Miquel Sas
 */
public class LayerBP extends Layer {

	//////////////////////
	// Forward components.

	/** Layer activation. */
	private Activation activation;
	/** Layer bias. */
	private double bias = 1.0;
	/** Inputs. */
	private double[] inputs;
	/** Output. */
	private double[] outputs;
	/** Triggers. Signals plus bias. */
	private double[] triggers;
	/** Weights (in, out). */
	private double[][] weights;

	///////////////////////
	// Backward components.

	/** Flat spot to avoid near zero derivatives in the back propagation process. */
	private double flatSpot = 0.01;
	/** Output deltas, result of applying derivatives to the error. */
	private double[] outputDeltas;
	/** Weighted input deltas, not applying any derivative. */
	private double[] inputDeltas;
	/** Learning rate. */
	private double learningRate = 0.01;

	/**
	 * Constructor.
	 * 
	 * @param inputSize Input size.
	 * @param outputSize Output size.
	 * @param activation The activation function.
	 */
	public LayerBP(int inputSize, int outputSize, Activation activation) {
		super(inputSize, outputSize);
		this.activation = activation;
	}

	/**
	 * Return the learning rate.
	 * 
	 * @return The learning rate.
	 */
	public double getLearningRate() {
		return learningRate;
	}

	/**
	 * Set the learning rate.
	 * 
	 * @param learningRate The learning rate.
	 */
	public void setLearningRate(double learningRate) {
		this.learningRate = learningRate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void initialize() {

		// Input and output sizes.
		int inputSize = getInputSize();
		int outputSize = getOutputSize();

		// Forward components.
		// Outputs, signals and triggers vector.
		// Weights: rows = inputSize, columns = outputSize. Randomize.
		inputs = new double[inputSize];
		outputs = new double[outputSize];
		triggers = new double[outputSize];
		weights = new double[inputSize][outputSize];
		Random random = new Random();
		for (int in = 0; in < inputSize; in++) {
			for (int out = 0; out < outputSize; out++) {
				weights[in][out] = random.nextDouble();
			}
		}

		// Backward components.
		inputDeltas = new double[inputSize];
		outputDeltas = new double[outputSize];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double[] forward(double[] inputs) {
		Matrix.copy(inputs, this.inputs);
		int inputSize = getInputSize();
		int outputSize = getOutputSize();
		for (int out = 0; out < outputSize; out++) {
			double signal = 0;
			for (int in = 0; in < inputSize; in++) {
				double input = inputs[in];
				double weight = weights[in][out];
				signal += (input * weight);
			}
			triggers[out] = signal + bias;
		}
		activation.activations(triggers, outputs);
		return outputs;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double[] backward(double[] errors) {
		int inputSize = getInputSize();
		int outputSize = getOutputSize();

		// Output deltas.
		double[] derivatives = new double[outputSize];
		activation.derivatives(triggers, outputs, derivatives);
		for (int out = 0; out < outputSize; out++) {
			outputDeltas[out] = errors[out] * (derivatives[out] + flatSpot);
		}

		// Input deltas and weights update.
		for (int in = 0; in < inputSize; in++) {
			double input = inputs[in];
			double inputDelta = 0;
			for (int out = 0; out < outputSize; out++) {
				double weight = weights[in][out];
				double outputDelta = outputDeltas[out];
				inputDelta += (weight * outputDelta);
				double gradient = (input * inputDelta);
				weights[in][out] += (gradient * learningRate);
			}
			inputDeltas[in] = inputDelta;
		}

		return inputDeltas;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void save(OutputStream os) throws IOException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void restore(InputStream is)throws IOException {
	}

}
