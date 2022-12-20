package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {
    Item createItem(Item item);

    Item getItemById(Long itemId);

    void checkItemExistsById(Long id);

    Item updateItem(ItemDto itemDto, Long itemId);

    List<Item> getItemsByOwner(Long userId);

    List<Item> getAllItems();
}
