package ru.practicum.shareitserver.item.service.impl;

import java.time.Clock;
import java.time.LocalDateTime;
import javax.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareitserver.booking.model.BookingState;
import ru.practicum.shareitserver.booking.model.BookingStatus;
import ru.practicum.shareitserver.booking.service.BookingService;
import ru.practicum.shareitserver.item.dto.CreateCommentDto;
import ru.practicum.shareitserver.item.exception.ItemNotFoundException;
import ru.practicum.shareitserver.item.exception.NotBookerException;
import ru.practicum.shareitserver.item.model.Comment;
import ru.practicum.shareitserver.item.model.Item;
import ru.practicum.shareitserver.item.repository.CommentRepository;
import ru.practicum.shareitserver.item.service.CommentService;
import ru.practicum.shareitserver.item.service.ItemService;
import ru.practicum.shareitserver.security.AuthService;
import ru.practicum.shareitserver.user.model.User;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final ItemService itemService;
    private final BookingService bookingService;
    private final AuthService authService;
    private final Clock clock;

    @Override
    @Transactional
    public Comment addComment(CreateCommentDto dto) {
        boolean hasBooked = bookingService.getBookingsOfCurrentUser(BookingState.PAST, 0, 10)
            .stream()
            .anyMatch(e -> e.getItem().getId() == dto.getItemId()
                && e.getStatus() == BookingStatus.APPROVED);

        if (!hasBooked) {
            throw new NotBookerException();
        }

        Item item = itemService.getItem(dto.getItemId())
            .orElseThrow(ItemNotFoundException::new);

        User user = authService.getCurrentUser();

        Comment comment = new Comment(null, dto.getText(), item, user, LocalDateTime.now(clock));
        item.addComment(comment);
        commentRepository.save(comment);
        return comment;
    }
}
