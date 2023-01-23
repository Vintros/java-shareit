package ru.practicum.shareit.user.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MapperUserTest {

    private final MapperUser mapperUser = new MapperUser();
    private User user;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        user = new User(null, "mail@ya.ru", "name");
        userDto = new UserDto(null, "mail@ya.ru", "name");
    }


    @Test
    void convertUserDtoToUser_withoutId_thenReturnWithoutId() {
        User actualUser = mapperUser.convertUserDtoToUser(userDto);

        assertEquals(user, actualUser);
    }

    @Test
    void convertUserDtoToUser_withId_thenReturnWithoutId() {
        userDto.setId(1L);

        User actualUser = mapperUser.convertUserDtoToUser(userDto);

        assertEquals(user, actualUser);
    }

    @Test
    void convertUserToUserDto_withoutId_thenReturnWithoutId() {
        UserDto actualUserDto = mapperUser.convertUserToUserDto(user);

        assertEquals(userDto, actualUserDto);
    }

    @Test
    void convertUserToUserDto_withId_thenReturnWithId() {
        userDto.setId(1L);
        user.setId(1L);

        UserDto actualUserDto = mapperUser.convertUserToUserDto(user);

        assertEquals(userDto, actualUserDto);
    }

    @Test
    void convertAllUsersToUsersDto_whenListOfUser_thenReturnListOfUserDto() {
        List<UserDto> usersDto = List.of(userDto);

        List<UserDto> actualUsersDto = mapperUser.convertAllUsersToUsersDto(List.of(user));

        assertEquals(usersDto, actualUsersDto);
    }

    @Test
    void convertAllUsersToUsersDto_whenNoUsers_thenReturnEmptyList() {
        List<UserDto> usersDto = new ArrayList<>();

        List<UserDto> actualUsersDto = mapperUser.convertAllUsersToUsersDto(List.of());

        assertEquals(usersDto, actualUsersDto);
    }
}