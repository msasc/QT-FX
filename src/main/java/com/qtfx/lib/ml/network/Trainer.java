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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.qtfx.lib.app.Session;
import com.qtfx.lib.math.Vector;
import com.qtfx.lib.ml.data.Pattern;
import com.qtfx.lib.ml.data.PatternSource;
import com.qtfx.lib.ml.function.IterationError;
import com.qtfx.lib.task.Task;

/**
 * Trainer task to train a network that can backward errors to movify internal data, e.g. weights.
 *
 * @author Miquel Sas
 */
public class Trainer extends Task {

	/** Additional label to show the current error. */
	private static final String LABEL_ERROR = "Error";
	/** Additional label to inform that the network is being saved. */
	private static final String LABEL_SAVING = "Saving";

	/** The network. */
	private Network network;
	/** The pattern source. */
	private PatternSource patternSource;
	/** The number of epochs or turns to the full list of patterns. */
	private int epochs = 100;
	/** The file to save the network when the error improves. */
	private File file;
	/** Iteration error function. */
	private IterationError errorFunction;

	/**
	 * @param session
	 */
	public Trainer(Session session) {
		super(session);
		addMessage(LABEL_ERROR);
		addMessage(LABEL_SAVING);
	}

	/**
	 * Set the network.
	 * 
	 * @param network The network.
	 */
	public void setNetwork(Network network) {
		this.network = network;
	}

	/**
	 * Set the pattern source.
	 * 
	 * @param patternSource The pattern source.
	 */
	public void setPatternSource(PatternSource patternSource) {
		this.patternSource = patternSource;
	}

	/**
	 * Set the number of epochs to process.
	 * 
	 * @param epochs The number of epochs to process.
	 */
	public void setEpochs(int epochs) {
		this.epochs = epochs;
	}

	/**
	 * Set the file to save/restore network data.
	 * 
	 * @param file The file to save/restore network data.
	 */
	public void setFile(File file) {
		this.file = file;
	}

	/**
	 * Set the iteration error function.
	 * 
	 * @param errorFunction The error function.
	 */
	public void setErrorFunction(IterationError errorFunction) {
		this.errorFunction = errorFunction;
	}

	/**
	 * Save the network.
	 * 
	 * @throws IOException
	 */
	private void save() throws IOException {
		if (file != null) {
			FileOutputStream fo = new FileOutputStream(file);
			BufferedOutputStream bo = new BufferedOutputStream(fo);
			network.save(bo);
			bo.close();
			fo.close();
		}
	}

	/**
	 * Restore the network.
	 * 
	 * @throws IOException
	 */
	private void restore() throws IOException {
		if (file != null && file.exists() && file.length() != 0) {
			FileInputStream fi = new FileInputStream(file);
			BufferedInputStream bi = new BufferedInputStream(fi);
			network.restore(bi);
			bi.close();
			fi.close();
		}
	}

	/**
	 * Check the correct status.
	 * 
	 * @throws IllegalStateException
	 */
	private void checkStatus() throws IllegalStateException {
		if (network == null) {
			throw new IllegalStateException("The network must be set");
		}
		if (patternSource == null) {
			throw new IllegalStateException("The pattern source mmust be set");
		}
		if (epochs < 1) {
			throw new IllegalStateException("The number of epochs must be at least 1");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void compute() throws Exception {
		
		// Check status.
		checkStatus();

		// Restore the network if applicable.
		updateMessage(LABEL_SAVING, "Restoring the network...");
		restore();
		updateMessage(LABEL_SAVING, "");

		// Calculate and register total work.
		double totalWork = patternSource.size() * epochs;
		double workDone = 0;
		setTotalWork(totalWork);

		Double bestError = null;

		// Iterate epochs.
		for (int epoch = 1; epoch <= epochs; epoch++) {

			// Reset the error function at each epoch.
			errorFunction.reset();

			// Iterate each pattern.
			for (int i = 0; i < patternSource.size(); i++) {

				// Work done and notify work.
				workDone += 1;
				StringBuilder work = new StringBuilder();
				work.append("Epoch " + epoch);
				work.append("Pattern " + (i + 1));
				update(work.toString(), workDone, totalWork);

				// Process the pattern.
				Pattern pattern = patternSource.get(i);
				double[] patternOutputs = pattern.getOutputs();
				double[] networkOutputs = network.forward(pattern.getInputs());

				// Register the error, get the total error and show it.
				errorFunction.add(patternOutputs, networkOutputs);
				double error = errorFunction.get();
				updateMessage(LABEL_ERROR, "Error " + error);

				// Save if error best than best error.
				if (bestError == null) {
					bestError = error;
				}
				if (error < bestError) {
					bestError = error;
					updateMessage(LABEL_SAVING, "Saving the network...");
					save();
					updateMessage(LABEL_SAVING, "");
				}

				// Errors or deltas and backward. Discard the return vector.
				double[] networkErrors = Vector.subtract(patternOutputs, networkOutputs);
				network.backward(networkErrors);
			}

		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isIndeterminate() {
		return false;
	}
}
