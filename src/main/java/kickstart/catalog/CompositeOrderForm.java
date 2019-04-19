package kickstart.catalog;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.HashSet;

public class CompositeOrderForm {
	@NotEmpty
	@Size(min = 2, max = 255, message = "Der Name muss zwischen 2 und 255 Zeichen lang sein.")
	private String name;
	private double priceNetto;
	private double priceBrutto;
	private String eanCode;
	private String herstellerUrl;
	private String selectedColour;
	@NotEmpty(message = "Bitte wählen Sie mindestens eine Kategorie für den Artikel aus.")
	private String selectedCategorie;

	public String getSelectedColour() {
		return selectedColour;
	}

	public void setSelectedColour(String selectedColour) {
		this.selectedColour = selectedColour;
	}

	public String getSelectedCategorie() {
		return selectedCategorie;
	}

	public void setSelectedCategorie(String selectedCategorie) {
		this.selectedCategorie = selectedCategorie;
	}

	public String getHerstellerUrl() {
		return herstellerUrl;
	}

	public void setHerstellerUrl(String herstellerUrl) {
		this.herstellerUrl = herstellerUrl;
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

	public double getPriceNetto() {
		return priceNetto;
	}

	public void setPriceNetto(double priceNetto) {
		this.priceNetto = priceNetto;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
