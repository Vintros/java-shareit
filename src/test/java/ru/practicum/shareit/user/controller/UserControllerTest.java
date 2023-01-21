package ru.practicum.shareit.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.common.exceptions.EntityNotExistsException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    MockMvc mvc;

    @MockBean
    UserService userService;

    Long id;
    Long wrongId;
    UserDto sentUserDto;
    UserDto expectedUserDto;

    @BeforeEach
    void setUp() {
        id = 1L;
        wrongId = 99L;
        sentUserDto = new UserDto(null, "mail@ya.ru", "user");
        expectedUserDto = new UserDto(1L, "mail@ya.ru", "user");
    }

    @SneakyThrows
    @Test
    void createUser_whenIsOk_thenReturnUser() {
        when(userService.createUser(sentUserDto)).thenReturn(expectedUserDto);

        String result = mvc.perform(post("/users")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(sentUserDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(expectedUserDto), result);
        verify(userService, times(1)).createUser(sentUserDto);
    }

    @SneakyThrows
    @Test
    void createUser_whenUserMailNotValid_thenReturnBadRequest() {
        sentUserDto.setEmail("bad");

        mvc.perform(post("/users")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(sentUserDto)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).createUser(any());
    }

    @SneakyThrows
    @Test
    void createUser_whenUserNameNotValid_thenReturnBadRequest() {
        sentUserDto.setName("  ");

        mvc.perform(post("/users")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(sentUserDto)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).createUser(any());
    }

    @SneakyThrows
    @Test
    void createUser_whenSentNoBody_thenError() {
        mvc.perform(post("/users")
                        .contentType("application/json"))
                .andExpect(status().isInternalServerError());

        verify(userService, never()).createUser(any());
    }

    @SneakyThrows
    @Test
    void updateUser_whenIsOk_thenReturnUpdatedUser() {
        sentUserDto.setName("name2");
        sentUserDto.setEmail("mail2@ya.ru");
        expectedUserDto.setName("name2");
        expectedUserDto.setEmail("mail2@ya.ru");
        when(userService.updateUser(id, sentUserDto)).thenReturn(expectedUserDto);


        String result = mvc.perform(patch("/users/{id}", id)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(sentUserDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(expectedUserDto), result);
        verify(userService, times(1)).updateUser(id, sentUserDto);
    }

    @SneakyThrows
    @Test
    void updateUser_whenSentNoBody_thenReturnError() {
        mvc.perform(patch("/users/{id}", id)
                        .contentType("application/json"))
                .andExpect(status().isInternalServerError());

        verify(userService, never()).updateUser(any(), any());
    }

    @SneakyThrows
    @Test
    void updateUser_whenUserNameNotValid_thenReturnBadRequest() {
        sentUserDto.setName("  ");

        mvc.perform(patch("/users/{id}", id)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(sentUserDto)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).updateUser(any(), any());
    }

    @SneakyThrows
    @Test
    void updateUser_whenUserEmailNotValid_thenReturnBadRequest() {
        sentUserDto.setEmail("bad");

        mvc.perform(patch("/users/{id}", id)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(sentUserDto)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).updateUser(any(), any());
    }

    @SneakyThrows
    @Test
    void updateUser_whenUserIdNotValid_thenReturnBadRequest() {
        mvc.perform(patch("/users/{id}", "notNumber")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(sentUserDto)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).updateUser(any(), any());
    }

    @SneakyThrows
    @Test
    void getUsers_whenNoUsers_thenReturnEmptyList() {
        String result = mvc.perform(get("/users")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(List.of()), result);
        verify(userService, times(1)).getDtoUsers();
    }

    @SneakyThrows
    @Test
    void getUsers_whenUserExist_thenReturnUsersList() {
        when(userService.getDtoUsers()).thenReturn(List.of(expectedUserDto));

        String result = mvc.perform(get("/users")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(List.of(expectedUserDto)), result);
        verify(userService, times(1)).getDtoUsers();
    }

    @SneakyThrows
    @Test
    void getUserById_whenUserFounded_thenReturnUser() {
        when(userService.getUserById(id)).thenReturn(expectedUserDto);

        String result = mvc.perform(get("/users/{id}", id)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(expectedUserDto), result);
        verify(userService, times(1)).getUserById(id);
    }

    @SneakyThrows
    @Test
    void getUserById_whenUserIdNotNumber_thenReturnBadRequest() {
        mvc.perform(get("/users/{id}", "notNumber")
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());

        verify(userService, never()).getUserById(any());
    }

    @SneakyThrows
    @Test
    void getUserById_whenUserNotFounded_thenReturnEntityNotExistsException() {
        when(userService.getUserById(wrongId)).thenThrow(EntityNotExistsException.class);

        mvc.perform(get("/users/{id}", wrongId)
                        .contentType("application/json"))
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    void deleteUser_whenUserIdNotNumber_thenReturnBadRequest() {
        mvc.perform(delete("/users/{id}", "notNumber")
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());

        verify(userService, never()).deleteUserById(any());
    }

    @SneakyThrows
    @Test
    void deleteUser() {
        mvc.perform(delete("/users/{id}", id)
                .contentType("application/json"));

        verify(userService, times(1)).deleteUserById(id);
    }
}