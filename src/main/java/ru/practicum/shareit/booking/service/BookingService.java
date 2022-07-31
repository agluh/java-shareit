package ru.practicum.shareit.booking.service;

import java.util.Collection;
import java.util.Optional;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;

public interface BookingService {

    Booking createBooking(CreateBookingDto dto);

    Booking reviewBooking(long bookingId, boolean isApproved);

    Optional<Booking> getBooking(long bookingId);

    Collection<Booking> getBookingsOfCurrentUser(BookingState state);

    Collection<Booking> getItemBookingsOfCurrentUser(BookingState state);

    Booking getPreviousBookingOfItem(long itemId);

    Booking getNextBookingOfItem(long itemId);
}
