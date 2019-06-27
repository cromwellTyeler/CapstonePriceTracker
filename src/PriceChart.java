import java.util.ArrayList;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * creates a new window for the price chart
 * @author owner
 *
 */
public class PriceChart {
	private double[] minPrices, avgPrices, maxPrices;
	private String iName;
	private ArrayList<Double> priceList;
	boolean setUp;
	CheckBox minBox, avgBox, maxBox;
	Stage stage;

	/**
	 * default constructor, initializes everything
	 */
	public PriceChart() {
		minPrices = new double[10];
		avgPrices = new double[10];
		maxPrices = new double[10];
		iName = "Temp";
		priceList = new ArrayList<>();
		setUp = true;
		stage = new Stage();
		minBox = new CheckBox("Min");
		avgBox = new CheckBox("Avg");
		maxBox = new CheckBox("Max");
		minBox.setSelected(true);
		avgBox.setSelected(true);
		maxBox.setSelected(true);
	}

	/**
	 * Constructor that takes in an item and initializes fields based 
	 * off it 
	 * @param item : the item being graphed
	 */
	public PriceChart(Item2 item) {
		iName = item.getName();
		minPrices = item.getMinPrices().clone();
		avgPrices = item.getAvgPrices().clone();
		maxPrices = item.getMaxPrices().clone();
		priceList = new ArrayList<>();
		setUp = true;
		stage = new Stage();
		minBox = new CheckBox("Min");
		avgBox = new CheckBox("Avg");
		maxBox = new CheckBox("Max");
		minBox.setSelected(true);
		avgBox.setSelected(true);
		maxBox.setSelected(true);
		// arrToList();
	}

	public void arrToList() {
		for (int i = 0; i < minPrices.length; i++) {
			if (minPrices[i] != 0)
				priceList.add(minPrices[i]);
		}

	}

	/**
	 * Generic method to convert an array to an ArrayList
	 * used to load things into the price chart 
	 * @param arr: array being converted
	 * @return temp : the conveted arrayList
	 */
	public ArrayList<Double> arrToList(double[] arr) {
		ArrayList<Double> temp = new ArrayList<>();
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] != 0)
				temp.add(arr[i]);
		}
		return temp;

	}

	/**
	 * Method that displays the chart
	 * Loads all the buttons and graphs
	 */
	public void display() {
		stage.setTitle("Lowest Prices of " + iName);

		HBox hbox = new HBox();

		Button reloadButton = new Button("Reload");
		reloadButton.setOnAction(e -> {
			stage.close();
			display();
		});

		hbox.getChildren().addAll(minBox, avgBox, maxBox, reloadButton);
		hbox.setPadding(new Insets(10, 10, 10, 10));
		hbox.setSpacing(10);
		hbox.setAlignment(Pos.CENTER);

		final NumberAxis xAxis = new NumberAxis();
		final NumberAxis yAxis = new NumberAxis();

		xAxis.setLabel("Days");
		final LineChart<Number, Number> lineChart = new LineChart<Number, Number>(xAxis, yAxis);

		lineChart.setTitle("Prices of " + iName);
		
		if (minBox.isSelected()) {
			XYChart.Series minSeries = new XYChart.Series();
			minSeries = setupMinSeries();
			minSeries.setName("Minimum Price");
			lineChart.getData().add(minSeries);
		}
		
		if (avgBox.isSelected()) {
			XYChart.Series avgSeries = new XYChart.Series();
			avgSeries = setupAvgSeries();
			avgSeries.setName("Average Price");
			lineChart.getData().add(avgSeries);
		}
		
		if (maxBox.isSelected()) {
			XYChart.Series maxSeries = new XYChart.Series();
			maxSeries = setupMaxSeries();
			maxSeries.setName("Maximum Price");
			lineChart.getData().add(maxSeries);
		}


		VBox layout = new VBox(10);
		layout.getChildren().addAll(lineChart, hbox);
		layout.setAlignment(Pos.CENTER);

		Scene scene = new Scene(layout);

		stage.setScene(scene);
		stage.show();
	}

	/**
	 * Sets up the seires for the minPrices
	 * Goes through avgPrices array in reverse
	 * @return the series for minPrices
	 */
	public Series setupMinSeries() {
		int counter = 0;
		XYChart.Series<Integer, Double> minSeries = new XYChart.Series<>();

		for (counter = minPrices.length - 1; minPrices[counter] == 0; counter--) {
		}
		// System.out.println(counter);

		for (int i = 0; i < minPrices.length && minPrices[i] != 0; i++) {
			minSeries.getData().add(new XYChart.Data(counter, minPrices[i]));
			counter--;
		}
		return minSeries;
	}

	/**
	 * Sets up the seires for the avgPrices
	 * Goes through avgPrices array in reverse
	 * @return the series for avgPrices
	 */
	public Series setupAvgSeries() {
		int counter = 0;
		XYChart.Series<Integer, Double> avgSeries = new XYChart.Series<>();

		//Skips over any trailing zeros if there are any
		for (counter = avgPrices.length - 1; avgPrices[counter] == 0; counter--) {
		}

		for (int i = 0; i < avgPrices.length && avgPrices[i] != 0; i++) {
			avgSeries.getData().add(new XYChart.Data(counter, avgPrices[i]));
			counter--;
		}

		return avgSeries;
	}

	/**
	 * Sets up the series for the maxPrices
	 * Goes through avgPrices array in reverse
	 * @return the series for maxPrices
	 */
	public Series setupMaxSeries() {
		int counter = 0;
		XYChart.Series<Integer, Double> maxSeries = new XYChart.Series<>();

		for (counter = maxPrices.length - 1; maxPrices[counter] == 0; counter--) {
		}

		// System.out.println("avg " + counter);
		for (int i = 0; i < maxPrices.length && maxPrices[i] != 0; i++) {
			maxSeries.getData().add(new XYChart.Data(counter, maxPrices[i]));
			counter--;
		}

		return maxSeries;
	}


	/**
	 * Setter method for iname
	 * @param iName: what we're setting iName to
	 */
	public void setIName(String iName) {
		this.iName = iName;
	}
	
	
/**
 * Getter Iname
 * @return returns iname
 */
	public String getIName() {
		return iName;
	}

	/**
	 * Sets the prices array
	 * calls arr to List
	 * @param prices : the new price array
	 */
	public void setPrices(double[] prices) {
		this.minPrices = prices.clone();
		arrToList();
	}
	
	/**
	 * Getter method for minPrices
	 * @return minPrices
	 */
	public double[] getPrices() {
		return minPrices;
	}

}
