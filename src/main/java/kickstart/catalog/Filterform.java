package kickstart.catalog;

import java.util.ArrayList;

public class Filterform {
	private ArrayList<String> selectedColours;
	private int minPrice;
	private int maxPrice;
	private String type;
	private ArrayList<String> selectedCategories;

	public Filterform(){}

	public void setSelectedColours(ArrayList<String> selectedColours) {
		this.selectedColours = selectedColours;
	}

	public ArrayList<String> getSelectedColours() {
		return selectedColours;
	}

	public int getMaxPrice() {
		return maxPrice;
	}

	public int getMinPrice() {
		return minPrice;
	}

	public void setMaxPrice(int maxPrice) {
		this.maxPrice = maxPrice;
	}

	public void setMinPrice(int minPrice) {
		this.minPrice = minPrice;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setSelectedCategories(ArrayList<String> selectedCategories) {
		this.selectedCategories = selectedCategories;
	}

	public ArrayList<String> getSelectedCategories() {
		return selectedCategories;
	}
}
