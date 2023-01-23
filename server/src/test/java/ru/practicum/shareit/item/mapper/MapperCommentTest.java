package ru.practicum.shareit.item.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MapperCommentTest {

    private final MapperComment mapperComment = new MapperComment();

    private Comment comment;
    private CommentDto commentDto;
    private User booker;
    private Item item;

    @BeforeEach
    void setUp() {
        LocalDateTime time = LocalDateTime.now();
        User user = new User(1L, "mail@ya.ru", "user");
        item = new Item(1L, user, "item", "useful", true, List.of(), 1L);
        booker = new User(2L, "mail2@ya.ru", "user2");
        comment = new Comment();
        comment.setId(1L);
        comment.setText("comment");
        comment.setItem(item);
        comment.setAuthor(booker);
        comment.setCreated(time);

        Comment commentWithoutId = new Comment();
        commentWithoutId.setText("text");
        commentWithoutId.setItem(item);
        commentWithoutId.setAuthor(booker);
        commentWithoutId.setCreated(time);

        commentDto = new CommentDto();
        commentDto.setText("comment");
    }

    @Test
    void convertCommentDtoToComment() {
        Comment result = mapperComment.convertCommentDtoToComment(commentDto, item, booker);

        assertNull(result.getId());
        assertEquals("comment", result.getText());
        assertEquals(item, result.getItem());
        assertEquals(booker, result.getAuthor());
        assertNotNull(result.getCreated());
    }

    @Test
    void convertCommentToCommentDto() {
        CommentDto result = mapperComment.convertCommentToCommentDto(comment);

        assertEquals(1L, result.getId());
        assertEquals("comment", result.getText());
        assertEquals(booker.getName(), result.getAuthorName());
        assertNotNull(result.getCreated());
    }

}