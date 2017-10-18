package learning.fx.oracle;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginByCode extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {

		GridPane grid = new GridPane();
		grid.setBackground(Background.EMPTY);
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		Text textTitle = new Text("Welcome");
		textTitle.setFont(Font.font("Arial Black", FontWeight.NORMAL, 32));
		textTitle.setFill(Color.valueOf("#919191"));
		textTitle.setEffect(new InnerShadow(BlurType.THREE_PASS_BOX, new Color(0, 0, 0, 0.7), 0.5, 0, 1, 1));
		grid.add(textTitle, 0, 0, 2, 1);

		Label labelName = new Label("User name");
		grid.add(labelName, 0, 1);

		TextField fieldName = new TextField();
		grid.add(fieldName, 1, 1);

		Label labelPwd = new Label("Password");
		grid.add(labelPwd, 0, 2);

		PasswordField fieldPwd = new PasswordField();
		grid.add(fieldPwd, 1, 2);

		Button btn = new Button("Sign in");
		HBox hbBtn = new HBox(10);
		hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
		hbBtn.getChildren().add(btn);
		grid.add(hbBtn, 1, 4);

		Text textAction = new Text();
		grid.add(textAction, 1, 6);

		btn.setOnAction((ActionEvent e) -> {
			textAction.setFill(Color.FIREBRICK);
			textAction.setText("Sign in button pressed: " + fieldName.getText());
		});

		Scene scene = new Scene(grid, 500, 275);
		scene.setFill(Color.BLANCHEDALMOND);

		primaryStage.setScene(scene);
		primaryStage.setTitle("JavaFX Welcome!");
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
