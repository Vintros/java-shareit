package ru.practicum.shareitg.user.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EmailCorrectValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EmailForUpdate {

    String message() default "incorrect email";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}