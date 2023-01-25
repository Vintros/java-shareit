package ru.practicum.shareits.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareits.booking.enums.State;
import ru.practicum.shareits.booking.dto.BookingDto;
import ru.practicum.shareits.booking.dto.BookingDtoRequest;
import ru.practicum.shareits.booking.service.BookingService;
import ru.practicum.shareits.common.model.FromSizeRequest;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingDto addBooking(@RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                 @RequestBody BookingDtoRequest bookingDtoRequest) {
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
                                           @RequestParam State state, @RequestParam Integer from,
                                           @RequestParam Integer size) {
        Sort sort = Sort.by("start").descending();
        Pageable pageable = FromSizeRequest.of(from, size, sort);
        return bookingService.getALLBookings(userId, state, pageable);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllBookingsByOwner(@RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                                  @RequestParam State state, @RequestParam Integer from,
                                                  @RequestParam Integer size) {
        Sort sort = Sort.by("start").descending();
        Pageable pageable = FromSizeRequest.of(from, size, sort);
        return bookingService.getAllBookingsByOwner(userId, state, pageable);
    }
}
