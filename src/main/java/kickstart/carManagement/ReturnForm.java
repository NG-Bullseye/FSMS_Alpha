package kickstart.carManagement;

import javax.validation.constraints.NotEmpty;

public interface ReturnForm {
	/**
	 * @return returns the name of the user that rented the truck
	 */
	@NotEmpty(message = "Bitte geben sie einen Username ein!")
	String getName();
}
