package ru.practicum.shareitserver.booking.repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareitserver.booking.model.Booking;
import ru.practicum.shareitserver.booking.model.BookingStatus;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b WHERE b.booker.id = ?1 ORDER BY b.start DESC")
    List<Booking> findBookingsOfUser(long userId, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = ?1 AND b.status = ?2 ORDER BY b.start DESC")
    List<Booking> findBookingsOfUserWithStatus(long userId, BookingStatus status,
        Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = ?1 AND b.end < ?2 ORDER BY b.start DESC")
    List<Booking> findPastBookingsOfUser(long userId, LocalDateTime now, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = ?1 AND b.start > ?2 ORDER BY b.start DESC")
    List<Booking> findFutureBookingsOfUser(long userId, LocalDateTime now, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = ?1 AND b.start < ?2 AND b.end > ?3 "
        + "ORDER BY b.start DESC")
    List<Booking> findBookingsOfUserBetween(long userId, LocalDateTime start, LocalDateTime end,
        Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.item.id IN ?1 ORDER BY b.start DESC")
    List<Booking> findBookingsItems(Collection<Long> ids, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.item.id IN ?1 AND b.status = ?2 ORDER BY b.start DESC")
    List<Booking> findBookingsOfItemsWithStatus(Collection<Long> ids, BookingStatus status,
        Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.item.id IN ?1 AND b.end < ?2 ORDER BY b.start DESC")
    List<Booking> findPastBookingsOfItems(Collection<Long> ids, LocalDateTime now,
        Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.item.id IN ?1 AND b.start > ?2 ORDER BY b.start DESC")
    List<Booking> findFutureBookingsOfItems(Collection<Long> ids, LocalDateTime now, Pageable
        pageable);

    @Query("SELECT b FROM Booking b WHERE b.item.id IN ?1 AND b.start < ?2 AND b.end > ?3 "
        + "ORDER BY b.start DESC")
    List<Booking> findBookingsOfItemsBetween(Collection<Long> ids, LocalDateTime start,
        LocalDateTime end, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.item.id = ?1 AND b.end < ?2 ORDER BY b.start DESC")
    Collection<Booking> findPastBookingsOfItem(long itemId, LocalDateTime now);

    @Query("SELECT b FROM Booking b WHERE b.item.id = ?1 AND b.start > ?2 ORDER BY b.start DESC")
    Collection<Booking> findFutureBookingsOfItem(long itemId, LocalDateTime now);
}
