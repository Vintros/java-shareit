package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserServiceImplTestIT {

    private final UserService userService;
    private final EntityManager em;

    @DirtiesContext
    @Test
    void createUser() {
        UserDto userDto = new UserDto(null, "mail@ya.ru", "user");
        userService.createUser(userDto);

        TypedQuery<User> query = em.createQuery("Select u from User u where u.email = :email", User.class);
        User user = query.setParameter("email", userDto.getEmail()).getSingleResult();

        assertEquals(1L, user.getId());
        assertEquals("mail@ya.ru", user.getEmail());
        assertEquals("user", user.getName());
    }

    @DirtiesContext
    @Test
    void updateUser() {
        UserDto userDto = new UserDto(null, "mail@ya.ru", "user");
        userService.createUser(userDto);
        UserDto userDtoUpdate = new UserDto(null, null, "user2");
        userService.updateUser(1L, userDtoUpdate);

        TypedQuery<User> query = em.createQuery("Select u from User u where u.email = :email", User.class);
        User user = query.setParameter("email", userDto.getEmail()).getSingleResult();

        assertEquals(1L, user.getId());
        assertEquals("mail@ya.ru", user.getEmail());
        assertEquals("user2", user.getName());
    }

    @DirtiesContext
    @Test
    void getUserById() {
        UserDto userDto = new UserDto(null, "mail@ya.ru", "user");
        userService.createUser(userDto);

        UserDto receivedUserDto = userService.getUserById(1L);

        assertEquals(1L, receivedUserDto.getId());
        assertEquals("mail@ya.ru", receivedUserDto.getEmail());
        assertEquals("user", receivedUserDto.getName());
    }

    @DirtiesContext
    @Test
    void deleteUserById() {
        UserDto userDto = new UserDto(null, "mail@ya.ru", "user");
        userService.createUser(userDto);
        userService.deleteUserById(1L);

        TypedQuery<User> query = em.createQuery("Select u from User u where u.id = 1L", User.class);

        assertThrows(NoResultException.class, query::getSingleResult);
    }

    @DirtiesContext
    @Test
    void getDtoUsers() {
        UserDto userDto = new UserDto(null, "mail@ya.ru", "user");
        userService.createUser(userDto);
        UserDto userDto2 = new UserDto(null, "mail2@ya.ru", "user2");
        userService.createUser(userDto2);

        List<UserDto> users = userService.getDtoUsers();

        userDto.setId(1L);
        userDto2.setId(2L);
        assertEquals(List.of(userDto, userDto2), users);
    }
}