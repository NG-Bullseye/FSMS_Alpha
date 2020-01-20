package kickstart.administration;

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
	private double priceNetto;
	private double priceBrutto;
	private String eanCode;
	private String herstellerUrl;
	@NotEmpty private String selectedCategorie;

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


	public String getSelectedCategorie() {
		return selectedCategorie;
	}

	public void setSelectedCategorie(String selectedCategorie) {
		this.selectedCategorie = selectedCategorie;
	}

	public String getSelectedColour() {
		return selectedColour;
	}

	public void setSelectedColour(String colour) {
		this.selectedColour = colour;
	}
}
