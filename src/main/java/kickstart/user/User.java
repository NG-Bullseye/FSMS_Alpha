package kickstart.user;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.salespointframework.useraccount.UserAccount;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * @author Daniel Koersten
 *
 */
@Entity
public class User {

	@Getter
	private @Id @GeneratedValue long id;

	@Getter
	@OneToOne
	private UserAccount userAccount;
	
	@Getter
	@Setter
	private String address;
	
	@Getter
	@Setter
	private String email;
	
	@Getter
	private String firstname;
	
	@Getter
	private String lastname;
	
	@Getter
	private int salary;
	
	@SuppressWarnings("unused")
	public User() {}

	public User(@NonNull UserAccount userAccount, @NonNull String firstname, @NonNull String lastname, @NonNull String email, @NonNull String address) {
		this.userAccount = userAccount;
		this.firstname = firstname;
		this.lastname =lastname;
		this.email = email;
		this.address = address;
		salary = 0;
		
		userAccount.setFirstname(firstname);
		userAccount.setLastname(lastname);
		
	}

	public void setSalary(int salary) {
		if (salary < 0) {
			throw new IllegalArgumentException();
			
		}
		this.salary = salary;
	}
	
	public void setFirstname(String firstname) {
		this.firstname = firstname;
		this.userAccount.setFirstname(firstname);
	}
	
	public void setLastname(String lastname) {
		this.lastname = lastname;
		this.userAccount.setLastname(lastname);
	}
}
