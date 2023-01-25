package ru.practicum.shareits.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareits.booking.storage.BookingRepository;
import ru.practicum.shareits.booking.enums.State;
import ru.practicum.shareits.booking.dto.BookingDto;
import ru.practicum.shareits.booking.dto.BookingDtoRequest;
import ru.practicum.shareits.booking.mapper.MapperBooking;
import ru.practicum.shareits.booking.model.Booking;
import ru.practicum.shareits.common.exceptions.*;
import ru.practicum.shareits.item.model.Item;
import ru.practicum.shareits.item.storage.ItemRepository;
import ru.practicum.shareits.user.model.User;
import ru.practicum.shareits.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static ru.practicum.shareits.booking.enums.Status.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final MapperBooking mapperBooking;

    @Override
    public BookingDto addBooking(Long userId, BookingDtoRequest bookingDtoRequest) {
        User booker = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotExistsException("such user not registered"));
        Item item = itemRepository.findById(bookingDtoRequest.getItemId())
                .orElseThrow(() -> new EntityNotExistsException("such item not registered"));
        if (!item.getAvailable()) {
            throw new ItemNotAvailableException("the item not available");
        }
        if (item.getUser().equals(booker)) {
            throw new ItemAccessErrorException("you can't book your own item");
        }
        Booking booking = mapperBooking.convertBookingRequestToBooking(bookingDtoRequest, item, booker);
        Booking createdBooking = bookingRepository.save(booking);
        log.info("booking with id: {} created", createdBooking.getId());
        return mapperBooking.convertBookingToBookingDto(createdBooking);
    }

    @Override
    public BookingDto considerationBooking(Long userId, Long bookingId, boolean approved) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotExistsException("such user not registered"));
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotExistsException("such booking not registered"));
        if (!booking.getItem().getUser().equals(owner)) {
            throw new ItemAccessErrorException("incorrect owner");
        }
        if (!booking.getStatus().equals(WAITING)) {
            throw new BookingAccessErrorException("booking status already changed");
        }
        if (approved) {
            booking.setStatus(APPROVED);
        } else {
            booking.setStatus(REJECTED);
        }
        log.info("booking with id: {}, considered", bookingId);
        return mapperBooking.convertBookingToBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto getBooking(Long userId, Long bookingId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotExistsException("such user not registered"));
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotExistsException("such booking not registered"));
        if (!booking.getBooker().equals(user) && !booking.getItem().getUser().equals(user)) {
            throw new ItemAccessErrorException("incorrect user");
        }
        log.info("booking with id: {} asked", bookingId);
        return mapperBooking.convertBookingToBookingDto(booking);
    }

    @Override
    public List<BookingDto> getALLBookings(Long userId, State state, Pageable pageable) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotExistsException("such user not registered");
        }
        List<Booking> bookings = new ArrayList<>();
        switch (state) {
            case ALL:
                bookings.addAll(bookingRepository.findAllByBooker_Id(userId, pageable));
                break;
            case CURRENT:
                bookings.addAll(bookingRepository.findAllByBooker_IdAndStartIsBeforeAndEndIsAfter(
                        userId, LocalDateTime.now(), LocalDateTime.now(), pageable));
                break;
            case FUTURE:
                bookings.addAll(bookingRepository.findAllByBooker_IdAndStartIsAfter(userId, LocalDateTime.now(),
                        pageable));
                break;
            case PAST:
                bookings.addAll(bookingRepository.findAllByBooker_IdAndEndIsBefore(userId, LocalDateTime.now(),
                        pageable));
                break;
            case WAITING:
                bookings.addAll(bookingRepository.findAllByBooker_IdAndStatusIs(userId, WAITING, pageable));
                break;
            case REJECTED:
                bookings.addAll(bookingRepository.findAllByBooker_IdAndStatusIs(userId, REJECTED, pageable));
                break;
        }
        log.info("all bookings with state: {} asked", state);
        return mapperBooking.convertAllBookingsToBookingsDto(bookings);
    }

    @Override
    public List<BookingDto> getAllBookingsByOwner(Long userId, State state, Pageable pageable) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotExistsException("such user not registered");
        }
        List<Booking> bookings = new ArrayList<>();
        switch (state) {
            case ALL:
                bookings.addAll(bookingRepository.findAllByItem_User_Id(userId, pageable));
                break;
            case CURRENT:
                bookings.addAll(bookingRepository.findAllByItem_User_IdAndStartIsBeforeAndEndIsAfter(
                        userId, LocalDateTime.now(), LocalDateTime.now(), pageable));
                break;
            case FUTURE:
                bookings.addAll(bookingRepository.findAllByItem_User_IdAndStartIsAfter(userId, LocalDateTime.now(),
                        pageable));
                break;
            case PAST:
                bookings.addAll(bookingRepository.findAllByItem_User_IdAndEndIsBefore(userId, LocalDateTime.now(),
                        pageable));
                break;
            case WAITING:
                bookings.addAll(bookingRepository.findAllByItem_User_IdAndStatusIs(userId, WAITING, pageable));
                break;
            case REJECTED:
                bookings.addAll(bookingRepository.findAllByItem_User_IdAndStatusIs(userId, REJECTED, pageable));
                break;
        }
        log.info("all bookings with state: {} asked", state);
        return mapperBooking.convertAllBookingsToBookingsDto(bookings);
    }
}
