import java.util.ArrayList;
import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Date;

public class CSVTest {
	public ArrayList<Item2> list;
	public Item2[] arr;
	File file;

	public CSVTest() {
		list = new ArrayList<>();
		arr = new Item2[4];
		run();
	}

	public CSVTest(ArrayList<Item2> items) {
		setList(items);
		run();
	}

	public void run() {
		try {
			file = new File("C:\\Users\\owner\\Desktop\\EclipseFiles\\GUI\\testItem.csv");

			if (file.exists()) {
				System.out.println("ITS REAL");
				readCsv();

			} else {
				System.out.println("BEING MADE");
				/**
				 * arr[0] = new Item2("Laptop", "Google.com", 20.00, new Date().toString());
				 * arr[1] = new Item2("Cum", "Amazon.com", 420.00, new Date().toString());
				 * arr[2] = new Item2("Loud", "eBay.com", 69.69, new Date().toString());
				 */

				list.add(new Item2("Laptop", "Google.com", 20.00, new Date().toString()));
				list.add(new Item2("Cum", "Amazon.com", 69.69, new Date().toString()));
				list.add(new Item2("Loud", "eBay.com", 69.69, new Date().toString()));

				writeCsv();
				readCsv();
			}
		} catch (Exception e) {

		}
	}

	public Item2[] getArr() {
		return arr;
	}

	public ArrayList<Item2> getList() {
		return list;
	}

	public void writeCsv() {
		try {
			PrintWriter pw = new PrintWriter(file);

			StringBuilder sb = new StringBuilder();

			double[] prices;
			// double[] temp = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10.5 };
			// list.get(0).setPrices(temp);

			for (int i = 0; i < list.size(); i++) {
				prices = list.get(i).getMinPrices();

				sb.append(list.get(i).getName() + ", ");
				sb.append("" + list.get(i).getMinPrice() + ", ");
				sb.append(list.get(i).getUrl() + ", ");
				sb.append(list.get(i).getUpdate() + ", ");

				for (int j = 0; j < prices.length; j++) {
					if (j < prices.length - 1)
						sb.append(prices[j] + ", ");
					else
						sb.append(prices[j] + " \n");
				}

			}

			pw.write(sb.toString());
			pw.close();

		} catch (Exception e) {

		}

		System.out.println("WOW");
	}

	/**
	 * public void writeCsv() { try { PrintWriter pw = new PrintWriter(file);
	 * 
	 * StringBuilder sb = new StringBuilder();
	 * 
	 * for (int i = 0; i < arr.length && arr[i] != null; i++) {
	 * 
	 * sb.append(arr[i].getName() + ", "); sb.append("" + arr[i].getPrice() + ", ");
	 * sb.append(arr[i].getUrl() + ", "); sb.append(arr[i].getUpdate() + "\n");
	 * 
	 * }
	 * 
	 * pw.write(sb.toString()); pw.close();
	 * 
	 * } catch (Exception e) {
	 * 
	 * }
	 * 
	 * System.out.println("WOW"); }
	 */

	/**
	 * public void readCsv() { try { Scanner scan = new Scanner(file); String[]
	 * tempArr; int counter = 0; while (scan.hasNext()) { String input =
	 * scan.nextLine(); tempArr = input.split(", "); arr[counter] = new
	 * Item2(tempArr[0], tempArr[2], Double.parseDouble(tempArr[1]), tempArr[3]);
	 * counter++; }
	 * 
	 * //printArr(); scan.close(); } catch (Exception ex) {
	 * 
	 * } }
	 */
	public void readCsv() {
		try {
			Scanner scan = new Scanner(file);
			String[] tempArr;
			Item2 tempItem;
			double[] priceArr = new double[10];
			while (scan.hasNext()) {
				String input = scan.nextLine();
				tempArr = input.split(", ");
				tempItem = new Item2(tempArr[0], tempArr[2], Double.parseDouble(tempArr[1]), tempArr[3]);

				for (int i = 0; i < priceArr.length; i++)
					priceArr[i] = Double.parseDouble(tempArr[i + 4]);

				tempItem.setMinPrices(priceArr);
				list.add(tempItem);
			}

			// printArr();
			scan.close();
		} catch (Exception ex) {

		}
	}

	public void setArr(Item2[] items) {
		arr = items.clone();
		writeCsv();
	}

	public void setList(ArrayList<Item2> items) {
		list = new ArrayList<Item2>(items);
		writeCsv();
	}

	public void printList() {
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i).getName());
		}
	}

	public void printArr() {
		for (int i = 0; i < arr.length && arr[i] != null; i++) {
			System.out.println(arr[i].getName());
		}
	}
}
