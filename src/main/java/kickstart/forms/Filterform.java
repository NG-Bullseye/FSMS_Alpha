package kickstart.forms;

import java.util.ArrayList;

public class Filterform {
	private ArrayList<String> selectedColours;
	private int minPrice;
	private int maxPrice;
	private String category;

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

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
}
