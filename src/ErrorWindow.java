import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ErrorWindow {
	private String error;
	private Label label;

	/**
	 * Basic constructor, just sets error to a default message 
	 */
	public ErrorWindow() {
		error = "Error not set";
	}

	/**
	 * Constructor that sets error to a given message
	 * @param error : what the error message will be 
	 */
	public ErrorWindow(String error) {
		this.error = error;
		label = new Label(error);
	}

	/**
	 * Displays the error to the user in a new window
	 */
	public void displayError() {
		Stage stage = new Stage();
		stage.setTitle("Error");
		StackPane spane = new StackPane();
		spane.getChildren().add(label);
		Scene scene = new Scene(spane, 400, 100);
		stage.setScene(scene);
		stage.show();

	}
}
