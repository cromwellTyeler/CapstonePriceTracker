import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Json {
	private ArrayList<Item2> items;
	private FileWriter file;
	private String path;
	private String parent;
	private String homePath;
	private String name;
	private boolean autosave;
	private boolean sameDateSave;

	/**
	 * Default Constructor
	 */
	public Json() {
		items = new ArrayList<>();
		homePath = System.getProperty("user.home");
		path = "";
		parent = "";
		name = "";
	}

	/**
	 * Constructor that takes in a path
	 * @param path : the file path for a file
	 */
	public Json(String path) {
		items = new ArrayList<>();
		this.path = path;
		homePath = System.getProperty("user.home");
		readItem(path);
		path = "";
		parent = "";
		name = "";
	}

	/**
	 * Setter for the path variable
	 * @param path : the new path
	 */
	public void setPath(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}

	/**
	 * Setter for the parent variable
	 * @param parent : the new parent
	 */
	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getParent() {
		return parent;
	}

	/**
	 * Setter for the name variable
	 * @param name : the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	/**
	 * Setter for the autosave variable
	 * @param autosave : the new autosave
	 */
	public void setAutoSave(boolean autosave) {
		this.autosave = autosave;
	}
	
	public boolean getAutoSave() {
		return autosave;
	}
	
	/**
	 * Setter for the sameDateSave variable
	 * @param sameDateSave : the new sameDateSave
	 */
	public void setSameDateSave(boolean sameDateSave) {
		this.sameDateSave = sameDateSave;
	}
	
	public boolean getSameDateSave() {
		return sameDateSave;
	}


	/**
	 * Writes the location of the last saved file to memory
	 * includes the path to its parent folder, the filename,
	 * and some user settings
	 */
	@SuppressWarnings("unchecked")
	public void writeParentPath() {
		JSONObject recentPaths = new JSONObject();
		recentPaths.put("path", path);
		recentPaths.put("parent", parent);
		recentPaths.put("name", name);
		recentPaths.put("autosave", autosave);
		recentPaths.put("sameDate", sameDateSave);

		JSONObject parentObject = new JSONObject();
		parentObject.put("paths", recentPaths);

		try (FileWriter file = new FileWriter(homePath + "\\Documents\\" + "recent.json")) {

			file.write(parentObject.toJSONString());
			file.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Reads in the path to the last opened file, its folder, 
	 * its name and some user settings
	 */
	public void readParentPath() {
		JSONParser jsonParser = new JSONParser();
		File file = new File(homePath + "\\Documents\\" + "recent.json");
		if (file.exists()) {
			try (FileReader reader = new FileReader(file.getPath())) {
				Object obj = jsonParser.parse(reader);

				JSONObject pathOverall = (JSONObject) obj;
				JSONObject pathObj = (JSONObject) pathOverall.get("paths");

				path = (String) pathObj.get("path");
				parent = (String) pathObj.get("parent");
				name = (String) pathObj.get("name");
				autosave = (boolean) pathObj.get("autosave");
				sameDateSave = (boolean) pathObj.get("sameDate");
				

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}

/**
 * Writes all the items to a Json file,
 * uses the createObj method to help
 * @param items: arrayList of items to be written to memory
 */
	@SuppressWarnings("unchecked")
	public void writeItems(ArrayList<Item2> items) {
		JSONArray list = new JSONArray();
		for (Item2 item : items) {
			list.add(createObj(item));
		}
		push(list);
	}

/**
 * helper method for writeItems
   creates a JSON object for each item
 * @param item : the item to be converted to a json object
 * @return the converted json object
 */
	@SuppressWarnings("unchecked")
	public JSONObject createObj(Item2 item) {
		JSONObject itemDetails = new JSONObject();
		itemDetails.put("Item Name", item.getName());
		itemDetails.put("URL", item.getUrl());
		itemDetails.put("minPrice", item.getMinPrice());
		itemDetails.put("maxPrice", item.getMaxPrice());
		itemDetails.put("avgPrice", item.getAvgPrice());
		itemDetails.put("update", item.getUpdate());
		itemDetails.put("prices", priceArr(item.getMinPrices()));
		itemDetails.put("avgPrices", priceArr(item.getAvgPrices()));
		itemDetails.put("maxPrices", priceArr(item.getMaxPrices()));
		itemDetails.put("filters", filterArr(item.getFilters()));
		
		JSONObject itemObject = new JSONObject();
		itemObject.put("item", itemDetails);
		return itemObject;
	}


// reads the json file for a given file path
	public void readItem(String path) {
		JSONParser jsonParser = new JSONParser();
		try (FileReader reader = new FileReader(path)) {
			Object obj = jsonParser.parse(reader);
			
			JSONArray employeeList = (JSONArray) obj;

			employeeList.forEach(emp -> parseItemObject((JSONObject) emp));

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public void parseItemObject(JSONObject obj) {
		JSONObject itemObj = (JSONObject) obj.get("item");

		String name = (String) itemObj.get("Item Name");
		String url = (String) itemObj.get("URL");
		String update = (String) itemObj.get("update");
		double minPrice = (double) itemObj.get("minPrice");
		double maxPrice = (double) itemObj.get("maxPrice");
		double avgPrice = (double) itemObj.get("avgPrice");

		
		double[] minPriceArr = jsonToArray((JSONArray) itemObj.get("prices")).clone();
		double[] avgPriceArr = jsonToArray((JSONArray) itemObj.get("avgPrices")).clone();
		double[] maxPriceArr = jsonToArray((JSONArray) itemObj.get("maxPrices")).clone();
		
		String[] filterArr = jsonToStringArray((JSONArray) itemObj.get("filters")).clone();

		Item2 item = new Item2(name, url, minPrice, update, minPriceArr);
		item.setMaxPrice(maxPrice);
		item.setMaxPrices(maxPriceArr);
		item.setAvgPrice(avgPrice);
		item.setAvgPrices(avgPriceArr);
		item.setFilters(filterArr);

		items.add(item);
	}
	
	//Converts JSONArray to an array of doubles
	public double[] jsonToArray(JSONArray jsonArr) {
		Iterator<Double> iterator = jsonArr.iterator();

		ArrayList<Double> priceList = new ArrayList<>();
		while (iterator.hasNext()) {
			priceList.add(iterator.next());
		}

		double[] priceArr = new double[priceList.size()];
		for (int i = 0; i < priceArr.length; i++) {
			priceArr[i] = (double) priceList.get(i);
		}
		return priceArr;	
	}
	
	
	//Converts JSONArray to an array of Strings
		public String[] jsonToStringArray(JSONArray jsonArr) {
			Iterator<String> iterator = jsonArr.iterator();

			ArrayList<String> filterList = new ArrayList<>();
			while (iterator.hasNext()) {
				filterList.add(iterator.next());
			}

			String[] filterArr = new String[filterList.size()];
			for (int i = 0; i < filterArr.length; i++) {
				filterArr[i] = (String) filterList.get(i);
			}
			return filterArr;	
		}

	//Converts array of doubles in to a JSONArray
	@SuppressWarnings("unchecked")
	public JSONArray priceArr(double[] prices) {
		JSONArray jarr = new JSONArray();

		for (int i = 0; i < prices.length; i++) {
			if (prices[i] != 0)
				jarr.add(prices[i]);
		}

		return jarr;
	}
	
	//Converts array of doubles in to a JSONArray
	@SuppressWarnings("unchecked")
	public JSONArray filterArr(String[] filters) {
		JSONArray jarr = new JSONArray();

		for (int i = 0; i < filters.length; i++) {
			if (filters[i] != "")
				jarr.add(filters[i]);
		}

		return jarr;
	}

// Helper method for writing json files to memory
// Actually does all saving
// Takes in an array of json objects, called by writeItems	
	public void push(JSONArray list) {
		try (FileWriter file = new FileWriter(path)) {
			file.write(list.toJSONString());
			file.flush();
		}

		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

// Getter method for the items ArrayList
	public ArrayList<Item2> getItems() {
		return items;
	}
}
