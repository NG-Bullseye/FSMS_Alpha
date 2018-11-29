package kickstart.customer;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.salespointframework.useraccount.UserAccount;

@Entity
public class Customer {

	private @Id @GeneratedValue long id;

	@OneToOne
	private UserAccount userAccount;

	private String address;
	private String email;
	private String firstname;
	private String lastname;

	@SuppressWarnings("unused")
	public Customer() {}

	public Customer(UserAccount userAccount, String firstname, String lastname, String email, String address) {
		this.userAccount = userAccount;
		this.firstname = firstname;
		this.lastname =lastname;
		this.email = email;
		this.address = address;
	}
	public long getId() {
		return id;
	}

	public String getAddress() {
		return address;
	}

	public String getEmail() {
		return email;
	}

	public UserAccount getUserAccount() {
		return userAccount;
	}
	
	public String getFirstname() {
		return firstname;
	}
	
	public String getLastname() {
		return lastname;
	}
}
