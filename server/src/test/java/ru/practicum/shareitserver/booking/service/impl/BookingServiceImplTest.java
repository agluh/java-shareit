package ru.practicum.shareitserver.booking.service.impl;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareitserver.booking.dto.CreateBookingDto;
import ru.practicum.shareitserver.booking.exception.ItemIsNotAvailableForBookingException;
import ru.practicum.shareitserver.booking.exception.NotAnOwnerOrBookerException;
import ru.practicum.shareitserver.booking.exception.SelfBookingNotAllowedException;
import ru.practicum.shareitserver.booking.model.Booking;
import ru.practicum.shareitserver.booking.model.BookingState;
import ru.practicum.shareitserver.booking.model.BookingStatus;
import ru.practicum.shareitserver.booking.repository.BookingRepository;
import ru.practicum.shareitserver.booking.service.BookingService;
import ru.practicum.shareitserver.item.exception.ItemNotFoundException;
import ru.practicum.shareitserver.item.exception.NotAnOwnerException;
import ru.practicum.shareitserver.item.model.Item;
import ru.practicum.shareitserver.item.service.ItemService;
import ru.practicum.shareitserver.security.AuthService;
import ru.practicum.shareitserver.user.model.User;

@SpringBootTest
class BookingServiceImplTest {

    @Autowired
    private BookingService service;

    @MockBean
    private BookingRepository bookingRepository;

    @MockBean
    private ItemService itemService;

    @MockBean
    private AuthService authService;

    @MockBean
    private Clock clock;

    @BeforeEach
    void setupClock() {
        when(clock.instant()).thenReturn(Instant.parse("2022-08-01T10:00:00.000Z"));
        when(clock.getZone()).thenReturn(ZoneId.of("UTC"));
    }

    @Test
    void givenStartAfterEnd_whenCreateBooking_thenExceptionWillBeThrown() {
        // Given
        final LocalDateTime start = LocalDateTime.now(clock);
        final LocalDateTime end = start.minusSeconds(1);

        CreateBookingDto dto = Mockito.mock(CreateBookingDto.class);
        when(dto.getStart()).thenReturn(start);
        when(dto.getEnd()).thenReturn(end);

        // When
        final Throwable throwable = catchThrowable(() -> service.createBooking(dto));

        // Then
        then(throwable).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void givenStartIsInPast_whenCreateBooking_thenExceptionWillBeThrown() {
        // Given
        final LocalDateTime start = LocalDateTime.now(clock).minusSeconds(1);
        final LocalDateTime end = start.plusDays(1);

        CreateBookingDto dto = Mockito.mock(CreateBookingDto.class);
        when(dto.getStart()).thenReturn(start);
        when(dto.getEnd()).thenReturn(end);

        // When
        final Throwable throwable = catchThrowable(() -> service.createBooking(dto));

        // Then
        then(throwable).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void givenItemNotExist_whenCreateBooking_thenExceptionWillBeThrown() {
        // Given
        final LocalDateTime start = LocalDateTime.now(clock).plusSeconds(1);
        final LocalDateTime end = start.plusDays(1);

        CreateBookingDto dto = Mockito.mock(CreateBookingDto.class);
        when(dto.getStart()).thenReturn(start);
        when(dto.getEnd()).thenReturn(end);
        when(dto.getItemId()).thenReturn(1L);

        when(itemService.getItem(1L)).thenReturn(Optional.empty());

        // When
        final Throwable throwable = catchThrowable(() -> service.createBooking(dto));

        // Then
        then(throwable).isInstanceOf(ItemNotFoundException.class);
    }

    @Test
    void givenItemNotAvailable_whenCreateBooking_thenExceptionWillBeThrown() {
        // Given
        final LocalDateTime start = LocalDateTime.now(clock).plusSeconds(1);
        final LocalDateTime end = start.plusDays(1);

        CreateBookingDto dto = Mockito.mock(CreateBookingDto.class);
        when(dto.getStart()).thenReturn(start);
        when(dto.getEnd()).thenReturn(end);
        when(dto.getItemId()).thenReturn(1L);

        Item item = Mockito.mock(Item.class);
        when(item.isAvailable()).thenReturn(false);

        when(itemService.getItem(1L)).thenReturn(Optional.of(item));

        // When
        final Throwable throwable = catchThrowable(() -> service.createBooking(dto));

        // Then
        then(throwable).isInstanceOf(ItemIsNotAvailableForBookingException.class);
    }

    @Test
    void givenBookerIsOwner_whenCreateBooking_thenExceptionWillBeThrown() {
        // Given
        final LocalDateTime start = LocalDateTime.now(clock).plusSeconds(1);
        final LocalDateTime end = start.plusDays(1);

        CreateBookingDto dto = Mockito.mock(CreateBookingDto.class);
        when(dto.getStart()).thenReturn(start);
        when(dto.getEnd()).thenReturn(end);
        when(dto.getItemId()).thenReturn(1L);

        User user = Mockito.mock(User.class);
        when(user.getId()).thenReturn(1L);
        when(authService.getCurrentUser()).thenReturn(user);

        Item item = Mockito.mock(Item.class);
        when(item.isAvailable()).thenReturn(true);
        when(item.getOwner()).thenReturn(user);

        when(itemService.getItem(1L)).thenReturn(Optional.of(item));

        // When
        final Throwable throwable = catchThrowable(() -> service.createBooking(dto));

        // Then
        then(throwable).isInstanceOf(SelfBookingNotAllowedException.class);
    }

    @Test
    void givenCorrectData_whenCreateBooking_thenCorrectObjectShouldBeSaved() {
        // Given
        final LocalDateTime start = LocalDateTime.now(clock).plusSeconds(1);
        final LocalDateTime end = start.plusDays(1);

        CreateBookingDto dto = Mockito.mock(CreateBookingDto.class);
        when(dto.getStart()).thenReturn(start);
        when(dto.getEnd()).thenReturn(end);
        when(dto.getItemId()).thenReturn(1L);

        User user = Mockito.mock(User.class);
        when(user.getId()).thenReturn(1L);
        when(authService.getCurrentUser()).thenReturn(user);

        User owner = Mockito.mock(User.class);
        when(owner.getId()).thenReturn(2L);

        Item item = Mockito.mock(Item.class);
        when(item.isAvailable()).thenReturn(true);
        when(item.getOwner()).thenReturn(owner);

        when(itemService.getItem(1L)).thenReturn(Optional.of(item));

        // When
        Booking booking = service.createBooking(dto);

        // Then
        verify(bookingRepository, times(1)).save(booking);
    }

    @Test
    void givenNotOwner_whenReviewBooking_thenExceptionWillBeThrown() {
        // Given
        User user = Mockito.mock(User.class);
        when(user.getId()).thenReturn(1L);
        when(authService.getCurrentUser()).thenReturn(user);

        User owner = Mockito.mock(User.class);
        when(owner.getId()).thenReturn(2L);

        Item item = Mockito.mock(Item.class);
        when(item.isAvailable()).thenReturn(true);
        when(item.getOwner()).thenReturn(owner);

        Booking booking = Mockito.mock(Booking.class);
        when(booking.getItem()).thenReturn(item);
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        // When
        final Throwable throwable = catchThrowable(() -> service.reviewBooking(1, true));

        // Then
        then(throwable).isInstanceOf(NotAnOwnerException.class);
    }

    @Test
    void givenBookingAlreadyReviewed_whenReviewBooking_thenExceptionWillBeThrown() {
        // Given
        User user = Mockito.mock(User.class);
        when(user.getId()).thenReturn(1L);
        when(authService.getCurrentUser()).thenReturn(user);

        Item item = Mockito.mock(Item.class);
        when(item.isAvailable()).thenReturn(true);
        when(item.getOwner()).thenReturn(user);

        Booking booking = Mockito.mock(Booking.class);
        when(booking.getItem()).thenReturn(item);
        when(booking.getStatus()).thenReturn(BookingStatus.REJECTED);
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        // When
        final Throwable throwable = catchThrowable(() -> service.reviewBooking(1, true));

        // Then
        then(throwable).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void givenWaitingBooking_whenApproveBooking_thenCorrectObjectShouldBeSaved() {
        // Given
        User user = Mockito.mock(User.class);
        when(user.getId()).thenReturn(1L);
        when(authService.getCurrentUser()).thenReturn(user);

        Item item = Mockito.mock(Item.class);
        when(item.isAvailable()).thenReturn(true);
        when(item.getOwner()).thenReturn(user);

        Booking booking = Mockito.mock(Booking.class);
        when(booking.getItem()).thenReturn(item);
        when(booking.getStatus()).thenReturn(BookingStatus.WAITING);
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        // When
        service.reviewBooking(1, true);

        // Then
        verify(bookingRepository, times(1)).save(booking);
        verify(booking, times(1)).setStatus(BookingStatus.APPROVED);
    }

    @Test
    void givenWaitingBooking_whenRejectBooking_thenCorrectObjectShouldBeSaved() {
        // Given
        User user = Mockito.mock(User.class);
        when(user.getId()).thenReturn(1L);
        when(authService.getCurrentUser()).thenReturn(user);

        Item item = Mockito.mock(Item.class);
        when(item.isAvailable()).thenReturn(true);
        when(item.getOwner()).thenReturn(user);

        Booking booking = Mockito.mock(Booking.class);
        when(booking.getItem()).thenReturn(item);
        when(booking.getStatus()).thenReturn(BookingStatus.WAITING);
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        // When
        service.reviewBooking(1, false);

        // Then
        verify(bookingRepository, times(1)).save(booking);
        verify(booking, times(1)).setStatus(BookingStatus.REJECTED);
    }

    @Test
    void givenUserNotOwnerNotBooker_whenGetBooking_thenExceptionWillBeThrown() {
        User user = Mockito.mock(User.class);
        when(user.getId()).thenReturn(1L);
        when(authService.getCurrentUser()).thenReturn(user);

        User other = Mockito.mock(User.class);
        when(other.getId()).thenReturn(2L);

        Item item = Mockito.mock(Item.class);
        when(item.isAvailable()).thenReturn(true);
        when(item.getOwner()).thenReturn(other);

        Booking booking = Mockito.mock(Booking.class);
        when(booking.getItem()).thenReturn(item);
        when(booking.getStatus()).thenReturn(BookingStatus.WAITING);
        when(booking.getBooker()).thenReturn(other);
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        final Throwable throwable = catchThrowable(() -> service.getBooking(1));

        // Then
        then(throwable).isInstanceOf(NotAnOwnerOrBookerException.class);
    }

    @Test
    void givenUserIsBooker_whenGetBooking_thenCorrectDataShouldBeFetched() {
        // Given
        User booker = Mockito.mock(User.class);
        when(booker.getId()).thenReturn(1L);
        when(authService.getCurrentUser()).thenReturn(booker);

        User owner = Mockito.mock(User.class);
        when(owner.getId()).thenReturn(2L);

        Item item = Mockito.mock(Item.class);
        when(item.isAvailable()).thenReturn(true);
        when(item.getOwner()).thenReturn(owner);

        Booking booking = Mockito.mock(Booking.class);
        when(booking.getItem()).thenReturn(item);
        when(booking.getStatus()).thenReturn(BookingStatus.WAITING);
        when(booking.getBooker()).thenReturn(booker);
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        // When
        service.getBooking(1);

        // Then
        verify(bookingRepository, times(1)).findById(1L);
    }

    @Test
    void givenUserIsOwner_whenGetBooking_thenCorrectDataShouldBeFetched() {
        // Given
        User owner = Mockito.mock(User.class);
        when(owner.getId()).thenReturn(1L);
        when(authService.getCurrentUser()).thenReturn(owner);

        User booker = Mockito.mock(User.class);
        when(booker.getId()).thenReturn(2L);

        Item item = Mockito.mock(Item.class);
        when(item.isAvailable()).thenReturn(true);
        when(item.getOwner()).thenReturn(owner);

        Booking booking = Mockito.mock(Booking.class);
        when(booking.getItem()).thenReturn(item);
        when(booking.getStatus()).thenReturn(BookingStatus.WAITING);
        when(booking.getBooker()).thenReturn(booker);
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        // When
        service.getBooking(1);

        // Then
        verify(bookingRepository, times(1)).findById(1L);
    }

    @Test
    void givenBookingWaitingState_whenGetBookingsOfCurrentUser_thenCorrectBookingsShouldBeFetched() {
        // Given
        User owner = Mockito.mock(User.class);
        when(owner.getId()).thenReturn(1L);
        when(authService.getCurrentUser()).thenReturn(owner);

        // When
        service.getBookingsOfCurrentUser(BookingState.WAITING, 0, 10);

        // Then
        verify(bookingRepository, times(1))
            .findBookingsOfUserWithStatus(1L, BookingStatus.WAITING,
                Pageable.ofSize(10));
    }

    @Test
    void givenBookingRejectedState_whenGetBookingsOfCurrentUser_thenCorrectBookingsShouldBeFetched() {
        // Given
        User owner = Mockito.mock(User.class);
        when(owner.getId()).thenReturn(1L);
        when(authService.getCurrentUser()).thenReturn(owner);

        // When
        service.getBookingsOfCurrentUser(BookingState.REJECTED, 0, 10);

        // Then
        verify(bookingRepository, times(1))
            .findBookingsOfUserWithStatus(1L, BookingStatus.REJECTED,
                Pageable.ofSize(10));
    }

    @Test
    void givenBookingPastState_whenGetBookingsOfCurrentUser_thenCorrectBookingsShouldBeFetched() {
        // Given
        User owner = Mockito.mock(User.class);
        when(owner.getId()).thenReturn(1L);
        when(authService.getCurrentUser()).thenReturn(owner);
        LocalDateTime now = LocalDateTime.now(clock);

        // When
        service.getBookingsOfCurrentUser(BookingState.PAST, 0, 10);

        // Then
        verify(bookingRepository, times(1))
            .findPastBookingsOfUser(1L, now, Pageable.ofSize(10));
    }

    @Test
    void givenBookingFutureState_whenGetBookingsOfCurrentUser_thenCorrectBookingsShouldBeFetched() {
        // Given
        User owner = Mockito.mock(User.class);
        when(owner.getId()).thenReturn(1L);
        when(authService.getCurrentUser()).thenReturn(owner);
        LocalDateTime now = LocalDateTime.now(clock);

        // When
        service.getBookingsOfCurrentUser(BookingState.FUTURE, 0, 10);

        // Then
        verify(bookingRepository, times(1))
            .findFutureBookingsOfUser(1L, now, Pageable.ofSize(10));
    }

    @Test
    void givenBookingCurrentState_whenGetBookingsOfCurrentUser_thenCorrectBookingsShouldBeFetched() {
        // Given
        User owner = Mockito.mock(User.class);
        when(owner.getId()).thenReturn(1L);
        when(authService.getCurrentUser()).thenReturn(owner);
        LocalDateTime now = LocalDateTime.now(clock);

        // When
        service.getBookingsOfCurrentUser(BookingState.CURRENT, 0, 10);

        // Then
        verify(bookingRepository, times(1))
            .findBookingsOfUserBetween(1L, now, now,
                Pageable.ofSize(10));
    }

    @Test
    void givenBookingAllState_whenGetBookingsOfCurrentUser_thenCorrectBookingsShouldBeFetched() {
        // Given
        User owner = Mockito.mock(User.class);
        when(owner.getId()).thenReturn(1L);
        when(authService.getCurrentUser()).thenReturn(owner);

        // When
        service.getBookingsOfCurrentUser(BookingState.ALL, 0, 10);

        // Then
        verify(bookingRepository, times(1))
            .findBookingsOfUser(1L, Pageable.ofSize(10));
    }

    @Test
    void givenNoItems_whenGetItemBookingsOfCurrentUser_thenEmptyCollectionShouldBeFetched() {
        // Given
        User owner = Mockito.mock(User.class);
        when(owner.getId()).thenReturn(1L);
        when(authService.getCurrentUser()).thenReturn(owner);

        when(itemService.getItemsOfCurrentUser(0, 10)).thenReturn(Collections.emptyList());

        // When
        Collection<Booking> result = service.getItemBookingsOfCurrentUser(BookingState.ALL, 0, 10);

        // Then
        then(result).isEmpty();
        verifyNoInteractions(bookingRepository);
    }

    @Test
    void givenBookingWaitingState_whenGetItemBookingsOfCurrentUser_thenCorrectBookingsShouldBeFetched() {
        // Given
        User owner = Mockito.mock(User.class);
        when(owner.getId()).thenReturn(1L);
        when(authService.getCurrentUser()).thenReturn(owner);

        Item item = Mockito.mock(Item.class);
        when(item.getId()).thenReturn(1L);
        when(item.getOwner()).thenReturn(owner);

        when(itemService.getItemsOfCurrentUser()).thenReturn(List.of(item));

        // When
        service.getItemBookingsOfCurrentUser(BookingState.WAITING, 0, 10);

        // Then
        verify(bookingRepository, times(1))
            .findBookingsOfItemsWithStatus(List.of(1L), BookingStatus.WAITING,
                Pageable.ofSize(10));
    }

    @Test
    void givenBookingRejectedState_whenGetItemBookingsOfCurrentUser_thenCorrectBookingsShouldBeFetched() {
        // Given
        User owner = Mockito.mock(User.class);
        when(owner.getId()).thenReturn(1L);
        when(authService.getCurrentUser()).thenReturn(owner);

        Item item = Mockito.mock(Item.class);
        when(item.getId()).thenReturn(1L);
        when(item.getOwner()).thenReturn(owner);

        when(itemService.getItemsOfCurrentUser()).thenReturn(List.of(item));

        // When
        service.getItemBookingsOfCurrentUser(BookingState.REJECTED, 0, 10);

        // Then
        verify(bookingRepository, times(1))
            .findBookingsOfItemsWithStatus(List.of(1L), BookingStatus.REJECTED,
                Pageable.ofSize(10));
    }

    @Test
    void givenBookingPastState_whenGetItemBookingsOfCurrentUser_thenCorrectBookingsShouldBeFetched() {
        // Given
        final LocalDateTime now = LocalDateTime.now(clock);

        User owner = Mockito.mock(User.class);
        when(owner.getId()).thenReturn(1L);
        when(authService.getCurrentUser()).thenReturn(owner);

        Item item = Mockito.mock(Item.class);
        when(item.getId()).thenReturn(1L);
        when(item.getOwner()).thenReturn(owner);

        when(itemService.getItemsOfCurrentUser()).thenReturn(List.of(item));

        // When
        service.getItemBookingsOfCurrentUser(BookingState.PAST, 0, 10);

        // Then
        verify(bookingRepository, times(1))
            .findPastBookingsOfItems(List.of(1L), now,
                Pageable.ofSize(10));
    }

    @Test
    void givenBookingFutureState_whenGetItemBookingsOfCurrentUser_thenCorrectBookingsShouldBeFetched() {
        // Given
        final LocalDateTime now = LocalDateTime.now(clock);

        User owner = Mockito.mock(User.class);
        when(owner.getId()).thenReturn(1L);
        when(authService.getCurrentUser()).thenReturn(owner);

        Item item = Mockito.mock(Item.class);
        when(item.getId()).thenReturn(1L);
        when(item.getOwner()).thenReturn(owner);

        when(itemService.getItemsOfCurrentUser()).thenReturn(List.of(item));

        // When
        service.getItemBookingsOfCurrentUser(BookingState.FUTURE, 0, 10);

        // Then
        verify(bookingRepository, times(1))
            .findFutureBookingsOfItems(List.of(1L), now,
                Pageable.ofSize(10));
    }

    @Test
    void givenBookingCurrentState_whenGetItemBookingsOfCurrentUser_thenCorrectBookingsShouldBeFetched() {
        // Given
        final LocalDateTime now = LocalDateTime.now(clock);

        User owner = Mockito.mock(User.class);
        when(owner.getId()).thenReturn(1L);
        when(authService.getCurrentUser()).thenReturn(owner);

        Item item = Mockito.mock(Item.class);
        when(item.getId()).thenReturn(1L);
        when(item.getOwner()).thenReturn(owner);

        when(itemService.getItemsOfCurrentUser()).thenReturn(List.of(item));

        // When
        service.getItemBookingsOfCurrentUser(BookingState.CURRENT, 0, 10);

        // Then
        verify(bookingRepository, times(1))
            .findBookingsOfItemsBetween(List.of(1L), now, now,
                Pageable.ofSize(10));
    }

    @Test
    void givenBookingAllState_whenGetItemBookingsOfCurrentUser_thenCorrectBookingsShouldBeFetched() {
        // Given
        User owner = Mockito.mock(User.class);
        when(owner.getId()).thenReturn(1L);
        when(authService.getCurrentUser()).thenReturn(owner);

        Item item = Mockito.mock(Item.class);
        when(item.getId()).thenReturn(1L);
        when(item.getOwner()).thenReturn(owner);

        when(itemService.getItemsOfCurrentUser()).thenReturn(List.of(item));

        // When
        service.getItemBookingsOfCurrentUser(BookingState.ALL, 0, 10);

        // Then
        verify(bookingRepository, times(1))
            .findBookingsItems(List.of(1L), Pageable.ofSize(10));
    }

    @Test
    void givenCorrectState_whenGetPreviousBookingOfItem_thenCorrectBookingShouldBeFetched() {
        // Given
        LocalDateTime now = LocalDateTime.now(clock);

        // When
        service.getPreviousBookingOfItem(1L);

        // Then
        verify(bookingRepository, times(1))
            .findPastBookingsOfItem(1, now);
    }

    @Test
    void givenCorrectState_whenGetNextBookingOfItem_thenCorrectBookingShouldBeFetched() {
        // Given
        LocalDateTime now = LocalDateTime.now(clock);

        // When
        service.getNextBookingOfItem(1L);

        // Then
        verify(bookingRepository, times(1))
            .findFutureBookingsOfItem(1, now);
    }
}