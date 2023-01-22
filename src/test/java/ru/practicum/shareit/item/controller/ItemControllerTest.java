package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.common.exceptions.EntityNotExistsException;
import ru.practicum.shareit.common.model.FromSizeRequest;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
class ItemControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    MockMvc mvc;

    @MockBean
    ItemService itemService;

    Long id;
    Long wrongId;
    ItemDto sentItemDto;
    ItemDto expectedItemDto;
    CommentDto sentCommentDto;
    CommentDto expectedCommentDto;

    @BeforeEach
    void setUp() {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        id = 1L;
        wrongId = 99L;

        sentItemDto = new ItemDto();
        sentItemDto.setName("item");
        sentItemDto.setDescription("useful thing");
        sentItemDto.setAvailable(true);

        expectedItemDto = new ItemDto();
        expectedItemDto.setId(id);
        expectedItemDto.setName("item");
        expectedItemDto.setDescription("useful thing");
        expectedItemDto.setAvailable(true);

        sentCommentDto = new CommentDto();
        sentCommentDto.setText("text");

        expectedCommentDto = new CommentDto();
        expectedCommentDto.setId(id);
        expectedCommentDto.setText("text");
        expectedCommentDto.setAuthorName("author");
        expectedCommentDto.setCreated(LocalDateTime.now());
    }

    @SneakyThrows
    @Test
    void createItem_whenIsOk_thenReturnItem() {
        when(itemService.createItem(sentItemDto, id)).thenReturn(expectedItemDto);

        String result = mvc.perform(post("/items")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(sentItemDto))
                        .header("X-Sharer-User-Id", id))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(expectedItemDto), result);
        verify(itemService, times(1)).createItem(sentItemDto, id);
    }

    @SneakyThrows
    @Test
    void createItem_whenNoId_thenReturnError() {
        mvc.perform(post("/items")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(sentItemDto)))
                .andExpect(status().isInternalServerError());

        verify(itemService, never()).createItem(sentItemDto, id);
    }

    @SneakyThrows
    @Test
    void createItem_whenNoBody_thenReturnError() {
        mvc.perform(post("/items")
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", id))
                .andExpect(status().isInternalServerError());

        verify(itemService, never()).createItem(sentItemDto, id);
    }

    @SneakyThrows
    @Test
    void createItem_whenItemNameNotValid_thenReturnBadRequest() {
        sentItemDto.setName(null);

        mvc.perform(post("/items")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(sentItemDto))
                        .header("X-Sharer-User-Id", id))
                .andExpect(status().isBadRequest());

        verify(itemService, never()).createItem(sentItemDto, id);
    }

    @SneakyThrows
    @Test
    void createItem_whenItemDescriptionNotValid_thenReturnBadRequest() {
        sentItemDto.setDescription("   ");

        mvc.perform(post("/items")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(sentItemDto))
                        .header("X-Sharer-User-Id", id))
                .andExpect(status().isBadRequest());

        verify(itemService, never()).createItem(sentItemDto, id);
    }

    @SneakyThrows
    @Test
    void createItem_whenItemAvailableNotValid_thenReturnBadRequest() {
        sentItemDto.setAvailable(null);

        mvc.perform(post("/items")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(sentItemDto))
                        .header("X-Sharer-User-Id", id))
                .andExpect(status().isBadRequest());

        verify(itemService, never()).createItem(sentItemDto, id);
    }

    @SneakyThrows
    @Test
    void updateItemById_whenIsOk_thenReturnUpdatedItem() {
        when(itemService.updateItemById(sentItemDto, id, id)).thenReturn(expectedItemDto);

        String result = mvc.perform(patch("/items/{itemId}", id)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(sentItemDto))
                        .header("X-Sharer-User-Id", id))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(expectedItemDto), result);
        verify(itemService, times(1)).updateItemById(sentItemDto, id, id);
    }

    @SneakyThrows
    @Test
    void updateItemById_whenHeaderNoId_thenError() {
        mvc.perform(patch("/items/{itemId}", id)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(sentItemDto)))
                .andExpect(status().isInternalServerError());

        verify(itemService, never()).updateItemById(any(), any(), any());
    }

    @SneakyThrows
    @Test
    void updateItemById_whenIdNotValid_thenBadRequest() {
        mvc.perform(patch("/items/{itemId}", "notNumber")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(sentItemDto))
                        .header("X-Sharer-User-Id", id))
                .andExpect(status().isBadRequest());

        verify(itemService, never()).updateItemById(any(), any(), any());
    }

    @SneakyThrows
    @Test
    void getItemById_whenItemFounded_thenReturnItem() {
        when(itemService.getItemById(id, id)).thenReturn(expectedItemDto);

        String result = mvc.perform(get("/items/{itemId}", id)
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", id))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(expectedItemDto), result);
    }

    @SneakyThrows
    @Test
    void getItemById_whenItemNotFounded_thenReturnEntityNotExistsException() {
        when(itemService.getItemById(wrongId, id)).thenThrow(EntityNotExistsException.class);

        mvc.perform(get("/items/{itemId}", wrongId)
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", id))
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    void getItemById_whenHeaderNoId_thenError() {
        mvc.perform(get("/items/{itemId}", id)
                        .contentType("application/json"))
                .andExpect(status().isInternalServerError());
    }

    @SneakyThrows
    @Test
    void getItemById_whenIdNotValid_thenBadRequest() {
        mvc.perform(get("/items/{itemId}", "notNumber")
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", id))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void getItemsByOwner_whenIsOk_thenReturnItemsList() {
        Sort sort = Sort.by("id").ascending();
        Pageable pageable = FromSizeRequest.of(0, 10, sort);
        when(itemService.getItemsByOwner(id, pageable)).thenReturn(List.of(expectedItemDto));

        String result = mvc.perform(get("/items")
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", id)
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(List.of(expectedItemDto)), result);
    }

    @SneakyThrows
    @Test
    void getItemsByOwner_whenNoHeaderId_thenReturnError() {
        mvc.perform(get("/items")
                        .contentType("application/json")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isInternalServerError());
    }

    @SneakyThrows
    @Test
    void getItemsByOwner_whenFromNotValid_thenReturnError() {
        mvc.perform(get("/items")
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", id)
                        .param("from", "-1")
                        .param("size", "10"))
                .andExpect(status().isInternalServerError());
    }

    @SneakyThrows
    @Test
    void getItemsByOwner_whenSizeNotValid_thenReturnError() {
        mvc.perform(get("/items")
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", id)
                        .param("from", "0")
                        .param("size", "0"))
                .andExpect(status().isInternalServerError());
    }

    @SneakyThrows
    @Test
    void getItemsByOwner_whenIdNotValid_thenReturnError() {
        mvc.perform(get("/items")
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", "notNumber")
                        .param("from", "0")
                        .param("size", "0"))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void searchItemsByRequest_whenIsOk_thenReturnItemsList() {
        Sort sort = Sort.by("id").ascending();
        Pageable pageable = FromSizeRequest.of(0, 10, sort);
        when(itemService.searchItemsByRequest("item", pageable)).thenReturn(List.of(expectedItemDto));

        String result = mvc.perform(get("/items/search")
                        .contentType("application/json")
                        .param("from", "0")
                        .param("size", "10")
                        .param("text", "item"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(List.of(expectedItemDto)), result);
    }

    @SneakyThrows
    @Test
    void searchItemsByRequest_whenNotFounded_thenReturnEmptyList() {
        Sort sort = Sort.by("id").ascending();
        Pageable pageable = FromSizeRequest.of(0, 10, sort);
        when(itemService.searchItemsByRequest("item", pageable)).thenReturn(List.of());

        String result = mvc.perform(get("/items/search")
                        .contentType("application/json")
                        .param("from", "0")
                        .param("size", "10")
                        .param("text", "item"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(List.of()), result);
    }

    @SneakyThrows
    @Test
    void searchItemsByRequest_whenNoFromNotValid_thenReturnError() {
        mvc.perform(get("/items/search")
                        .contentType("application/json")
                        .param("from", "-1")
                        .param("size", "10")
                        .param("text", "item"))
                .andExpect(status().isInternalServerError());
    }

    @SneakyThrows
    @Test
    void searchItemsByRequest_whenNoSizeNotValid_thenReturnError() {
        mvc.perform(get("/items/search")
                        .contentType("application/json")
                        .param("from", "0")
                        .param("size", "0")
                        .param("text", "item"))
                .andExpect(status().isInternalServerError());
    }

    @SneakyThrows
    @Test
    void addComment_whenIsOk_thenReturnComment() {
        when(itemService.createComment(id, id, sentCommentDto)).thenReturn(expectedCommentDto);

        mvc.perform(post("/items/{itemId}/comment", id)
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", id)
                        .content(mapper.writeValueAsString(sentCommentDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expectedCommentDto)));

        verify(itemService, times(1)).createComment(any(), any(), any());
    }


    @SneakyThrows
    @Test
    void addComment_whenNoHeaderId_thenReturnError() {
        mvc.perform(post("/items/{itemId}/comment", id)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(sentCommentDto)))
                .andExpect(status().isInternalServerError());
    }

    @SneakyThrows
    @Test
    void addComment_whenIdNotValid_thenReturnError() {
        mvc.perform(post("/items/{itemId}/comment", id)
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", "notNumber")
                        .content(mapper.writeValueAsString(sentCommentDto)))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void addComment_whenCommentNotValid_thenReturnError() {
        sentCommentDto.setText("  ");

        mvc.perform(post("/items/{itemId}/comment", id)
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", id)
                        .content(mapper.writeValueAsString(sentCommentDto)))
                .andExpect(status().isBadRequest());
    }



}