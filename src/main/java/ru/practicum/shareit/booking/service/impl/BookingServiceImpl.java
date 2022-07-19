package ru.practicum.shareit.booking.service.impl;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.booking.exception.ItemIsNotAvailableForBookingException;
import ru.practicum.shareit.booking.exception.NotAnOwnerOrBookerException;
import ru.practicum.shareit.booking.exception.SelfBookingNotAllowedException;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.NotAnOwnerException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.security.AuthService;
import ru.practicum.shareit.user.model.User;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ItemService itemService;
    private final AuthService authService;

    @Override
    public Booking createBooking(CreateBookingDto dto) {
        if (dto.getStart().isAfter(dto.getEnd())) {
            throw new IllegalArgumentException("Bad date range");
        }

        if (dto.getStart().isBefore(LocalDateTime.now())) {
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

        return bookingRepository.findById(bookingId);
    }

    @Override
    public Collection<Booking> getBookingsOfCurrentUser(BookingState state) {
        User user = authService.getCurrentUser();

        switch (state) {
            case WAITING:
                return bookingRepository.findBookingsByBookerIdAndStatusOrderByStartDesc(
                    user.getId(), BookingStatus.WAITING);
            case REJECTED:
                return bookingRepository.findBookingsByBookerIdAndStatusOrderByStartDesc(
                    user.getId(), BookingStatus.REJECTED);
            case PAST:
                return bookingRepository.findBookingsByBookerIdAndEndBeforeOrderByStartDesc(
                    user.getId(), LocalDateTime.now());
            case FUTURE:
                return bookingRepository.findBookingsByBookerIdAndStartAfterOrderByStartDesc(
                    user.getId(), LocalDateTime.now());
            case CURRENT:
                LocalDateTime now = LocalDateTime.now();
                return bookingRepository
                    .findBookingsByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(
                        user.getId(), now, now);
            case ALL:
                return bookingRepository.findBookingsByBookerIdOrderByStartDesc(user.getId());

            default:
                throw new IllegalArgumentException("This should not have happened");
        }
    }

    @Override
    public Collection<Booking> getItemBookingsOfCurrentUser(BookingState state) {
        Collection<Long> ids = itemService.getItemsOfCurrentUser().stream()
            .map(Item::getId)
            .collect(Collectors.toList());

        if (ids.isEmpty()) {
            return Collections.emptyList();
        }

        switch (state) {
            case WAITING:
                return bookingRepository.findBookingsByItemIdInAndStatusOrderByStartDesc(ids,
                    BookingStatus.WAITING);
            case REJECTED:
                return bookingRepository.findBookingsByItemIdInAndStatusOrderByStartDesc(ids,
                    BookingStatus.REJECTED);
            case PAST:
                return bookingRepository.findBookingsByItemIdInAndEndBeforeOrderByStartDesc(ids,
                    LocalDateTime.now());
            case FUTURE:
                return bookingRepository.findBookingsByItemIdInAndStartAfterOrderByStartDesc(ids,
                    LocalDateTime.now());
            case CURRENT:
                LocalDateTime now = LocalDateTime.now();
                return bookingRepository
                    .findBookingsByItemIdInAndStartBeforeAndEndAfterOrderByStartDesc(ids, now, now);
            case ALL:
                return bookingRepository.findBookingsByItemIdInOrderByStartDesc(ids);

            default:
                throw new IllegalArgumentException("This should not have happened");
        }
    }

    @Override
    public Booking getPreviousBookingOfItem(long itemId, LocalDateTime now) {
        return bookingRepository.findBookingsByItemIdAndEndBeforeOrderByStartDesc(itemId, now)
            .stream()
            .max(Comparator.comparing(Booking::getEnd))
            .orElse(null);
    }

    @Override
    public Booking getNextBookingOfItem(long itemId, LocalDateTime now) {
        return bookingRepository.findBookingsByItemIdAndStartAfterOrderByStartDesc(itemId, now)
            .stream()
            .findFirst()
            .orElse(null);
    }
}
