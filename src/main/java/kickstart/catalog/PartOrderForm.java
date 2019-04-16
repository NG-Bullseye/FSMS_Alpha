package kickstart.catalog;

import java.util.HashSet;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class PartOrderForm {
	@NotEmpty(message = "Bitte geben Sie einen Namen für das Produkt ein.")
	@Size(min = 2, max = 255, message = "Der Name muss zwischen 2 und 255 Zeichen lang sein.")
	private String name;
	@NotEmpty(message = "Bitte geben Sie eine Beschreibung für das Produkt ein.")
	@Size(min = 2, max = 255, message = "Die Beschreibung muss zwischen 2 und 255 Zeichen lang sein.")
	private String description;
	@NotEmpty(message = "Bitte wählen Sie mindestens eine Farbe aus")
	private String selectedColour;
	@NotNull(message = "Bitte geben Sie einen Preis für das Produkt ein.")

	private double price;
	private double priceNetto;
	private double priceBrutto;
	@NotNull(message = "Bitte geben Sie ein eanCode ein")
	private String eanCode;

	@NotNull(message = "Bitte geben Sie ein Gewicht für das Produkt ein.")
	@Min(0)
	@Max(1500)
	private double weight;
	@NotEmpty(message = "Bitte wählen Sie mindestens eine Kategorie für den Artikel aus.")
	private HashSet<String> selectedCategories;

	public double getPriceNetto() {
		return priceNetto;
	}

	public double getPriceBrutto() {
		return priceBrutto;
	}

	public void setPriceBrutto(double priceBrutto) {
		this.priceBrutto = priceBrutto;
	}

	public String getEanCode() {
		return eanCode;
	}

	public void setEanCode(String eanCode) {
		this.eanCode = eanCode;
	}

	public void setPriceNetto(double priceNetto) {
		this.priceNetto = priceNetto;
	}

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

	public String getSelectedColour() {
		return selectedColour;
	}

	public void setSelectedColours(String colour) {
		this.selectedColour = colour;
	}

	public HashSet<String> getSelectedCategories() {
		return selectedCategories;
	}

	public void setSelectedCategories(HashSet<String> selectedCategories) {
		this.selectedCategories = selectedCategories;
	}
}
