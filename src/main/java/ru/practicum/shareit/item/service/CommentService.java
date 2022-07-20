package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CreateCommentDto;
import ru.practicum.shareit.item.model.Comment;

public interface CommentService {

    Comment addComment(CreateCommentDto dto);
}
