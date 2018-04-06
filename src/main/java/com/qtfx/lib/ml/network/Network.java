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

import java.util.ArrayList;
import java.util.List;

/**
 * A feed forward neural network, essentially a list of layers.
 *
 * @author Miquel Sas
 */
public class Network {

	/** List of layers. */
	private List<Layer> layers = new ArrayList<>();
	/** The input size of the network. */
	private int inputSize;

	/**
	 * Default constructor.
	 */
	public Network(int inputSize) {
		super();
		this.inputSize = inputSize;
	}

	/**
	 * Add a layer to the network.
	 * 
	 * @param layer The layer.
	 */
	public void addLayer(Layer layer) {
		if (layers.isEmpty()) {
			layer.inputSize = inputSize;
		} else {
			layer.inputSize = layers.get(layers.size() - 1).getOutputSize();
		}
		layer.network = this;
		layers.add(layer);
	}

	/**
	 * Check if a layer is the input layer.
	 * 
	 * @param layer The layer.
	 * @return A boolean.
	 */
	boolean isInputLayer(Layer layer) {
		if (layers.isEmpty()) {
			throw new IllegalStateException("Network is empty");
		}
		return layers.get(0).equals(layer);
	}

	/**
	 * Check if a layer is the output layer.
	 * 
	 * @param layer The layer.
	 * @return A boolean.
	 */
	boolean isOutputLayer(Layer layer) {
		if (layers.isEmpty()) {
			throw new IllegalStateException("Network is empty");
		}
		return layers.get(layers.size() - 1).equals(layer);
	}

	/**
	 * Check if a layer is a hidden layer.
	 * 
	 * @param layer The layer.
	 * @return A boolean.
	 */
	boolean isHiddenLayer(Layer layer) {
		return (!isInputLayer(layer) && !isOutputLayer(layer));
	}

}
