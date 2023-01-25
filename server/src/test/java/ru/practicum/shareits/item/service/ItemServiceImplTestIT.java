package ru.practicum.shareits.item.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareits.common.model.FromSizeRequest;
import ru.practicum.shareits.item.dto.ItemDto;
import ru.practicum.shareits.item.model.Item;
import ru.practicum.shareits.user.dto.UserDto;
import ru.practicum.shareits.user.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemServiceImplTestIT {

    private final ItemService itemService;
    private final UserService userService;
    private final EntityManager em;

    @DirtiesContext
    @Test
    void createItem() {
        UserDto userDto = new UserDto(null, "mail@ya.ru", "user");
        userService.createUser(userDto);

        ItemDto itemDto = new ItemDto();
        itemDto.setName("item");
        itemDto.setDescription("useful");
        itemDto.setAvailable(true);
        itemService.createItem(itemDto, 1L);

        TypedQuery<Item> query = em.createQuery("Select i from Item i where i.name = :name", Item.class);
        Item item = query.setParameter("name", itemDto.getName()).getSingleResult();

        assertEquals(1L, item.getId());
        assertEquals("item", item.getName());
        assertTrue(item.getAvailable());
        assertEquals("user", item.getUser().getName());
    }

    @DirtiesContext
    @Test
    void getItemsByOwner() {
        UserDto userDto = new UserDto(null, "mail@ya.ru", "user");
        userService.createUser(userDto);

        ItemDto itemDto = new ItemDto();
        itemDto.setName("item");
        itemDto.setDescription("useful");
        itemDto.setAvailable(true);
        itemService.createItem(itemDto, 1L);

        ItemDto itemDto2 = new ItemDto();
        itemDto2.setName("item2");
        itemDto2.setDescription("useful2");
        itemDto2.setAvailable(true);
        itemService.createItem(itemDto2, 1L);

        Sort sort = Sort.by("id").ascending();
        Pageable pageable = FromSizeRequest.of(0, 10, sort);

        List<ItemDto> items = itemService.getItemsByOwner(1L, pageable);

        assertEquals(2, items.size());
        assertEquals("item", items.get(0).getName());
        assertEquals("item2", items.get(1).getName());
    }

    @DirtiesContext
    @Test
    void searchItemsByRequest() {
        UserDto userDto = new UserDto(null, "mail@ya.ru", "user");
        userService.createUser(userDto);

        ItemDto itemDto = new ItemDto();
        itemDto.setName("item");
        itemDto.setDescription("useful");
        itemDto.setAvailable(true);
        itemService.createItem(itemDto, 1L);

        ItemDto itemDto2 = new ItemDto();
        itemDto2.setName("item2");
        itemDto2.setDescription("useful2");
        itemDto2.setAvailable(true);
        itemService.createItem(itemDto2, 1L);

        Sort sort = Sort.by("id").ascending();
        Pageable pageable = FromSizeRequest.of(0, 10, sort);

        List<ItemDto> items = itemService.searchItemsByRequest("FuL2", pageable);

        assertEquals(1, items.size());
        assertEquals("item2", items.get(0).getName());
    }
}