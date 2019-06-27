import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SaveWindow {
	String message;
	Label label;
	boolean exit;
	Stage stage;
	boolean saveAndClose;

	public SaveWindow() {
		message = "Message not set";
		exit = false;
		stage = new Stage();
		saveAndClose = false;
	}

	public SaveWindow(String message) {
		this.message = message;
		label = new Label(message);
		exit = false;
		stage = new Stage();
		saveAndClose = false;
	}

	public void displayError() {
		stage.setTitle("Not Saved");
		
		
		Button yesButton = new Button("Yes");
		yesButton.setOnAction(e -> yesPressed());
		yesButton.setLayoutX(125);
		yesButton.setLayoutY(200);
		
		Button noButton = new Button("No");
		noButton.setOnAction(e -> noPressed());
		noButton.setLayoutX(50);
		noButton.setLayoutY(200);
		
		Button saveAndCloseButton = new Button("Save and Close");
		saveAndCloseButton.setOnAction(e -> saveAndCloseButtonPressed());
		saveAndCloseButton.setLayoutX(50);
		saveAndCloseButton.setLayoutY(200);
		
		
		HBox hBox = new HBox();
		hBox.setPadding(new Insets(30, 50, 10, 50));
		hBox.setSpacing(10);
		hBox.getChildren().addAll(yesButton,noButton, saveAndCloseButton);
		
		VBox vbox = new VBox();
		vbox.setPadding(new Insets (30, 50, 10, 50));
		vbox.getChildren().addAll(label,hBox);
		
		StackPane spane = new StackPane();
		spane.getChildren().addAll(vbox);
		Scene scene = new Scene(spane);
		
		
		stage.setScene(scene);
		stage.show();

	}
	
	public void yesPressed() {
		exit = true;
		stage.close();
		saveAndClose = false;
	}
	
	public void noPressed() {
		exit = false;
		stage.close();
		saveAndClose = false;
	}
	
	public void saveAndCloseButtonPressed() {
		saveAndClose = true;
		stage.close();
		exit = true;
	}
	
	public boolean getExit() {
		return exit;
	}
	
	public boolean getSaveAndClose() {
		return saveAndClose;
	}
}