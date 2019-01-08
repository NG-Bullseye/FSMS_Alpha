package kickstart.validation;

import java.lang.annotation.*;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * @author Daniel Koersten
 *
 */
@Documented
@Constraint(validatedBy = isRegisteredValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface isRegistered {
    String message() default "E-Mail is already registered!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}