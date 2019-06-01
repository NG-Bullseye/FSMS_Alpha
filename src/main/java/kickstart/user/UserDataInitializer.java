package kickstart.user;

import java.util.Arrays;

import org.salespointframework.core.DataInitializer;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Initalizes {@link User}.
 * 
 * @author Daniel Koersten
 */
@Component
@Order(10)
class UserDataInitializer implements DataInitializer {

	private final UserAccountManager userAccountManager;
	private final UserRepository userRepository;
	private static final int salary = 0;
	private static final int bigSalary = 0;

	/**
	 * Creates a new {@link UserDataInitializer} with the given
	 * {@link UserAccountManager} and {@link UserRepository}.
	 * 
	 * @param userAccountManager must not be {@literal null}.
	 * @param userRepository     must not be {@literal null}.
	 */
	UserDataInitializer(UserAccountManager userAccountManager, UserRepository userRepository) {

		Assert.notNull(userRepository, "UserRepository must not be null!");
		Assert.notNull(userAccountManager, "UserAccountManager must not be null!");

		this.userAccountManager = userAccountManager;
		this.userRepository = userRepository;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.salespointframework.core.DataInitializer#initialize()
	 */
	@Override
	public void initialize() {

		if (userAccountManager.findByUsername("admin").isPresent()||userAccountManager.findByUsername("chef").isPresent()||userAccountManager.findByUsername("mitarbeiter").isPresent()) {
			return;
		}

		UserAccount ua1 = userAccountManager.create("admin", "123", Role.of("ROLE_EMPLOYEE"));
		ua1.add(Role.of("ROLE_MANAGER"));
		ua1.add(Role.of("ROLE_ADMIN"));

		UserAccount ua2 = userAccountManager.create("mitarbeiter", "123", Role.of("ROLE_EMPLOYEE"));

		UserAccount ua3 = userAccountManager.create("chef", "123", Role.of("ROLE_EMPLOYEE"));
		ua3.add(Role.of("ROLE_MANAGER"));

		User c1 = new User(ua1, "Karsten", "unbekannt", "Karstrn@gmail.com", "irgendwo in Berlin");
		User c2 = new User(ua2, "Leonard", "Wecke", "Leonard.r.Wecke@gmail.com", "Wu7, Dresden");
		User c3 = new User(ua3, "Toni", "Stark", "ironman@gmail.com", "Private Straße 15, NY");
		c2.setSalary(salary);
		c3.setSalary(bigSalary);

		userRepository.saveAll(Arrays.asList(c1, c2, c3));
	}
}
