package ru.practicum.shareitg.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareitg.user.dto.UserRequestDto;
import ru.practicum.shareitg.user.dto.ValidateForCreate;
import ru.practicum.shareitg.user.dto.ValidateForUpdate;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {

    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody @Validated(ValidateForCreate.class) UserRequestDto userRequestDto) {
        log.info("Creating user={}", userRequestDto);
        return userClient.createUser(userRequestDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable Long id,
                              @Validated(ValidateForUpdate.class) @RequestBody UserRequestDto userRequestDto) {
        log.info("Updating user {}, userId={}", userRequestDto, id);
        return userClient.updateUser(id, userRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getUsers() {
        log.info("Get all users");
        return userClient.getDtoUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable Long id) {
        log.info("Get user by id={}", id);
        return userClient.getUserById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long id) {
        log.info("Delete user by id={}", id);
        return userClient.deleteUserById(id);
    }





}
