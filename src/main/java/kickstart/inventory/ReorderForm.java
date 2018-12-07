package kickstart.inventory;

import javax.validation.constraints.Positive;
import javax.validation.constraints.NotNull;

public interface ReorderForm {

	@NotNull(message = "Bitte geben sie eine Zahl ein")
	@Positive(message = "Bitte geben sie eine positive Zahl ein.")
	public int getAmount();
}
