package ru.practicum.shareit.user.mapper;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

@Service
public class MapperUser {

    public User convertUserDtoToUser(UserDto userDto) {
        return new User(null, userDto.getEmail(), userDto.getName());
    }

    public UserDto convertUserToUserDto(User user) {
        return new UserDto(user.getId(), user.getEmail(), user.getName());
    }

    public List<UserDto> convertAllUsersToUsersDto(List<User> users) {
        List<UserDto> usersDto = new ArrayList<>();
        for (User user : users) {
            usersDto.add(convertUserToUserDto(user));
        }
        return usersDto;
    }
}
