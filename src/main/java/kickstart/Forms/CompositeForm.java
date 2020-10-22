package kickstart.Forms;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;

public class CompositeForm {
	@Size(max = 255, message = "Der Name darf maximal 255 Zeichen lang sein.")
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
	private String selectedCategorie;
	@Getter
	@Setter
	private long criticalAmount;



}
