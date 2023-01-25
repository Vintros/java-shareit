package ru.practicum.shareits.request.mapper;

import org.springframework.stereotype.Service;
import ru.practicum.shareits.request.dto.ItemRequestDto;
import ru.practicum.shareits.request.model.ItemRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemRequestMapper {

    public ItemRequest convertToItemRequest(ItemRequestDto itemRequestDto, Long userId) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setUserId(userId);
        itemRequest.setDescription(itemRequestDto.getDescription());
        itemRequest.setCreated(LocalDateTime.now());

        return itemRequest;
    }

    public ItemRequestDto convertToItemRequestDto(ItemRequest itemRequest) {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(itemRequest.getId());
        itemRequestDto.setUserId(itemRequest.getUserId());
        itemRequestDto.setDescription(itemRequest.getDescription());
        itemRequestDto.setCreated(itemRequest.getCreated());
        itemRequestDto.setItems(itemRequest.getItems());
        return itemRequestDto;
    }

    public List<ItemRequestDto> convertAllToItemRequestDto(List<ItemRequest> itemRequests) {
        return itemRequests.stream()
                .map(this::convertToItemRequestDto)
                .collect(Collectors.toList());
    }
}
