package Wawi.View;

import static org.assertj.core.api.Assertions.*;
import Wawi.AbstractWebIntegrationTests;
import Wawi.Controller.MainController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;


class ManagerControllerTest extends AbstractWebIntegrationTests {

	private final static String URI = "/";
	@Autowired
	MainController mainController;


	@Test
	void testCommitManager() {

		UserAccount userAccount= new UserAccount();
		userAccount.add(Role.of("ROLE_MANAGER"));
		Optional<UserAccount> loggedInUserWeb=Optional.of(userAccount);

		Model model = new ExtendedModelMap();
		String reset="";

		String returnedView = mainController.refreshView(
				model,
				reset
			, loggedInUserWeb);

		assertThat(returnedView).isEqualTo("ManagerView");

		//Iterable<Object> object = (Iterable<Object>) model.asMap().get("ManagerView");

		//assertThat(object).hasSize(9);
	}

}