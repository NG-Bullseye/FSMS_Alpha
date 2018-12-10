package kickstart.user;

import org.springframework.data.util.Streamable;

import java.util.ArrayList;
import java.util.List;

import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountIdentifier;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;


@Service
@Transactional
public class UserManagement {

	private final UserRepository users;
	private final UserAccountManager userAccounts;
	private final JavaMailer mailSender;

	UserManagement(UserRepository users, UserAccountManager userAccounts, JavaMailer mailSender) {

		Assert.notNull(users, "UserRepository must not be null!");
		Assert.notNull(userAccounts, "UserAccountManager must not be null!");
		Assert.notNull(mailSender, "JavaMailer must not be null!");

		this.users = users;
		this.userAccounts = userAccounts;
		this.mailSender = mailSender;
	}

	public User createUser(RegistrationForm form) {

		Assert.notNull(form, "Registration form must not be null!");

		UserAccount userAccount = userAccounts.create(form.getName(), form.getPassword(), Role.of("ROLE_CUSTOMER"));
		mailSender.sendCustomerRegistrationMessage(form.getEmail());
		return users.save(new User(userAccount, form.getFirstname(), form.getLastname(), form.getEmail(), form.getAddress()));
	}
	
	public void editData(EditForm form) {

		Assert.notNull(form, "Registration form must not be null!");
		
		long requestedId = Long.parseLong(form.getId());
		User user = findUserById(requestedId);
		user.setFirstname(form.getFirstname());
		user.setLastname(form.getLastname());
		user.setAddress(form.getAddress());
		user.setEmail(form.getEmail());
		UserAccount userAccount = user.getUserAccount();
		userAccounts.changePassword(userAccount, form.getPassword());
		return;
	}
	
	public User findUser (UserAccount userAccount) {
		
		User user = users.findByUserAccount(userAccount);
		return user;
	}
	
public User findUserById (long id) {
		
		User user = users.findById(id);
		return user;
	}

	public Streamable<User> findAll() {
		return Streamable.of(users.findAll());
	}
	
	public Streamable<User> findAllCustomers() {
		Iterable<User> userList = users.findAll();
		List<User> customersList = new ArrayList();
		
		
		for (User user : userList) {
		    if (user.getUserAccount().hasRole(Role.of("ROLE_CUSTOMER"))) {
		    	customersList.add(user);
		    }
		}
		
		Iterable<User> employees = customersList; 
		return Streamable.of(employees);
	}
	
	public Streamable<User> findAllEmployees() {
		Iterable<User> userList = users.findAll();
		List<User> employeesList = new ArrayList();
		
		
		for (User user : userList) {
		    if (user.getUserAccount().hasRole(Role.of("ROLE_EMPLOYEE"))) {
		        employeesList.add(user);
		    }
		}
		
		Iterable<User> employees = employeesList; 
		return Streamable.of(employees);
	}
	
	public void useraccountActivation(UserAccountIdentifier accountId, int type) {
		if (type == 0) { // deaktivieren
			userAccounts.disable(accountId);
			return;
		} else if (type == 1) { // aktivieren
			userAccounts.enable(accountId);
			return;
		} else {
			throw new IllegalArgumentException("Parameter type has illegal value");
		}
		
	}
	
	public void changeRole(User user, int type) {
		UserAccount userAccount = user.getUserAccount();
		if (type == 0) { // Kunde zum Mitarbeiter machen
			userAccount.add(Role.of("ROLE_EMPLOYEE"));
			userAccount.remove(Role.of("ROLE_CUSTOMER"));
			user.setSalary(50);
			return;
		} else if (type == 1) { // Mitarbeiter zum Kunde machen
			userAccount.add(Role.of("ROLE_CUSTOMER"));
			userAccount.remove(Role.of("ROLE_EMPLOYEE"));
			user.setSalary(0);
			return;
		} else {
			throw new IllegalArgumentException("Parameter type has illegal value");
		}
	}
	
	public void changeSalary(MoneyForm form) {
		
		Assert.notNull(form, "Money form must not be null!");
		
		long requestedId = Long.parseLong(form.getId());
		User user = findUserById(requestedId);
		int salary = Integer.parseInt(form.getSalary());
		user.setSalary(salary);
		return;
	}
	
}
