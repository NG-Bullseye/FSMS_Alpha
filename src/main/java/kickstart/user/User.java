package kickstart.user;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.salespointframework.useraccount.UserAccount;

import lombok.NonNull;

@Entity
public class User {

	private @Id @GeneratedValue long id;

	@OneToOne
	private UserAccount userAccount;

	private String address;
	private String email;
	private String firstname;
	private String lastname;
	private int salary;

	@SuppressWarnings("unused")
	public User() {}

	public User(UserAccount userAccount, String firstname, String lastname, String email, String address) {
		this.userAccount = userAccount;
		this.firstname = firstname;
		this.lastname =lastname;
		this.email = email;
		this.address = address;
		salary = 0;
	}

	public UserAccount getUserAccount() {
		return userAccount;
	}

	public long getId() {
		return id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(@NonNull String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(@NonNull String email) {
		this.email = email;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(@NonNull String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(@NonNull String lastname) {
		this.lastname = lastname;
	}

	public int getSalary() {
		return salary;
	}

	public void setSalary(int salary) {
		if (salary < 0) {
			throw new IllegalArgumentException();
			
		}
		this.salary = salary;
	}

	
	
}
