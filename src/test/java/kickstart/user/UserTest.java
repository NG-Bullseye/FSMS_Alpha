package kickstart.user;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.annotation.Transient;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
@Transactional
public class UserTest {
	
	User user;
	UserAccount userAccount;
	private static final String username = "test1545649854";
	private static final String firstname = "Daniel";
	private static final String lastname = "Tester";
	private static final String email = "E-mail";
	private static final String address = "Adresse";
	private static final String password = "123";
	private static final int salary = 12;
	private static final int negativeSalary = -12;

	
	@Autowired
	private UserAccountManager userAccounts;
	
	
	@BeforeAll
	@Transient
	public void setUp() {
	    
	    this.userAccount = userAccounts.create(username, password, Role.of("ROLE_CUSTOMER"));
		this.user = new User(userAccount, firstname, lastname, email, address);
		return;
		
	}
	
	@Test
	@Transient
	public void Salary() {
		
		Assertions.assertEquals(0, user.getSalary(), "Gehalt sollte nach Initialisierung 0 sein!");
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			user.setSalary(negativeSalary);
	    }, "Gehalt sollte nicht negativ sein!");
		user.setSalary(salary);
		Assertions.assertEquals(12, user.getSalary(), "Gehalt wurde nicht korrekt gesetzt!");
		
	}
	
	@Test
	@Transient
	public void Address() {
		
		Assertions.assertThrows(NullPointerException.class, () -> {
			user.setAddress(null);
	    }, "Adresse muss spezifiziert sein!");
		Assertions.assertEquals(address, user.getAddress(), "Adresse wurde nicht korrekt gesetzt!");
		
	}
	
	@Test
	@Transient
	public void Email() {
		
		Assertions.assertThrows(NullPointerException.class, () -> {
			user.setEmail(null);
	    }, "Email muss spezifiziert sein!");
		Assertions.assertEquals(email, user.getEmail(), "E-Mail wurde nicht korrekt gesetzt!");
		
	}
	
	@Test
	@Transient
	public void Firstname() {
		
		Assertions.assertThrows(NullPointerException.class, () -> {
			user.setFirstname(null);
	    }, "Vorname muss spezifiziert sein!");
		Assertions.assertEquals(firstname, user.getFirstname(), "Vorname wurde nicht korrekt gesetzt!");
		
	}
	
	@Test
	@Transient
	public void Lastname() {
		
		Assertions.assertThrows(NullPointerException.class, () -> {
			user.setLastname(null);
	    }, "Nachname muss spezifiziert sein!");
		Assertions.assertEquals(lastname, user.getLastname(), "Nachname wurde nicht korrekt gesetzt!");
		
	}
}