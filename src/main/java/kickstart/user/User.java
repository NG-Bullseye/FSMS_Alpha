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
	@Setter
	private String firstname;
	
	@Getter
	@Setter
	private String lastname;
	
	@Getter
	private int salary;

	public User(@NonNull UserAccount userAccount, @NonNull String firstname, @NonNull String lastname, @NonNull String email, @NonNull String address) {
		this.userAccount = userAccount;
		this.firstname = firstname;
		this.lastname =lastname;
		this.email = email;
		this.address = address;
		salary = 0;
	}

	public void setSalary(int salary) {
		if (salary < 0) {
			throw new IllegalArgumentException();
			
		}
		this.salary = salary;
	}
}
