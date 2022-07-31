package ru.practicum.shareit.item.service.impl;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.CreateCommentDto;
import ru.practicum.shareit.item.exception.NotABookerException;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.service.CommentService;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.security.AuthService;
import ru.practicum.shareit.user.model.User;

@SpringBootTest
class CommentServiceImplTest {

    @Autowired
    private CommentService service;

    @MockBean
    private ItemService itemService;

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private BookingService bookingService;

    @MockBean
    private AuthService authService;

    @Test
    void givenUserWasBooker_whenCreateComment_thenCorrectObjectShouldBeSaved() {
        // Given
        User user = Mockito.mock(User.class);
        when(user.getId()).thenReturn(1L);
        when(authService.getCurrentUser()).thenReturn(user);

        Item item = Mockito.mock(Item.class);
        when(item.getId()).thenReturn(1L);

        Booking booking = Mockito.mock(Booking.class);
        when(booking.getItem()).thenReturn(item);
        when(booking.getStatus()).thenReturn(BookingStatus.APPROVED);
        when(bookingService.getBookingsOfCurrentUser(BookingState.PAST)).thenReturn(List.of(booking));

        CreateCommentDto dto = Mockito.mock(CreateCommentDto.class);
        when(dto.getItemId()).thenReturn(1L);
        when(dto.getText()).thenReturn("Text");

        when(itemService.getItem(1L)).thenReturn(Optional.of(item));

        // When
        Comment comment = service.addComment(dto);

        // Then
        verify(commentRepository, times(1)).save(comment);
    }

    @Test
    void givenUserNotBooker_whenCreateComment_thenErrorShouldBeThrown() {
        // Given
        when(bookingService.getBookingsOfCurrentUser(BookingState.PAST)).thenReturn(Collections.emptyList());

        CreateCommentDto dto = Mockito.mock(CreateCommentDto.class);

        // When
        final Throwable throwable = catchThrowable(() -> service.addComment(dto));

        // Then
        then(throwable).isInstanceOf(NotABookerException.class);
    }
}