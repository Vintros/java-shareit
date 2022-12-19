package ru.practicum.shareit.item.model;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.ItemNotExistsException;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class InMemoryItemStorage implements ItemStorage {

    private Long id = 0L;
    private final Map<Long, Item> items = new HashMap<>();

    @Override
    public Item createItem(Item item) {
        Long id = getNewId();
        item.setId(id);
        items.put(id, item);
        return item;
    }

    @Override
    public Item getItemById(Long id) {
        return items.get(id);
    }

    @Override
    public void checkItemExistsById(Long id) {
        if (!items.containsKey(id)) {
            throw new ItemNotExistsException("such item not registered");
        }
    }

    @Override
    public Item updateItem(ItemDto itemDto, Long itemId) {
        Item item = items.get(itemId);
        if (itemDto.getName() != null && !itemDto.getName().isEmpty()) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null && !itemDto.getDescription().isEmpty()) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        return item;
    }

    @Override
    public List<Item> getItemsByOwner(Long userId) {
        return items.values().stream()
                .filter(i -> i.getOwnerId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> getAllItems() {
        return new ArrayList<>(items.values());
    }

    private Long getNewId() {
        return ++id;
    }
}
