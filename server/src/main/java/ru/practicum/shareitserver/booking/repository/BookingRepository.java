package ru.practicum.shareitserver.booking.repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareitserver.booking.model.Booking;
import ru.practicum.shareitserver.booking.model.BookingStatus;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findBookingsByBookerIdOrderByStartDesc(long userId, Pageable pageable);

    List<Booking> findBookingsByBookerIdAndStatusOrderByStartDesc(long userId,
        BookingStatus status, Pageable pageable);

    List<Booking> findBookingsByBookerIdAndEndBeforeOrderByStartDesc(long userId,
        LocalDateTime now, Pageable pageable);

    List<Booking> findBookingsByBookerIdAndStartAfterOrderByStartDesc(long userId,
        LocalDateTime now, Pageable pageable);

    List<Booking> findBookingsByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(long userId,
        LocalDateTime start, LocalDateTime end, Pageable pageable);

    List<Booking> findBookingsByItemIdInOrderByStartDesc(Collection<Long> ids,
        Pageable pageable);

    List<Booking> findBookingsByItemIdInAndStatusOrderByStartDesc(Collection<Long> ids,
        BookingStatus status, Pageable pageable);

    List<Booking> findBookingsByItemIdInAndEndBeforeOrderByStartDesc(Collection<Long> ids,
        LocalDateTime now, Pageable pageable);

    List<Booking> findBookingsByItemIdInAndStartAfterOrderByStartDesc(Collection<Long> ids,
        LocalDateTime now, Pageable pageable);

    List<Booking> findBookingsByItemIdInAndStartBeforeAndEndAfterOrderByStartDesc(
        Collection<Long> ids, LocalDateTime start, LocalDateTime end, Pageable pageable);

    Collection<Booking> findBookingsByItemIdAndEndBeforeOrderByStartDesc(long itemId,
        LocalDateTime now);

    Collection<Booking> findBookingsByItemIdAndStartAfterOrderByStartDesc(long itemId,
        LocalDateTime now);
}
