package ru.practicum.shareit.booking.mapper;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoForItem;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.booking.enums.Status.WAITING;

@Service
public class MapperBooking {

    public BookingDto convertBookingToBookingDto(Booking booking) {
        return new BookingDto(
                booking.getId(),
                booking.getItem(),
                booking.getBooker(),
                booking.getStart(),
                booking.getEnd(),
                booking.getStatus());
    }

    public BookingDtoForItem convertBookingToBookingDtoForItem(Booking booking) {
        return new BookingDtoForItem(
                booking.getId(),
                booking.getBooker().getId()
        );
    }

    public Booking convertBookingRequestToBooking(BookingDtoRequest bookingDtoRequest, Item item, User booker) {
        Booking booking = new Booking();
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStart(bookingDtoRequest.getStart());
        booking.setEnd(bookingDtoRequest.getEnd());
        booking.setStatus(WAITING);
        return booking;
    }


    public List<BookingDto> convertAllBookingsToBookingsDto(List<Booking> bookings) {
        return bookings.stream()
                .map(this::convertBookingToBookingDto)
                .collect(Collectors.toList());
    }
}
