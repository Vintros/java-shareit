package ru.practicum.shareit.item.mapper;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Service
public class MapperComment {

    public Comment convertCommentDtoToComment(CommentDto commentDto, Item item, User user) {
        Comment comment = new Comment();
        comment.setText(commentDto.getText());
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());
        return comment;
    }

    public CommentDto convertCommentToCommentDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setText(comment.getText());
        commentDto.setAuthorName(comment.getAuthor().getName());
        commentDto.setCreated(comment.getCreated());
        return commentDto;
    }
}
