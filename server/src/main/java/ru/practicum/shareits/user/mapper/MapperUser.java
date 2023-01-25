package ru.practicum.shareits.user.mapper;

import org.springframework.stereotype.Service;
import ru.practicum.shareits.user.dto.UserDto;
import ru.practicum.shareits.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MapperUser {

    public User convertUserDtoToUser(UserDto userDto) {
        return new User(null, userDto.getEmail(), userDto.getName());
    }

    public UserDto convertUserToUserDto(User user) {
        return new UserDto(user.getId(), user.getEmail(), user.getName());
    }

    public List<UserDto> convertAllUsersToUsersDto(List<User> users) {
        return users.stream()
                .map(this::convertUserToUserDto)
                .collect(Collectors.toList());
    }
}
