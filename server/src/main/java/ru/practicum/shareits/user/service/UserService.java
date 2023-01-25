package ru.practicum.shareits.user.service;

import ru.practicum.shareits.user.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto createUser(UserDto userDto);

    UserDto updateUser(Long id, UserDto userDto);

    UserDto getUserById(Long id);

    void deleteUserById(Long id);

    List<UserDto> getDtoUsers();
}
