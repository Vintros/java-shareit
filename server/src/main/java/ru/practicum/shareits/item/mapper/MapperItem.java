package ru.practicum.shareits.item.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareits.booking.model.Booking;
import ru.practicum.shareits.user.model.User;
import ru.practicum.shareits.item.dto.ItemDto;
import ru.practicum.shareits.item.model.Item;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MapperItem {

    public ItemDto convertItemToItemDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        itemDto.setComments(item.getComments());
        if (item.getRequestId() != null) {
            itemDto.setRequestId(item.getRequestId());
        }
        return itemDto;
    }

    public ItemDto convertItemToItemDtoForOwner(Item item,
                                                Booking lastBooking, Booking nextBooking) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        if (item.getRequestId() != null) {
            itemDto.setRequestId(item.getRequestId());
        }
        if (lastBooking != null) {
            itemDto.setLastBooking(lastBooking);
        }
        if (nextBooking != null) {
            itemDto.setNextBooking(nextBooking);
        }
        itemDto.setComments(item.getComments());
        return itemDto;
    }

    public Item convertItemDtoToItem(ItemDto itemDto, User user) {
        Item item = new Item();
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setUser(user);
        item.setRequestId(itemDto.getRequestId());
        return item;
    }

    public List<ItemDto> convertAllItemsToItemsDto(List<Item> items) {
        return items.stream()
                .map(this::convertItemToItemDto)
                .collect(Collectors.toList());
    }
}
