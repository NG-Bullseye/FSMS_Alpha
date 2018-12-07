package kickstart.user;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

interface EditForm {
	
	@NotEmpty(message = "Bitte geben sie einen Vornamen ein!")
	//@Pattern(regexp = "[^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$]", message = "Bitte geben sie einen gültigen Vorname ein!")
	String getFirstname();
	
	@NotEmpty(message = "Bitte geben sie einen Nachnamen ein!")
	//@Pattern(regexp = "[^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$]", message = "Bitte geben sie einen gültigen Nachname ein!")
	String getLastname();

	@NotEmpty(message = "Bitte geben sie eine E-Mail ein!")
	@Email(message = "E-Mail Adresse ungültig!")
	String getEmail();

	@NotEmpty(message = "Bitte geben sie ein Passwort ein!")
	//@Pattern(regexp = "[CDATA[^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{4,32}$]]", message = "Passwort zu leicht! 4-32 Zeichen mit: Zahl, Groß-, Kleinbuchstaben")
	String getPassword();

	@NotEmpty(message = "Bitte geben sie eine Adresse ein!")
	String getAddress();
	
	@NotEmpty(message = "Id sollte nicht null sein!")
	@Pattern(regexp = "^\\d{1,9}$", message = "Id kann nicht negativ sein!")
	String getId();
}
