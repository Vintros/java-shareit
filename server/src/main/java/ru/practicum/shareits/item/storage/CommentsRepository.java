package ru.practicum.shareits.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareits.item.model.Comment;

@Repository
public interface CommentsRepository extends JpaRepository<Comment, Long> {

}
