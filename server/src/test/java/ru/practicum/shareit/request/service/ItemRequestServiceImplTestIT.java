package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.common.model.FromSizeRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemRequestServiceImplTestIT {

    private final ItemRequestService itemRequestService;
    private final UserService userService;
    private final EntityManager em;

    @DirtiesContext
    @Test
    void addRequest() {
        UserDto userDto = new UserDto(null, "mail@ya.ru", "user");
        userService.createUser(userDto);

        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("something");
        itemRequestService.addRequest(itemRequestDto, 1L);

        TypedQuery<ItemRequest> query = em.createQuery("Select i from ItemRequest i where i.description = :des",
                ItemRequest.class);
        ItemRequest itemRequest = query.setParameter("des", itemRequestDto.getDescription()).getSingleResult();

        assertEquals(1L, itemRequest.getId());
        assertEquals(1L, itemRequest.getUserId());
        assertNotNull(itemRequest.getCreated());
    }

    @DirtiesContext
    @Test
    void getOwnRequests() {
        UserDto userDto = new UserDto(null, "mail@ya.ru", "user");
        userService.createUser(userDto);
        UserDto userDto2 = new UserDto(null, "mail2@ya.ru", "user2");
        userService.createUser(userDto2);

        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("something");
        itemRequestService.addRequest(itemRequestDto, 1L);

        ItemRequestDto itemRequestDto2 = new ItemRequestDto();
        itemRequestDto2.setDescription("something2");
        itemRequestService.addRequest(itemRequestDto2, 1L);

        ItemRequestDto itemRequestDto3 = new ItemRequestDto();
        itemRequestDto3.setDescription("something3");
        itemRequestService.addRequest(itemRequestDto3, 2L);

        List<ItemRequestDto> requestsDto = itemRequestService.getOwnRequests(1L);

        assertEquals(2, requestsDto.size());
    }

    @DirtiesContext
    @Test
    void getAllRequests() {
        Sort sort = Sort.by("created").descending();
        Pageable pageable = FromSizeRequest.of(0, 10, sort);

        UserDto userDto = new UserDto(null, "mail@ya.ru", "user");
        userService.createUser(userDto);
        UserDto userDto2 = new UserDto(null, "mail2@ya.ru", "user2");
        userService.createUser(userDto2);

        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("something");
        itemRequestService.addRequest(itemRequestDto, 1L);

        ItemRequestDto itemRequestDto2 = new ItemRequestDto();
        itemRequestDto2.setDescription("something2");
        itemRequestService.addRequest(itemRequestDto2, 1L);

        ItemRequestDto itemRequestDto3 = new ItemRequestDto();
        itemRequestDto3.setDescription("something3");
        itemRequestService.addRequest(itemRequestDto3, 2L);

        List<ItemRequestDto> requestsDto = itemRequestService.getAllRequests(1L, pageable);

        assertEquals(1, requestsDto.size());
    }
}