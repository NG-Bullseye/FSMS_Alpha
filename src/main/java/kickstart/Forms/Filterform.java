package kickstart.Forms;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class Filterform {
	@Getter
	@Setter
	private ArrayList<String> selectedColours;
	@Getter
	@Setter
	@Min(0)
	@Max(99999999)
	private Integer minPriceBrutto = 0;
	@Getter
	@Setter
	@Min(0)
	@Max(99999999)
	private Integer maxPriceBrutto = 99999999;
	@Getter
	@Setter
	@Min(0)
	@Max(99999999)
	private Integer minPriceNetto = 0;
	@Getter
	@Setter
	@Min(0)
	@Max(99999999)
	private Integer maxPriceNetto = 99999999;
	@Getter
	@Setter
	private ArrayList<String> selectedCategories;
}
