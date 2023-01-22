package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.enums.State;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.common.model.FromSizeRequest;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingDto addBooking(@RequestHeader(value = "X-Sharer-User-Id") Long userId, @Valid @RequestBody BookingDtoRequest bookingDtoRequest) {
        return bookingService.addBooking(userId, bookingDtoRequest);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto considerationBooking(@RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                           @PathVariable Long bookingId, @RequestParam boolean approved) {
        return bookingService.considerationBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBooking(@RequestHeader(value = "X-Sharer-User-Id") Long userId, @PathVariable Long bookingId) {
        return bookingService.getBooking(userId, bookingId);
    }

    @GetMapping
    public List<BookingDto> getAllBookings(@RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                           @RequestParam(defaultValue = "ALL") State state,
                                           @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                           @Positive @RequestParam(defaultValue = "10") Integer size) {
        Sort sort = Sort.by("start").descending();
        Pageable pageable = FromSizeRequest.of(from, size, sort);
        return bookingService.getALLBookings(userId, state, pageable);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllBookingsByOwner(@RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                                  @RequestParam(defaultValue = "ALL") State state,
                                                  @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                  @Positive @RequestParam(defaultValue = "10") Integer size) {
        Sort sort = Sort.by("start").descending();
        Pageable pageable = FromSizeRequest.of(from, size, sort);
        return bookingService.getAllBookingsByOwner(userId, state, pageable);
    }
}
