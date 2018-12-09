package kickstart.inventory;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public interface ReorderForm {

	@NotEmpty(message = "Bitte geben sie eine Zahl ein")
	@Pattern(regexp = "^\\d{1,4}|$", message = "Ungültige Anzahl!")
	public String getAmount();
}
