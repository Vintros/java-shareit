package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.ValidateForCreate;
import ru.practicum.shareit.user.dto.ValidateForUpdate;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
@Validated
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserDto createUser(@Validated(ValidateForCreate.class) @RequestBody UserDto userDto) {
        return userService.createUser(userDto);
    }

    @PatchMapping("/{id}")
    public UserDto updateUser(@PathVariable Long id, @Validated(ValidateForUpdate.class) @RequestBody UserDto userDto) {
        return userService.updateUser(id, userDto);
    }

    @GetMapping
    public List<UserDto> getUsers() {
        return userService.getDtoUsers();
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
    }
}
