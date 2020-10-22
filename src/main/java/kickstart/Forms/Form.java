package kickstart.Forms;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

public class Form {
	@Getter
	@Setter
	@Size(max = 255, message = "Der Name darf maximal 255 Zeichen lang sein.")
	private String name;
	@Getter
	@Setter
	private String selectedColour;
	@Getter
	@Setter
	@Min(value = 0)
	private double priceNetto;
	@Getter
	@Setter
	@Min(value = 0)
	private double priceBrutto;
	@Getter
	@Setter
	@Min(value = 0)
	private long criticalAmount;
	@Getter
	@Setter
	private String eanCode;
	@Getter
	@Setter
	private String herstellerUrl;



}
