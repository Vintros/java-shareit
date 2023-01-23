package ru.practicum.shareit.user.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailCorrectValidator implements ConstraintValidator<EmailForUpdate, String> {

    String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

    @Override
    public boolean isValid(String email, ConstraintValidatorContext cxt) {
        if (email == null) {
            return true;
        }
        return email.matches(regexPattern);
    }
}
