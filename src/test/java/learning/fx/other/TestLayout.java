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

package learning.fx.other;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

/**
 * Test a suitable layout for the system dialog needs.
 * 
 * @author Miquel Sas
 */
public class TestLayout extends Application {

	private static Button ACCEPT = new Button("Accept");
	private static Button APPLY = new Button("Apply");
	private static Button CANCEL = new Button("Cancel");
	private static Button CLOSE = new Button("Close");
	private static Button FINISH = new Button("Finish");
	private static Button IGNORE = new Button("Ignore");
	private static Button NEXT = new Button("Next");
	private static Button NO = new Button("No");
	private static Button OK = new Button("Ok");
	private static Button PREVIOUS = new Button("Previous");
	private static Button RETRY = new Button("Retry");
	private static Button YES = new Button("Yes");

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {

		// Root is a border pane.
		BorderPane root = new BorderPane();

		// A bottom flow pane for buttons.
		FlowPane bottom = new FlowPane(Orientation.HORIZONTAL);
		bottom.setAlignment(Pos.CENTER_RIGHT);
		bottom.getChildren().addAll(getBoxes());
		root.setBottom(bottom);

		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle("Test layout");

		Button[] buttons = { ACCEPT, APPLY };
		for (Button button : buttons) {
			button.addEventFilter(ActionEvent.ACTION, ae -> {
				if (ae.isConsumed())
					return;
				showMessage(stage);
			});
		}
		CANCEL.addEventFilter(ActionEvent.ACTION, ae -> {
			ae.consume();
			stage.close();
		});

		stage.show();
	}

	private HBox[] getBoxes() {
		List<HBox> boxes = new ArrayList<>();
		boxes.add(getBox(ACCEPT, APPLY));
		boxes.add(getBox(CANCEL, CLOSE));
		boxes.add(getBox(FINISH, IGNORE));
		boxes.add(getBox(NEXT, NO));
		boxes.add(getBox(OK, PREVIOUS));
		boxes.add(getBox(RETRY, YES));
		return boxes.toArray(new HBox[boxes.size()]);
	}

	private HBox getBox(Button... buttons) {
		HBox box = new HBox(3);
		box.setPadding(new Insets(2, 5, 2, 5));
		box.getChildren().addAll(buttons);
		return box;
	}

	private void showMessage(Window window) {

		BorderPane root = new BorderPane();

		Text top = new Text("Voilà");
		root.setTop(top);

		TextArea center = new TextArea();
		center.setText("kajd nlaskdn lcqcjeno3icjnlwxajnslaskjxnslksakjdlaskjd b lkkj lakkjdlaksj d alkdj asslkj");
		center.setWrapText(true);
		root.setCenter(center);

		Button button = new Button("Close");
		root.setBottom(button);

		Scene scene = new Scene(root);

		Stage stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initStyle(StageStyle.DECORATED);
		stage.initOwner(window);
		stage.setScene(scene);

		button.addEventHandler(ActionEvent.ACTION, ae -> {
			if (ae.isConsumed())
				return;
			stage.close();
		});

		stage.show();
	}
}
