package pl.ftims.ias.your_climbing_gym.dto.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;


public class EmailValidation implements ConstraintValidator<Email, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null) {
            return true;
        }
        Pattern p = Pattern.compile("^[-!#$%&*+-/=?^_`{|}~a-z0-9]+@[a-z]+.[a-z]{2,5}$");
        return p.matcher(s.toLowerCase()).matches();
    }
}
