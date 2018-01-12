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
 * A neural network.
 *
 * @author Miquel Sas
 */
public class Network {

	/** List of layers. */
	private List<Layer> layers = new ArrayList<>();

	/**
	 * Constructor.
	 */
	public Network() {
		super();
	}

	/**
	 * Add a layer.
	 * 
	 * @param layer The layer to add.
	 */
	public void addLayer(Layer layer) {
		if (!layers.isEmpty()) {
			Layer previous = layers.get(layers.size() - 1);
			if (previous.getOutputSize() == layer.getInputSize()) {
				throw new IllegalArgumentException("Invalid previous and current output vs input sizes.");
			}
		}
		layers.add(layer);
	}

}
