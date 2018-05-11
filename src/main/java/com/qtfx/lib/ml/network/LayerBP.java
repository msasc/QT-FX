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

import com.qtfx.lib.math.Vector;
import com.qtfx.lib.ml.function.Activation;
import com.qtfx.lib.util.Random;

/**
 * Perceptron back propagation layer for multilayer perceptron neural networks.
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
	/** Last weight deltas for momentum. */
	private double[][] weightDeltas;
	/** The bias weight in case the bias is adjusted (?), */
	private double biasWeight = 1.0;
	/** Learning rate. */
	private double eta = 0.1;
	/** Momentum factor. */
	private double alpha = 0.0;
	/** Weight decay factor, which is also a regularization term. */
	private double lambda = 0.0;

	/**
	 * Constructor.
	 * 
	 * @param outputSize
	 */
	public LayerBP(int outputSize) {
		super(outputSize);
	}

	/**
	 * Set the momentum factor <em>alpha</em>.
	 * 
	 * @param alpha The momentum factor alpha.
	 */
	public void setAlpha(double alpha) {
		this.alpha = alpha;
	}

	/**
	 * Set the learning rate <em>eta</em>.
	 * 
	 * @param eta The learning rate eta.
	 */
	public void setEta(double eta) {
		this.eta = eta;
	}

	/**
	 * Set the flat spot to avoid near zero derivatives in the back propagation process.
	 * 
	 * @param flatSpot The flat spot to avoid near zero derivatives in the back propagation process.
	 */
	public void setFlatSpot(double flatSpot) {
		this.flatSpot = flatSpot;
	}

	/**
	 * Set the weight decay factor, which is also a regularization term.
	 * 
	 * @param lambda The weight decay factor.
	 */
	public void setLambda(double lambda) {
		this.lambda = lambda;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initialize() {

		// Input and output sizes.
		int inputSize = getInputSize();
		int outputSize = getOutputSize();

		// Forward components: inputs, outputs, triggers and weights (in, out).
		inputs = new double[inputSize];
		outputs = new double[outputSize];
		triggers = new double[outputSize];
		weights = new double[inputSize][outputSize];

		// Randomize weights.
		for (int in = 0; in < inputSize; in++) {
			for (int out = 0; out < outputSize; out++) {
				weights[in][out] = Random.nextDouble();
			}
		}

		// Backward components: output, input and weight deltas.
		outputDeltas = new double[outputSize];
		inputDeltas = new double[inputSize];
		weightDeltas = new double[inputSize][outputSize];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double[] forward(double[] inputs) {
		Vector.copy(inputs, this.inputs);
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

		// Output deltas: apply activation derivatives to errors. Include a flat spot to avoid near zero derivatives.
		double[] derivatives = new double[outputSize];
		activation.derivatives(triggers, outputs, derivatives);
		for (int out = 0; out < outputSize; out++) {
			outputDeltas[out] = errors[out] * (derivatives[out] + flatSpot);
		}

		// Current gradients and input deltas.
		for (int in = 0; in < inputSize; in++) {
			double inputDelta = 0;
			for (int out = 0; out < outputSize; out++) {
				double weight = weights[in][out];
				double outputDelta = outputDeltas[out];
				inputDelta += (weight * outputDelta);
			}
			inputDeltas[in] = inputDelta;
		}

		// Adjust weights.
		for (int in = 0; in < inputSize; in++) {
			// Input -> previous layer output
			double input = inputs[in];
			for (int out = 0; out < outputSize; out++) {
				// Output delta -> output error * derivative -> gradient
				double outputDelta = outputDeltas[out];
				// Previous weight delta.
				double weightDelta = weightDeltas[in][out];
				// New weight delta
				double delta = (1 - alpha) * eta * outputDelta * input * (alpha * weightDelta);
				weightDeltas[in][out] = delta;
				weights[in][out] += delta;
				weights[in][out] *= (1.0 - eta * lambda);
			}
		}
		
		// Adjust bias.

		return inputDeltas;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void save(OutputStream os) throws IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void restore(InputStream is) throws IOException {
		// TODO Auto-generated method stub
	}

}
