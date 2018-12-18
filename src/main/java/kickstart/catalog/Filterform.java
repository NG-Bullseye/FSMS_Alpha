package kickstart.catalog;

import java.util.ArrayList;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

public class Filterform {
	@NotEmpty(message = "Bitte wählen Sie mindestens eine Farbe aus.")
	private ArrayList<String> selectedColours;
	@NotEmpty
	@Min(1)
	@Max(10000)
	private Integer minPrice = 1;
	@NotEmpty
	@Min(1)
	@Max(10000)
	private Integer maxPrice = 10000;
	@NotEmpty(message = "Bitte wählen Sie einen Produkttyp aus.")
	private String type;
	@NotEmpty(message = "Bitte wählen Sie mindestens eine Kategorie aus.")
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
