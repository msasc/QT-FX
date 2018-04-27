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

import com.qtfx.lib.app.TextServer;
import com.qtfx.lib.util.Icons;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * A panel located at the top of the container, aimed to contain an info panel and necessary controls like the close
 * button.
 *
 * @author Miquel Sas
 */
public class ChartInfo {

	/** The parent container. */
	private ChartContainer container;
	/** Grid that contains the cursor information and the buttons. */
	private GridPane pane = new GridPane();
	/** Text flow info. */
	private TextFlow textFlow = new TextFlow();

	/** Temporary list of texts. */
	private List<Text> texts = new ArrayList<>();

	/**
	 * Constructor.
	 * 
	 * @param container The parent container.
	 */
	public ChartInfo(ChartContainer container) {
		super();
		this.container = container;

		// Column 0 is a text flow that will expand.
		ColumnConstraints c0 = new ColumnConstraints();
		c0.setHgrow(Priority.ALWAYS);
		c0.setMaxWidth(Double.MAX_VALUE);
		pane.getColumnConstraints().add(c0);
		pane.add(textFlow, 0, 0);
		pane.setStyle("-fx-border-width: 0 0 1 0; -fx-border-color: black;");
		pane.setPadding(new Insets(3, 3, 3, 3));

		// Node 1, 0: buttons in an HBox.
		HBox hbox = new HBox(2);
		pane.add(hbox, 1, 0);

		// Close button.
		Button buttonClose = new Button();
		buttonClose.setDefaultButton(false);
		buttonClose.setCancelButton(false);
		buttonClose.setGraphic(Icons.get(Icons.FLAT_24x24_CLOSE));
		buttonClose.setPadding(new Insets(0, 0, 0, 0));

		buttonClose.setTooltip(new Tooltip(TextServer.getString("tooltipRemoveChart",
			container.getChart().getLocale())));
		buttonClose.setStyle("-fx-content-display: graphic-only;");
		buttonClose.setOnAction(e -> {
			this.container.getChart().removeContainer(this.container);
		});

		hbox.getChildren().add(buttonClose);
	}

	/**
	 * Return the pane.
	 * 
	 * @return The pane.
	 */
	public GridPane getPane() {
		return pane;
	}

	/**
	 * Start info process.
	 */
	public void startInfo() {
		texts.clear();
	}

	/**
	 * Add the information.
	 * 
	 * @param info Information text.
	 * @param style Style.
	 */
	public void addInfo(String info, String style) {
		Text text = new Text();
		text.setStyle("-fx-font-size: 14; " + style);
		text.setText(info);
		if (!texts.isEmpty()) {
			texts.add(new Text(" - "));
		}
		texts.add(text);
	}

	/**
	 * End info process and show.
	 */
	public void endInfo() {
		textFlow.getChildren().clear();
		textFlow.getChildren().addAll(texts);
	}
}
