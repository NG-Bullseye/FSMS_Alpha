package Wawi.Forms;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class CompositeOrderForm {
	@NotEmpty
	@Size(min = 2, max = 255, message = "Der Name muss zwischen 2 und 255 Zeichen lang sein.")
	@Getter
	@Setter
	private String name;
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
	private String selectedColour;
	@Getter
	@Setter
	@NotEmpty(message = "Bitte wählen Sie mindestens eine Kategorie für den Artikel aus.")
	private String selectedCategorie;


}
