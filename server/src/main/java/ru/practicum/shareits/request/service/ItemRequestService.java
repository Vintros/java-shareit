package ru.practicum.shareits.request.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareits.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto addRequest(ItemRequestDto itemRequestDto, Long userId);

    List<ItemRequestDto> getOwnRequests(Long userId);

    List<ItemRequestDto> getAllRequests(Long userId, Pageable pageable);

    ItemRequestDto getRequestById(Long requestId, Long userId);
}
