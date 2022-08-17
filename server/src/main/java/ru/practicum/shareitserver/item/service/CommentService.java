package ru.practicum.shareitserver.item.service;

import ru.practicum.shareitserver.item.dto.CreateCommentDto;
import ru.practicum.shareitserver.item.model.Comment;

public interface CommentService {

    Comment addComment(CreateCommentDto dto);
}
