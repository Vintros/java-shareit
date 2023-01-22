package ru.practicum.shareit.booking.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.common.model.FromSizeRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.practicum.shareit.booking.enums.Status.APPROVED;
import static ru.practicum.shareit.booking.enums.Status.WAITING;

@DataJpaTest
class BookingRepositoryTest {


    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private BookingRepository bookingRepository;

    LocalDateTime start;
    LocalDateTime end;
    User user;
    User booker;
    Item item;
    LocalDateTime time;
    Sort sort = Sort.by("start").descending();
    Pageable pageable = FromSizeRequest.of(0, 10, sort);

    @BeforeEach
    public void setUp() {
        time = LocalDateTime.now();
        start = LocalDateTime.now().plusDays(1);
        end = LocalDateTime.now().plusDays(2);
        user = new User(1L, "mail@ya.ru", "user");
        userRepository.save(user);
        booker = new User(2L, "mail2@ya.ru", "user2");
        userRepository.save(booker);

        ItemRequest itemRequest1 = new ItemRequest();
        itemRequest1.setUserId(1L);
        itemRequest1.setDescription("something");
        itemRequest1.setCreated(time);
        itemRequestRepository.save(itemRequest1);

        ItemRequest itemRequest2 = new ItemRequest();
        itemRequest2.setUserId(2L);
        itemRequest2.setDescription("something2");
        itemRequest2.setCreated(time.plusDays(1));
        itemRequestRepository.save(itemRequest2);

        ItemRequest itemRequest3 = new ItemRequest();
        itemRequest3.setUserId(2L);
        itemRequest3.setDescription("something3");
        itemRequest3.setCreated(time.plusDays(2));
        itemRequestRepository.save(itemRequest3);

        item = new Item(1L, user, "item", "useful", true, List.of(), 1L);
        itemRepository.save(item);
        Item item2 = new Item(2L, user, "item2", "useful2", false, List.of(), 1L);
        itemRepository.save(item2);
        Item item3 = new Item(3L, user, "item3", "useful3", true, List.of(), 1L);
        itemRepository.save(item3);
        Item item4 = new Item(4L, booker, "item4", "useful4", true, List.of(), 2L);
        itemRepository.save(item4);

        Booking booking = new Booking(1L, item, booker, Status.WAITING, start, end);
        bookingRepository.save(booking);

        Booking booking2 = new Booking(2L, item, booker, Status.WAITING, start.plusDays(1), end.plusDays(5));
        bookingRepository.save(booking2);

        Booking booking3 = new Booking(3L, item4, user, Status.WAITING, start.plusDays(1), end.plusDays(5));
        bookingRepository.save(booking3);

        Booking booking4 = new Booking(4L, item4, booker, APPROVED, start.minusDays(5), end.minusDays(3));
        bookingRepository.save(booking4);
    }

    @DirtiesContext
    @Test
    void findAllByBooker_Id() {
        List<Booking> result = bookingRepository.findAllByBooker_Id(2L, pageable);

        assertEquals(3, result.size());
        assertEquals(2, result.get(0).getId());
    }

    @DirtiesContext
    @Test
    void findAllByBooker_IdAndStartIsBeforeAndEndIsAfter() {
        List<Booking> result = bookingRepository.findAllByBooker_IdAndStartIsAfter(2L, time, pageable);

        assertEquals(2, result.size());
        assertEquals(2, result.get(0).getId());
    }

    @DirtiesContext
    @Test
    void findAllByBooker_IdAndStartIsAfter() {
        List<Booking> result = bookingRepository.findAllByBooker_IdAndStartIsAfter(2L, time, pageable);

        assertEquals(2, result.size());
        assertEquals(2, result.get(0).getId());
    }

    @DirtiesContext
    @Test
    void findAllByBooker_IdAndEndIsBefore() {
        List<Booking> result = bookingRepository.findAllByBooker_IdAndEndIsBefore(2L, time, pageable);

        assertEquals(1, result.size());
        assertEquals(4, result.get(0).getId());
    }

    @DirtiesContext
    @Test
    void findAllByBooker_IdAndStatusIs() {
        List<Booking> result = bookingRepository.findAllByBooker_IdAndStatusIs(2L, APPROVED, pageable);

        assertEquals(1, result.size());
        assertEquals(4, result.get(0).getId());
    }

    @DirtiesContext
    @Test
    void findAllByItem_User_Id() {
        List<Booking> result = bookingRepository.findAllByItem_User_Id(1L, pageable);

        assertEquals(2, result.size());
        assertEquals(2, result.get(0).getId());
    }

    @DirtiesContext
    @Test
    void findAllByItem_User_IdAndStartIsBeforeAndEndIsAfter() {
        List<Booking> result = bookingRepository
                .findAllByItem_User_IdAndStartIsBeforeAndEndIsAfter(1L, time.plusDays(4), time.plusDays(4), pageable);

        assertEquals(1, result.size());
        assertEquals(2, result.get(0).getId());
    }

    @DirtiesContext
    @Test
    void findAllByItem_User_IdAndStartIsAfter() {
        List<Booking> result = bookingRepository.findAllByItem_User_IdAndStartIsAfter(1L, time, pageable);

        assertEquals(2, result.size());
        assertEquals(2, result.get(0).getId());
    }

    @DirtiesContext
    @Test
    void findAllByItem_User_IdAndEndIsBefore() {
        List<Booking> result = bookingRepository.findAllByItem_User_IdAndStartIsAfter(1L, time, pageable);

        assertEquals(2, result.size());
        assertEquals(2, result.get(0).getId());
    }

    @DirtiesContext
    @Test
    void findAllByItem_User_IdAndStatusIs() {
        List<Booking> result = bookingRepository.findAllByItem_User_IdAndStatusIs(2L, WAITING, pageable);

        assertEquals(1, result.size());
        assertEquals(3, result.get(0).getId());
    }

    @DirtiesContext
    @Test
    void findFirstByItemIdAndStartBeforeOrderByStart() {
        Booking result = bookingRepository.findFirstByItemIdAndStartBeforeOrderByStart(4L, time, sort);

        assertEquals(4, result.getId());
    }

    @DirtiesContext
    @Test
    void findFirstByItemIdAndStartAfterOrderByStart() {
        Booking result = bookingRepository.findFirstByItemIdAndStartAfterOrderByStart(4L, time, sort);

        assertEquals(3, result.getId());
    }

    @DirtiesContext
    @Test
    void existsByBooker_IdAndEndIsBeforeAndStatusIs_true() {
        Boolean result = bookingRepository.existsByBooker_IdAndEndIsBeforeAndStatusIs(2L, time, APPROVED);

        assertTrue(result);
    }

    @DirtiesContext
    @Test
    void existsByBooker_IdAndEndIsBeforeAndStatusIs_false() {
        Boolean result = bookingRepository.existsByBooker_IdAndEndIsBeforeAndStatusIs(2L, time, WAITING);

        assertFalse(result);
    }
}