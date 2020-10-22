package kickstart.Forms;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class PartOrderForm {
	@Getter
	@Setter
	@NotEmpty(message = "Bitte geben Sie einen Namen für das Produkt ein.")
	@Size(min = 1, max = 255, message = "Der Name muss zwischen 2 und 255 Zeichen lang sein.")
	private String name;
	//@NotEmpty(message = "Bitte geben Sie einen Preis für das Produkt ein.")
	@Getter
	@Setter
	private String selectedColour;
	//@NotNull(message = )
	@Getter
	@Setter
	private double priceNetto;
	@Getter
	@Setter
	private double priceBrutto;
	@Getter
	@Setter
	private String eanCode;
	@Getter
	@Setter
	private String herstellerUrl;
	@Getter
	@Setter
	@NotEmpty
	private String selectedCategorie;

}
