package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserStorage {

    User createUser(User user);

    User updateUser(Long id, UserDto userDto);

    User getUserById(Long id);

    void deleteUserById(Long id);

    List<User> getUsers();

    void checkUserExistsById(Long id);
}
