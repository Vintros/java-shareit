package ru.practicum.shareits.request.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareits.common.exceptions.EntityNotExistsException;
import ru.practicum.shareits.common.model.FromSizeRequest;
import ru.practicum.shareits.request.dto.ItemRequestDto;
import ru.practicum.shareits.request.mapper.ItemRequestMapper;
import ru.practicum.shareits.request.model.ItemRequest;
import ru.practicum.shareits.request.storage.ItemRequestRepository;
import ru.practicum.shareits.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {

    @InjectMocks
    private ItemRequestServiceImpl service;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRequestRepository itemRequestRepository;

    @Mock
    private ItemRequestMapper mapper;

    private Long id;
    private ItemRequest itemRequestWithId;
    private ItemRequest itemRequestWithoutId;
    private ItemRequestDto itemRequestDto;
    private ItemRequestDto expectedItemRequestDto;

    @BeforeEach
    void setUp() {

        id = 1L;

        LocalDateTime time = LocalDateTime.now();
        itemRequestWithId = new ItemRequest();
        itemRequestWithId.setId(1L);
        itemRequestWithId.setUserId(2L);
        itemRequestWithId.setDescription("something");
        itemRequestWithId.setCreated(time);

        itemRequestWithoutId = new ItemRequest();
        itemRequestWithoutId.setUserId(2L);
        itemRequestWithoutId.setDescription("something");
        itemRequestWithoutId.setCreated(time);

        itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("something");

        expectedItemRequestDto = new ItemRequestDto();
        expectedItemRequestDto.setId(1L);
        expectedItemRequestDto.setUserId(2L);
        expectedItemRequestDto.setDescription("something");
        expectedItemRequestDto.setCreated(time);
    }

    @Test
    void addRequest_whenIsOk_thenReturnItemRequest() {
        when(userRepository.existsById(2L)).thenReturn(true);
        when(itemRequestRepository.save(itemRequestWithoutId)).thenReturn(itemRequestWithId);
        when(mapper.convertToItemRequest(itemRequestDto, 2L)).thenReturn(itemRequestWithoutId);
        when(mapper.convertToItemRequestDto(itemRequestWithId)).thenReturn(expectedItemRequestDto);

        ItemRequestDto actualItemRequestDto = service.addRequest(itemRequestDto, 2L);

        assertEquals(expectedItemRequestDto, actualItemRequestDto);
        verify(itemRequestRepository, times(1)).save(itemRequestWithoutId);
    }

    @Test
    void addRequest_whenUserNotFounded_thenReturnEntityNotExistsException() {
        when(userRepository.existsById(2L)).thenReturn(false);

        assertThrows(EntityNotExistsException.class, () -> service.addRequest(itemRequestDto, 2L));
        verify(itemRequestRepository, never()).save(itemRequestWithoutId);
    }

    @Test
    void getOwnRequests_whenAllIsOk_thenReturnListOfItemRequest() {
        when(userRepository.existsById(2L)).thenReturn(true);
        Sort sortLast = Sort.by("created").descending();
        when(itemRequestRepository.findAllByUserId(2L, sortLast)).thenReturn(List.of(itemRequestWithId));
        when(mapper.convertAllToItemRequestDto(List.of(itemRequestWithId))).thenReturn(List.of(expectedItemRequestDto));

        List<ItemRequestDto> actualResult = service.getOwnRequests(2L);

        assertEquals(List.of(expectedItemRequestDto), actualResult);
    }

    @Test
    void getOwnRequests_whenUserNotFounded_thenReturnEntityNotExistsException() {
        when(userRepository.existsById(2L)).thenReturn(false);

        assertThrows(EntityNotExistsException.class, () -> service.addRequest(itemRequestDto, 2L));
        verify(itemRequestRepository, never()).save(itemRequestWithoutId);
    }

    @Test
    void getAllRequests_whenAllIsOk_thenReturnListOfItemRequest() {
        Sort sort = Sort.by("created").descending();
        Pageable pageable = FromSizeRequest.of(0, 10, sort);
        when(itemRequestRepository.findAllByUserIdIsNot(2L, pageable)).thenReturn(List.of(itemRequestWithId));
        when(mapper.convertAllToItemRequestDto(List.of(itemRequestWithId))).thenReturn(List.of(expectedItemRequestDto));

        List<ItemRequestDto> actualResult = service.getAllRequests(2L, pageable);

        assertEquals(List.of(expectedItemRequestDto), actualResult);
    }

    @Test
    void getRequestById_whenIsOk_thenReturnItemRequest() {
        when(userRepository.existsById(2L)).thenReturn(true);
        when(itemRequestRepository.findById(id)).thenReturn(Optional.of(itemRequestWithId));
        when(mapper.convertToItemRequestDto(itemRequestWithId)).thenReturn(expectedItemRequestDto);

        ItemRequestDto actualResult = service.getRequestById(id, 2L);

        assertEquals(expectedItemRequestDto, actualResult);
    }

    @Test
    void getRequestById_whenUserNotFounded_thenReturnEntityNotExistsException() {
        when(userRepository.existsById(2L)).thenReturn(false);

        assertThrows(EntityNotExistsException.class, () -> service.getRequestById(id, 2L));
    }

    @Test
    void getRequestById_whenRequestNotFounded_thenReturnEntityNotExistsException() {
        when(userRepository.existsById(2L)).thenReturn(true);
        when(itemRequestRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotExistsException.class, () -> service.getRequestById(id, 2L));
    }
}