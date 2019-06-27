import java.util.ArrayList;
import java.util.List;
import com.ebay.services.client.ClientConfig;
import com.ebay.services.client.FindingServiceClientFactory;
import com.ebay.services.finding.FindItemsAdvancedRequest;
import com.ebay.services.finding.FindItemsAdvancedResponse;
import com.ebay.services.finding.FindItemsByKeywordsRequest;
import com.ebay.services.finding.FindItemsByKeywordsResponse;
import com.ebay.services.finding.FindingServicePortType;
import com.ebay.services.finding.ListingInfo;
import com.ebay.services.finding.PaginationInput;
import com.ebay.services.finding.SearchItem;
import com.ebay.services.finding.AckValue;

public class eBay {

	private double price;
	private double maxPrice, avgPrice;;
	private String keywords;
	private ArrayList<Double> prices;
	private String urlString;
	private String[] filters;
	ClientConfig config;
	FindingServicePortType serviceClient;
	PaginationInput pi;
	AckValue ack;

	public eBay() {
		try {
			prices = new ArrayList<>();
			price = 10000;
			maxPrice = 0;
			config = new ClientConfig();
			config.setApplicationId("TyelerCr-PriceScr-PRD-3dc0df799-15c263c6");
			serviceClient = FindingServiceClientFactory.getServiceClient(config);

			pi = new PaginationInput();
			pi.setEntriesPerPage(50);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public eBay(Item2 item) {
		try {
			prices = new ArrayList<>();
			price = 10000;
			maxPrice = 0;
			config = new ClientConfig();
			config.setApplicationId("TyelerCr-PriceScr-PRD-3dc0df799-15c263c6");
			serviceClient = FindingServiceClientFactory.getServiceClient(config);
			
			keywords = item.getName();
			filters = item.getFilters().clone();

			pi = new PaginationInput();
			pi.setEntriesPerPage(50);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void find() {
		FindItemsByKeywordsRequest request = new FindItemsByKeywordsRequest();

		request.setKeywords(keywords);
		request.setPaginationInput(pi);

		FindItemsByKeywordsResponse result = serviceClient.findItemsByKeywords(request);

		//System.out.println("Ack = " + result.getAck().toString());

		if (result.getAck().toString().equals("SUCCESS")) {
			//System.out.println("Find " + result.getSearchResult().getCount() + "items.");
			List<SearchItem> items = result.getSearchResult().getItem();

			process(items);
		} else {
			//System.out.println("Ack failed, maybe change keywords or something");
		}
	}

	public void process(List<SearchItem> items) {
		price = 10000;
		maxPrice = 0;
		avgPrice = 0;
		double value = 0;
		double numItems = 0;
		double sum = 0;
		String title = "";
		ListingInfo listInfo = null;
		boolean passesFilter = false;
		
		for (SearchItem item : items) {
			passesFilter = checkFilter(item);
			if ((!item.getListingInfo().getListingType().toString().equals("Auction")
					&& item.getCountry().equals("US")) && passesFilter) {

				if (item.getListingInfo().getListingType().equals("AuctionWithBIN")
						&& item.getListingInfo().isBuyItNowAvailable()) {
					value = item.getListingInfo().getBuyItNowPrice().getValue();

				} else {
					value = item.getSellingStatus().getCurrentPrice().getValue();
				}
				
				if (value < price) {
					price = value;
					urlString = item.getViewItemURL();
					listInfo = item.getListingInfo();
					title = item.getTitle();
				}
				if (value > maxPrice) {
					maxPrice = value;
				}
				numItems += 1;
				sum += value;

			}
		}

		avgPrice = sum / numItems;
		avgPrice = Math.round(avgPrice * 100) / 100;
		if (price == 10000)
			price = 0;

		if (listInfo != null) {
			// System.out.println(title + ": " + value);
			// System.out.println(urlString);
			// System.out.println(listInfo.getListingType());
		}
	}
	
	public boolean checkFilter(SearchItem item) {
		String title = item.getTitle();
		
		if (filters == null)
			return true;
		
		if (filters[0] == null)
			return true;
		
		for (String filter : filters) {
			if (title.toLowerCase().contains(filter.toLowerCase()))
				return false;
		}
		
		return true;
	}

	/**
	 * Getters and setters for the "normal" data types
	 */
	public double getPrice() {
		return price;
	}

	public double getMaxPrice() {
		return maxPrice;
	}

	public double getAvgPrice() {
		return avgPrice;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public ArrayList<Double> getPrices() {
		return prices;
	}

	public void setPrices(ArrayList<Double> prices) {
		this.prices = new ArrayList<Double>(prices);
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getUrlString() {
		return urlString;
	}

	public void setUrlString(String urlString) {
		this.urlString = urlString;
	}
	
	public String[] getFilters() {
		return filters;
	}

	public void setFilters(String[] filters) {
		this.filters = filters.clone();
	}
}
