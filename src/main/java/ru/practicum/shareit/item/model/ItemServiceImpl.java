package ru.practicum.shareit.item.model;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ItemIncorrectOwnerException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.MapperItem;
import ru.practicum.shareit.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemStorage itemStorage;
    private final UserStorage userStorage;
    private final MapperItem mapperItem;

    @Override
    public ItemDto createItem(ItemDto itemDto, Long userId) {
        userStorage.checkUserExistsById(userId);
        Item item = mapperItem.convertItemDtoToItem(itemDto);
        item.setOwnerId(userId);
        Item createdItem = itemStorage.createItem(item);
        log.info("Item with id: {} created", item.getId());
        return mapperItem.convertItemToItemDto(createdItem);
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, Long itemId, Long userId) {
        userStorage.checkUserExistsById(userId);
        itemStorage.checkItemExistsById(itemId);
        Item item = itemStorage.getItemById(itemId);
        checkCorrectOwner(item, userId);
        Item updatedItem = itemStorage.updateItem(itemDto, itemId);
        log.info("Item with id: {} updated", item.getId());
        return mapperItem.convertItemToItemDto(updatedItem);
    }

    @Override
    public ItemDto getItemById(Long itemId) {
        itemStorage.checkItemExistsById(itemId);
        Item item = itemStorage.getItemById(itemId);
        log.info("Item with id: {} requested", item.getId());
        return mapperItem.convertItemToItemDto(item);
    }

    @Override
    public List<ItemDto> getItemsByOwner(Long userId) {
        userStorage.checkUserExistsById(userId);
        List<Item> items = itemStorage.getItemsByOwner(userId);
        log.info("Owner's items with id: {} requested", userId);
        return mapperItem.convertAllItemsToItemsDto(items);
    }

    @Override
    public List<ItemDto> searchItemsByRequest(String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        String searchText = text.toLowerCase();
        Predicate<Item> isName = i -> i.getName().toLowerCase().contains(searchText);
        Predicate<Item> isDescription = i -> i.getDescription().toLowerCase().contains(searchText);
        List<Item> items = itemStorage.getAllItems()
                .stream()
                .filter(isName.or(isDescription))
                .filter(Item::getAvailable)
                .collect(Collectors.toList());
        log.info("Search for items by \"{}\" requested", text);
        return mapperItem.convertAllItemsToItemsDto(items);
    }

    private void checkCorrectOwner(Item item, Long userId) {
        if (item.getOwnerId().equals(userId)) {
            throw new ItemIncorrectOwnerException("incorrect owner");
        }
    }
}
