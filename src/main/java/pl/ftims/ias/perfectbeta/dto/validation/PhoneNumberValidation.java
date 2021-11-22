package pl.ftims.ias.perfectbeta.dto.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;


public class PhoneNumberValidation implements ConstraintValidator<PhoneNumber, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        Pattern p = Pattern.compile("^[+]?[-\\s0-9]{9,15}$");
        return p.matcher(value.toLowerCase()).matches();
    }
}
