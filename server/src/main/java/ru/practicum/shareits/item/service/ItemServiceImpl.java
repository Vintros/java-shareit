package ru.practicum.shareits.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareits.booking.enums.Status;
import ru.practicum.shareits.booking.model.Booking;
import ru.practicum.shareits.booking.storage.BookingRepository;
import ru.practicum.shareits.common.exceptions.EntityNotExistsException;
import ru.practicum.shareits.common.exceptions.ItemAccessErrorException;
import ru.practicum.shareits.common.exceptions.ItemNotAvailableException;
import ru.practicum.shareits.item.mapper.MapperItem;
import ru.practicum.shareits.item.storage.CommentsRepository;
import ru.practicum.shareits.item.storage.ItemRepository;
import ru.practicum.shareits.request.storage.ItemRequestRepository;
import ru.practicum.shareits.user.model.User;
import ru.practicum.shareits.user.storage.UserRepository;
import ru.practicum.shareits.item.dto.CommentDto;
import ru.practicum.shareits.item.mapper.MapperComment;
import ru.practicum.shareits.item.model.Comment;
import ru.practicum.shareits.item.dto.ItemDto;
import ru.practicum.shareits.item.model.Item;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentsRepository commentsRepository;
    private final ItemRequestRepository itemRequestRepository;
    private final MapperItem mapperItem;
    private final MapperComment mapperComment;

    @Override
    public ItemDto createItem(ItemDto itemDto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotExistsException("such user not registered"));
        if (itemDto.getRequestId() != null && !itemRequestRepository.existsById(itemDto.getRequestId())) {
            throw new EntityNotExistsException("such request not registered");
        }
        Item item = mapperItem.convertItemDtoToItem(itemDto, user);
        Item createdItem = itemRepository.save(item);
        log.info("Item with id: {} created", item.getId());
        return mapperItem.convertItemToItemDto(createdItem);
    }

    @Override
    public ItemDto updateItemById(ItemDto itemDto, Long itemId, Long userId) {
        checkCorrectOwner(itemId, userId);
        Item updatedItem = itemRepository.updateItemById(itemDto, itemId);
        log.info("Item with id: {} updated", itemId);
        return mapperItem.convertItemToItemDto(updatedItem);
    }

    @Override
    public ItemDto getItemById(Long itemId, Long userId) {
        ItemDto itemDto;
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotExistsException("such item not registered"));
        if (item.getUser().getId().equals(userId)) {
            itemDto = addBookingInfoToItem(item);
        } else {
            itemDto = mapperItem.convertItemToItemDto(item);
        }
        log.info("Item with id: {} requested", item.getId());
        return itemDto;
    }

    @Override
    public List<ItemDto> getItemsByOwner(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotExistsException("such user not registered"));
        List<Item> items = itemRepository.findAllByUserId(userId, pageable);
        log.info("Owner's items with id: {} requested", user.getId());
        List<ItemDto> itemsDto = new ArrayList<>();
        for (Item item : items) {
            itemsDto.add(addBookingInfoToItem(item));
        }
        return itemsDto;
    }

    @Override
    public List<ItemDto> searchItemsByRequest(String request, Pageable pageable) {
        if (request.isBlank()) {
            return new ArrayList<>();
        }
        List<Item> items = itemRepository
                .findItemsByRequest(request, pageable);
        log.info("Search for items by \"{}\" requested", request);
        return mapperItem.convertAllItemsToItemsDto(items);
    }

    @Override
    public CommentDto createComment(Long itemId, Long userId, CommentDto commentDto) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotExistsException("such item not registered"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotExistsException("such user not registered"));
        if (!bookingRepository.existsByBooker_IdAndEndIsBeforeAndStatusIs(userId, LocalDateTime.now(), Status.APPROVED)) {
            throw new ItemNotAvailableException("user have never booked this item");
        }
        Comment comment = mapperComment.convertCommentDtoToComment(commentDto, item, user);
        Comment savedComment = commentsRepository.save(comment);
        log.info("comment with id: {} for item with id: {} created", savedComment.getId(), itemId);
        return mapperComment.convertCommentToCommentDto(savedComment);
    }

    private void checkCorrectOwner(Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotExistsException("such item not registered"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotExistsException("such user not registered"));
        if (!item.getUser().equals(user)) {
            throw new ItemAccessErrorException("incorrect owner");
        }
    }

    private ItemDto addBookingInfoToItem(Item item) {
        Sort sortLast = Sort.by("start").descending();
        LocalDateTime now = LocalDateTime.now();
        Booking lastBooking = bookingRepository.findFirstByItemIdAndStartBeforeOrderByStart(item.getId(),
                now, sortLast);
        Sort sortNext = Sort.by("start").ascending();
        Booking nextBooking = bookingRepository.findFirstByItemIdAndStartAfterOrderByStart(item.getId(),
                now, sortNext);
        return mapperItem.convertItemToItemDtoForOwner(item, lastBooking, nextBooking);
    }
}
