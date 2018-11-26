package kickstart.forms;

import java.util.HashSet;

public class Form {
	private String name;
	private String description;
	private HashSet<String> selectedColours;
	private double price;
	private double weight;
	private String type;
	private HashSet<String> selectedCategories;


	public Form(){ }

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public HashSet<String> getSelectedCategories() {
		return selectedCategories;
	}

	public void setSelectedCategories(HashSet<String> selectedCategories) {
		this.selectedCategories = selectedCategories;
	}
}
