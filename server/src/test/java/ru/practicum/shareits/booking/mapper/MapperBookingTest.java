package ru.practicum.shareits.booking.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareits.booking.dto.BookingDto;
import ru.practicum.shareits.booking.dto.BookingDtoRequest;
import ru.practicum.shareits.booking.enums.Status;
import ru.practicum.shareits.booking.model.Booking;
import ru.practicum.shareits.item.model.Item;
import ru.practicum.shareits.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MapperBookingTest {

    private final MapperBooking mapperBooking = new MapperBooking();

    private Booking booking;
    private Booking bookingWithoutId;
    private BookingDtoRequest bookingDtoRequest;
    private BookingDto bookingDto;
    private User booker;
    private Item item;

    @BeforeEach
    void setUp() {
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(2);
        User user = new User(1L, "mail@ya.ru", "user");
        item = new Item(1L, user, "item", "useful", true, List.of(), 1L);
        booker = new User(2L, "mail2@ya.ru", "user2");
        booking = new Booking(1L, item, booker, Status.WAITING, start, end);
        bookingWithoutId = new Booking(null, item, booker, Status.WAITING, start, end);

        bookingDtoRequest = new BookingDtoRequest();
        bookingDtoRequest.setItemId(1L);
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