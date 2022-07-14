package ru.practicum.shareit.item.service.impl;

import java.time.LocalDateTime;
import javax.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.CreateCommentDto;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.NotABookerException;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.service.CommentService;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final ItemService itemService;
    private final BookingService bookingService;
    private final UserService userService;

    @Override
    @Transactional
    public Comment addComment(CreateCommentDto dto) {
        boolean hasBooked = bookingService.getBookingsOfUser(dto.getUserId(), BookingState.PAST)
            .stream()
            .anyMatch(e -> e.getItem().getId() == dto.getItemId()
                && e.getStatus() == BookingStatus.APPROVED);

        if (!hasBooked) {
            throw new NotABookerException();
        }

        Item item = itemService.getItem(dto.getItemId())
            .orElseThrow(ItemNotFoundException::new);

        User user = userService.getUser(dto.getUserId())
            .orElseThrow(UserNotFoundException::new);

        Comment comment = new Comment(null, dto.getText(), item, user, LocalDateTime.now());
        item.addComment(comment);
        commentRepository.save(comment);
        return comment;
    }
}
