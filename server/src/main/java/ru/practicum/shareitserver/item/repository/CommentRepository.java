package ru.practicum.shareitserver.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareitserver.item.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
