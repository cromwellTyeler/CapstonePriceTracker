import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.datatransfer.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main2 extends Application {

	private Stage window;
	private BorderPane menu;
	private TableView<Item2> table;
	private TextField nameInput, priceInput, urlInput;
	private Item2[] itemArr;
	private ArrayList<Item2> itemList;
	private Scene scene;
	private VBox vBox;
	private eBay ebay;
	private PriceChart chart;
	private ErrorWindow error;
	private Json json;
	private HBox hBox;
	private Menu fileMenu, optionsMenu, editMenu;
	private FileChooser fileChooser;
	private File file;
	private String path, parent, name;
	private boolean saved, autosave, sameDate;
	long start, stop;
	public static void main(String[] args) {
		launch(args);
	}

	/*
	 * Starts and displays the Gui, initializes the file if there was a recently
	 * opened one and creates the GUI layout
	 * 
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		// itemArr = csv.getArr();
		saved = true;
		autosave = false;
		sameDate = false;

		json = new Json();
		json.readParentPath();
		path = json.getPath();
		autosave = json.getAutoSave();
		sameDate = json.getSameDateSave();
		parent = json.getParent();
		name = json.getName();

		if (name.isEmpty())
			name = "(unsaved file)";

		File tempFile = new File(path);
		if (!path.isEmpty() && tempFile.exists()) {
			json.readItem(path);
			name = json.getName();
		} else {
			path = "";
		}

		itemList = new ArrayList<Item2>(json.getItems());

		window = primaryStage;
		window.setTitle("eBay Price Scraper - " + name);

		setUpTable();
		setUpHbox(primaryStage);
		setUpMenu(primaryStage);

		MenuBar menuBar = new MenuBar();
		menuBar.getMenus().addAll(fileMenu, optionsMenu, editMenu);

		menu = new BorderPane();
		menu.setTop(menuBar);

		fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("JSON fies", "*.json"));

		vBox = new VBox();
		vBox.getChildren().addAll(menu, table, hBox);

		scene = new Scene(vBox);

		window.setScene(scene);
		window.show();

		primaryStage.setOnCloseRequest(e -> {
			e.consume();
			closeProgram(primaryStage);
		});

	}

	/**
	 * Handles the window close if they have saved it closes fine if not it displays
	 * a close without saving window
	 * 
	 * @param primaryStage
	 *            : the stage from the main GUI being displayed
	 */
	public void closeProgram(Stage primaryStage) {
		if (saved) {
			Platform.exit();
		} else {
			int answer = ConfirmBox.display(3, "Close Window", "Are you sure you want to close without saving?");
			if (answer == 2) {
				saveFile(primaryStage);
				Platform.exit();
			} else if (answer == 1)
				Platform.exit();
		}
	}

	/**
	 * Helper method for start Loads all of the items in the itemsList into an
	 * observable list
	 * 
	 * @return items: the new converted observable list
	 */
	public ObservableList<Item2> getItem() {
		ObservableList<Item2> items = FXCollections.observableArrayList();
		int i = 0;
		while (i < itemList.size()) {
			items.add(itemList.get(i));
			i++;
		}

		return items;
	}

	/**
	 * short cut function to reload the gui columns and set all the inputs to false
	 */
	public void reloadGui() {
		table.getColumns().get(0).setVisible(false);
		table.getColumns().get(0).setVisible(true);
		nameInput.clear();
		priceInput.clear();
		urlInput.clear();
	}

	/**
	 * Helper function to get the selected Item
	 * 
	 * @return Item2 item: selected item
	 */
	public Item2 getSelectedItem() {
		return table.getSelectionModel().getSelectedItems().get(0);
	}

	/**
	 * BUTTON METHODS START HERE
	 */

	/**
	 * Handles the addButton being clicked creates a new Item Adds a new item to the
	 * table view and the itemsList
	 * 
	 * @param primaryStage
	 *            : main stage of GUI, used for saving
	 */
	public void addButtonClicked(Stage primaryStage) {
		String name = nameInput.getText();

		if (name != null && !name.isEmpty()) {
			Item2 item = new Item2();
			item.setName(nameInput.getText());
			item.setMinPrice(0);
			item.setUrl("Please press the eBay button to get started :)");
			item.setUpdate("NA");
			table.getItems().add(item);

			itemList.add(item);
			reloadGui();

			saved = false;
			if (autosave) {
				saveFile(primaryStage);
			}
		} else {
			error = new ErrorWindow("Please fill in the name textbox\nbefore using the Update button");
			error.displayError();
		}
	}

	/**
	 * Handles the deleteButton being clicked Removes an item from the tableview and
	 * the ItemsList
	 * 
	 * @param primaryStage
	 *            : main stage of GUI, used for saving
	 */

	public void deleteButtonClicked(Stage primaryStage) {
		ObservableList<Item2> itemSelected, allItems;
		allItems = table.getItems();
		itemSelected = table.getSelectionModel().getSelectedItems();

		Item2 item = getSelectedItem();
		if (item != null) {
			removeItem(item);
			itemSelected.forEach(allItems::remove);
			saved = false;

			if (autosave) {
				saveFile(primaryStage);
			}
		} else {
			error = new ErrorWindow("Select a row before using the Update button");
			error.displayError();
		}

	}

	/**
	 * Helper method for the deleteButton Goes through the item List and removes the
	 * selected item from it
	 * 
	 * @param item
	 *            : the selected item from the table view
	 */
	public void removeItem(Item2 item) {
		Item2 temp;
		for (int i = 0; i < itemList.size(); i++) {
			temp = itemList.get(i);
			if (temp.getName().equals(item.getName()) && temp.getMinPrice() == item.getMinPrice())
				itemList.remove(i);
		}

	}

	/**
	 * Handles the ebayButton being clicked calls the ebay class and provides it
	 * with the item name it then searches for items with the given name and returns
	 * all the price information
	 * 
	 * @param primaryStage
	 *            : main Stage from GUI, needed for saving
	 */
	public void eBayButtonClicked(Stage primaryStage) {
		double newPrice;
		double newMaxPrice;
		double newAvgPrice;
		Item2 item = getSelectedItem();
		if (item != null) {

			String curDate = new Date().toString().substring(0, 10);

			ebay = new eBay(item);
			ebay.find();

			newPrice = ebay.getPrice();

			newMaxPrice = ebay.getMaxPrice();
			newAvgPrice = ebay.getAvgPrice();

			if (!item.sameDate() || sameDate) {
				item.setUrl(ebay.getUrlString());
				item.setUpdate(curDate);
				item.setMinPrice(newPrice);
				item.setMaxPrice(newMaxPrice);
				item.setAvgPrice(newAvgPrice);
			} else {
				item.replaceAvgPrice(newAvgPrice);
				item.replaceMaxPrice(newMaxPrice);
				item.replaceMinPrice(newPrice);
				item.setUrl(ebay.getUrlString());
				item.setUpdate(curDate);
			}

			reloadGui();

			saved = false;
			if (autosave) {
				saveFile(primaryStage);
			}
		} else {
			error = new ErrorWindow("Select a row before using the eBay button");
			error.displayError();
		}
	}

	/**
	 * Method isn't being used now, used to handle the update button being pressed
	 * may be used later so i'm keeping it in here
	 * 
	 * @param primaryStage
	 */
	public void updateButtonClicked(Stage primaryStage) {
		ObservableList<Item2> itemSelected;
		itemSelected = table.getSelectionModel().getSelectedItems();
		String stringPrice = priceInput.getText();

		if (!itemSelected.isEmpty() && !stringPrice.isEmpty()) {
			double newPrice = Double.parseDouble(priceInput.getText());
			Item2 item = itemSelected.get(0);

			updateItemArr(newPrice, newPrice, newPrice, item);

			reloadGui();

			saved = false;

			if (autosave) {
				saveFile(primaryStage);
			}
		} else {
			error = new ErrorWindow("Select a row and type in a price \n before using the Update button");
			error.displayError();
		}

	}

	/**
	 * Handles the copyURL Button being clicked copies the url of the lowest price
	 * eBay item to clipboard
	 */
	public void copyUrlButtonClicked() {
		Item2 item = getSelectedItem();

		if (item != null) {
			Toolkit toolkit = Toolkit.getDefaultToolkit();
			Clipboard clipboard = toolkit.getSystemClipboard();
			StringSelection strSel = new StringSelection(item.getUrl());
			clipboard.setContents(strSel, null);

		} else {
			error = new ErrorWindow("Select a row before using the Copy URL button");
			error.displayError();
		}
	}

	/**
	 * Handles the chartButton being clicked creates a new PriceChart object and
	 * displays its price array
	 */
	public void chartButtonClicked() {
		Item2 item = getSelectedItem();
		if (item != null) {
			if (item.getMinPrice() == 0) {
				error = new ErrorWindow("Please press the eBay button before charting");
				error.displayError();
			} else {
				chart = new PriceChart(item);
				chart.display();
			}
		} else {
			error = new ErrorWindow("Select a row before using the Graph Prices button");
			error.displayError();
		}
	}

	/**
	 * Helper method, updates the item in the itemsList
	 * 
	 * @param minPrice
	 *            the new minPrice
	 * @param avgPrice
	 *            the new avgPrice
	 * @param maxPrice
	 *            the new maxPrice
	 * @param item
	 *            the item being updated
	 */
	public void updateItemArr(double minPrice, double avgPrice, double maxPrice, Item2 item) {
		for (int i = 0; i < itemList.size(); i++) {
			if (item.getName().equals(itemList.get(i).getName())) {
				itemList.get(i).setMinPrice(minPrice);
				itemList.get(i).setAvgPrice(avgPrice);
				itemList.get(i).setMaxPrice(maxPrice);
			}
		}

		table.setItems(getItem());
	}

	/**
	 * Handles the save menu Item being selected calls SaveAs if the file hasn't
	 * been saved yet
	 * 
	 * @param primaryStage:
	 *            main stage of GUI, used for saving
	 */
	public void saveFile(Stage primaryStage) {
		if (path.isEmpty()) {
			saveAsFile(primaryStage);
		} else {
			if (!itemList.isEmpty()) {
				json.writeItems(itemList);
				saved = true;

				json.setName(name);
				json.setParent(parent);
				json.setPath(path);
				json.setAutoSave(autosave);
				json.setSameDateSave(sameDate);
				json.writeParentPath();

			} else {
				error = new ErrorWindow("You can't save an empty file");
				error.displayError();
			}
		}
	}

	/**
	 * Handles the saveAS menu Item being selected opens up a saveDialog browser and
	 * takes the parent and path names
	 * 
	 * @param primaryStage:
	 *            main stage of GUI, used for saving
	 */
	public void saveAsFile(Stage primaryStage) {
		//System.out.println("SAVE AS");
		if (!parent.isEmpty())
			fileChooser.setInitialDirectory(new File(parent));
		file = fileChooser.showSaveDialog(primaryStage);

		if (file != null) {
			String parentTemp = file.getParent();
			String name = file.getName();
			String pathTemp = file.getPath();

			path = pathTemp;
			parent = parentTemp;

			fileChooser.setInitialDirectory(new File(parentTemp));

			json.setPath(pathTemp);
			json.writeItems(itemList);
			json.setName(name);
			json.setParent(parentTemp);
			json.writeParentPath();

			vBox = new VBox();
			vBox.getChildren().addAll(menu, table, hBox);

			scene = new Scene(vBox);
			window.setScene(scene);
			window.setTitle("eBay Price Scraper - " + name);
			window.show();

			saved = true;
		}
	}

	/**
	 * Handles the openFile Item being selected calls the JSON class to help process
	 * through and calls reload GUI
	 * 
	 * @param primaryStage:
	 *            main stage of GUI, used for saving
	 */
	public void openFile(Stage primaryStage) {
		
		if (!parent.isEmpty())
			fileChooser.setInitialDirectory(new File(parent));
		file = fileChooser.showOpenDialog(primaryStage);
		if (file != null) {
			String parentTemp = file.getParent();
			path = file.getPath();

			fileChooser.setInitialDirectory(new File(parentTemp));
			json = new Json(path);

			String name = file.getName();
			json.setName(name);
			json.setParent(parentTemp);
			json.writeParentPath();

			itemList = new ArrayList<Item2>(json.getItems());

			reloadGui();
			setUpTable();
			vBox = new VBox();
			vBox.getChildren().addAll(menu, table, hBox);

			scene = new Scene(vBox);
			window.setScene(scene);
			window.setTitle("eBay Price Scraper - " + name);
			window.show();

		}
	}

	/**
	 * Handles the newFile menu Item being clicked Creates a new file and resets all
	 * information
	 */
	public void newFile() {
		itemList.clear();
		path = "";

		reloadGui();
		setUpTable();
		vBox = new VBox();
		vBox.getChildren().addAll(menu, table, hBox);

		scene = new Scene(vBox);
		window.setScene(scene);
		window.setTitle("eBay Price Scraper - " + "(new file)");
		window.show();

	}

	/**
	 * Handles the editName menu item being selected Opens up a dialog window and
	 * takes in the users information Renames the selected Item
	 * 
	 * @param primaryStage
	 */
	public void editNamePressed(Stage primaryStage) {
		Item2 item = getSelectedItem();
		if (item != null) {
			String newName = DialogBox.display("What woud you like to change the name \n of " + item.getName() + " to?",
					"Edit name");

			if (!newName.isEmpty())
				item.setName(newName);

			reloadGui();

			saved = false;

			if (autosave) {
				saveFile(primaryStage);
			}

		} else {
			error = new ErrorWindow("Select a row before editing an item name.");
			error.displayError();
		}

	}

	/**
	 * Handles the clearItem menu item being pressed Resets all the item's
	 * information except the name
	 * 
	 * @param primaryStage:
	 *            main stage of GUI, used for saving
	 */
	public void clearItemPressed(Stage primaryStage) {
		Item2 item = getSelectedItem();
		if (item != null) {
			int clear = ConfirmBox.display(2, "clear - " + item.getName(),
					"Are you sure you want to clear this item?");
			if (clear == 1) {
				item.clear();
				reloadGui();

				saved = false;
				if (autosave) {
					saveFile(primaryStage);
				}
			}
		} else {
			error = new ErrorWindow("Select a row before using clear item");
			error.displayError();
		}
	}

	/**
	 * Handles the button press of manage filters opens the filters window
	 * 
	 * @param primaryStage
	 *            : main stage of Gui, needed for saving
	 */
	public void manageFilters(Stage primaryStage) {
		Item2 item = getSelectedItem();
		if (item != null) {

			String[] filters = FilterWindow.display(item);

			item.setFilters(filters);

			saved = false;
			if (autosave) {
				saveFile(primaryStage);
			}
		} else {
			error = new ErrorWindow("Select a row before managing filtes.");
			error.displayError();
		}
	}

	/**
	 * BUTTON METHODS END HERE
	 */

	/**
	 * GUI SET UP METHODS START HERE
	 */

	/**
	 * Helper method for the start method sets up the tableview of the GUI
	 */
	@SuppressWarnings("unchecked")
	public void setUpTable() {
		TableColumn<Item2, String> nameColumn = new TableColumn<>("Name");
		nameColumn.setMinWidth(300);
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

		TableColumn<Item2, Double> minPriceColumn = new TableColumn<>("Min");
		minPriceColumn.setMinWidth(60);
		minPriceColumn.setCellValueFactory(new PropertyValueFactory<>("minPrice"));

		TableColumn<Item2, Double> avgPriceColumn = new TableColumn<>("Avg");
		avgPriceColumn.setMinWidth(60);
		avgPriceColumn.setCellValueFactory(new PropertyValueFactory<>("avgPrice"));

		TableColumn<Item2, Double> maxPriceColumn = new TableColumn<>("Max");
		maxPriceColumn.setMinWidth(60);
		maxPriceColumn.setCellValueFactory(new PropertyValueFactory<>("maxPrice"));

		TableColumn<Item2, String> urlColumn = new TableColumn<>("URL");
		urlColumn.setMinWidth(400);
		urlColumn.setCellValueFactory(new PropertyValueFactory<>("url"));

		TableColumn<Item2, String> updateColumn = new TableColumn<>("Last Update");
		updateColumn.setMinWidth(100);
		updateColumn.setCellValueFactory(new PropertyValueFactory<>("update"));

		table = new TableView<>();
		table.setItems(getItem());
		table.getColumns().addAll(nameColumn, minPriceColumn, avgPriceColumn, maxPriceColumn, urlColumn, updateColumn);

		table.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent click) {
				if (click.getClickCount() >= 2) {
					Item2 item = table.getSelectionModel().getSelectedItems().get(0);
				}

			}

		});
	}

	/**
	 * Helper method for the start method sets up the HBox, the buttons and all of
	 * the button presses
	 */
	public void setUpHbox(Stage primaryStage) {
		// Name Input
		nameInput = new TextField();
		nameInput.setPromptText("Name");
		nameInput.setMinWidth(100);

		// price Input
		priceInput = new TextField();
		priceInput.setPromptText("Price");
		priceInput.setMinWidth(100);

		// url Input
		urlInput = new TextField();
		urlInput.setPromptText("URL");
		urlInput.setMinWidth(100);

		// Button
		Button addButton = new Button("Add");
		addButton.setOnAction(e -> addButtonClicked(primaryStage));

		Button deleteButton = new Button("Delete");
		deleteButton.setOnAction(e -> deleteButtonClicked(primaryStage));

		Button updateButton = new Button("Update");
		updateButton.setOnAction(e -> updateButtonClicked(primaryStage));

		Button eBayButton = new Button("eBay");
		eBayButton.setOnAction(e -> eBayButtonClicked(primaryStage));

		Button copyUrlButton = new Button("Copy URL");
		copyUrlButton.setOnAction(e -> copyUrlButtonClicked());

		Button chartButton = new Button("Graph Prices");
		chartButton.setOnAction(e -> chartButtonClicked());

		hBox = new HBox();
		hBox.setPadding(new Insets(10, 10, 10, 10));
		hBox.setSpacing(20);
		hBox.getChildren().addAll(nameInput, addButton, deleteButton, eBayButton, copyUrlButton, chartButton);

	}

	/**
	 * Sets up the menu of the main GUI creates all of the menu buttons/options
	 * 
	 * @param primaryStage
	 *            : the stage from the primary GUI mostly needed for save dialogs
	 *            and such
	 */
	public void setUpMenu(Stage primaryStage) {
		fileMenu = new Menu("_File");

		MenuItem newFile = new MenuItem("New...");
		newFile.setOnAction(e -> newFile());

		MenuItem saveFile = new MenuItem("Save...");
		saveFile.setOnAction(e -> saveFile(primaryStage));

		MenuItem saveAsFile = new MenuItem("Save As...");
		saveAsFile.setOnAction(e -> saveAsFile(primaryStage));

		MenuItem openFile = new MenuItem("Open...");
		openFile.setOnAction(e -> openFile(primaryStage));

		fileMenu.getItems().add(newFile);
		fileMenu.getItems().add(openFile);
		fileMenu.getItems().add(saveFile);
		fileMenu.getItems().add(saveAsFile);

		optionsMenu = new Menu("_Options");

		CheckMenuItem autoSave = new CheckMenuItem("Autosave");
		if (autosave)
			autoSave.setSelected(true);

		autoSave.setOnAction(e -> {
			if (autoSave.isSelected())
				autosave = true;
			else
				autosave = false;
		});

		CheckMenuItem sameDateSave = new CheckMenuItem("Save Same Date Data");
		if (sameDate)
			sameDateSave.setSelected(true);

		sameDateSave.setOnAction(e -> {
			if (sameDateSave.isSelected())
				sameDate = true;
			else
				sameDate = false;
		});

		optionsMenu.getItems().addAll(autoSave, sameDateSave);

		editMenu = new Menu("_Edit");
		MenuItem changeName = new MenuItem("Edit Name");
		changeName.setOnAction(e -> editNamePressed(primaryStage));

		MenuItem clearItem = new MenuItem("Clear Item");
		clearItem.setOnAction(e -> clearItemPressed(primaryStage));

		MenuItem manageFilter = new MenuItem("Manage Filters");
		manageFilter.setOnAction(e -> manageFilters(primaryStage));

		editMenu.getItems().addAll(changeName, clearItem, manageFilter);
	}

}
