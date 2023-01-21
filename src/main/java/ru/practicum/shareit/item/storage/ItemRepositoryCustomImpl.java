package ru.practicum.shareit.item.storage;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.common.exceptions.EntityNotExistsException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

@Repository
public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {

    private final ItemRepository itemRepository;

    public ItemRepositoryCustomImpl(@Lazy ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public Item updateItemById(ItemDto itemDto, Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotExistsException("such item not registered"));
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        return itemRepository.save(item);
    }
}
