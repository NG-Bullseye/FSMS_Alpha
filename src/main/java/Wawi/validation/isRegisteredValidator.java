package Wawi.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import Wawi.user.User;
import Wawi.user.UserManagement;

/**
 * @author Daniel Koersten
 *
 */
public class isRegisteredValidator implements ConstraintValidator<isRegistered, String> {

	private final UserManagement userManagement;

	public isRegisteredValidator(UserManagement userManagement) {
		this.userManagement = userManagement;

	}

	@Override
	public void initialize(isRegistered inputEmail) {
		// empty
	}

	@Override
	public boolean isValid(String inputValue, ConstraintValidatorContext context) {

		Iterable<User> userList = userManagement.findAll();
		for (User user : userList) {
			if (user.getEmail().equals(inputValue)) {
				return false;
			}
		}

		return true;
	}

}
