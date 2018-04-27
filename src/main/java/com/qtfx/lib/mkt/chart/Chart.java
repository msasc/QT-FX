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

import com.qtfx.lib.app.TextServer;
import com.qtfx.lib.mkt.data.PlotData;
import com.qtfx.lib.util.Icons;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
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
	 * Enum toolbar button actions.
	 */
	enum Action {
		SCROLL_BACK, SCROLL_FRONT, SCROLL_START, SCROLL_END, ZOOM_IN, ZOOM_OUT;
	}

	/**
	 * Mouse pressed-released event handler for toolbar button actions.
	 */
	class Handler implements EventHandler<MouseEvent> {
		private Button button;
		private Action action;

		public Handler(Button button, Action action) {
			super();
			this.button = button;
			this.action = action;
		}

		@Override
		public void handle(MouseEvent e) {
			if (e.getEventType() == MouseEvent.MOUSE_PRESSED) {
				pressedButton = button;
				if (action == Action.SCROLL_FRONT) {
					new Thread(new Scroll(button, -1, e.getX(), e.getY())).start();
					return;
				}
				if (action == Action.SCROLL_FRONT) {
					new Thread(new Scroll(button, -1, e.getX(), e.getY())).start();
					return;
				}
				if (action == Action.SCROLL_BACK) {
					new Thread(new Scroll(button, 1, e.getX(), e.getY())).start();
					return;
				}
				if (action == Action.SCROLL_END) {
					Platform.runLater(() -> {
						PlotData plotData = containers.get(0).getPlotData();
						if (plotData.scrollEnd()) {
							plot(plotData);
						}
					});
					return;
				}
				if (action == Action.SCROLL_START) {
					Platform.runLater(() -> {
						PlotData plotData = containers.get(0).getPlotData();
						if (plotData.scrollStart()) {
							plot(plotData);
						}
					});
				}
				if (action == Action.ZOOM_IN) {
					new Thread(new Zoom(button, Action.ZOOM_IN, e.getX(), e.getY())).start();
					return;
				}
				if (action == Action.ZOOM_OUT) {
					new Thread(new Zoom(button, Action.ZOOM_OUT, e.getX(), e.getY())).start();
					return;
				}
			}
			if (e.getEventType() == MouseEvent.MOUSE_RELEASED) {
				pressedButton = null;
				getPane().requestFocus();
			}
		}
	}

	/**
	 * Runnable to zoom data in and out.
	 */
	class Zoom implements Runnable {
		private Button button;
		private Action action;
		private double x;
		private double y;
		private int zoom;

		public Zoom(Button button, Action action, double x, double y) {
			super();
			this.button = button;
			this.action = action;
			this.x = x;
			this.y = y;
		}

		@Override
		public void run() {
			int sleep = 500;
			while (button == pressedButton) {
				PlotData plotData = containers.get(0).getPlotData();
				if (action == Action.ZOOM_IN) {
					zoom = plotData.getBarsToScrollOrZoom() * 1;
				}
				if (action == Action.ZOOM_OUT) {
					zoom = plotData.getBarsToScrollOrZoom() * (-1);
				}
				Platform.runLater(() -> {
					if (plotData.zoom(zoom)) {
						plot(plotData);
						containers.forEach(container -> container.setChartInfo(x, y));
					}
				});
				try {
					Thread.sleep(sleep);
				} catch (InterruptedException ignore) {
				}
			}
		}
	}

	/**
	 * Runnable to scroll data back and front.
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

		Button btFront = getButton(Icons.FLAT_24x24_SCROLL_FRONT, "tooltipScrollFront", Action.SCROLL_FRONT);
		Button btEnd = getButton(Icons.FLAT_24x24_SCROLL_END, "tooltipScrollEnd", Action.SCROLL_END);
		Button btBack = getButton(Icons.FLAT_24x24_SCROLL_BACK, "tooltipScrollBack", Action.SCROLL_BACK);
		Button btStart = getButton(Icons.FLAT_24x24_SCROLL_START, "tooltipScrollStart", Action.SCROLL_START);
		Button btZoomIn = getButton(Icons.FLAT_24x24_ZOOM_IN, "tooltipZoomIn", Action.ZOOM_IN);
		Button btZoomOut = getButton(Icons.FLAT_24x24_ZOOM_OUT, "tooltipZoomOut", Action.ZOOM_OUT);

		toolBar.getItems().add(btFront);
		toolBar.getItems().add(btEnd);
		toolBar.getItems().add(getSeparator());
		toolBar.getItems().add(btBack);
		toolBar.getItems().add(btStart);
		toolBar.getItems().add(getSeparator());
		toolBar.getItems().add(btZoomIn);
		toolBar.getItems().add(btZoomOut);

		// Horizontal axis.
		horizontalAxis = new ChartHorizontalAxis(this);
		pane.setBottom(horizontalAxis.getPane());
	}

	/**
	 * Returns a new instance of the separator for the tool bar.
	 * 
	 * @return The separator.
	 */
	private Separator getSeparator() {
		Separator separator = new Separator(Orientation.VERTICAL);
		separator.setMinWidth(4);
		separator.setPadding(new Insets(0, 0, 0, 3));
		return separator;
	}

	/**
	 * Returns a flat button.
	 * 
	 * @param icon The icon path.
	 * @param tooltip The tooltip key.
	 * @param action The button action.
	 * @return The button.
	 */
	private Button getButton(String icon, String tooltip, Action action) {
		Button button = new Button();
		button.setDefaultButton(false);
		button.setCancelButton(false);
		button.setGraphic(Icons.get(icon));
		button.setPadding(new Insets(0, 0, 0, 0));
		button.setTooltip(new Tooltip(TextServer.getString(tooltip, getLocale())));
		button.setStyle("-fx-content-display: graphic-only;");
		button.addEventHandler(MouseEvent.ANY, new Handler(button, action));
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
