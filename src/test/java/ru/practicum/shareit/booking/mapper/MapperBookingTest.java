package ru.practicum.shareit.booking.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MapperBookingTest {

    MapperBooking mapperBooking = new MapperBooking();

    Long id;
    Booking booking;
    Booking bookingWithoutId;
    BookingDtoRequest bookingDtoRequest;
    BookingDto bookingDto;
    LocalDateTime start;
    LocalDateTime end;
    User user;
    User booker;
    Item item;

    @BeforeEach
    void setUp() {
        start = LocalDateTime.now().plusDays(1);
        end = LocalDateTime.now().plusDays(2);
        user = new User(1L, "mail@ya.ru", "user");
        item = new Item(1L, user, "item", "useful", true, List.of(), 1L);
        booker = new User(2L, "mail2@ya.ru", "user2");
        booking = new Booking(1L, item, booker, Status.WAITING, start, end);
        bookingWithoutId = new Booking(null, item, booker, Status.WAITING, start, end);

        bookingDtoRequest = new BookingDtoRequest();
        bookingDtoRequest.setItemId(id);
        bookingDtoRequest.setStart(start);
        bookingDtoRequest.setEnd(end);

        bookingDto = new BookingDto();
        bookingDto.setId(1L);
        bookingDto.setStart(start);
        bookingDto.setEnd(end);
        bookingDto.setStatus(Status.WAITING);
        bookingDto.setBooker(booker);
        bookingDto.setItem(item);
    }

    @Test
    void convertBookingToBookingDto() {
        BookingDto result = mapperBooking.convertBookingToBookingDto(booking);

        assertEquals(bookingDto, result);
    }

    @Test
    void convertBookingRequestToBooking() {
        Booking result = mapperBooking.convertBookingRequestToBooking(bookingDtoRequest, item, booker);

        assertEquals(bookingWithoutId, result);
    }

    @Test
    void convertAllBookingsToBookingsDto() {
        List<BookingDto> result = mapperBooking.convertAllBookingsToBookingsDto(List.of(booking));

        assertEquals(List.of(bookingDto), result);
    }
}