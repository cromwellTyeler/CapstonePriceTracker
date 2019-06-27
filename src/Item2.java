import java.util.ArrayList;
import java.util.Date;

//Class that stores Items
public class Item2 {

	private String name;
	private String url;
	private double minPrice;
	private double avgPrice;
	private double maxPrice;
	private String update;
	private double[] minPrices;
	private double[] avgPrices;
	private double[] maxPrices;
	private String[] filters;

	/**
	 * Default constructor makes everything to empty Strings, 0 or empty arrays
	 */
	public Item2() {
		this.name = "";
		this.url = "";
		this.minPrice = 0;
		minPrices = new double[10];
		this.update = new Date().toString().substring(0, 10);
		avgPrices = new double[10];
		maxPrices = new double[10];
		filters = new String[10];
	}

	/**
	 * Constructor that takes in a minPrice array, name, url and, update Isn't used
	 * all that much anymore
	 * 
	 * @param name
	 *            : name of item
	 * @param url
	 *            : url to find item
	 * @param minPrice
	 *            : new min price
	 * @param update:
	 *            new update time
	 */
	public Item2(String name, String url, double minPrice, String update) {
		this.name = name;
		this.url = url;
		this.minPrice = minPrice;
		this.update = update.substring(0, 10);
		minPrices = new double[10];
		avgPrices = new double[10];
		maxPrices = new double[10];
		avgPrices[0] = minPrice;
		minPrices[0] = minPrice;
		maxPrices[0] = minPrice;
		filters = new String[10];
	}

	/**
	 * Constructor that takes in name, url, minPrice, update, and minPrices
	 * Currently being used. I just don't want to make a constructor thats stupid
	 * long
	 * 
	 * @param name
	 * @param url
	 * @param minPrice
	 * @param update
	 * @param minPrices
	 */
	public Item2(String name, String url, double minPrice, String update, double[] minPrices) {
		this.name = name;
		this.url = url;
		this.minPrice = minPrice;
		this.update = update;
		this.minPrices = minPrices.clone();
		avgPrices = new double[10];
		maxPrices = new double[10];
		avgPrices[0] = minPrice;
		maxPrices[0] = minPrice;
		filters = new String[10];
	}

	/**
	 * Getter method for name
	 * 
	 * @return name: name of item
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter method for name
	 * 
	 * @param name
	 *            : the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Getter method for avgPrice
	 * 
	 * @return avgPrice: avgPrice of item
	 */
	public double getAvgPrice() {
		return avgPrice;
	}

	/**
	 * Setter method for avgPrice calls setAvgPrices as well
	 * 
	 * @param avgPrice
	 *            : the new avgPrice
	 */
	public void setAvgPrice(double avgPrice) {
		setAvgPrices(addPrice(avgPrices, avgPrice));
		avgPrices[0] = avgPrice;
		this.avgPrice = avgPrice;
	}

	/**
	 * Another setter for avgPrice Just changes the the first thing in the array,
	 * doesn't push everything back called if the eBay button is pressed more than
	 * once on the same day
	 * 
	 * @param avgPrice
	 *            : the new avgPrice
	 */
	public void replaceAvgPrice(double avgPrice) {
		if (avgPrices.length == 0)
			avgPrices = new double[10];
		avgPrices[0] = avgPrice;
		this.avgPrice = avgPrice;

	}

	/**
	 * Getter method for maxPrice
	 * 
	 * @return maxPrice: maxPrice of item
	 */
	public double getMaxPrice() {
		return maxPrice;
	}

	/**
	 * Setter method for maxPrice calls setMaxPrices as well
	 * 
	 * @param maxPrice
	 *            : the new maxPrice
	 */
	public void setMaxPrice(double maxPrice) {
		setMaxPrices(addPrice(maxPrices, maxPrice));
		maxPrices[0] = maxPrice;
		this.maxPrice = maxPrice;
	}

	/**
	 * Another setter for maxPrice Just changes the the first thing in the array,
	 * doesn't push everything back called if the eBay button is pressed more than
	 * once on the same day
	 * 
	 * @param maxPrice
	 *            : the new maxPrice
	 */
	public void replaceMaxPrice(double maxPrice) {
		if (maxPrices.length == 0)
			maxPrices = new double[10];
		maxPrices[0] = maxPrice;
		this.maxPrice = maxPrice;
	}

	/**
	 * Getter method for minPrice
	 * 
	 * @return minPrice: minPrice of item
	 */
	public double getMinPrice() {
		return minPrice;
	}

	/**
	 * Setter method for minPrice calls setMinPrices as well
	 * 
	 * @param minPrice
	 *            : the new minPrice
	 */
	public void setMinPrice(double minPrice) {
		setMinPrices(addPrice(minPrices, minPrice));
		minPrices[0] = minPrice;
		this.minPrice = minPrice;
	}

	/**
	 * Another setter for minPrice Just changes the the first thing in the array,
	 * doesn't push everything back called if the eBay button is pressed more than
	 * once on the same day
	 * 
	 * @param minPrice
	 *            : the new minPrice
	 */
	public boolean replaceMinPrice(double minPrice) {
		if (minPrices.length == 0)
			minPrices = new double [10];
		minPrices[0] = minPrice;
		this.minPrice = minPrice;
		return true;
	}

	/**
	 * Getter method for url
	 * 
	 * @return url: url of item
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Setter for url
	 * 
	 * @param url
	 *            : the new url
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * Getter method for update
	 * 
	 * @return update: update of item
	 */
	public String getUpdate() {
		return update;
	}

	/**
	 * setter for update
	 * 
	 * @param update
	 *            : the new update
	 */
	public void setUpdate(String update) {
		this.update = update;
	}

	/**
	 * setter for MinPrices
	 * 
	 * @param prices
	 *            : the new minPrices
	 */
	public void setMinPrices(double[] prices) {
		this.minPrices = prices.clone();
	}

	/**
	 * Getter method for minPrices
	 * 
	 * @return minPrices: minPrices of item
	 */
	public double[] getMinPrices() {
		return minPrices;
	}

	/**
	 * Setter for avgPrices
	 * 
	 * @param avgPrices:
	 *            the new avgPrices
	 */
	public void setAvgPrices(double[] avgPrices) {
		this.avgPrices = avgPrices.clone();
	}

	/**
	 * Getter method for avgPrices
	 * 
	 * @return avgPrices: avgPrices of item
	 */
	public double[] getAvgPrices() {
		return avgPrices;
	}

	/**
	 * Setter for maxPrices
	 * 
	 * @param maxPrices
	 *            : the new maxPrices
	 */
	public void setMaxPrices(double[] maxPrices) {
		this.maxPrices = maxPrices.clone();
	}

	/**
	 * Getter method for name
	 * 
	 * @return name: name of item
	 */
	public double[] getMaxPrices() {
		return maxPrices;
	}

	/**
	 * Setter for filters
	 * 
	 * @param filters
	 *            : the new filters
	 */
	public void setFilters(String[] filters) {
		this.filters = filters.clone();
	}

	/**
	 * Getter method for name
	 * 
	 * @return name: name of item
	 */
	public String[] getFilters() {
		return filters;
	}

	/**
	 * Helper method to add a new Price to an array in the right order Does
	 * different things if the array is empty, if its full, or not full
	 * 
	 * @param arr
	 *            : array being changed
	 * @param newPrice
	 *            : the new price
	 * @return the new and reordered array
	 */
	public double[] addPrice(double[] arr, double newPrice) {
		if (arr.length == 0) {
			arr = new double[10];
			arr[0] = newPrice;
			return arr;
		} else if (arr[arr.length - 1] == 0) {
			for (int i = arr.length - 1; i > 0; i--) {
				arr[i] = arr[i - 1];
			}
			return arr;
		} else {
			double[] temp = new double[(arr.length * 2)];
			for (int i = 0; i < arr.length; i++)
				temp[i + 1] = arr[i];
			return temp;
		}
	}

	/**
	 * Checks if the current date is the same as update
	 * 
	 * @return returns true if it is, false if it isn't
	 */
	public boolean sameDate() {
		String curDate = new Date().toString().substring(0, 10);
		return update.equals(curDate);
	}

	/**
	 * Clears all the information in this item object Called by the clear Item menu
	 * object in main
	 */
	public void clear() {
		this.url = "Please press the eBay button to get started :)";
		this.minPrice = 0;
		this.avgPrice = 0;
		this.maxPrice = 0;
		minPrices = new double[10];
		this.update = new Date().toString().substring(0, 10);
		avgPrices = new double[10];
		maxPrices = new double[10];
		filters = new String[10];
	}

	/**
	 * Prints all the prices in the minPrices array used for testing
	 */
	public void printMinPrices() {
		for (int i = 0; i < minPrices.length; i++)
			System.out.println(minPrices[i] + " ");
	}

	/**
	 * Prints all the prices in the avgPrices array used for testing
	 */
	public void printAvgPrices() {
		for (int i = 0; i < avgPrices.length; i++)
			System.out.println(avgPrices[i] + " ");
	}

	/**
	 * Prints all the prices in the maxPrices array used for testing
	 */
	public void printMaxPrices() {
		for (int i = 0; i < maxPrices.length; i++)
			System.out.println(maxPrices[i] + " ");
	}

	/**
	 * Prints all the prices in the filters array used for testing
	 */
	public void printFilters() {
		for (int i = 0; i < filters.length; i++)
			System.out.println(filters[i] + " ITEMS");
	}
}
