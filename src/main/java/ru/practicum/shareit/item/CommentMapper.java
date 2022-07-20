package ru.practicum.shareit.item;

import java.util.Collection;
import java.util.stream.Collectors;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;

public class CommentMapper {

    public static CommentDto toDto(Comment comment) {
        return new CommentDto(
            comment.getId(),
            comment.getAuthor().getName(),
            comment.getText(),
            comment.getCreatedAt()
        );
    }

    public static Collection<CommentDto> toDto(Collection<Comment> comments) {
        return comments.stream()
            .map(CommentMapper::toDto)
            .collect(Collectors.toList());
    }
}
