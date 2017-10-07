package learning.fx.projavafx8.ch05;

import javafx.scene.control.ButtonType;

public class Alert extends javafx.scene.control.Alert {

	public Alert(AlertType alertType) {
		super(alertType);
		// TODO Auto-generated constructor stub
	}

	public Alert(AlertType alertType, String contentText, ButtonType... buttons) {
		super(alertType, contentText, buttons);
		// TODO Auto-generated constructor stub
	}

}
