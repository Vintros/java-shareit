package ru.practicum.shareits.booking.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareits.booking.enums.State;
import ru.practicum.shareits.booking.dto.BookingDto;
import ru.practicum.shareits.booking.dto.BookingDtoRequest;

import java.util.List;

public interface BookingService {

    BookingDto addBooking(Long id, BookingDtoRequest bookingDtoRequest);

    BookingDto considerationBooking(Long userId, Long bookingId, boolean approved);

    BookingDto getBooking(Long userId, Long bookingId);

    List<BookingDto> getALLBookings(Long userId, State state, Pageable pageable);

    List<BookingDto> getAllBookingsByOwner(Long userId, State state, Pageable pageable);
}
