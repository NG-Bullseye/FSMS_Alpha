package kickstart.customer;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.salespointframework.useraccount.UserAccount;

@Entity
public class Employee {

	private @Id @GeneratedValue long id;

	@OneToOne
	private UserAccount userAccount;

	private String address;
	private String email;
	private String firstname;
	private String lastname;
	private int salary;

	@SuppressWarnings("unused")
	public Employee() {}

	public Employee(UserAccount userAccount, String name, String firstname, String lastname, String email, String address) {
		this.userAccount = userAccount;
		this.firstname = firstname;
		this.lastname =lastname;
		this.email = email;
		this.address = address;
		salary = 0;
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
	
	public int getSalary() {
		return salary;
	}
	
	public void setSalary(int salary) {
		this.salary = salary;
	}
}
