package ru.practicum.shareits.item.storage;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareits.item.dto.ItemDto;
import ru.practicum.shareits.item.model.Item;

public interface ItemRepositoryCustom {

    @Transactional
    Item updateItemById(ItemDto itemDto, Long itemId);

}
