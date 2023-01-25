package ru.practicum.shareits.booking.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareits.booking.dto.BookingDto;
import ru.practicum.shareits.booking.dto.BookingDtoRequest;
import ru.practicum.shareits.booking.enums.State;
import ru.practicum.shareits.booking.model.Booking;
import ru.practicum.shareits.common.model.FromSizeRequest;
import ru.practicum.shareits.item.dto.ItemDto;
import ru.practicum.shareits.item.service.ItemService;
import ru.practicum.shareits.user.dto.UserDto;
import ru.practicum.shareits.user.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.practicum.shareits.booking.enums.Status.APPROVED;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingServiceImplTestIT {

    private final BookingService bookingService;
    private final ItemService itemService;
    private final UserService userService;
    private final EntityManager em;

    @DirtiesContext
    @Test
    void addBooking() {
        UserDto userDto = new UserDto(null, "mail@ya.ru", "user");
        userService.createUser(userDto);
        UserDto userDto2 = new UserDto(null, "mail2@ya.ru", "user2");
        userService.createUser(userDto2);

        ItemDto itemDto = new ItemDto();
        itemDto.setName("item");
        itemDto.setDescription("useful");
        itemDto.setAvailable(true);
        itemService.createItem(itemDto, 1L);

        BookingDtoRequest bookingDtoRequest = new BookingDtoRequest();
        bookingDtoRequest.setItemId(1L);
        bookingDtoRequest.setStart(LocalDateTime.now().plusDays(1));
        bookingDtoRequest.setEnd(LocalDateTime.now().plusDays(3));
        bookingService.addBooking(2L, bookingDtoRequest);

        TypedQuery<Booking> query = em.createQuery("Select b from Booking b where b.item.name = :name", Booking.class);
        Booking booking = query.setParameter("name", itemDto.getName()).getSingleResult();

        assertEquals(1L, booking.getId());
        assertEquals("item", booking.getItem().getName());
        assertEquals("user2", booking.getBooker().getName());
    }

    @DirtiesContext
    @Test
    void considerationBooking() {
        UserDto userDto = new UserDto(null, "mail@ya.ru", "user");
        userService.createUser(userDto);
        UserDto userDto2 = new UserDto(null, "mail2@ya.ru", "user2");
        userService.createUser(userDto2);

        ItemDto itemDto = new ItemDto();
        itemDto.setName("item");
        itemDto.setDescription("useful");
        itemDto.setAvailable(true);
        itemService.createItem(itemDto, 1L);

        BookingDtoRequest bookingDtoRequest = new BookingDtoRequest();
        bookingDtoRequest.setItemId(1L);
        bookingDtoRequest.setStart(LocalDateTime.now().plusDays(1));
        bookingDtoRequest.setEnd(LocalDateTime.now().plusDays(3));
        bookingService.addBooking(2L, bookingDtoRequest);

        bookingService.considerationBooking(1L, 1L, true);

        TypedQuery<Booking> query = em.createQuery("Select b from Booking b where b.item.name = :name", Booking.class);
        Booking booking = query.setParameter("name", itemDto.getName()).getSingleResult();

        assertEquals(APPROVED, booking.getStatus());
    }

    @DirtiesContext
    @Test
    void getALLBookings() {
        Sort sort = Sort.by("id").ascending();
        Pageable pageable = FromSizeRequest.of(0, 10, sort);

        UserDto userDto = new UserDto(null, "mail@ya.ru", "user");
        userService.createUser(userDto);
        UserDto userDto2 = new UserDto(null, "mail2@ya.ru", "user2");
        userService.createUser(userDto2);

        ItemDto itemDto = new ItemDto();
        itemDto.setName("item");
        itemDto.setDescription("useful");
        itemDto.setAvailable(true);
        itemService.createItem(itemDto, 1L);

        BookingDtoRequest bookingDtoRequest = new BookingDtoRequest();
        bookingDtoRequest.setItemId(1L);
        bookingDtoRequest.setStart(LocalDateTime.now().plusDays(1));
        bookingDtoRequest.setEnd(LocalDateTime.now().plusDays(3));
        bookingService.addBooking(2L, bookingDtoRequest);

        BookingDtoRequest bookingDtoRequest2 = new BookingDtoRequest();
        bookingDtoRequest2.setItemId(1L);
        bookingDtoRequest2.setStart(LocalDateTime.now().plusDays(5));
        bookingDtoRequest2.setEnd(LocalDateTime.now().plusDays(7));
        bookingService.addBooking(2L, bookingDtoRequest2);

        List<BookingDto> allBookings = bookingService.getALLBookings(2L, State.WAITING, pageable);

        assertEquals(2, allBookings.size());
    }

    @DirtiesContext
    @Test
    void getAllBookingsByOwner() {
        Sort sort = Sort.by("id").ascending();
        Pageable pageable = FromSizeRequest.of(0, 10, sort);

        UserDto userDto = new UserDto(null, "mail@ya.ru", "user");
        userService.createUser(userDto);
        UserDto userDto2 = new UserDto(null, "mail2@ya.ru", "user2");
        userService.createUser(userDto2);

        ItemDto itemDto = new ItemDto();
        itemDto.setName("item");
        itemDto.setDescription("useful");
        itemDto.setAvailable(true);
        itemService.createItem(itemDto, 1L);

        BookingDtoRequest bookingDtoRequest = new BookingDtoRequest();
        bookingDtoRequest.setItemId(1L);
        bookingDtoRequest.setStart(LocalDateTime.now().plusDays(1));
        bookingDtoRequest.setEnd(LocalDateTime.now().plusDays(3));
        bookingService.addBooking(2L, bookingDtoRequest);

        BookingDtoRequest bookingDtoRequest2 = new BookingDtoRequest();
        bookingDtoRequest2.setItemId(1L);
        bookingDtoRequest2.setStart(LocalDateTime.now().plusDays(5));
        bookingDtoRequest2.setEnd(LocalDateTime.now().plusDays(7));
        bookingService.addBooking(2L, bookingDtoRequest2);

        List<BookingDto> allBookings = bookingService.getAllBookingsByOwner(1L, State.WAITING, pageable);

        assertEquals(2, allBookings.size());
    }
}