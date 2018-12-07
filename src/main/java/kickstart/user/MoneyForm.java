package kickstart.user;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

interface MoneyForm {

	@NotEmpty(message = "Bitte geben sie ein Gehalt ein!")
	@Pattern(regexp = "^\\d{1,4}$", message = "Eingabe ungültig!")
	String getSalary();
	
	@NotEmpty(message = "Id sollte nicht null sein!")
	@Pattern(regexp = "^\\d{1,9}$", message = "Id kann nicht negativ sein!")
	String getId();
}