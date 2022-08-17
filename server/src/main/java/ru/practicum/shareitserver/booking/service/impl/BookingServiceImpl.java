package ru.practicum.shareitserver.booking.service.impl;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareitserver.booking.dto.CreateBookingDto;
import ru.practicum.shareitserver.booking.exception.BookingNotFoundException;
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

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ItemService itemService;
    private final AuthService authService;
    private final Clock clock;

    @Override
    public Booking createBooking(CreateBookingDto dto) {
        if (dto.getStart().isAfter(dto.getEnd())) {
            throw new IllegalArgumentException("Bad date range");
        }

        if (dto.getStart().isBefore(LocalDateTime.now(clock))) {
            throw new IllegalArgumentException("Start date is in the past");
        }

        Item item = itemService.getItem(dto.getItemId())
            .orElseThrow(ItemNotFoundException::new);

        if (!item.isAvailable()) {
            throw new ItemIsNotAvailableForBookingException();
        }

        User user = authService.getCurrentUser();

        if (item.getOwner().getId().equals(user.getId())) {
            throw new SelfBookingNotAllowedException();
        }

        Booking booking = new Booking(null, dto.getStart(), dto.getEnd(), item,
            user, BookingStatus.WAITING);
        bookingRepository.save(booking);
        return booking;
    }

    @Override
    public Booking reviewBooking(long bookingId, boolean isApproved) {
        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(BookingNotFoundException::new);

        User user = authService.getCurrentUser();

        if (!booking.getItem().getOwner().getId().equals(user.getId())) {
            throw new NotAnOwnerException();
        }

        if (booking.getStatus() != BookingStatus.WAITING) {
            throw new IllegalArgumentException("Booking already reviewed");
        }

        booking.setStatus(isApproved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        bookingRepository.save(booking);
        return booking;
    }

    @Override
    public Optional<Booking> getBooking(long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(BookingNotFoundException::new);

        User user = authService.getCurrentUser();

        if (!booking.getItem().getOwner().getId().equals(user.getId())
            && !booking.getBooker().getId().equals(user.getId())) {
            throw new NotAnOwnerOrBookerException();
        }

        return Optional.of(booking);
    }

    @Override
    public Collection<Booking> getBookingsOfCurrentUser(BookingState state, int from, int size) {
        User user = authService.getCurrentUser();
        int page = from / size;

        switch (state) {
            case WAITING:
                return bookingRepository.findBookingsOfUserWithStatus(
                    user.getId(), BookingStatus.WAITING, PageRequest.of(page, size));
            case REJECTED:
                return bookingRepository.findBookingsOfUserWithStatus(
                    user.getId(), BookingStatus.REJECTED, PageRequest.of(page, size));
            case PAST:
                return bookingRepository.findPastBookingsOfUser(
                    user.getId(), LocalDateTime.now(clock), PageRequest.of(page, size));
            case FUTURE:
                return bookingRepository.findFutureBookingsOfUser(
                    user.getId(), LocalDateTime.now(clock), PageRequest.of(page, size));
            case CURRENT:
                LocalDateTime now = LocalDateTime.now(clock);
                return bookingRepository
                    .findBookingsOfUserBetween(
                        user.getId(), now, now, PageRequest.of(page, size));
            case ALL:
                return bookingRepository.findBookingsOfUser(user.getId(),
                    PageRequest.of(page, size));

            default:
                throw new IllegalArgumentException("This should not have happened");
        }
    }

    @Override
    public Collection<Booking> getItemBookingsOfCurrentUser(BookingState state,
            int from, int size) {
        Collection<Long> ids = itemService.getItemsOfCurrentUser().stream()
            .map(Item::getId)
            .collect(Collectors.toList());

        if (ids.isEmpty()) {
            return Collections.emptyList();
        }

        int page = from / size;

        switch (state) {
            case WAITING:
                return bookingRepository.findBookingsOfItemsWithStatus(
                    ids, BookingStatus.WAITING, PageRequest.of(page, size));
            case REJECTED:
                return bookingRepository.findBookingsOfItemsWithStatus(
                    ids, BookingStatus.REJECTED, PageRequest.of(page, size));
            case PAST:
                return bookingRepository.findPastBookingsOfItems(
                    ids, LocalDateTime.now(clock), PageRequest.of(page, size));
            case FUTURE:
                return bookingRepository.findFutureBookingsOfItems(
                    ids, LocalDateTime.now(clock), PageRequest.of(page, size));
            case CURRENT:
                LocalDateTime now = LocalDateTime.now(clock);
                return bookingRepository
                    .findBookingsOfItemsBetween(
                        ids, now, now, PageRequest.of(page, size));
            case ALL:
                return bookingRepository.findBookingsItems(ids,
                    PageRequest.of(page, size));

            default:
                throw new IllegalArgumentException("This should not have happened");
        }
    }

    @Override
    public Booking getPreviousBookingOfItem(long itemId) {
        LocalDateTime now = LocalDateTime.now(clock);
        return bookingRepository.findPastBookingsOfItem(itemId, now)
            .stream()
            .max(Comparator.comparing(Booking::getEnd))
            .orElse(null);
    }

    @Override
    public Booking getNextBookingOfItem(long itemId) {
        LocalDateTime now = LocalDateTime.now(clock);
        return bookingRepository.findFutureBookingsOfItem(itemId, now)
            .stream()
            .findFirst()
            .orElse(null);
    }
}
