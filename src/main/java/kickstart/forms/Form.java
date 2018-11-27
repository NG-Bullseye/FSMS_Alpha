package kickstart.forms;

import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.*;
import java.util.HashSet;

public class Form {
	@NotEmpty
	private String name;
	@NotEmpty
	private String description;
	@NotEmpty(message = "Bitte wählen sie eine Farbe aus")
	private HashSet<String> selectedColours;
	@NotNull
	private double price;
	@NotNull
	private double weight;
	@NotEmpty(message = "Bitte wählen sie eine Kategorie für den Artikel aus.")
	private HashSet<String> selectedCategories;


	public Form(){ }

	public void setDescription(String description) {
		this.description = description;
	}
	@NotEmpty
	public String getDescription() {
		return description;
	}
	@NotEmpty
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@DecimalMin("1.0") @DecimalMax("50000")
	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	@DecimalMin("0.1") @DecimalMax("5000")
	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	@NotNull(message = "Bitte wählen sie eine Kategorie für den Artikel aus.")
	public HashSet<String> getSelectedColours() {
		return selectedColours;
	}

	public void setSelectedColours(HashSet<String> colour) {
		this.selectedColours = colour;
	}
	@NotNull
	public HashSet<String> getSelectedCategories() {
		return selectedCategories;
	}

	public void setSelectedCategories(HashSet<String> selectedCategories) {
		this.selectedCategories = selectedCategories;
	}
}
