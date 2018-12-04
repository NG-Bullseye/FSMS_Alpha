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

	UserManagement(UserRepository users, UserAccountManager userAccounts) {

		Assert.notNull(users, "UserRepository must not be null!");
		Assert.notNull(userAccounts, "UserAccountManager must not be null!");

		this.users = users;
		this.userAccounts = userAccounts;
	}

	public User createUser(RegistrationForm form) {

		Assert.notNull(form, "Registration form must not be null!");

		UserAccount userAccount = userAccounts.create(form.getName(), form.getPassword(), Role.of("ROLE_CUSTOMER"));
		return users.save(new User(userAccount, form.getFirstname(), form.getLastname(), form.getEmail(), form.getAddress()));
	}
	
	public User findUser (UserAccount userAccount) {
		
		User user = users.findByUserAccount(userAccount);
		return user;
	}
	
public User findUserById (long id) {
		
		User user = users.findById(id);
		return user;
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
		if (type == 0) {
			userAccounts.disable(accountId);
			return;
		} else if (type == 1) {
			userAccounts.enable(accountId);
			return;
		} else {
			return;
		}
		
	}
	
}
