package kickstart.user;

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
	//@Email(message = "E-Mail Adresse ungültig!")
	@Pattern(regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])", message = "E-Mail Adresse ungültig!")
	@isRegistered(message = "E-Mail Adresse bereits registriert!")
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
