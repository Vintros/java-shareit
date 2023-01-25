package ru.practicum.shareits.request.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareits.request.dto.ItemRequestDto;
import ru.practicum.shareits.request.model.ItemRequest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ItemRequestMapperTest {

    private final ItemRequestMapper itemRequestMapper = new ItemRequestMapper();

    private ItemRequest itemRequestWithId;
    private ItemRequestDto itemRequestDto;
    private ItemRequestDto expectedItemRequestDto;

    @BeforeEach
    void setUp() {
        LocalDateTime time = LocalDateTime.now();

        itemRequestWithId = new ItemRequest();
        itemRequestWithId.setId(1L);
        itemRequestWithId.setUserId(2L);
        itemRequestWithId.setDescription("something");
        itemRequestWithId.setCreated(time);

        ItemRequest itemRequestWithoutId = new ItemRequest();
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