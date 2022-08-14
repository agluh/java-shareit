package ru.practicum.shareitserver.item;

import java.util.Collection;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import ru.practicum.shareitserver.item.dto.CommentDto;
import ru.practicum.shareitserver.item.model.Comment;

@Service
public class CommentMapper {

    public CommentDto toDto(Comment comment) {
        return new CommentDto(
            comment.getId(),
            comment.getAuthor().getName(),
            comment.getText(),
            comment.getCreatedAt()
        );
    }

    public Collection<CommentDto> toDto(Collection<Comment> comments) {
        return comments.stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }
}
