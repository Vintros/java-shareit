package ru.practicum.shareit.item.storage.comments;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.comments.Comment;

@Repository
public interface CommentsRepository extends JpaRepository<Comment, Long> {

}
