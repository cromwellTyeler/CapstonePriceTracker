import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmBox {

	static int answer = 0;

	/**
	 * Displays a box with up to 3 buttons for users to select, returns what they choose
	 * @param buttons : decides how many buttons to show 
	 * 				1 = yes, 2 = no, 3 = save and close
	 * @param title : what the title of the window is
	 * @param message : message to be displayed in a label
	 * @return an int based off of what the user selects
	 */
	public static int display(int buttons, String title, String message) {
		Stage window = new Stage();

		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(title);
		window.setMinWidth(250);

		Label label = new Label();
		label.setText(message);

		HBox hbox = new HBox();

		if (buttons >= 1) {
			Button yesButton = new Button("Yes");
			yesButton.setOnAction(e -> {
				answer = 1;
				window.close();
			});
			hbox.getChildren().add(yesButton);
		}

		if (buttons >= 2) {
			Button noButton = new Button("No");
			noButton.setOnAction(e -> {
				answer = 0;
				window.close();
			});
			hbox.getChildren().add(noButton);
		}
		
		if (buttons >= 3) {
			Button saveAndCloseButton = new Button("Save and Close");
			saveAndCloseButton.setOnAction(e -> {
				answer = 2;
				window.close();
			});
			hbox.getChildren().add(saveAndCloseButton);
		}


		hbox.setPadding(new Insets(10, 10, 10, 10));
		hbox.setSpacing(20);

		VBox layout = new VBox(10);
		layout.getChildren().addAll(label, hbox);
		layout.setPadding(new Insets(10, 10, 10, 10));

		layout.setAlignment(Pos.CENTER);

		Scene scene = new Scene(layout);
		window.setScene(scene);
		window.showAndWait();

		return answer;
	}

}
