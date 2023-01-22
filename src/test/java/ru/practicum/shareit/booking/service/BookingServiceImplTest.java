package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.enums.State;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.mapper.MapperBooking;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.common.exceptions.*;
import ru.practicum.shareit.common.model.FromSizeRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    private BookingService service;
    private UserRepository userRepository;
    private ItemRepository itemRepository;
    private BookingRepository bookingRepository;

    Long id;
    User user;
    User booker;
    Item item;
    LocalDateTime start;
    LocalDateTime end;
    BookingDtoRequest bookingDtoRequest;
    BookingDto expectedBookingDto;
    Booking booking;
    Booking bookingWithoutId;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        itemRepository = mock(ItemRepository.class);
        bookingRepository = mock(BookingRepository.class);
        MapperBooking mapperBooking = new MapperBooking();
        service = new BookingServiceImpl(userRepository, itemRepository,
                bookingRepository, mapperBooking);

        id = 1L;
        start = LocalDateTime.now().plusDays(1);
        end = LocalDateTime.now().plusDays(2);
        user = new User(1L, "mail@ya.ru", "user");
        booker = new User(2L, "mail2@ya.ru", "user2");
        item = new Item(1L, user, "item", "useful", true, List.of(), null);

        bookingDtoRequest = new BookingDtoRequest();
        bookingDtoRequest.setItemId(id);
        bookingDtoRequest.setStart(start);
        bookingDtoRequest.setEnd(end);

        booking = new Booking(id, item, booker, Status.WAITING, start, end);
        bookingWithoutId = new Booking(null, item, booker, Status.WAITING, start, end);

        expectedBookingDto = mapperBooking.convertBookingToBookingDto(booking);
    }

    @Test
    void addBooking_whenIsOk_thenReturnBooking() {
        when(userRepository.findById(2L)).thenReturn(Optional.of(booker));
        when(itemRepository.findById(id)).thenReturn(Optional.of(item));
        when(bookingRepository.save(bookingWithoutId)).thenReturn(booking);

        BookingDto actualResult = service.addBooking(2L, bookingDtoRequest);

        assertEquals(expectedBookingDto, actualResult);
        verify(bookingRepository, times(1)).save(bookingWithoutId);
    }

    @Test
    void addBooking_whenUserNotFounded_thenEntityNotExistsException() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(EntityNotExistsException.class, () -> service.addBooking(2L, bookingDtoRequest));
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void addBooking_whenItemNotValid_thenBookingTimeNotAllowedException() {
        bookingDtoRequest.setStart(end);
        bookingDtoRequest.setEnd(start);

        assertThrows(BookingTimeNotAllowedException.class, () -> service.addBooking(2L, bookingDtoRequest));
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void addBooking_whenItemNotAvailable_thenItemNotAvailableException() {
        item.setAvailable(false);
        when(userRepository.findById(2L)).thenReturn(Optional.of(booker));
        when(itemRepository.findById(id)).thenReturn(Optional.of(item));

        assertThrows(ItemNotAvailableException.class, () -> service.addBooking(2L, bookingDtoRequest));
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void addBooking_whenBookerIsUser_thenItemAccessErrorException() {
        item.setUser(booker);
        when(userRepository.findById(2L)).thenReturn(Optional.of(booker));
        when(itemRepository.findById(id)).thenReturn(Optional.of(item));

        assertThrows(ItemAccessErrorException.class, () -> service.addBooking(2L, bookingDtoRequest));
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void considerationBooking_whenApprove_thenReturnBooking() {
        Booking bookingApproved = new Booking(id, item, booker, Status.APPROVED, start, end);
        expectedBookingDto.setStatus(Status.APPROVED);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookingRepository.findById(id)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(bookingApproved)).thenReturn(bookingApproved);

        BookingDto actualResult = service.considerationBooking(1L, id, true);

        assertEquals(expectedBookingDto, actualResult);
        verify(bookingRepository, times(1)).save(bookingApproved);
    }

    @Test
    void considerationBooking_whenRejected_thenReturnBooking() {
        Booking bookingRejected = new Booking(id, item, booker, Status.REJECTED, start, end);
        expectedBookingDto.setStatus(Status.REJECTED);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookingRepository.findById(id)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(bookingRejected)).thenReturn(bookingRejected);

        BookingDto actualResult = service.considerationBooking(1L, id, false);

        assertEquals(expectedBookingDto, actualResult);
        verify(bookingRepository, times(1)).save(bookingRejected);
    }

    @Test
    void considerationBooking_whenUserNotFounded_thenEntityNotExistsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotExistsException.class, () -> service.considerationBooking(1L, id, false));
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void considerationBooking_whenBookingNotFounded_thenEntityNotExistsException() {
        when(userRepository.findById(2L)).thenReturn(Optional.of(user));
        when(bookingRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotExistsException.class, () -> service.considerationBooking(2L, id, false));
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void considerationBooking_whenUserNotOwner_thenEntityNotExistsException() {
        when(userRepository.findById(2L)).thenReturn(Optional.of(booker));
        when(bookingRepository.findById(id)).thenReturn(Optional.of(booking));

        assertThrows(ItemAccessErrorException.class, () -> service.considerationBooking(2L, id, false));
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void considerationBooking_whenStatusAlreadyChanged_thenEntityNotExistsException() {
        booking.setStatus(Status.APPROVED);
        when(userRepository.findById(2L)).thenReturn(Optional.of(user));
        when(bookingRepository.findById(id)).thenReturn(Optional.of(booking));

        assertThrows(BookingAccessErrorException.class, () -> service.considerationBooking(2L, id, false));
        verify(bookingRepository, never()).save(any());
    }


    @Test
    void getBooking_whenIsOk_thenReturnBooking() {
        when(userRepository.findById(2L)).thenReturn(Optional.of(booker));
        when(bookingRepository.findById(id)).thenReturn(Optional.of(booking));

        BookingDto actualResult = service.getBooking(2L, id);

        assertEquals(expectedBookingDto, actualResult);
    }

    @Test
    void getBooking_whenUserNotFounded_thenEntityNotExistsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotExistsException.class, () -> service.getBooking(1L, id));
    }

    @Test
    void getBooking_whenBookingNotFounded_thenEntityNotExistsException() {
        when(userRepository.findById(2L)).thenReturn(Optional.of(user));
        when(bookingRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotExistsException.class, () -> service.getBooking(2L, id));
    }

    @Test
    void getBooking_whenUserNotBooker_thenEntityNotExistsException() {
        User notBooker = new User(3L, "not@booker.ru", "not");
        when(userRepository.findById(3L)).thenReturn(Optional.of(notBooker));
        when(bookingRepository.findById(id)).thenReturn(Optional.of(booking));

        assertThrows(ItemAccessErrorException.class, () -> service.getBooking(3L, id));
    }

    @Test
    void getALLBookings_WhenStateAll_ThenReturnBookingsList() {
        Sort sort = Sort.by("start").descending();
        Pageable pageable = FromSizeRequest.of(0, 10, sort);
        when(userRepository.existsById(2L)).thenReturn(true);
        when(bookingRepository.findAllByBooker_Id(2L, pageable)).thenReturn(List.of(booking));

        List<BookingDto> result = service.getALLBookings(2L, State.ALL, pageable);

        assertEquals(List.of(expectedBookingDto), result);
    }

    @Test
    void getALLBookings_WhenStateCurrent_ThenReturnBookingsList() {
        Sort sort = Sort.by("start").descending();
        Pageable pageable = FromSizeRequest.of(0, 10, sort);
        when(userRepository.existsById(2L)).thenReturn(true);
        when(bookingRepository.findAllByBooker_IdAndStartIsBeforeAndEndIsAfter(any(), any(), any(), any())).thenReturn(List.of(booking));

        List<BookingDto> result = service.getALLBookings(2L, State.CURRENT, pageable);

        assertEquals(List.of(expectedBookingDto), result);
    }

    @Test
    void getALLBookings_WhenStateFuture_ThenReturnBookingsList() {
        Sort sort = Sort.by("start").descending();
        Pageable pageable = FromSizeRequest.of(0, 10, sort);
        when(userRepository.existsById(2L)).thenReturn(true);
        when(bookingRepository.findAllByBooker_IdAndStartIsAfter(any(), any(), any())).thenReturn(List.of(booking));

        List<BookingDto> result = service.getALLBookings(2L, State.FUTURE, pageable);

        assertEquals(List.of(expectedBookingDto), result);
    }

    @Test
    void getALLBookings_WhenStatePast_ThenReturnBookingsList() {
        Sort sort = Sort.by("start").descending();
        Pageable pageable = FromSizeRequest.of(0, 10, sort);
        when(userRepository.existsById(2L)).thenReturn(true);
        when(bookingRepository.findAllByBooker_IdAndEndIsBefore(any(), any(), any())).thenReturn(List.of(booking));

        List<BookingDto> result = service.getALLBookings(2L, State.PAST, pageable);

        assertEquals(List.of(expectedBookingDto), result);
    }

    @Test
    void getALLBookings_WhenStateWaiting_ThenReturnBookingsList() {
        Sort sort = Sort.by("start").descending();
        Pageable pageable = FromSizeRequest.of(0, 10, sort);
        when(userRepository.existsById(2L)).thenReturn(true);
        when(bookingRepository.findAllByBooker_IdAndStatusIs(2L, Status.WAITING, pageable)).thenReturn(List.of(booking));

        List<BookingDto> result = service.getALLBookings(2L, State.WAITING, pageable);

        assertEquals(List.of(expectedBookingDto), result);
    }

    @Test
    void getALLBookings_WhenStateRejected_ThenReturnBookingsList() {
        Sort sort = Sort.by("start").descending();
        Pageable pageable = FromSizeRequest.of(0, 10, sort);
        when(userRepository.existsById(2L)).thenReturn(true);
        when(bookingRepository.findAllByBooker_IdAndStatusIs(2L, Status.REJECTED, pageable)).thenReturn(List.of(booking));

        List<BookingDto> result = service.getALLBookings(2L, State.REJECTED, pageable);

        assertEquals(List.of(expectedBookingDto), result);
    }


    @Test
    void getALLBookings_whenUserNotFounded_thenEntityNotExistsException() {
        Sort sort = Sort.by("start").descending();
        Pageable pageable = FromSizeRequest.of(0, 10, sort);
        when(userRepository.existsById(1L)).thenReturn(false);

        assertThrows(EntityNotExistsException.class, () -> service.getALLBookings(1L, State.WAITING, pageable));
    }

    @Test
    void getAllBookingsByOwner_WhenStateAll_ThenReturnBookingsList() {
        Sort sort = Sort.by("start").descending();
        Pageable pageable = FromSizeRequest.of(0, 10, sort);
        when(userRepository.existsById(2L)).thenReturn(true);
        when(bookingRepository.findAllByItem_User_Id(2L, pageable)).thenReturn(List.of(booking));

        List<BookingDto> result = service.getAllBookingsByOwner(2L, State.ALL, pageable);

        assertEquals(List.of(expectedBookingDto), result);
    }

    @Test
    void getAllBookingsByOwner_WhenStateCurrent_ThenReturnBookingsList() {
        Sort sort = Sort.by("start").descending();
        Pageable pageable = FromSizeRequest.of(0, 10, sort);
        when(userRepository.existsById(2L)).thenReturn(true);
        when(bookingRepository.findAllByItem_User_IdAndStartIsBeforeAndEndIsAfter(any(), any(), any(), any())).thenReturn(List.of(booking));

        List<BookingDto> result = service.getAllBookingsByOwner(2L, State.CURRENT, pageable);

        assertEquals(List.of(expectedBookingDto), result);
    }

    @Test
    void getAllBookingsByOwner_WhenStateFuture_ThenReturnBookingsList() {
        Sort sort = Sort.by("start").descending();
        Pageable pageable = FromSizeRequest.of(0, 10, sort);
        when(userRepository.existsById(2L)).thenReturn(true);
        when(bookingRepository.findAllByItem_User_IdAndStartIsAfter(any(), any(), any())).thenReturn(List.of(booking));

        List<BookingDto> result = service.getAllBookingsByOwner(2L, State.FUTURE, pageable);

        assertEquals(List.of(expectedBookingDto), result);
    }

    @Test
    void getAllBookingsByOwner_WhenStatePast_ThenReturnBookingsList() {
        Sort sort = Sort.by("start").descending();
        Pageable pageable = FromSizeRequest.of(0, 10, sort);
        when(userRepository.existsById(2L)).thenReturn(true);
        when(bookingRepository.findAllByItem_User_IdAndEndIsBefore(any(), any(), any())).thenReturn(List.of(booking));

        List<BookingDto> result = service.getAllBookingsByOwner(2L, State.PAST, pageable);

        assertEquals(List.of(expectedBookingDto), result);
    }

    @Test
    void getAllBookingsByOwner_WhenStateWaiting_ThenReturnBookingsList() {
        Sort sort = Sort.by("start").descending();
        Pageable pageable = FromSizeRequest.of(0, 10, sort);
        when(userRepository.existsById(2L)).thenReturn(true);
        when(bookingRepository.findAllByItem_User_IdAndStatusIs(2L, Status.WAITING, pageable)).thenReturn(List.of(booking));

        List<BookingDto> result = service.getAllBookingsByOwner(2L, State.WAITING, pageable);

        assertEquals(List.of(expectedBookingDto), result);
    }

    @Test
    void getAllBookingsByOwner_WhenStateRejected_ThenReturnBookingsList() {
        Sort sort = Sort.by("start").descending();
        Pageable pageable = FromSizeRequest.of(0, 10, sort);
        when(userRepository.existsById(2L)).thenReturn(true);
        when(bookingRepository.findAllByItem_User_IdAndStatusIs(2L, Status.REJECTED, pageable)).thenReturn(List.of(booking));

        List<BookingDto> result = service.getAllBookingsByOwner(2L, State.REJECTED, pageable);

        assertEquals(List.of(expectedBookingDto), result);
    }


    @Test
    void getAllBookingsByOwner_whenUserNotFounded_thenEntityNotExistsException() {
        Sort sort = Sort.by("start").descending();
        Pageable pageable = FromSizeRequest.of(0, 10, sort);
        when(userRepository.existsById(1L)).thenReturn(false);

        assertThrows(EntityNotExistsException.class, () -> service.getAllBookingsByOwner(1L, State.WAITING, pageable));
    }

}