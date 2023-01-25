package ru.practicum.shareitg.user.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UserNameNotBlankValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotBlankForUpdate {

    String message() default "incorrect name";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
