package kickstart.catalog;

import java.util.HashSet;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class PartOrderForm {
	@NotEmpty(message = "Bitte geben Sie einen Namen für das Produkt ein.")
	@Size(min = 1, max = 255, message = "Der Name muss zwischen 2 und 255 Zeichen lang sein.")
	private String name;

	//@NotEmpty(message = "Bitte geben Sie einen Preis für das Produkt ein.")
	private String selectedColour;
	//@NotNull(message = )
	private double price;
	private double priceNetto;
	private double priceBrutto;
	private String eanCode;
	private String herstellerUrl;

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

	public String getHerstellerUrl() {
		return herstellerUrl;
	}

	public void setHerstellerUrl(String herstellerUrl) {
		this.herstellerUrl = herstellerUrl;
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

	public void setSelectedColour(String colour) {
		this.selectedColour = colour;
	}

	public HashSet<String> getSelectedCategories() {
		return selectedCategories;
	}

	public void setSelectedCategories(HashSet<String> selectedCategories) {
		this.selectedCategories = selectedCategories;
	}


}
