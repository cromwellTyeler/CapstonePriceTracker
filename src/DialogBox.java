import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DialogBox {
	private static String newName;
	
	/**
	 * Displays a window with a given message and title and 
	 * a box for a user to type input into
	 * @param message : message to be displayed
	 * @param title : title of the window 
	 * @return newName: the string they input into the textfield
	 */
	public static String display(String message, String title){
		Stage window = new Stage();
		newName = "";

		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(title);
		window.setMinWidth(250);

		HBox hbox = new HBox();

		Label label = new Label();
		label.setText(message);
		
		TextField nameInput = new TextField();
		nameInput.setPromptText("Name");
		nameInput.setMinWidth(100);
		
		
		Button nameButton = new Button("Edit");
		nameButton.setOnAction(e -> {
			newName = nameInput.getText();
			window.close();
		});
		
		hbox.getChildren().addAll(nameInput, nameButton);
		hbox.setPadding(new Insets(10, 10, 10, 10));
		hbox.setSpacing(20);
		
		
		VBox layout = new VBox(10);
		layout.getChildren().addAll(label, hbox);
		layout.setPadding(new Insets(10, 10, 10, 10));
		
		layout.setAlignment(Pos.CENTER);

		Scene scene = new Scene(layout);
		window.setScene(scene);
		window.showAndWait();
		
		
		

		return newName;
	}

}
