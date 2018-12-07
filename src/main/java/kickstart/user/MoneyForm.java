package kickstart.user;
import javax.validation.constraints.NotEmpty;

interface MoneyForm {

	@NotEmpty(message = "Bitte geben sie ein Gehalt ein!")
	int getSalary();
	
	@NotEmpty
	String getId();
}