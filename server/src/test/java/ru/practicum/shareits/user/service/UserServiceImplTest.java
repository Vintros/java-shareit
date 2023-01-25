package ru.practicum.shareits.user.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareits.common.exceptions.EntityNotExistsException;
import ru.practicum.shareits.user.dto.UserDto;
import ru.practicum.shareits.user.mapper.MapperUser;
import ru.practicum.shareits.user.model.User;
import ru.practicum.shareits.user.storage.UserRepository;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    private UserRepository userRepository;
    private UserService userService;

    private Long id;
    private Long wrongId;
    private UserDto sentUserDto;
    private User userWithoutId;
    private User user;
    private UserDto expectedUserDto;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        MapperUser mapperUser = new MapperUser();
        userService = new UserServiceImpl(userRepository, mapperUser);

        id = 1L;
        wrongId = 99L;
        sentUserDto = new UserDto(null, "mail@ya.ru", "user");
        userWithoutId = new User(null, "mail@ya.ru", "user");
        user = new User(1L, "mail@ya.ru", "user");
        expectedUserDto = new UserDto(1L, "mail@ya.ru", "user");
    }

    @Test
    void createUser_whenUserReceived_thenReturnedUser() {
        when(userRepository.save(userWithoutId)).thenReturn(user);

        UserDto actualUserDto = userService.createUser(sentUserDto);

        assertEquals(expectedUserDto, actualUserDto);
        verify(userRepository, times(1)).save(userWithoutId);
    }

    @Test
    void updateUser_whenUserReceived_thenReturnedUser() {
        UserDto sentUpdatedUserDto = new UserDto(null, "mail2@ya.ru", "user2");
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        User updatedUser = new User(1L, "mail2@ya.ru", "user2");
        when(userRepository.save(updatedUser)).thenReturn(updatedUser);
        UserDto respondedUserDto = new UserDto(1L, "mail2@ya.ru", "user2");

        UserDto actualUserDto = userService.updateUser(id, sentUpdatedUserDto);

        assertEquals(respondedUserDto, actualUserDto);
        InOrder inOrder = inOrder(userRepository);
        inOrder.verify(userRepository, times(1)).findById(id);
        inOrder.verify(userRepository, times(1)).save(updatedUser);
    }

    @Test
    void updateUser_whenUpdateReceivedOnlyMail_thenReturnedUser() {
        UserDto sentUpdatedUserDto = new UserDto(null, "mail2@ya.ru", null);
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        User updatedUser = new User(1L, "mail2@ya.ru", "user");
        when(userRepository.save(updatedUser)).thenReturn(updatedUser);
        UserDto respondedUserDto = new UserDto(1L, "mail2@ya.ru", "user");

        UserDto actualUserDto = userService.updateUser(id, sentUpdatedUserDto);

        assertEquals(respondedUserDto, actualUserDto);
        InOrder inOrder = inOrder(userRepository);
        inOrder.verify(userRepository, times(1)).findById(id);
        inOrder.verify(userRepository, times(1)).save(updatedUser);
    }


    @Test
    void updateUser_whenUpdateReceivedOnlyName_thenReturnedUser() {
        UserDto sentUpdatedUserDto = new UserDto(null, null, "user2");
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        User updatedUser = new User(1L, "mail@ya.ru", "user2");
        when(userRepository.save(updatedUser)).thenReturn(updatedUser);
        UserDto respondedUserDto = new UserDto(1L, "mail@ya.ru", "user2");

        UserDto actualUserDto = userService.updateUser(id, sentUpdatedUserDto);

        assertEquals(respondedUserDto, actualUserDto);
        InOrder inOrder = inOrder(userRepository);
        inOrder.verify(userRepository, times(1)).findById(id);
        inOrder.verify(userRepository, times(1)).save(updatedUser);
    }

    @Test
    void updateUser_whenUserIdWrong_thenEntityNotExistsException() {
        UserDto sentUpdatedUserDto = new UserDto(null, "mail2@ya.ru", "user2");
        when(userRepository.findById(wrongId)).thenReturn(Optional.empty());

        assertThrows(EntityNotExistsException.class, () -> userService.updateUser(wrongId, sentUpdatedUserDto));
        verify(userRepository, never()).save(any(User.class));
    }


    @Test
    void getUserById_wWhenUserFounded_thenReturnUser() {
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        UserDto actualUserDto = userService.getUserById(id);

        assertEquals(expectedUserDto, actualUserDto);
    }

    @Test
    void getUserById_whenUserIdWrong_thenEntityNotExistsException() {
        when(userRepository.findById(wrongId)).thenReturn(Optional.empty());

        assertThrows(EntityNotExistsException.class, () -> userService.getUserById(wrongId));
    }

    @Test
    void deleteUserById_whenUserFounded_thenDelete() {
        when(userRepository.existsById(id)).thenReturn(true);

        userService.deleteUserById(id);

        verify(userRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteUserById_whenUserIdNotFounded_thenEntityNotExistsException() {
        when(userRepository.existsById(wrongId)).thenReturn(false);

        assertThrows(EntityNotExistsException.class, () -> userService.deleteUserById(wrongId));
    }

    @Test
    void getDtoUsers_whenUserFounded_thenReturnListOfUser() {
        when(userRepository.findAll()).thenReturn(List.of(user));
        List<UserDto> usersDto = List.of(expectedUserDto);

        List<UserDto> actualUsersDto = userService.getDtoUsers();

        assertEquals(usersDto, actualUsersDto);
    }

    @Test
    void getDtoUsers_whenNoUsers_thenReturnEmptyList() {
        List<UserDto> usersDto = List.of();

        List<UserDto> actualUsersDto = userService.getDtoUsers();

        assertEquals(usersDto, actualUsersDto);
    }
}