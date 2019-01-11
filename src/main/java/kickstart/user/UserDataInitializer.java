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
	private static final int salary = 150;
	private static final int bigSalary = 300;

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

		if (userAccountManager.findByUsername("chef").isPresent()) {
			return;
		}

		UserAccount ua1 = userAccountManager.create("daniel", "123", Role.of("ROLE_CUSTOMER"));
		UserAccount ua4 = userAccountManager.create("hans", "123", Role.of("ROLE_CUSTOMER"));
		UserAccount ua2 = userAccountManager.create("mitarbeitah", "123", Role.of("ROLE_EMPLOYEE"));
		UserAccount ua3 = userAccountManager.create("chef", "123", Role.of("ROLE_BOSS"));
		ua3.add(Role.of("ROLE_EMPLOYEE"));

		User c1 = new User(ua1, "Daniel", "Körsten", "daniel@beispiel.de", "Coole Straße 18, Gera");
		User c4 = new User(ua4, "Hans", "Schrödinger", "hans@katze.de", "Katzenweg 18, 06688 Krumbach");
		User c2 = new User(ua2, "Alfons", "Zitterbacke", "alfons@moebl-hier.de", "Arbeiterstraße 1, Dresden");
		User c3 = new User(ua3, "Daniel", "Matusek", "besterTutor@tu-dresden.de", "Private Straße 15, Dresden");
		c2.setSalary(salary);
		c3.setSalary(bigSalary);

		userRepository.saveAll(Arrays.asList(c1, c2, c3, c4));
	}
}
