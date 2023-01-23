package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.common.exceptions.EntityNotExistsException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.MapperUser;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final MapperUser mapperUser;

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = mapperUser.convertUserDtoToUser(userDto);
        User createdUser = userRepository.save(user);
        log.info("User with id: {} created", createdUser.getId());
        return mapperUser.convertUserToUserDto(createdUser);
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotExistsException("such user not registered"));
        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }
        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }
        User updatedUser = userRepository.save(user);
        log.info("User with id: {} updated", id);
        return mapperUser.convertUserToUserDto(updatedUser);
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotExistsException("such user not registered"));
        log.info("User with id: {} requested", id);
        return mapperUser.convertUserToUserDto(user);
    }

    @Override
    public void deleteUserById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotExistsException("such user not registered");
        }
        userRepository.deleteById(id);
        log.info("User with id: {} deleted", id);
    }

    @Override
    public List<UserDto> getDtoUsers() {
        List<User> users = userRepository.findAll();
        log.info("All users requested");
        return mapperUser.convertAllUsersToUsersDto(users);
    }
}
