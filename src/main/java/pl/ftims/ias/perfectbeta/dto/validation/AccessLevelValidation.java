package pl.ftims.ias.perfectbeta.dto.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AccessLevelValidation implements ConstraintValidator<AccessLevel, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return (value.equalsIgnoreCase("CLIMBER") ||
                value.equalsIgnoreCase("MANAGER") ||
                value.equalsIgnoreCase("ADMINISTRATOR"));
    }
}
