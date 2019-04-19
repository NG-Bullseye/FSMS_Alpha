package kickstart.catalog;

import java.util.HashSet;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

public class Form {
	@Size(max = 255, message = "Der Name darf maximal 255 Zeichen lang sein.")
	private String name;
	private String selectedColour;
	@Min(value = 0)
	@Max(10000)
	private double price;
	private String eanCode;
	private String herstellerUrl;
	@Min(value = 0)
	@Max(1500)
	private double weight;

	public String getHerstellerUrl() {
		return herstellerUrl;
	}

	public void setHerstellerUrl(String herstellerUrl) {
		this.herstellerUrl = herstellerUrl;
	}

	public String getEanCode() {
		return eanCode;
	}

	public void setEanCode(String eanCode) {
		this.eanCode = eanCode;
	}

	private HashSet<String> selectedCategories;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public String getSelectedColour() {
		return selectedColour;
	}

	public void setSelectedColour(String selectedColour) {
		this.selectedColour = selectedColour;
	}

	public HashSet<String> getSelectedCategories() {
		return selectedCategories;
	}

	public void setSelectedCategories(HashSet<String> selectedCategories) {
		this.selectedCategories = selectedCategories;
	}
}
