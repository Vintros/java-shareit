package ru.practicum.shareitg.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareitg.request.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping("/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> addRequest(@Valid @RequestBody ItemRequestDto itemRequestDto,
                                             @RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        log.info("Creating item request {}, by userId={}", itemRequestDto, userId);
        return itemRequestClient.addRequest(itemRequestDto, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getOwnRequests(@RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        log.info("Get own item requests, by userId={}", userId);
        return itemRequestClient.getOwnRequests(userId);
    }

    @GetMapping("{requestId}")
    public ResponseEntity<Object> getRequestById(@RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                         @PathVariable Long requestId) {
        log.info("Get item request id={}, by userId={}", requestId, userId);
        return itemRequestClient.getRequestById(requestId, userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequests(@RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                               @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                               @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("Get all item requests, by userId={}", userId);
        return itemRequestClient.getAllRequests(userId, from, size);
    }
}
