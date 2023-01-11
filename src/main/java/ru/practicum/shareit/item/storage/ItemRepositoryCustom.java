package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import javax.transaction.Transactional;

public interface ItemRepositoryCustom {

    @Transactional
    Item updateItemById(ItemDto itemDto, Long itemId);

}
