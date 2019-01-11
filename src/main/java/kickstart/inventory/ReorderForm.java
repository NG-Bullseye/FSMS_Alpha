package kickstart.inventory;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * This form stores is used to reorder items in the inventory.
 */
public interface ReorderForm {

	/**
	 * 
	 * @return Returns the string representation of a positive number(that means
	 *         "153" for 153)
	 */
	@NotEmpty(message = "Bitte geben sie eine Zahl ein")
	@Pattern(regexp = "^\\d{1,4}|$", message = "Ungültige Anzahl!")
	public String getAmount();
}
