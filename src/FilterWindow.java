import java.util.List;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

//Class that creates a new window for editing an item's filters
public class FilterWindow {
	private static String[] filters;
	private static String newFilter = "";
	private static ListView lview = new ListView();

	/**
	 * Displays the filter window, creates buttons
	 * @param item: the item we want to change the filters of
	 * @return returns a string array of the items new filters
	 */
	public static String[] display(Item2 item) {
		int ROW_HEIGHT = 24;
		Stage window = new Stage();
		filters = item.getFilters().clone();
		// filters[0] = "WOW";
		// filters[1] = "Test";

		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Filter - " + item.getName());
		window.setMinWidth(250);

		HBox hbox = new HBox();

		Label label = new Label();
		label.setText("Items with these words will be filtered out "
				+ "of ebay searches.");

		TextField nameInput = new TextField();
		nameInput.setPromptText("Name");
		nameInput.setMinWidth(100);

		Button addButton = new Button("Add");
		addButton.setOnAction(e -> {
			//newFilter = trim(nameInput.getText());
			newFilter = nameInput.getText();
			if (!newFilter.equals("") && !newFilter.equals(" "))
				lview.getItems().add(newFilter);
			nameInput.clear();
			refreshArray();
		});

		Button deleteButton = new Button("Delete");
		deleteButton.setOnAction(e -> deleteButtonClicked());
		
		Button clearButton = new Button("Clear");
		clearButton.setOnAction(e -> {
			lview.getItems().clear();
			refreshArray();
			nameInput.clear();			
		});

		hbox.getChildren().addAll(nameInput, addButton, deleteButton, clearButton);
		hbox.setPadding(new Insets(10, 10, 10, 10));
		hbox.setSpacing(20);

		lview.getItems().clear();

		if (filters != null) {
			load();
		}

		lview.setPrefHeight(ROW_HEIGHT * 5);

		VBox layout = new VBox(10);
		layout.getChildren().addAll(label, lview, hbox);
		layout.setPadding(new Insets(10, 10, 10, 10));

		layout.setAlignment(Pos.CENTER);

		Scene scene = new Scene(layout);
		window.setScene(scene);
		window.showAndWait();
		window.setOnCloseRequest(e -> {
			lview.getItems().clear();
		});
		
		return checkLength();
	}
	
	/**
	 * helper method, see's how long filter is 
	 * mostly for safe returning
	 * @return the filter array or an array with an empty value
	 */
	public static String[] checkLength()
	{
		if (filters.length < 1)
			return new String[1];
		return filters;
	}

	/**
	 * fills the listview based off the filters array
	 */
	public static void load() {

		for (String filter : filters) {
			if (filter != null) {
				lview.getItems().add(filter);
			}
		}

	}
/**
	Makes the filters array equal to what the listview is
	called after every listview change
	*/
	public static void refreshArray() {
		List<String> items = lview.getItems();
		filters = new String[items.size()];
		for (int i = 0; i < filters.length; i++) {
			filters[i] = items.get(i);
		}
	}
/**
	 Shortens the string down to one word if it needs to
	 Used for testing, isn't used anymore
	 @param newFilter: the string being shortened 
	 */
	public static String trim(String newFilter) {
		String[] temp = newFilter.split(" ");
		// System.out.println(temp[0]);
		return temp[0];
	}


	/**
	 * helper method for the delete button
	 * removes the item from the list view and calls
	 * refresh array
	 */
	public static void deleteButtonClicked() {
		ObservableList<String> itemSelected, allItems;
		allItems = lview.getItems();
		itemSelected = lview.getSelectionModel().getSelectedItems();
		itemSelected.forEach(allItems::remove);
		refreshArray();
	}
}