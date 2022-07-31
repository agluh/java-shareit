package ru.practicum.shareit.booking.repository;

import java.time.LocalDateTime;
import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Collection<Booking> findBookingsByBookerIdOrderByStartDesc(long userId);

    Collection<Booking> findBookingsByBookerIdAndStatusOrderByStartDesc(long userId,
        BookingStatus status);

    Collection<Booking> findBookingsByBookerIdAndEndBeforeOrderByStartDesc(long userId,
        LocalDateTime now);

    Collection<Booking> findBookingsByBookerIdAndStartAfterOrderByStartDesc(long userId,
        LocalDateTime now);

    Collection<Booking> findBookingsByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(long userId,
        LocalDateTime start, LocalDateTime end);

    Collection<Booking> findBookingsByItemIdInOrderByStartDesc(Collection<Long> ids);

    Collection<Booking> findBookingsByItemIdInAndStatusOrderByStartDesc(Collection<Long> ids,
        BookingStatus status);

    Collection<Booking> findBookingsByItemIdInAndEndBeforeOrderByStartDesc(Collection<Long> ids,
        LocalDateTime now);

    Collection<Booking> findBookingsByItemIdInAndStartAfterOrderByStartDesc(Collection<Long> ids,
        LocalDateTime now);

    Collection<Booking> findBookingsByItemIdInAndStartBeforeAndEndAfterOrderByStartDesc(
        Collection<Long> ids, LocalDateTime start, LocalDateTime end);

    Collection<Booking> findBookingsByItemIdAndEndBeforeOrderByStartDesc(long itemId,
        LocalDateTime now);

    Collection<Booking> findBookingsByItemIdAndStartAfterOrderByStartDesc(long itemId,
        LocalDateTime now);
}
