package ru.practicum.shareit.booking.controller;

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
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.enums.State;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.common.model.FromSizeRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
class BookingControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private  MockMvc mvc;

    @MockBean
    private   BookingService bookingService;

    private  Long id;
    private  Long userId;
    private  BookingDtoRequest bookingDtoRequest;
    private   BookingDto expectedBookingDto;

    @BeforeEach
    void setUp() {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        id = 1L;
        userId = 2L;
        Long wrongId = 99L;
        LocalDateTime start = LocalDateTime.now().plusDays(2);
        LocalDateTime end = LocalDateTime.now().plusDays(3);

        bookingDtoRequest = new BookingDtoRequest();
        bookingDtoRequest.setItemId(id);
        bookingDtoRequest.setStart(start);
        bookingDtoRequest.setEnd(end);

        expectedBookingDto = new BookingDto();
        expectedBookingDto.setId(id);
        expectedBookingDto.setStart(start);
        expectedBookingDto.setEnd(end);
        expectedBookingDto.setStatus(Status.WAITING);
        expectedBookingDto.setBooker(new User(userId, "mail@ya.ru", "name"));
        expectedBookingDto.setItem(new Item(id, null, "item", "useful item", true, List.of(), null));

    }

    @SneakyThrows
    @Test
    void addBooking_whenIsOk_thenReturnBooking() {
        when(bookingService.addBooking(userId, bookingDtoRequest)).thenReturn(expectedBookingDto);

        String result = mvc.perform(post("/bookings")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(bookingDtoRequest))
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(expectedBookingDto), result);
        verify(bookingService, times(1)).addBooking(userId, bookingDtoRequest);
    }


    @SneakyThrows
    @Test
    void addBooking_whenNoId_thenReturnError() {
        mvc.perform(post("/bookings")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(bookingDtoRequest)))
                .andExpect(status().isInternalServerError());

        verify(bookingService, never()).addBooking(userId, bookingDtoRequest);
    }

    @SneakyThrows
    @Test
    void addBooking_whenNoBody_thenReturnError() {
        mvc.perform(post("/bookings")
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", id))
                .andExpect(status().isInternalServerError());

        verify(bookingService, never()).addBooking(userId, bookingDtoRequest);
    }

    @SneakyThrows
    @Test
    void addBooking_whenBookingItemIdNotValid_thenReturnBadRequest() {
        bookingDtoRequest.setItemId(null);

        mvc.perform(post("/bookings")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(bookingDtoRequest))
                        .header("X-Sharer-User-Id", id))
                .andExpect(status().isBadRequest());

        verify(bookingService, never()).addBooking(userId, bookingDtoRequest);
    }

    @SneakyThrows
    @Test
    void addBooking_whenBookingStartNotValid_thenReturnBadRequest() {
        bookingDtoRequest.setStart(LocalDateTime.now().minusDays(1));

        mvc.perform(post("/bookings")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(bookingDtoRequest))
                        .header("X-Sharer-User-Id", id))
                .andExpect(status().isBadRequest());

        verify(bookingService, never()).addBooking(userId, bookingDtoRequest);
    }

    @SneakyThrows
    @Test
    void addBooking_whenBookingEndNotValid_thenReturnBadRequest() {
        bookingDtoRequest.setEnd(null);

        mvc.perform(post("/bookings")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(bookingDtoRequest))
                        .header("X-Sharer-User-Id", id))
                .andExpect(status().isBadRequest());

        verify(bookingService, never()).addBooking(userId, bookingDtoRequest);
    }

    @SneakyThrows
    @Test
    void considerationBooking_whenIsOk_thenReturnBooking() {
        when(bookingService.considerationBooking(userId, 1L, true)).thenReturn(expectedBookingDto);

        String result = mvc.perform(patch("/bookings/{bookingId}", 1L)
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", userId)
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(expectedBookingDto), result);
        verify(bookingService, times(1)).considerationBooking(userId, 1L, true);
    }


    @SneakyThrows
    @Test
    void considerationBooking_whenNoId_thenReturnError() {
        mvc.perform(patch("/bookings/{bookingId}", 1L)
                        .contentType("application/json")
                        .param("approved", "true"))
                .andExpect(status().isInternalServerError());

        verify(bookingService, never()).considerationBooking(any(), any(), anyBoolean());
    }

    @SneakyThrows
    @Test
    void considerationBooking_whenNoApproved_thenReturnError() {
        mvc.perform(patch("/bookings/{bookingId}", 1L)
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", id))
                .andExpect(status().isInternalServerError());

        verify(bookingService, never()).considerationBooking(any(), any(), anyBoolean());
    }

    @SneakyThrows
    @Test
    void considerationBooking_whenUserIdNotValid_thenReturnBadRequest() {
        mvc.perform(patch("/bookings/{bookingId}", 1L)
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", "notNumber")
                        .param("approved", "true"))
                .andExpect(status().isBadRequest());

        verify(bookingService, never()).considerationBooking(any(), any(), anyBoolean());
    }

    @SneakyThrows
    @Test
    void considerationBooking_whenBookingIdNotValid_thenReturnBadRequest() {
        mvc.perform(patch("/bookings/{bookingId}", "notNumber")
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", userId)
                        .param("approved", "true"))
                .andExpect(status().isBadRequest());

        verify(bookingService, never()).considerationBooking(any(), any(), anyBoolean());
    }

    @SneakyThrows
    @Test
    void getBooking_whenIsOk_thenReturnBooking() {
        when(bookingService.getBooking(userId, id)).thenReturn(expectedBookingDto);

        String result = mvc.perform(get("/bookings/{bookingId}", id)
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(expectedBookingDto), result);
        verify(bookingService, times(1)).getBooking(userId, id);
    }

    @SneakyThrows
    @Test
    void getBooking_whenNoId_thenReturnError() {
        mvc.perform(get("/bookings/{bookingId}", id)
                        .contentType("application/json"))
                .andExpect(status().isInternalServerError());

        verify(bookingService, never()).getBooking(any(), any());
    }

    @SneakyThrows
    @Test
    void getBooking_whenUserIdNotValid_thenReturnBadRequest() {
        mvc.perform(get("/bookings/{bookingId}", id)
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", "notNumber"))
                .andExpect(status().isBadRequest());

        verify(bookingService, never()).getBooking(any(), any());
    }

    @SneakyThrows
    @Test
    void getBooking_whenBookingIdNotValid_thenReturnBadRequest() {
        mvc.perform(get("/bookings/{bookingId}", "notNumber")
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isBadRequest());

        verify(bookingService, never()).getBooking(any(), any());
    }

    @SneakyThrows
    @Test
    void getAllBookings_whenIsOk_thenReturnBookingsList() {
        Sort sort = Sort.by("start").descending();
        Pageable pageable = FromSizeRequest.of(0, 10, sort);
        when(bookingService.getALLBookings(userId, State.ALL, pageable)).thenReturn(List.of(expectedBookingDto));

        String result = mvc.perform(get("/bookings")
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", userId)
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(List.of(expectedBookingDto)), result);
    }

    @SneakyThrows
    @Test
    void getAllBookings_whenNoHeaderId_thenReturnError() {
        mvc.perform(get("/bookings")
                        .contentType("application/json")
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isInternalServerError());
    }

    @SneakyThrows
    @Test
    void getAllBookings_whenFromNotValid_thenReturnError() {
        mvc.perform(get("/bookings")
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", userId)
                        .param("state", "ALL")
                        .param("from", "-1")
                        .param("size", "10"))
                .andExpect(status().isInternalServerError());
    }

    @SneakyThrows
    @Test
    void getAllBookings_whenSizeNotValid_thenReturnError() {
        mvc.perform(get("/bookings")
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", userId)
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "0"))
                .andExpect(status().isInternalServerError());
    }

    @SneakyThrows
    @Test
    void getAllBookings_whenIdNotValid_thenReturnError() {
        mvc.perform(get("/bookings")
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", "notNumber")
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "0"))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void getAllBookings_whenStateNotValid_thenReturnError() {
        mvc.perform(get("/bookings")
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", userId)
                        .param("state", "BAD")
                        .param("from", "0")
                        .param("size", "0"))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void getAllBookingsByOwner_whenIsOk_thenReturnBookingsList() {
        Sort sort = Sort.by("start").descending();
        Pageable pageable = FromSizeRequest.of(0, 10, sort);
        when(bookingService.getAllBookingsByOwner(userId, State.ALL, pageable)).thenReturn(List.of(expectedBookingDto));

        String result = mvc.perform(get("/bookings/owner")
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", userId)
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(List.of(expectedBookingDto)), result);
    }

    @SneakyThrows
    @Test
    void getAllBookingsByOwner_whenNoHeaderId_thenReturnError() {
        mvc.perform(get("/bookings/owner")
                        .contentType("application/json")
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isInternalServerError());
    }

    @SneakyThrows
    @Test
    void getAllBookingsByOwner_whenFromNotValid_thenReturnError() {
        mvc.perform(get("/bookings/owner")
                        .contentType("application/json")
                        .param("state", "ALL")
                        .header("X-Sharer-User-Id", id)
                        .param("from", "-1")
                        .param("size", "10"))
                .andExpect(status().isInternalServerError());
    }

    @SneakyThrows
    @Test
    void getAllBookingsByOwner_whenSizeNotValid_thenReturnError() {
        mvc.perform(get("/bookings/owner")
                        .contentType("application/json")
                        .param("state", "ALL")
                        .header("X-Sharer-User-Id", id)
                        .param("from", "0")
                        .param("size", "0"))
                .andExpect(status().isInternalServerError());
    }

    @SneakyThrows
    @Test
    void getAllBookingsByOwner_whenIdNotValid_thenReturnError() {
        mvc.perform(get("/bookings/owner")
                        .contentType("application/json")
                        .param("state", "ALL")
                        .header("X-Sharer-User-Id", "notNumber")
                        .param("from", "0")
                        .param("size", "0"))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void getAllBookingsByOwner_whenStateNotValid_thenReturnError() {
        mvc.perform(get("/bookings/owner")
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", userId)
                        .param("state", "BAD")
                        .param("from", "0")
                        .param("size", "0"))
                .andExpect(status().isBadRequest());
    }
}