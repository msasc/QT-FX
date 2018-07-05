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

package com.qtfx.app.plaf.action;

import com.qtfx.app.plaf.QTFX;
import com.qtfx.lib.app.Session;
import com.qtfx.lib.gui.TaskPane;
import com.qtfx.lib.gui.action.handlers.ActionEventHandler;
import com.qtfx.lib.ml.network.Trainer;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

/**
 * Action to train networks.
 *
 * @author Miquel Sas
 */
public class ActionTrainNetwork extends ActionEventHandler {

	/** Trainer task. */
	private Trainer trainer;

	/**
	 * Constructor.
	 * 
	 * @param node The reference node.
	 */
	public ActionTrainNetwork(Node node) {
		super(node);
	}

	/**
	 * Set the trainer task.
	 * 
	 * @param trainer The trainer task.
	 */
	public void setTrainer(Trainer trainer) {
		this.trainer = trainer;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handle(ActionEvent event) {
		if (trainer == null) {
			throw new IllegalStateException();
		}
		
		String tabText = Session.getSession().getString("tabTraining");
		if (!QTFX.isTab(getNode(), tabText)) {
			TaskPane taskPane = new TaskPane();
			taskPane.addTask(trainer);

			Tab tab = new Tab();
			tab.setText(tabText);
			tab.setContent(taskPane.getPane());
			tab.setOnCloseRequest(e -> {
				if (!taskPane.canClose()) {
					e.consume();
				}
			});

			TabPane tabPane = QTFX.getTabPane(getNode());
			tabPane.getTabs().add(tab);
			tabPane.getSelectionModel().select(tab);
		} else {
			Tab tab = QTFX.getTab(getNode(), tabText);
			TaskPane taskPane = TaskPane.getTaskPane(tab.getContent());
			taskPane.addTask(trainer);

			TabPane tabPane = QTFX.getTabPane(getNode());
			tabPane.getSelectionModel().select(tab);
		}
	}

}
