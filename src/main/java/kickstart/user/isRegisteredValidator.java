package kickstart.user;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class isRegisteredValidator implements ConstraintValidator<isRegistered, String> {
	
	private final UserManagement userManagement;
	
	public isRegisteredValidator(UserManagement userManagement) {
		this.userManagement = userManagement;
		
	}
	
	@Override
    public void initialize(isRegistered inputEmail) {
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