package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.EmailIncorrectException;
import ru.practicum.shareit.exceptions.UserNameIncorrectException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.MapperUser;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;
    private final MapperUser mapperUser;

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = mapperUser.convertUserDtoToUser(userDto);
        User createdUser = userStorage.createUser(user);
        log.info("User with id: {} created", createdUser.getId());
        return mapperUser.convertUserToUserDto(createdUser);
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        if (userDto.getEmail() != null) {
            checkCorrectEmail(userDto.getEmail());
        }
        if (userDto.getName() != null && userDto.getName().isBlank()) {
            throw new UserNameIncorrectException("the name cannot be empty");
        }
        User updatedUser = userStorage.updateUser(id, userDto);
        log.info("User with id: {} updated", updatedUser.getId());
        return mapperUser.convertUserToUserDto(updatedUser);
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = userStorage.getUserById(id);
        log.info("User with id: {} requested", id);
        return mapperUser.convertUserToUserDto(user);
    }

    @Override
    public void deleteUserById(Long id) {
        userStorage.deleteUserById(id);
        log.info("User with id: {} deleted", id);
    }

    @Override
    public List<UserDto> getDtoUsers() {
        List<User> users = userStorage.getUsers();
        log.info("All users requested");
        return mapperUser.convertAllUsersToUsersDto(users);
    }

    private void checkCorrectEmail(String email) {
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        if (!email.matches(regexPattern)) {
            throw new EmailIncorrectException("incorrect email");
        }
    }
}
