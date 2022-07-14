package ru.practicum.shareit.booking.service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;

public interface BookingService {

    Booking createBooking(CreateBookingDto dto);

    Booking reviewBooking(long bookingId, long userId, boolean isApproved);

    Optional<Booking> getBooking(long bookingId, long userId);

    Collection<Booking> getBookingsOfUser(long userId, BookingState state);

    Collection<Booking> getItemBookingsOfUser(long userId, BookingState state);

    Booking getPreviousBookingOfItem(long itemId, LocalDateTime now);

    Booking getNextBookingOfItem(long itemId, LocalDateTime now);
}
