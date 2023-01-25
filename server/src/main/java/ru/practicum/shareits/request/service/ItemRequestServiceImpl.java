package ru.practicum.shareits.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareits.request.storage.ItemRequestRepository;
import ru.practicum.shareits.common.exceptions.EntityNotExistsException;
import ru.practicum.shareits.request.dto.ItemRequestDto;
import ru.practicum.shareits.request.mapper.ItemRequestMapper;
import ru.practicum.shareits.request.model.ItemRequest;
import ru.practicum.shareits.user.storage.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRequestMapper itemRequestMapper;

    @Override
    public ItemRequestDto addRequest(ItemRequestDto itemRequestDto, Long userId) {
        checkUserExists(userId);
        ItemRequest itemRequest = itemRequestMapper.convertToItemRequest(itemRequestDto, userId);
        ItemRequest savedItemRequest = itemRequestRepository.save(itemRequest);
        log.info("Request with id: {} created", savedItemRequest.getId());
        return itemRequestMapper.convertToItemRequestDto(savedItemRequest);
    }

    @Override
    public List<ItemRequestDto> getOwnRequests(Long userId) {
        checkUserExists(userId);
        Sort sortLast = Sort.by("created").descending();
        List<ItemRequest> itemRequests = itemRequestRepository.findAllByUserId(userId, sortLast);
        log.info("Owner's requests with id: {} requested", userId);
        return itemRequestMapper.convertAllToItemRequestDto(itemRequests);
    }

    @Override
    public List<ItemRequestDto> getAllRequests(Long userId, Pageable pageable) {
        List<ItemRequest> itemRequests = itemRequestRepository.findAllByUserIdIsNot(userId, pageable);
        log.info("All requests are requested");
        return itemRequestMapper.convertAllToItemRequestDto(itemRequests);
    }

    @Override
    public ItemRequestDto getRequestById(Long requestId, Long userId) {
        checkUserExists(userId);
        ItemRequest itemRequest = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotExistsException("such request not registered"));
        log.info("Request with id: {} requested", requestId);
        return itemRequestMapper.convertToItemRequestDto(itemRequest);
    }

    private void checkUserExists(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotExistsException("such user not registered");
        }
    }
}
