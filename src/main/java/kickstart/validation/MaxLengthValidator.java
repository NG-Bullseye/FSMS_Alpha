package kickstart.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MaxLengthValidator implements ConstraintValidator<MaxLength, String> {
	
	@Override
    public void initialize(MaxLength input) {
    }
	
	@Override
	public boolean isValid(String inputValue, ConstraintValidatorContext context) {
		
		int length = inputValue.length();
		if (length > 255) {
			return false;
		}
		
		return true;
	}
 
    
 
}