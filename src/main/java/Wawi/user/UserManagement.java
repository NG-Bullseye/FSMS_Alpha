package Wawi.user;

import java.util.ArrayList;
import java.util.List;

import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountIdentifier;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import Wawi.exception.UnAllowedException;
import lombok.NonNull;


@Service
@Transactional
public class UserManagement {

	private final UserRepository users;
	private final UserAccountManager userAccounts;

	/**
	 * Creates a new {@link UserManagement} with the {@link UserRepository},*
	 * @param users        must not be {@literal null}.
	 * @param userAccounts must not be {@literal null}.
	 */
	UserManagement(UserRepository users, UserAccountManager userAccounts) {

		Assert.notNull(users, "UserRepository must not be null!");
		Assert.notNull(userAccounts, "UserAccountManager must not be null!");

		this.users = users;
		this.userAccounts = userAccounts;
	}

	/**
	 * Creates a new {@link User} using the information given in the
	 * {@link RegistrationForm}.
	 * 
	 * @param form must not be {@literal null}.
	 * @return the new {@link User} instance.
	 */
	public User createUser(@NonNull RegistrationForm form) {

		UserAccount userAccount = userAccounts.create(form.getName(), form.getPassword(), Role.of("ROLE_CUSTOMER"));
		//mailSender.sendCustomerRegistrationMessage(form.getEmail());
		return users.save(
				new User(userAccount, form.getFirstname(), form.getLastname(), form.getEmail(), form.getAddress()));
	}

	/**
	 * Edits a {@link User} using the information given in the {@link EditForm}.
	 * 
	 * @param form must not be {@literal null}. Contains Id of the {@link User} and
	 *             the entered information. Empty fields in {@link EditForm} will be
	 *             skipped.
	 */
	public void editData(@NonNull EditForm form) {

		long requestedId = Long.parseLong(form.getId());
		User user = findUserById(requestedId);

		if (!form.getFirstname().isEmpty()) {
			user.setFirstname(form.getFirstname());
		}
		if (!form.getLastname().isEmpty()) {
			user.setLastname(form.getLastname());
		}
		if (!form.getAddress().isEmpty()) {
			user.setAddress(form.getAddress());
		}
		if (!form.getEmail().isEmpty()) {
			user.setEmail(form.getEmail());
		}
		if (!form.getPassword().isEmpty()) {
			UserAccount userAccount = user.getUserAccount();
			userAccounts.changePassword(userAccount, form.getPassword());
		}
		return;
	}

	/**
	 * Returns a {@link User} for given {@link UserAccount}.
	 * 
	 * @param userAccount must not be {@literal null}.
	 * @return {@link User} for given {@link UserAccount}.
	 */
	public User findUser(@NonNull UserAccount userAccount) {
		try {
			User user = users.findByUserAccount(userAccount);
			return user;
		}catch (Exception e){
			return null;
		}

	}

	/**
	 * Returns a {@link User} for given Id.
	 * 
	 * @param id must not be {@literal null}.
	 * @return the requested {@link User}.
	 */
	public User findUserById(long id) {

		User user = users.findById(id);
		return user;
	}

	/**
	 * Returns all {@link User} currently saved.
	 * 
	 * @return all {@link User} entities.
	 */
	public Streamable<User> findAll() {
		return Streamable.of(users.findAll());
	}

	/**
	 * Returns all {@link User} with Role of Customer currently saved.
	 * 
	 * @return all {@link User} with Role of Customer.
	 */
	public Streamable<User> findAllCustomers() {
		Iterable<User> userList = findAll();
		List<User> customersList = new ArrayList<User>();

		for (User user : userList) {
			if (user.getUserAccount().hasRole(Role.of("ROLE_CUSTOMER"))) {
				customersList.add(user);
			}
		}

		Iterable<User> employees = customersList;
		return Streamable.of(employees);
	}

	/**
	 * Returns all {@link User} with Role of Employee currently saved.
	 * 
	 * @return all {@link User} with Role of Employee.
	 */
	public Streamable<User> findAllEmployees() {
		Iterable<User> userList = findAll();
		List<User> employeesList = new ArrayList<User>();

		for (User user : userList) {
			if (user.getUserAccount().hasRole(Role.of("ROLE_EMPLOYEE"))) {
				employeesList.add(user);
			}
		}

		Iterable<User> employees = employeesList;
		return Streamable.of(employees);
	}

	/**
	 * Makes it possible to disable or enable a {@link User} for given
	 * {@link UserAccountIdentifier} and type.
	 * 
	 * @param accountId must not be {@literal null}.
	 * @param type      Definition: 0 for disable and 1 for enable.
	 * @throws IllegalArgumentException if no type is given.
	 */
	public void useraccountActivation(@NonNull UserAccountIdentifier accountId, int type) {

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

	/**
	 * Makes it possible to change the Role of a given {@link User}.
	 * 
	 * @param requestedUser must not be {@literal null}. Contains {@link User} which
	 *                      should be changed.
	 * @param loggedIn      must not be {@literal null}. Contains logged in
	 *                      {@link User}.
	 * @param type          Definition: 0 - customer to employee; 1 - employee to
	 *                      customer; 2 - employee to boss (admin); 3 - boss (admin)
	 *                      to employee
	 * @throws UnAllowedException       if somebody try to change his own Role.
	 * @throws IllegalArgumentException if no type is given.
	 */
	public void changeRole(@NonNull User requestedUser, @NonNull User loggedIn, int type) throws UnAllowedException {
		if (requestedUser.getId() == loggedIn.getId()) { // Nutzer möchte sich selbst befördern oder herabstufen
			throw new UnAllowedException("You are not allowed to change your own Role!");
		}

		UserAccount userAccount = requestedUser.getUserAccount();

		switch (type) {
		case 0: // Kunde zum Mitarbeiter machen
			userAccount.add(Role.of("ROLE_EMPLOYEE"));
			userAccount.remove(Role.of("ROLE_CUSTOMER"));
			requestedUser.setSalary(150);
			break;
		case 1: // Mitarbeiter zum Kunde machen
			userAccount.add(Role.of("ROLE_CUSTOMER"));
			userAccount.remove(Role.of("ROLE_EMPLOYEE"));
			requestedUser.setSalary(0);
			break;
		case 2: // Mitarbeiter zum Admin machen
			userAccount.add(Role.of("ROLE_BOSS"));
			break;
		case 3: // Admin zum Mitarbeiter machen
			userAccount.remove(Role.of("ROLE_BOSS"));
			break;
		default:
			throw new IllegalArgumentException("Parameter type has illegal value");
		}
	}

	/**
	 * Adjusts the salary for a {@link User} using the information given in the
	 * {@link MoneyForm}.
	 * 
	 * @param form must not be {@literal null}. Contains Id of the {@link User} and
	 *             the new salary.
	 */
	public void changeSalary(@NonNull MoneyForm form) {

		long requestedId = Long.parseLong(form.getId());
		User user = findUserById(requestedId);
		int salary = Integer.parseInt(form.getSalary());
		user.setSalary(salary);
		return;
	}

}
