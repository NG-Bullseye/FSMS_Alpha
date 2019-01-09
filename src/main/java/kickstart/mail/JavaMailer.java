package kickstart.mail;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import lombok.NonNull;

/**
 * @author Daniel Koersten
 *
 */
@Component
public class JavaMailer {
  
    private final JavaMailSender emailSender;
    
    /**
     * Creates a new {@link JavaMailer} with the given JavaMailSender.
     * 
     * @param emailSender must not be {@literal null}.
     */
    public JavaMailer (@NonNull JavaMailSender emailSender) {
    	this.emailSender = emailSender;
    }
    
    /**
     * Creates a new SimpleMailMessage which will be transfered after registration.
     * 
     * @param email must not be {@literal null}. Contains mail address of recipient.
     */
    public void sendCustomerRegistrationMessage(@NonNull String email) {
        SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(email);
		message.setSubject("Möbel-Hier Registrierung");
		message.setText("Sie haben sich erfolgreich bei Möbel-Hier registriert!");
		sendMail(message);
		return;
    }
    
    public void sendCustomerConfirmationMessage(@NonNull String email) {
    	SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(email);
		message.setSubject("Möbel-Hier Bestellung");
		message.setText("Ihre Ware ist ab sofort abholbereit!");
		sendMail(message);
		return;
    }
    
    /**
     * Send away a previously created SimpleMailMessage.
     * 
     * @param message must not be {@literal null}. Contains a impleMailMessage.
     */
    private void sendMail (SimpleMailMessage message) {
    	emailSender.send(message);
    	return;
    }
}