package ru.practicum.shareit.user.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UserNameNotBlankValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotBlankForUpdate {

    String message() default "the name cannot be empty";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
