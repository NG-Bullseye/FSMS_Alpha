package kickstart.catalog;

import java.util.ArrayList;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class Filterform {
	@NotNull(message = "Bitte wählen Sie mindestens eine Farbe aus.")
	private ArrayList<String> selectedColours;
	@NotNull
	@Min(0)
	@Max(99999999)
	private Integer minPriceNetto = 0;
	@NotNull
	@Min(0)
	@Max(99999999)
	private Integer maxPriceNetto = 99999999;
	/*
	@NotNull(message = "Bitte wählen Sie einen Produkttyp aus.")
	private String type;
	 */

	@NotNull(message = "Bitte wählen Sie mindestens eine Kategorie aus.")
	private ArrayList<String> selectedCategories;

	public void setSelectedColours(ArrayList<String> selectedColours) {
		this.selectedColours = selectedColours;
	}

	public ArrayList<String> getSelectedColours() {
		return selectedColours;
	}

	public int getMaxPriceNetto() {
		return maxPriceNetto;
	}

	public int getMinPriceNetto() {
		return minPriceNetto;
	}

	public void setMaxPriceNetto(int maxPriceNetto) {
		this.maxPriceNetto = maxPriceNetto;
	}

	public void setMinPriceNetto(int minPriceNetto) {
		this.minPriceNetto = minPriceNetto;
	}

	/*
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	 */


	public void setSelectedCategories(ArrayList<String> selectedCategories) {
		this.selectedCategories = selectedCategories;
	}

	public ArrayList<String> getSelectedCategories() {
		return selectedCategories;
	}
}
