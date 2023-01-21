package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.common.exceptions.EntityNotExistsException;
import ru.practicum.shareit.common.exceptions.ItemAccessErrorException;
import ru.practicum.shareit.common.exceptions.ItemNotAvailableException;
import ru.practicum.shareit.common.model.FromSizeRequest;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.MapperComment;
import ru.practicum.shareit.item.mapper.MapperItem;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentsRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {


    private ItemService service;
    private UserRepository userRepository;
    private ItemRepository itemRepository;
    private BookingRepository bookingRepository;
    private CommentsRepository commentsRepository;
    private ItemRequestRepository itemRequestRepository;
    private MapperComment mapperComment;

    Long id;
    User user;
    User booker;
    Item item;
    Item itemWithoutId;
    ItemDto itemDto;
    ItemDto expectedItemDto;
    ItemRequest itemRequest;
    LocalDateTime start;
    LocalDateTime end;
    LocalDateTime time;
    Booking booking;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        itemRepository = mock(ItemRepository.class);
        bookingRepository = mock(BookingRepository.class);

        commentsRepository = mock(CommentsRepository.class);
        itemRequestRepository = mock(ItemRequestRepository.class);
        mapperComment = mock(MapperComment.class);
        MapperItem mapperItem = new MapperItem();
        service = new ItemServiceImpl(itemRepository, userRepository,
                bookingRepository, commentsRepository, itemRequestRepository,
                mapperItem, mapperComment);

        id = 1L;
        start = LocalDateTime.now().plusDays(1);
        end = LocalDateTime.now().plusDays(2);
        user = new User(1L, "mail@ya.ru", "user");
        booker = new User(2L, "mail2@ya.ru", "user2");
        item = new Item(1L, user, "item", "useful", true, List.of(), null);
        itemWithoutId = new Item(null, user, "item", "useful", true, List.of(), null);

        booking = new Booking(id, item, booker, Status.WAITING, start, end);

        itemDto = new ItemDto();
        itemDto.setName("item");
        itemDto.setDescription("useful");
        itemDto.setAvailable(true);

        expectedItemDto = new ItemDto();
        expectedItemDto.setId(id);
        expectedItemDto.setName("item");
        expectedItemDto.setDescription("useful");
        expectedItemDto.setAvailable(true);

        time = LocalDateTime.now();
        itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setUserId(2L);
        itemRequest.setDescription("something");
        itemRequest.setCreated(time);
    }

    @Test
    void createItem_whenIsOk_thenReturnItem() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(itemRepository.save(itemWithoutId)).thenReturn(item);

        ItemDto result = service.createItem(itemDto, 1L);

        assertEquals(expectedItemDto, result);
        verify(itemRepository, times(1)).save(itemWithoutId);
    }

    @Test
    void createItem_whenItemContainsRequest_thenReturnItem() {
        itemDto.setRequestId(id);
        itemWithoutId.setRequestId(id);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(itemRepository.save(itemWithoutId)).thenReturn(item);
        when(itemRequestRepository.existsById(id)).thenReturn(true);

        ItemDto result = service.createItem(itemDto, 1L);

        assertEquals(expectedItemDto, result);
        verify(itemRepository, times(1)).save(itemWithoutId);
    }

    @Test
    void createItem_whenWhenUserNotFound_thenEntityNotExistsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotExistsException.class, () -> service.createItem(itemDto, id));

        verify(itemRepository, never()).save(any());
    }

    @Test
    void createItem_whenWhenRequestNotFound_thenEntityNotExistsException() {
        itemDto.setRequestId(id);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(itemRequestRepository.existsById(id)).thenReturn(false);

        assertThrows(EntityNotExistsException.class, () -> service.createItem(itemDto, id));

        verify(itemRepository, never()).save(any());
    }

    @Test
    void updateItemById_whenIsOk_thenReturnItem() {
        Item updatedItem = new Item(1L, user, "item2", "useful2", true, List.of(), null);
        expectedItemDto.setName("item2");
        expectedItemDto.setDescription("useful2");
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(itemRepository.findById(id)).thenReturn(Optional.of(item));
        when(itemRepository.updateItemById(itemDto, id)).thenReturn(updatedItem);

        ItemDto result = service.updateItemById(itemDto, id, id);

        assertEquals(expectedItemDto, result);
    }

    @Test
    void updateItemById_whenWhenUserNotFound_thenEntityNotExistsException() {
        when(itemRepository.findById(id)).thenReturn(Optional.of(item));
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotExistsException.class, () -> service.updateItemById(itemDto, id, id));
    }

    @Test
    void updateItemById_whenWhenItemNotFound_thenEntityNotExistsException() {
        when(itemRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotExistsException.class, () -> service.updateItemById(itemDto, id, id));
    }

    @Test
    void updateItemById_whenWhenUserNotOwner_thenItemAccessErrorException() {
        when(itemRepository.findById(id)).thenReturn(Optional.of(item));
        when(userRepository.findById(1L)).thenReturn(Optional.of(booker));

        assertThrows(ItemAccessErrorException.class, () -> service.updateItemById(itemDto, id, id));
    }

    @Test
    void getItemById_whenIsOk_thenReturnItem() {
        when(itemRepository.findById(id)).thenReturn(Optional.of(item));

        ItemDto result = service.getItemById(id, 2L);

        assertEquals(expectedItemDto, result);
        verify(bookingRepository, never()).findFirstByItemIdAndStartBeforeOrderByStart(any(), any(), any());
        verify(bookingRepository, never()).findFirstByItemIdAndStartAfterOrderByStart(any(), any(), any());
    }

    @Test
    void getItemById_whenUserIsOwner_thenReturnItem() {
        expectedItemDto.setLastBooking(booking);
        expectedItemDto.setNextBooking(booking);
        when(itemRepository.findById(id)).thenReturn(Optional.of(item));
        when(bookingRepository.findFirstByItemIdAndStartBeforeOrderByStart(any(), any(), any()))
                .thenReturn(booking);
        when(bookingRepository.findFirstByItemIdAndStartAfterOrderByStart(any(), any(), any()))
                .thenReturn(booking);

        ItemDto result = service.getItemById(id, id);

        assertEquals(expectedItemDto, result);
        assertNotNull(result.getNextBooking());
        assertNotNull(result.getLastBooking());
    }

    @Test
    void getItemById_whenWhenItemNotFound_thenEntityNotExistsException() {
        when(itemRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotExistsException.class, () -> service.getItemById(id, id));
    }


    @Test
    void getItemsByOwner_whenIsOk_thenReturnItem() {
        Sort sort = Sort.by("id").ascending();
        Pageable pageable = FromSizeRequest.of(0, 10, sort);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(itemRepository.findAllByUserId(id, pageable)).thenReturn(List.of(item));

        List<ItemDto> result = service.getItemsByOwner(id, pageable);

        assertEquals(List.of(expectedItemDto), result);
    }

    @Test
    void getItemsByOwner_whenWhenUserNotFound_thenEntityNotExistsException() {
        Sort sort = Sort.by("id").ascending();
        Pageable pageable = FromSizeRequest.of(0, 10, sort);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotExistsException.class, () -> service.getItemsByOwner(id, pageable));
    }

    @Test
    void searchItemsByRequest_whenIsOk_thenReturnItemsList() {
        Sort sort = Sort.by("id").ascending();
        Pageable pageable = FromSizeRequest.of(0, 10, sort);
        when(itemRepository.findItemsByRequest("request", pageable)).thenReturn(List.of(item));

        List<ItemDto> result = service.searchItemsByRequest("request", pageable);

        assertEquals(List.of(expectedItemDto), result);
    }

    @Test
    void searchItemsByRequest_whenRequestIsBlank_thenReturnItemsList() {
        Sort sort = Sort.by("id").ascending();
        Pageable pageable = FromSizeRequest.of(0, 10, sort);
        List<ItemDto> result = service.searchItemsByRequest(" ", pageable);

        assertEquals(List.of(), result);
        verify(itemRepository, never()).findItemsByRequest(anyString(), any());
    }

    @Test
    void createComment_whenIsOk_thenReturnComment() {
        Comment commentWithoutId = new Comment();
        commentWithoutId.setText("comment");
        commentWithoutId.setItem(item);
        commentWithoutId.setAuthor(booker);
        commentWithoutId.setCreated(time);

        Comment comment = new Comment();
        comment.setId(id);
        comment.setText("comment");
        comment.setItem(item);
        comment.setAuthor(booker);
        comment.setCreated(time);

        CommentDto commentDto = new CommentDto();
        commentDto.setText("comment");

        CommentDto expectedCommentDto = new CommentDto();
        expectedCommentDto.setText("comment");
        expectedCommentDto.setId(id);
        expectedCommentDto.setCreated(time);
        expectedCommentDto.setAuthorName(booker.getName());

        when(itemRepository.findById(id)).thenReturn(Optional.of(item));
        when(userRepository.findById(2L)).thenReturn(Optional.of(booker));
        when(bookingRepository.existsByBooker_IdAndEndIsBeforeAndStatusIs(any(), any(), any()))
                .thenReturn(true);
        when(mapperComment.convertCommentDtoToComment(commentDto, item, booker)).thenReturn(commentWithoutId);
        when(mapperComment.convertCommentToCommentDto(comment)).thenReturn(expectedCommentDto);
        when(commentsRepository.save(commentWithoutId)).thenReturn(comment);

        CommentDto result = service.createComment(id, 2L, commentDto);

        assertEquals(expectedCommentDto, result);
        verify(commentsRepository, times(1)).save(commentWithoutId);
    }

    @Test
    void createComment_whenWhenItemNotFound_thenEntityNotExistsException() {
        CommentDto commentDto = new CommentDto();
        commentDto.setText("comment");
        when(itemRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotExistsException.class, () -> service.createComment(id, id, commentDto));
        verify(commentsRepository, never()).save(any());
    }

    @Test
    void createComment_whenWhenUserNotFound_thenEntityNotExistsException() {
        CommentDto commentDto = new CommentDto();
        commentDto.setText("comment");
        when(itemRepository.findById(id)).thenReturn(Optional.of(item));
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotExistsException.class, () -> service.createComment(id, id, commentDto));
        verify(commentsRepository, never()).save(any());
    }

    @Test
    void createComment_whenWhenUserNotBookItem_thenEntityNotExistsException() {
        CommentDto commentDto = new CommentDto();
        commentDto.setText("comment");
        when(itemRepository.findById(id)).thenReturn(Optional.of(item));
        when(userRepository.findById(id)).thenReturn(Optional.of(booker));
        when(bookingRepository.existsByBooker_IdAndEndIsBeforeAndStatusIs(any(), any(), any()))
                .thenReturn(false);

        assertThrows(ItemNotAvailableException.class, () -> service.createComment(id, id, commentDto));
        verify(commentsRepository, never()).save(any());
    }
}