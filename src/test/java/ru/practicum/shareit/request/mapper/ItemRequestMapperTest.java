package ru.practicum.shareit.request.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ItemRequestMapperTest {

    ItemRequestMapper itemRequestMapper = new ItemRequestMapper();

    ItemRequest itemRequestWithId;
    ItemRequest itemRequestWithoutId;
    ItemRequestDto itemRequestDto;
    ItemRequestDto expectedItemRequestDto;
    LocalDateTime time;
    User user;
    User booker;
    Item item;

    @BeforeEach
    void setUp() {
        time = LocalDateTime.now();

        user = new User(1L, "mail@ya.ru", "user");
        item = new Item(1L, user, "item", "useful", true, List.of(), 1L);
        booker = new User(2L, "mail2@ya.ru", "user2");

        itemRequestWithId = new ItemRequest();
        itemRequestWithId.setId(1L);
        itemRequestWithId.setUserId(2L);
        itemRequestWithId.setDescription("something");
        itemRequestWithId.setCreated(time);

        itemRequestWithoutId = new ItemRequest();
        itemRequestWithoutId.setUserId(2L);
        itemRequestWithoutId.setDescription("something");
        itemRequestWithoutId.setCreated(time);

        itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("something");

        expectedItemRequestDto = new ItemRequestDto();
        expectedItemRequestDto.setId(1L);
        expectedItemRequestDto.setUserId(2L);
        expectedItemRequestDto.setDescription("something");
        expectedItemRequestDto.setCreated(time);
    }

    @Test
    void convertToItemRequest() {
        ItemRequest result = itemRequestMapper.convertToItemRequest(itemRequestDto, 2L);

        assertNull(result.getId());
        assertEquals(2L, result.getUserId());
        assertNotNull(result.getCreated());
        assertEquals("something", result.getDescription());
    }

    @Test
    void convertToItemRequestDto() {
        ItemRequestDto result = itemRequestMapper.convertToItemRequestDto(itemRequestWithId);

        assertEquals(expectedItemRequestDto, result);
    }

    @Test
    void convertAllToItemRequestDto() {
        List<ItemRequestDto> result = itemRequestMapper.convertAllToItemRequestDto(List.of(itemRequestWithId));

        assertEquals(List.of(expectedItemRequestDto), result);
    }
}