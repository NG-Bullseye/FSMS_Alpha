package kickstart.validation;

import java.lang.annotation.*;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = MaxLengthValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface MaxLength {
    String message() default "Eingabe zu lang!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}