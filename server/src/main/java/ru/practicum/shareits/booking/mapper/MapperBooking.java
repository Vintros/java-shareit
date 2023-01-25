package ru.practicum.shareits.booking.mapper;

import org.springframework.stereotype.Service;
import ru.practicum.shareits.booking.enums.Status;
import ru.practicum.shareits.booking.model.Booking;
import ru.practicum.shareits.booking.dto.BookingDto;
import ru.practicum.shareits.booking.dto.BookingDtoRequest;
import ru.practicum.shareits.item.model.Item;
import ru.practicum.shareits.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MapperBooking {

    public BookingDto convertBookingToBookingDto(Booking booking) {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(booking.getId());
        bookingDto.setStart(booking.getStart());
        bookingDto.setEnd(booking.getEnd());
        bookingDto.setStatus(booking.getStatus());
        bookingDto.setBooker(booking.getBooker());
        bookingDto.setItem(booking.getItem());
        return bookingDto;
    }

    public Booking convertBookingRequestToBooking(BookingDtoRequest bookingDtoRequest, Item item, User booker) {
        Booking booking = new Booking();
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStart(bookingDtoRequest.getStart());
        booking.setEnd(bookingDtoRequest.getEnd());
        booking.setStatus(Status.WAITING);
        return booking;
    }

    public List<BookingDto> convertAllBookingsToBookingsDto(List<Booking> bookings) {
        return bookings.stream()
                .map(this::convertBookingToBookingDto)
                .collect(Collectors.toList());
    }
}
