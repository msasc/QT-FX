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

package com.qtfx.lib.gui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.FlowPane;

/**
 * A status bar utility. Extends flow pane with default right horizontal alignment.
 *
 * @author Miquel Sas
 */
public class StatusBar {

	/** Internal flow pane. */
	private FlowPane flowPane;
	/** Default progress width. */
	private double progressWidth = 80;

	/**
	 * Default constructor.
	 */
	public StatusBar() {
		super();
		flowPane = new FlowPane();
		flowPane.setHgap(10);
		flowPane.setVgap(2);
		flowPane.setAlignment(Pos.CENTER_RIGHT);
	}

	/**
	 * Set the padding.
	 * 
	 * @param insets The insets.
	 */
	public void setPadding(Insets insets) {
		flowPane.setPadding(insets);
	}

	/**
	 * Return the internal flow pane.
	 * 
	 * @return The internal pane.
	 */
	public FlowPane getPane() {
		return flowPane;
	}

	/**
	 * Set the default width for progress bars.
	 * 
	 * @param progressWidth The width.
	 */
	public void setProgressWidth(double progressWidth) {
		this.progressWidth = progressWidth;
	}

	/**
	 * Remove the label with the given id.
	 * 
	 * @param id The id of the label.
	 */
	public void removeLabel(String id) {
		Platform.runLater(() -> {
			Node node = FX.getNode(flowPane.getChildren(), id);
			if (node == null || !(node instanceof Label)) {
				throw new IllegalArgumentException();
			}
			flowPane.getChildren().remove(node);
		});
	}

	/**
	 * Set the text to the label with the given id. If such label does not exist, it is created and added to the end.
	 * 
	 * @param id The id of the label.
	 * @param text The text.
	 */
	public void setLabel(String id, String text) {
		setLabel(id, text, null);
	}

	/**
	 * Set the text to the label with the given id. If such label does not exist, it is created and added to the end.
	 * 
	 * @param id The id of the label.
	 * @param text The text.
	 * @param style Optional style.
	 */
	public void setLabel(String id, String text, String style) {
		Platform.runLater(() -> {
			Node node = FX.getNode(flowPane.getChildren(), id);
			if (node != null && !(node instanceof Label)) {
				throw new IllegalArgumentException();
			}
			final Label label;
			final boolean add;
			if (node == null) {
				label = new Label();
				label.setId(id);
				add = true;
			} else {
				label = (Label) node;
				add = false;
			}
			if (style != null) {
				label.setStyle(style);
			}
			if (add) {
				flowPane.getChildren().add(label);
			}
			label.setText(text);
		});
	}

	/**
	 * Remove the progress bar with the given id.
	 * 
	 * @param id The id of the progress bar.
	 */
	public void removeProgress(String id) {
		Platform.runLater(() -> {
			Node node = FX.getNode(flowPane.getChildren(), id);
			if (node == null || !(node instanceof ProgressBar)) {
				throw new IllegalArgumentException();
			}
			flowPane.getChildren().remove(node);
		});
	}

	/**
	 * Set the progress of the progress bar with the given id. If such progress bar does not exist, it is created and
	 * added to the end.
	 * 
	 * @param id
	 * @param workDone
	 * @param totalWork
	 */
	public void setProgress(String id, double workDone, double totalWork) {
		Platform.runLater(() -> {
			Node node = FX.getNode(flowPane.getChildren(), id);
			if (node != null && !(node instanceof ProgressBar)) {
				throw new IllegalArgumentException();
			}
			final ProgressBar progressBar;
			final boolean add;
			if (node == null) {
				progressBar = new ProgressBar();
				progressBar.setId(id);
				progressBar.setPrefWidth(progressWidth);
				add = true;
			} else {
				progressBar = (ProgressBar) node;
				add = false;
			}
			if (add) {
				flowPane.getChildren().add(progressBar);
			}
			progressBar.setProgress(workDone / totalWork);
		});
	}

	/**
	 * Remove the status with the given id.
	 * 
	 * @param id The id.
	 */
	public void removeStatus(String id) {
		removeLabel(id + "-label");
		removeProgress(id + "-progress");
	}

	/**
	 * Set the status (label and progress).
	 * 
	 * @param id The status id.
	 * @param text The text.
	 * @param workDone Work done.
	 * @param totalWork Total work.
	 */
	public void setStatus(String id, String text, double workDone, double totalWork) {
		setLabel(id + "-label", text);
		setProgress(id + "-progress", workDone, totalWork);
	}
}
