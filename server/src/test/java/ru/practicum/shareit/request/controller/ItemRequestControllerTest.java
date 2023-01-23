package ru.practicum.shareit.request.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.common.model.FromSizeRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
class ItemRequestControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ItemRequestService itemRequestService;

    private Long id;
    private Long userId;
    private ItemRequestDto sentItemRequestDto;
    private ItemRequestDto expectedItemRequestDto;


    @BeforeEach
    void setUp() {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        id = 1L;
        userId = 2L;
        Long wrongId = 99L;
        LocalDateTime time = LocalDateTime.now();

        sentItemRequestDto = new ItemRequestDto();
        sentItemRequestDto.setDescription("useful thing");
        sentItemRequestDto.setCreated(time);

        expectedItemRequestDto = new ItemRequestDto();
        expectedItemRequestDto.setId(id);
        expectedItemRequestDto.setUserId(userId);
        expectedItemRequestDto.setDescription("useful thing");
        expectedItemRequestDto.setCreated(time);
    }

    @SneakyThrows
    @Test
    void addRequest_whenIsOk_thenReturnRequest() {
        when(itemRequestService.addRequest(sentItemRequestDto, userId)).thenReturn(expectedItemRequestDto);

        String result = mvc.perform(post("/requests")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(sentItemRequestDto))
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(expectedItemRequestDto), result);
        verify(itemRequestService, times(1)).addRequest(sentItemRequestDto, userId);
    }

    @SneakyThrows
    @Test
    void addRequest_whenNoIdHeader_thenReturnError() {
        mvc.perform(post("/requests")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(sentItemRequestDto)))
                .andExpect(status().isInternalServerError());

        verify(itemRequestService, never()).addRequest(sentItemRequestDto, userId);
    }

    @SneakyThrows
    @Test
    void addRequest_whenIdNotNumber_thenReturnError() {
        mvc.perform(post("/requests")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(sentItemRequestDto))
                        .header("X-Sharer-User-Id", "notNumber"))
                .andExpect(status().isBadRequest());

        verify(itemRequestService, never()).addRequest(sentItemRequestDto, userId);
    }

    @SneakyThrows
    @Test
    void addRequest_whenRequestNotValid_thenReturnError() {
        sentItemRequestDto.setDescription("  ");

        mvc.perform(post("/requests")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(sentItemRequestDto))
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isBadRequest());

        verify(itemRequestService, never()).addRequest(sentItemRequestDto, userId);
    }

    @SneakyThrows
    @Test
    void getOwnRequests_whenIsOk_thenReturnListOfRequests() {
        when(itemRequestService.getOwnRequests(userId)).thenReturn(List.of(expectedItemRequestDto));

        String result = mvc.perform(get("/requests")
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(List.of(expectedItemRequestDto)), result);
        verify(itemRequestService, times(1)).getOwnRequests(userId);
    }


    @SneakyThrows
    @Test
    void getOwnRequests_whenNoIdHeader_thenReturnError() {
        mvc.perform(get("/requests")
                        .contentType("application/json"))
                .andExpect(status().isInternalServerError());

        verify(itemRequestService, never()).getOwnRequests(userId);
    }

    @SneakyThrows
    @Test
    void getOwnRequests_whenIdNotNumber_thenReturnError() {
        mvc.perform(get("/requests")
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", "notNumber"))
                .andExpect(status().isBadRequest());

        verify(itemRequestService, never()).getOwnRequests(userId);
    }

    @SneakyThrows
    @Test
    void getRequestById_whenIsOk_thenReturnRequest() {
        when(itemRequestService.getRequestById(id, userId)).thenReturn(expectedItemRequestDto);

        String result = mvc.perform(get("/requests/{requestId}", id)
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(expectedItemRequestDto), result);
        verify(itemRequestService, times(1)).getRequestById(id, userId);
    }

    @SneakyThrows
    @Test
    void getRequestById_whenNoIdHeader_thenReturnError() {
        mvc.perform(get("/requests/{requestId}", id)
                        .contentType("application/json"))
                .andExpect(status().isInternalServerError());
    }

    @SneakyThrows
    @Test
    void getRequestById_whenIdNotNumber_thenReturnError() {
        mvc.perform(get("/requests/{requestId}", id)
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", "notNumber"))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void getAllRequests_whenIsOk_thenReturnRequestsList() {
        Sort sort = Sort.by("created").descending();
        Pageable pageable = FromSizeRequest.of(0, 10, sort);
        when(itemRequestService.getAllRequests(userId, pageable)).thenReturn(List.of(expectedItemRequestDto));

        String result = mvc.perform(get("/requests/all")
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", userId)
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(List.of(expectedItemRequestDto)), result);
        verify(itemRequestService, times(1)).getAllRequests(userId, pageable);
    }


    @SneakyThrows
    @Test
    void getAllRequests_whenNoHeaderId_thenReturnError() {
        mvc.perform(get("/requests/all")
                        .contentType("application/json")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isInternalServerError());
    }

    @SneakyThrows
    @Test
    void getAllRequests_whenFromNotValid_thenReturnError() {
        mvc.perform(get("/requests/all")
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", userId)
                        .param("from", "-1")
                        .param("size", "10"))
                .andExpect(status().isInternalServerError());
    }

    @SneakyThrows
    @Test
    void getAllRequests_whenSizeNotValid_thenReturnError() {
        mvc.perform(get("/requests/all")
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", userId)
                        .param("from", "0")
                        .param("size", "0"))
                .andExpect(status().isInternalServerError());
    }

    @SneakyThrows
    @Test
    void getAllRequests_whenIdNotValid_thenReturnError() {
        mvc.perform(get("/requests/all")
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", "notNumber")
                        .param("from", "0")
                        .param("size", "0"))
                .andExpect(status().isBadRequest());
    }
}