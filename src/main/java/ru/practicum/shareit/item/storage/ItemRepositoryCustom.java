package ru.practicum.shareit.item.storage;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

public interface ItemRepositoryCustom {

    @Transactional
    Item updateItemById(ItemDto itemDto, Long itemId);

}
