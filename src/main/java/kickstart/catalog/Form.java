package kickstart.catalog;

import java.util.HashSet;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Form {
	@Size(max = 255,message = "Der Name darf maximal 255 Zeichen lang sein.")
	private String name;
	@Size(max = 255,message = "Die Beschreibung darf maximal  255 Zeichen lang sein.")
	private String description;
	private HashSet<String> selectedColours;
	@Min(value = 0)
	@Max(10000)
	private double price;
	@Min(value = 0)
	@Max(1500)
	private double weight;
	private HashSet<String> selectedCategories;



	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

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

	public HashSet<String> getSelectedColours() {
		return selectedColours;
	}

	public void setSelectedColours(HashSet<String> colour) {
		this.selectedColours = colour;
	}
	public HashSet<String> getSelectedCategories() {
		return selectedCategories;
	}

	public void setSelectedCategories(HashSet<String> selectedCategories) {
		this.selectedCategories = selectedCategories;
	}
}
