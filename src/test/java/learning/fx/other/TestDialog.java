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

import java.util.Optional;

import com.qtfx.library.util.Icons;

import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.stage.Stage;

/**
 * Test dialog.
 * 
 * @author Miquel Sas
 */
public class TestDialog extends Application {
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage stage) throws Exception {

		// Dialog.
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setTitle("Title");
//		dialog.setHeaderText("Header text");
		dialog.setContentText("Content text ksajdhaksjdhak djkahs kdjash kdjhas kdjhas kjdh askjdh askjdh askjdh akjd ");
		
		DialogPane dialogPane = dialog.getDialogPane();

		// Some buttons.
		ButtonType typeOk = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
		ButtonType typeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
		ButtonType typeOther1 = new ButtonType("Other1", ButtonBar.ButtonData.OTHER);
		ButtonType typeOther2 = new ButtonType("Other2", ButtonBar.ButtonData.OTHER);
		ButtonType typeOther3 = new ButtonType("Other3", ButtonBar.ButtonData.OTHER);
		ButtonType typeNext = new ButtonType("Next", ButtonBar.ButtonData.NEXT_FORWARD);

		// Add buttons.
		dialogPane.getButtonTypes().addAll(typeOk, typeCancel, typeOther1, typeNext, typeOther2, typeOther3);

		Button buttonOk = (Button) dialog.getDialogPane().lookupButton(typeOk);
		Button buttonOther = (Button) dialog.getDialogPane().lookupButton(typeOther1);
//		Image image = new Image(new FileInputStream(Icons.app_16x16_accept));
//		buttonOk.setGraphic(new ImageView(image));
		
//		dialogPane.getStyleClass().add("alert");
//		dialogPane.getStyleClass().add("warning");
//		dialogPane.setPrefWidth(1000);
		dialogPane.setGraphic(Icons.get(Icons.APP_32x32_DIALOG_INFORMATION));
		
		dialog.setResizable(true);
		
		Optional<ButtonType> type = dialog.showAndWait();
		if (type.isPresent() && type.get().equals(typeOk)) {
			System.out.println("Lo botó OK");
		}

	}

}
