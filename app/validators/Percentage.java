package validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({java.lang.annotation.ElementType.FIELD})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PercentageValidator.class)
public @interface Percentage {
    String message() default "Since this number is a percentage, can not be greater than one";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
