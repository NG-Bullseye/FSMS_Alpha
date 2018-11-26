package kickstart.customer;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

interface RegistrationForm {

	@NotEmpty(message = "Bitte geben sie einen Username ein!")
	String getName();
	
	@NotEmpty(message = "Bitte geben sie einen Vornamen ein!")
	String getFirstname();
	
	@NotEmpty(message = "Bitte geben sie einen Nachnamen ein!")
	String getLastname();

	@NotEmpty(message = "Bitte geben sie eine E-Mail ein!")
	//@Email(message = "E-Mail Adresse ungültig!")
	String getEmail();

	@NotEmpty(message = "Bitte geben sie ein Passwort ein!")
	String getPassword();

	@NotEmpty(message = "Bitte geben sie eine Adresse ein!")
	String getAddress();
}
