package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.common.model.FromSizeRequest;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemDto createItem(@Valid @RequestBody ItemDto itemDto,
                              @RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        return itemService.createItem(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItemById(@RequestBody ItemDto itemDto, @PathVariable Long itemId,
                                  @RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        return itemService.updateItemById(itemDto, itemId, userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable Long itemId, @RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        return itemService.getItemById(itemId, userId);
    }

    @GetMapping
    public List<ItemDto> getItemsByOwner(@RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                         @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                         @Positive @RequestParam(defaultValue = "10") Integer size) {
        Sort sort = Sort.by("id").ascending();
        Pageable pageable = FromSizeRequest.of(from, size, sort);
        return itemService.getItemsByOwner(userId, pageable);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItemsByRequest(@RequestParam String text,
                                              @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                              @Positive @RequestParam(defaultValue = "10") Integer size) {
        Sort sort = Sort.by("id").ascending();
        Pageable pageable = FromSizeRequest.of(from, size, sort);
        return itemService.searchItemsByRequest(text, pageable);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@PathVariable Long itemId, @RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                 @Valid @RequestBody CommentDto commentDto) {
        return itemService.createComment(itemId, userId, commentDto);
    }
}
