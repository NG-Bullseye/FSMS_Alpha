package kickstart.catalog;

import javax.validation.constraints.Size;

public class CompositeForm {
	@Size(max = 255, message = "Der Name darf maximal 255 Zeichen lang sein.")
	private String name;
	@Size(max = 255, message = "Die Beschreibung darf maximal 255 Zeichen lang sein.")
	private String description;
	private double priceNetto;
	private double priceBrutto;
	private String eanCode;
	private String herstellerUrl;


	public double getPriceNetto() {
		return priceNetto;
	}

	public void setPriceNetto(double priceNetto) {
		this.priceNetto = priceNetto;
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

	public String getHerstellerUrl() {
		return herstellerUrl;
	}

	public void setHerstellerUrl(String herstellerUrl) {
		this.herstellerUrl = herstellerUrl;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
