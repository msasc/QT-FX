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
import com.qtfx.lib.util.Icons;
import com.qtfx.lib.util.TextServer;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
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

	/**
	 * Scroll data back and front.
	 */
	class Scroll implements Runnable {
		private Button button;
		private int scroll;
		private double x;
		private double y;

		public Scroll(Button button, int scroll, double x, double y) {
			super();
			this.button = button;
			this.scroll = scroll;
			this.x = x;
			this.y = y;
		}

		@Override
		public void run() {
			int sleep = 100;
			int minSleep = 10;
			while (button == pressedButton) {
				Platform.runLater(() -> {
					PlotData plotData = containers.get(0).getPlotData();
					if (plotData.scroll(scroll)) {
						plot(plotData);
						containers.forEach(container -> container.setChartInfo(x, y));
					}
				});
				try {
					Thread.sleep(sleep > minSleep ? sleep-- : minSleep);
				} catch (InterruptedException ignore) {
				}
			}
		}

	}

	/** Working locale. */
	private Locale locale;
	/** Effective border pane. */
	private BorderPane pane;
	/** Top tool bar. */
	private ToolBar toolBar = new ToolBar();
	/** List of containers. */
	private List<ChartContainer> containers = new ArrayList<>();
	/** Chart horizontal axis. */
	private ChartHorizontalAxis horizontalAxis;

	/** Pressed button. */
	private Button pressedButton;

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

		// Set the tool bar.
		pane.setTop(toolBar);

		// Button move front.
		Button buttonFront = getButton(Icons.FLAT_24x24_SCROLL_FRONT, "tooltipScrollFront");
		buttonFront.setOnMousePressed(e -> {
			pressedButton = buttonFront;
			new Thread(new Scroll(buttonFront, -1, e.getX(), e.getY())).start();
		});
		buttonFront.setOnMouseReleased(e -> {
			pressedButton = null;
		});

		// Button move end.
		Button buttonEnd = getButton(Icons.FLAT_24x24_SCROLL_END, "tooltipScrollEnd");
		buttonEnd.setOnAction(e -> {
			Platform.runLater(() -> {
				PlotData plotData = containers.get(0).getPlotData();
				if (plotData.scrollEnd()) {
					plot(plotData);
				}
			});
		});

		// Button move back.
		Button buttonBack = getButton(Icons.FLAT_24x24_SCROLL_BACK, "tooltipScrollBack");
		buttonBack.setOnMousePressed(e -> {
			pressedButton = buttonBack;
			new Thread(new Scroll(buttonBack, 1, e.getX(), e.getY())).start();
		});
		buttonBack.setOnMouseReleased(e -> {
			pressedButton = null;
		});

		// Button move start.
		Button buttonStart = getButton(Icons.FLAT_24x24_SCROLL_START, "tooltipScrollStart");
		buttonStart.setOnAction(e -> {
			Platform.runLater(() -> {
				PlotData plotData = containers.get(0).getPlotData();
				if (plotData.scrollStart()) {
					plot(plotData);
				}
			});
		});

		Separator separator = new Separator(Orientation.VERTICAL);
		separator.setMinWidth(4);
		separator.setPadding(new Insets(0, 0, 0, 3));

		toolBar.getItems().add(buttonFront);
		toolBar.getItems().add(buttonEnd);
		toolBar.getItems().add(separator);
		toolBar.getItems().add(buttonBack);
		toolBar.getItems().add(buttonStart);

		// Horizontal axis.
		horizontalAxis = new ChartHorizontalAxis(this);
		pane.setBottom(horizontalAxis.getPane());
	}

	/**
	 * Returns a flat button.
	 * 
	 * @param icon The icon path.
	 * @param tooltip The tooltip key.
	 * @return The button.
	 */
	private Button getButton(String icon, String tooltip) {
		Button button = new Button();
		button.setDefaultButton(false);
		button.setCancelButton(false);
		button.setGraphic(Icons.get(icon));
		button.setPadding(new Insets(0, 0, 0, 0));
		button.setTooltip(new Tooltip(TextServer.getString(tooltip)));
		button.setStyle("-fx-content-display: graphic-only;");
		return button;
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
	 * 
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
