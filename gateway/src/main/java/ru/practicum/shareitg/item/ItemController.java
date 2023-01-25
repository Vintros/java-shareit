package ru.practicum.shareitg.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareitg.item.dto.CommentDto;
import ru.practicum.shareitg.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;


@Controller
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> createItem(@Valid @RequestBody ItemDto itemDto,
                                             @RequestHeader(value = "X-Sharer-User-Id") Long userId) {
            log.info("Creating item {}, by userId={}", itemDto, userId);
        return itemClient.createItem(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItemById(@RequestBody ItemDto itemDto, @PathVariable Long itemId,
                                                 @RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        log.info("Updating item {}, by userId={}", itemDto, userId);
        return itemClient.updateItemById(itemDto, itemId, userId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@PathVariable Long itemId,
                                              @RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        log.info("Get item with Id={}, by userId={}", itemId, userId);
        return itemClient.getItemById(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getItemsByOwner(@RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                                  @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                  @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("Get items by owner with Id={}", userId);
        return itemClient.getItemsByOwner(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItemsByRequest(@RequestParam String text,
                                                       @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                       @Positive @RequestParam(defaultValue = "10") Integer size,
                                                       @RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        log.info("Search items by request={} bu userId={}", text, userId);
        return itemClient.searchItemsByRequest(userId, text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@PathVariable Long itemId, @RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                 @Valid @RequestBody CommentDto commentDto) {
        log.info("Creating comment {}, to item {} by userId={}", commentDto, itemId, userId);
        return itemClient.createComment(itemId, userId, commentDto);
    }
}
