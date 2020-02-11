package validators;

import play.data.validation.Constraints.*;
import play.libs.F;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PercentageValidator extends Validator<Double> implements ConstraintValidator<Percentage, Double> {

    @Override
    public boolean isValid(Double value) {

        return this.isValid(value, null);

    }

    @Override
    public F.Tuple<String, Object[]> getErrorMessageKey() {
        return new F.Tuple<>(
                "Since this number is a percentage, can not be greater than one", new Object[]{""}
        );
    }

    @Override
    public void initialize(Percentage constraintAnnotation) {

    }

    @Override
    public boolean isValid(Double value, ConstraintValidatorContext context) {
        if(value < 1)
        {
            return true;
        }

        return false;
    }
}
