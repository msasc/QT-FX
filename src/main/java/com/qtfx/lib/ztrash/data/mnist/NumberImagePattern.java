/*
 * Copyright (C) 2015 Miquel Sas
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

package com.qtfx.lib.ztrash.data.mnist;

import com.qtfx.lib.ztrash.data.Pattern;
import com.qtfx.lib.ztrash.function.Normalizer;

/**
 * A MNIST number image pattern.
 *
 * @author Miquel Sas
 */
public class NumberImagePattern implements Pattern {

	/** Underlying number image. */
	private NumberImage image;
	/** Patter inputs. */
	private double[] inputs;
	/** Pattern outputs. */
	private double[] outputs;
	/** A boolean that indicates if input and output must be bipolar. */
	private boolean bipolar;

	/**
	 * Constructor.
	 * 
	 * @param image The number image.
	 * @param bipolar A boolean that indicates if input must be bipolar.
	 */
	public NumberImagePattern(NumberImage image, boolean bipolar) {
		super();
		this.image = image;
		this.bipolar = bipolar;
	}

	/**
	 * Return the pattern input.
	 * 
	 * @return The pattern input.
	 */
	@Override
	public double[] getInputs() {
		if (inputs == null) {
			Normalizer normalizer = new Normalizer(255, 0, 1, (bipolar ? -1 : 0));
			inputs = new double[NumberImage.ROWS * NumberImage.COLUMNS];
			int index = 0;
			for (int row = 0; row < NumberImage.ROWS; row++) {
				for (int column = 0; column < NumberImage.COLUMNS; column++) {
					double imageByte = image.getImage()[row][column];
					inputs[index++] = normalizer.normalize(imageByte);
				}
			}
		}
		return inputs;
	}

	/**
	 * Return the pattern output.
	 * 
	 * @return The pattern output.
	 */
	@Override
	public double[] getOutputs() {
		if (outputs == null) {
			Normalizer normalizer = new Normalizer(1, 0, 1, (bipolar ? -1 : 0));
			int number = image.getNumber();
			outputs = new double[10];
			int index = 0;
			for (int i = 0; i < number; i++) {
				outputs[index++] = normalizer.normalize(0.0);
			}
			outputs[index++] = normalizer.normalize(1.0);
			for (int i = number + 1; i < 10; i++) {
				outputs[index++] = normalizer.normalize(0.0);
			}
		}
		return outputs;
	}

	/**
	 * Return the label.
	 * 
	 * @return The label.
	 */
	@Override
	public String getLabel() {
		return Integer.toString(image.getNumber());
	}

	/**
	 * Returns the number image.
	 * 
	 * @return The number image.
	 */
	public NumberImage getImage() {
		return image;
	}
}
