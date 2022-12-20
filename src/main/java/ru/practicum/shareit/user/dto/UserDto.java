package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDto {

    private Long id;
    @Email
    @NotBlank(message = "incorrect email")
    private String email;
    @NotBlank(message = "the name cannot be empty")
    private String name;

}
