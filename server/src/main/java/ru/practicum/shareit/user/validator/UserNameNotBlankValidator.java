package ru.practicum.shareit.user.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserNameNotBlankValidator implements ConstraintValidator<NotBlankForUpdate, String> {

    @Override
    public boolean isValid(String name, ConstraintValidatorContext cxt) {
        if (name == null) {
            return true;
        }
        return !name.isBlank();
    }
}
