package ru.practicum.shareits.item.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareits.booking.enums.Status;
import ru.practicum.shareits.booking.model.Booking;
import ru.practicum.shareits.booking.storage.BookingRepository;
import ru.practicum.shareits.common.exceptions.EntityNotExistsException;
import ru.practicum.shareits.common.model.FromSizeRequest;
import ru.practicum.shareits.item.dto.ItemDto;
import ru.practicum.shareits.item.model.Item;
import ru.practicum.shareits.request.model.ItemRequest;
import ru.practicum.shareits.request.storage.ItemRequestRepository;
import ru.practicum.shareits.user.model.User;
import ru.practicum.shareits.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ItemRepositoryTest {

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @BeforeEach
    public void setUp() {
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(2);
        User user = new User(1L, "mail@ya.ru", "user");
        userRepository.save(user);
        User booker = new User(2L, "mail2@ya.ru", "user2");
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

        Item item = new Item(1L, user, "item", "useful", true, List.of(), 1L);
        itemRepository.save(item);
        Item item2 = new Item(2L, user, "item2", "useful2", false, List.of(), 1L);
        itemRepository.save(item2);
        Item item3 = new Item(3L, user, "item3", "useful3", true, List.of(), 1L);
        itemRepository.save(item3);
        Item item4 = new Item(4L, booker, "item4", "useful4", true, List.of(), 2L);
        itemRepository.save(item4);

        Booking booking = new Booking(1L, item, booker, Status.WAITING, start, end);
        bookingRepository.save(booking);
    }

    @DirtiesContext
    @Test
    void findAllByUserId() {
        Sort sort = Sort.by("id").ascending();
        Pageable pageable = FromSizeRequest.of(0, 10, sort);

        List<Item> result = itemRepository.findAllByUserId(1L, pageable);

        assertEquals(3, result.size());
        assertEquals("useful", result.get(0).getDescription());
    }

    @DirtiesContext
    @Test
    void findAllByUserId_whenFomTwoSizeTwo() {
        Sort sort = Sort.by("id").ascending();
        Pageable pageable = FromSizeRequest.of(2, 2, sort);

        List<Item> result = itemRepository.findAllByUserId(1L, pageable);

        assertEquals(1, result.size());
        assertEquals("useful3", result.get(0).getDescription());
    }

    @DirtiesContext
    @Test
    void findItemsByRequest_whenRequestInName() {
        Sort sort = Sort.by("id").ascending();
        Pageable pageable = FromSizeRequest.of(0, 10, sort);

        List<Item> result = itemRepository.findItemsByRequest("iTem", pageable);

        assertEquals(4, result.size());
        assertEquals("useful", result.get(0).getDescription());
    }


    @DirtiesContext
    @Test
    void findItemsByRequest_whenRequestInDescriptionAndAvailableTrue() {
        Sort sort = Sort.by("id").ascending();
        Pageable pageable = FromSizeRequest.of(0, 10, sort);

        List<Item> result = itemRepository.findItemsByRequest("UsEf", pageable);

        assertEquals(3, result.size());
        assertEquals("useful", result.get(0).getDescription());
    }

    @DirtiesContext
    @Test
    void findItemsByRequest_whenFromTwoAndSizeTwo() {
        Sort sort = Sort.by("id").ascending();
        Pageable pageable = FromSizeRequest.of(2, 2, sort);

        List<Item> result = itemRepository.findItemsByRequest("iTem", pageable);

        assertEquals(2, result.size());
        assertEquals("useful3", result.get(0).getDescription());
    }

    @DirtiesContext
    @Test
    void updateItemById_whenUpdateName() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("updated item");

        Item result = itemRepository.updateItemById(itemDto, 1L);

        assertEquals(1, result.getId());
        assertEquals("useful", result.getDescription());
        assertEquals("updated item", result.getName());
        assertTrue(result.getAvailable());
    }

    @DirtiesContext
    @Test
    void updateItemById_whenUpdateDescription() {
        ItemDto itemDto = new ItemDto();
        itemDto.setDescription("updated");

        Item result = itemRepository.updateItemById(itemDto, 1L);

        assertEquals(1, result.getId());
        assertEquals("updated", result.getDescription());
        assertEquals("item", result.getName());
        assertTrue(result.getAvailable());
    }

    @DirtiesContext
    @Test
    void updateItemById_whenUpdateAvailable() {
        ItemDto itemDto = new ItemDto();
        itemDto.setAvailable(false);

        Item result = itemRepository.updateItemById(itemDto, 1L);

        assertEquals(1, result.getId());
        assertEquals("useful", result.getDescription());
        assertEquals("item", result.getName());
        assertFalse(result.getAvailable());
    }

    @DirtiesContext
    @Test
    void updateItemById_whenUserNotFounded_thenEntityNotExistsException() {
        ItemDto itemDto = new ItemDto();
        itemDto.setAvailable(false);

        assertThrows(EntityNotExistsException.class, () -> itemRepository.updateItemById(itemDto, 99L));
    }
}
