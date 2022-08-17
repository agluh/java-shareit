package ru.practicum.shareitserver.booking.service;

import java.util.Collection;
import java.util.Optional;
import ru.practicum.shareitserver.booking.dto.CreateBookingDto;
import ru.practicum.shareitserver.booking.model.Booking;
import ru.practicum.shareitserver.booking.model.BookingState;

public interface BookingService {

    Booking createBooking(CreateBookingDto dto);

    Booking reviewBooking(long bookingId, boolean isApproved);

    Optional<Booking> getBooking(long bookingId);

    Collection<Booking> getBookingsOfCurrentUser(BookingState state, int from, int size);

    Collection<Booking> getItemBookingsOfCurrentUser(BookingState state, int from, int size);

    Booking getPreviousBookingOfItem(long itemId);

    Booking getNextBookingOfItem(long itemId);
}
