package ru.practicum.shareitg.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareitg.user.validator.EmailForUpdate;
import ru.practicum.shareitg.user.validator.NotBlankForUpdate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Validated
public class UserRequestDto {

    @Email(groups = ValidateForCreate.class, message = "mega incorrect email")
    @EmailForUpdate(groups = ValidateForUpdate.class)
    @NotBlank(groups = ValidateForCreate.class, message = "empty email")
    private String email;

    @NotBlankForUpdate(groups = ValidateForUpdate.class)
    @NotBlank(groups = ValidateForCreate.class, message = "the name cannot be empty")
    private String name;


}
