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

package com.qtfx.lib.mkt.chart;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.qtfx.lib.mkt.data.PlotData;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;

/**
 * A top panel aimed to contain all the panels involved in the display of a trading chart. From top to down the panels
 * are:
 * <ul>
 * <li>A chart panel to display prices and over-chart indicators, with its corresponding right-vertical axis panel.</li>
 * <li>Several optional panels to display not over-chart indicators, with their corresponding right-vertical axis
 * panels.</li>
 * <li>A optional panel to display volumes and over-volumes indicators, with its corresponding right-vertical axis
 * panel.</li>
 * <li>A bottom horizontal axis panel.</li>
 * </ul>
 * 
 * @author Miquel Sas
 */
public class Chart {

	/** Working locale. */
	private Locale locale;
	/** Effective border pane. */
	private BorderPane pane;
	/** List of containers. */
	private List<ChartContainer> containers = new ArrayList<>();
	/** Chart horizontal axis. */
	private ChartHorizontalAxis horizontalAxis;

	/**
	 * Constructor.
	 * 
	 * @param locale Working locale.
	 */
	public Chart(Locale locale) {
		super();
		this.locale = locale;

		// Configure the pane.
		pane = new BorderPane();
		
		// Horizontal axis.
		horizontalAxis = new ChartHorizontalAxis(this);
		pane.setBottom(horizontalAxis.getPane());
	}

	/**
	 * Return the working locale.
	 * 
	 * @return The locale.
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * Return the chart pane.
	 * 
	 * @return The chart pane.
	 */
	public BorderPane getPane() {
		return pane;
	}

	/**
	 * Return a non modifiable list of containers.
	 * @return The list of containers.
	 */
	public List<ChartContainer> getContainers() {
		return new ArrayList<>(containers);
	}

	/**
	 * Add plot data.
	 * 
	 * @param plotData The plot data.
	 */
	public void addPlotData(PlotData plotData) {
		ChartContainer container = new ChartContainer(this, plotData);
		containers.add(container);
		pane.setCenter(container.getPane());
		container.getPane().prefWidthProperty().bind(
			Bindings.selectDouble(container.getPane().parentProperty(), "width"));
	}
	/**
	 * Remove the argument container.
	 * 
	 * @param container The container to remove.
	 */
	public void removeContainer(ChartContainer container) {
		containers.remove(container);
		if (!(pane.getCenter() instanceof SplitPane)) {
			pane.setCenter(null);
		}
	}
	
	/**
	 * Plot the char by plotting every container.
	 * 
	 * @param plotData The reference plot data.
	 */
	public void plot(PlotData plotData) {
		Platform.runLater(() -> {
			containers.forEach(container -> container.getPlotData().setIndexes(plotData));
			containers.forEach(container -> container.plot());
			horizontalAxis.plot();
		});
	}
}
