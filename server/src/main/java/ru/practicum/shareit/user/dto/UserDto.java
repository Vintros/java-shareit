package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.user.validator.EmailForUpdate;
import ru.practicum.shareit.user.validator.NotBlankForUpdate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Validated
public class UserDto {

    private Long id;

    @Email(groups = ValidateForCreate.class, message = "incorrect email")
    @EmailForUpdate(groups = ValidateForUpdate.class)
    @NotBlank(groups = ValidateForCreate.class, message = "incorrect email")
    private String email;

    @NotBlankForUpdate(groups = ValidateForUpdate.class)
    @NotBlank(groups = ValidateForCreate.class, message = "the name cannot be empty")
    private String name;

}
