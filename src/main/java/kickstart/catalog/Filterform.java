package kickstart.catalog;

import java.util.ArrayList;
import java.util.Arrays;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class Filterform {
	private ArrayList<String> selectedColours;
	@Min(0)
	@Max(99999999)
	private Integer minPriceBrutto = 0;
	@Min(0)
	@Max(99999999)
	private Integer maxPriceBrutto = 99999999;
	@Min(0)
	@Max(99999999)
	private Integer minPriceNetto = 0;
	@Min(0)
	@Max(99999999)
	private Integer maxPriceNetto = 99999999;

	private ArrayList<String> selectedCategories;


	public Integer getMinPriceBrutto() {
		return minPriceBrutto;
	}

	public void setMinPriceBrutto(Integer minPriceBrutto) {
		this.minPriceBrutto = minPriceBrutto;
	}

	public Integer getMaxPriceBrutto() {
		return maxPriceBrutto;
	}

	public void setMaxPriceBrutto(Integer maxPriceBrutto) {
		this.maxPriceBrutto = maxPriceBrutto;
	}

	public void setMinPriceNetto(Integer minPriceNetto) {
		this.minPriceNetto = minPriceNetto;
	}

	public void setMaxPriceNetto(Integer maxPriceNetto) {
		this.maxPriceNetto = maxPriceNetto;
	}



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

	public void setSelectedCategories(ArrayList<String> selectedCategories) {
		this.selectedCategories = selectedCategories;
	}

	public ArrayList<String> getSelectedCategories() {
		return selectedCategories;
	}
}
