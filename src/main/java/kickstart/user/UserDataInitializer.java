package kickstart.user;

import java.util.Arrays;

import org.salespointframework.core.DataInitializer;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
@Order(10)
class UserDataInitializer implements DataInitializer {

	private final UserAccountManager userAccountManager;
	private final UserRepository customerRepository;

	UserDataInitializer(UserAccountManager userAccountManager, UserRepository customerRepository) {

		Assert.notNull(customerRepository, "UserRepository must not be null!");
		Assert.notNull(userAccountManager, "UserAccountManager must not be null!");

		this.userAccountManager = userAccountManager;
		this.customerRepository = customerRepository;
	}

	@Override
	public void initialize() {

		if (userAccountManager.findByUsername("chef").isPresent()) {
			return;
		}

		UserAccount bossAccount = userAccountManager.create("boss", "123", Role.of("ROLE_BOSS"));
		userAccountManager.save(bossAccount);

		Role customerRole = Role.of("ROLE_CUSTOMER");

		UserAccount ua1 = userAccountManager.create("daniel", "123", Role.of("ROLE_CUSTOMER"));
		UserAccount ua2 = userAccountManager.create("mitarbeitah", "123", Role.of("ROLE_EMPLOYEE"));
		UserAccount ua3 = userAccountManager.create("chef", "123", Role.of("ROLE_BOSS"));
		ua3.add(Role.of("ROLE_EMPLOYEE"));

		User c1 = new User(ua1, "Daniel", "Körsten", "daniel@beispiel.de", "Coole Straße 18, Gera");
		User c2 = new User(ua2, "Alfons", "Zitterbacke", "alfons@moebl-hier.de", "Arbeiterstraße 1, Dresden");
		User c3 = new User(ua3, "Daniel", "Matusek", "besterTutor@tu-dresden.de", "Private Straße 15, Dresden");

		customerRepository.saveAll(Arrays.asList(c1, c2, c3));
	}
}
