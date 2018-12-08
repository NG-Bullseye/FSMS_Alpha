package kickstart.carManagement;

import javax.validation.constraints.NotEmpty;

public interface ReturnForm {
	@NotEmpty(message = "Bitte geben sie einen Username ein!")
	String getName();
}
