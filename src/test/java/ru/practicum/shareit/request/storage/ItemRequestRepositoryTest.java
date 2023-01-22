package ru.practicum.shareit.request.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.common.model.FromSizeRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ItemRequestRepositoryTest {

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
    Booking booking;

    @BeforeEach
    public void setUp() {
        start = LocalDateTime.now().plusDays(1);
        end = LocalDateTime.now().plusDays(2);
        user = new User(1L, "mail@ya.ru", "user");
        userRepository.save(user);
        booker = new User(2L, "mail2@ya.ru", "user2");
        userRepository.save(booker);
        item = new Item(1L, user, "item", "useful", true, List.of(), null);
        itemRepository.save(item);
        booking = new Booking(1L, item, booker, Status.WAITING, start, end);
        bookingRepository.save(booking);

        LocalDateTime time = LocalDateTime.now();
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
    }

    @DirtiesContext
    @Test
    void findAllByUserId() {
        Sort sort = Sort.by("created").descending();

        List<ItemRequest> result = itemRequestRepository.findAllByUserId(2L, sort);

        assertEquals(2, result.size());
        assertEquals("something3", result.get(0).getDescription());
    }

    @DirtiesContext
    @Test
    void findAllByUserId_whenNoItems() {
        Sort sort = Sort.by("created").descending();

        List<ItemRequest> result = itemRequestRepository.findAllByUserId(5L, sort);

        assertEquals(0, result.size());
    }

    @DirtiesContext
    @Test
    void findAllByUserIdNotLike() {
        Sort sort = Sort.by("created").descending();
        Pageable pageable = FromSizeRequest.of(0, 10, sort);

        List<ItemRequest> result = itemRequestRepository.findAllByUserIdNotLike(5L, pageable);

        assertEquals(3, result.size());
        assertEquals("something3", result.get(0).getDescription());
    }

    @DirtiesContext
    @Test
    void findAllByUserIdNotLike_whenPageOneAndSizeOne() {
        Sort sort = Sort.by("created").descending();
        Pageable pageable = FromSizeRequest.of(1, 1, sort);

        List<ItemRequest> result = itemRequestRepository.findAllByUserIdNotLike(5L, pageable);

        assertEquals(1, result.size());
        assertEquals("something2", result.get(0).getDescription());
    }


}