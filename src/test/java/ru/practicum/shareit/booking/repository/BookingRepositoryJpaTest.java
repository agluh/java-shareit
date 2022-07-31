package ru.practicum.shareit.booking.repository;

import static org.assertj.core.api.BDDAssertions.then;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@DataJpaTest
class BookingRepositoryJpaTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private BookingRepository repository;

    @Test
    void test_findBookingsByBookerIdOrderByStartDesc() {
        // Given
        User booker = new User(null, "User name 1", "email@example.com");
        User owner = new User(null, "User name 2", "email2@example.com");
        Item item = new Item(null, "Item name", "Item description", true, owner, null);
        final LocalDateTime start = LocalDateTime.of(2022, Month.AUGUST, 1, 9, 50);
        final LocalDateTime end = start.plusDays(1);
        Booking booking = new Booking(null, start, end, item, booker, BookingStatus.WAITING);
        em.persist(owner);
        em.persist(booker);
        em.persist(item);
        em.persist(booking);

        // When
        Collection<Booking> result = repository.findBookingsByBookerIdOrderByStartDesc(booker.getId());

        // Then
        then(result).size().isEqualTo(1);
        then(result).containsExactlyElementsOf(List.of(booking));
    }

    @Test
    void test_findBookingsByBookerIdAndStatusOrderByStartDesc() {
        // Given
        User booker = new User(null, "User name 1", "email@example.com");
        User owner = new User(null, "User name 2", "email2@example.com");
        Item item = new Item(null, "Item name", "Item description", true, owner, null);
        final LocalDateTime start = LocalDateTime.of(2022, Month.AUGUST, 1, 9, 50);
        final LocalDateTime end = start.plusDays(1);
        Booking booking = new Booking(null, start, end, item, booker, BookingStatus.REJECTED);
        em.persist(owner);
        em.persist(booker);
        em.persist(item);
        em.persist(booking);

        // When
        Collection<Booking> result = repository.findBookingsByBookerIdAndStatusOrderByStartDesc(booker.getId(), BookingStatus.REJECTED);

        // Then
        then(result).size().isEqualTo(1);
        then(result).containsExactlyElementsOf(List.of(booking));
    }

    @Test
    void test_findBookingsByBookerIdAndEndBeforeOrderByStartDesc() {
        // Given
        User booker = new User(null, "User name 1", "email@example.com");
        User owner = new User(null, "User name 2", "email2@example.com");
        Item item = new Item(null, "Item name", "Item description", true, owner, null);
        final LocalDateTime start = LocalDateTime.of(2022, Month.AUGUST, 1, 9, 50);
        final LocalDateTime end = start.plusDays(1);
        final LocalDateTime shouldEndedBefore = end.plusSeconds(1);
        Booking booking = new Booking(null, start, end, item, booker, BookingStatus.WAITING);
        em.persist(owner);
        em.persist(booker);
        em.persist(item);
        em.persist(booking);

        // When
        Collection<Booking> result = repository.findBookingsByBookerIdAndEndBeforeOrderByStartDesc(booker.getId(), shouldEndedBefore);

        // Then
        then(result).size().isEqualTo(1);
        then(result).containsExactlyElementsOf(List.of(booking));
    }

    @Test
    void test_findBookingsByBookerIdAndStartAfterOrderByStartDesc() {
        // Given
        User booker = new User(null, "User name 1", "email@example.com");
        User owner = new User(null, "User name 2", "email2@example.com");
        Item item = new Item(null, "Item name", "Item description", true, owner, null);
        final LocalDateTime start = LocalDateTime.of(2022, Month.AUGUST, 1, 9, 50);
        final LocalDateTime end = start.plusDays(1);
        final LocalDateTime shouldStartAfter = start.minusSeconds(1);
        Booking booking = new Booking(null, start, end, item, booker, BookingStatus.WAITING);
        em.persist(owner);
        em.persist(booker);
        em.persist(item);
        em.persist(booking);

        // When
        Collection<Booking> result = repository.findBookingsByBookerIdAndStartAfterOrderByStartDesc(booker.getId(), shouldStartAfter);

        // Then
        then(result).size().isEqualTo(1);
        then(result).containsExactlyElementsOf(List.of(booking));
    }

    @Test
    void test_findBookingsByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc() {
        // Given
        User booker = new User(null, "User name 1", "email@example.com");
        User owner = new User(null, "User name 2", "email2@example.com");
        Item item = new Item(null, "Item name", "Item description", true, owner, null);
        final LocalDateTime start = LocalDateTime.of(2022, Month.AUGUST, 1, 9, 50);
        final LocalDateTime end = start.plusDays(1);
        final LocalDateTime shouldStartAfter = start.plusSeconds(1);
        final LocalDateTime shouldEndedBefore = end.minusSeconds(1);
        Booking booking = new Booking(null, start, end, item, booker, BookingStatus.WAITING);
        em.persist(owner);
        em.persist(booker);
        em.persist(item);
        em.persist(booking);

        // When
        Collection<Booking> result = repository.findBookingsByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(booker.getId(), shouldStartAfter, shouldEndedBefore);

        // Then
        then(result).size().isEqualTo(1);
        then(result).containsExactlyElementsOf(List.of(booking));
    }

    @Test
    void test_findBookingsByItemIdInOrderByStartDesc() {
        // Given
        User booker = new User(null, "User name 1", "email@example.com");
        User owner = new User(null, "User name 2", "email2@example.com");
        Item item = new Item(null, "Item name", "Item description", true, owner, null);
        final LocalDateTime start = LocalDateTime.of(2022, Month.AUGUST, 1, 9, 50);
        final LocalDateTime end = start.plusDays(1);
        Booking booking = new Booking(null, start, end, item, booker, BookingStatus.WAITING);
        em.persist(owner);
        em.persist(booker);
        em.persist(item);
        em.persist(booking);

        // When
        Collection<Booking> result = repository.findBookingsByItemIdInOrderByStartDesc(List.of(item.getId()));

        // Then
        then(result).size().isEqualTo(1);
        then(result).containsExactlyElementsOf(List.of(booking));
    }

    @Test
    void test_findBookingsByItemIdInAndStatusOrderByStartDesc() {
        // Given
        User booker = new User(null, "User name 1", "email@example.com");
        User owner = new User(null, "User name 2", "email2@example.com");
        Item item = new Item(null, "Item name", "Item description", true, owner, null);
        final LocalDateTime start = LocalDateTime.of(2022, Month.AUGUST, 1, 9, 50);
        final LocalDateTime end = start.plusDays(1);
        Booking booking = new Booking(null, start, end, item, booker, BookingStatus.REJECTED);
        em.persist(owner);
        em.persist(booker);
        em.persist(item);
        em.persist(booking);

        // When
        Collection<Booking> result = repository.findBookingsByItemIdInAndStatusOrderByStartDesc(List.of(item.getId()), BookingStatus.REJECTED);

        // Then
        then(result).size().isEqualTo(1);
        then(result).containsExactlyElementsOf(List.of(booking));
    }

    @Test
    void test_findBookingsByItemIdInAndEndBeforeOrderByStartDesc() {
        // Given
        User booker = new User(null, "User name 1", "email@example.com");
        User owner = new User(null, "User name 2", "email2@example.com");
        Item item = new Item(null, "Item name", "Item description", true, owner, null);
        final LocalDateTime start = LocalDateTime.of(2022, Month.AUGUST, 1, 9, 50);
        final LocalDateTime end = start.plusDays(1);
        final LocalDateTime shouldEndedBefore = end.plusSeconds(1);
        Booking booking = new Booking(null, start, end, item, booker, BookingStatus.WAITING);
        em.persist(owner);
        em.persist(booker);
        em.persist(item);
        em.persist(booking);

        // When
        Collection<Booking> result = repository.findBookingsByItemIdInAndEndBeforeOrderByStartDesc(List.of(item.getId()), shouldEndedBefore);

        // Then
        then(result).size().isEqualTo(1);
        then(result).containsExactlyElementsOf(List.of(booking));
    }

    @Test
    void test_findBookingsByItemIdInAndStartAfterOrderByStartDesc() {
        // Given
        User booker = new User(null, "User name 1", "email@example.com");
        User owner = new User(null, "User name 2", "email2@example.com");
        Item item = new Item(null, "Item name", "Item description", true, owner, null);
        final LocalDateTime start = LocalDateTime.of(2022, Month.AUGUST, 1, 9, 50);
        final LocalDateTime end = start.plusDays(1);
        final LocalDateTime shouldStartAfter = start.minusSeconds(1);
        Booking booking = new Booking(null, start, end, item, booker, BookingStatus.WAITING);
        em.persist(owner);
        em.persist(booker);
        em.persist(item);
        em.persist(booking);

        // When
        Collection<Booking> result = repository.findBookingsByItemIdInAndStartAfterOrderByStartDesc(List.of(item.getId()), shouldStartAfter);

        // Then
        then(result).size().isEqualTo(1);
        then(result).containsExactlyElementsOf(List.of(booking));
    }

    @Test
    void test_findBookingsByItemIdInAndStartBeforeAndEndAfterOrderByStartDesc() {
        // Given
        User booker = new User(null, "User name 1", "email@example.com");
        User owner = new User(null, "User name 2", "email2@example.com");
        Item item = new Item(null, "Item name", "Item description", true, owner, null);
        final LocalDateTime start = LocalDateTime.of(2022, Month.AUGUST, 1, 9, 50);
        final LocalDateTime end = start.plusDays(1);
        final LocalDateTime shouldStartAfter = start.plusSeconds(1);
        final LocalDateTime shouldEndedBefore = end.minusSeconds(1);
        Booking booking = new Booking(null, start, end, item, booker, BookingStatus.WAITING);
        em.persist(owner);
        em.persist(booker);
        em.persist(item);
        em.persist(booking);

        // When
        Collection<Booking> result = repository.findBookingsByItemIdInAndStartBeforeAndEndAfterOrderByStartDesc(List.of(item.getId()), shouldStartAfter, shouldEndedBefore);

        // Then
        then(result).size().isEqualTo(1);
        then(result).containsExactlyElementsOf(List.of(booking));
    }

    @Test
    void test_findBookingsByItemIdAndEndBeforeOrderByStartDesc() {
        // Given
        User booker = new User(null, "User name 1", "email@example.com");
        User owner = new User(null, "User name 2", "email2@example.com");
        Item item = new Item(null, "Item name", "Item description", true, owner, null);
        final LocalDateTime start = LocalDateTime.of(2022, Month.AUGUST, 1, 9, 50);
        final LocalDateTime end = start.plusDays(1);
        final LocalDateTime shouldEndedBefore = end.plusSeconds(1);
        Booking booking = new Booking(null, start, end, item, booker, BookingStatus.WAITING);
        em.persist(owner);
        em.persist(booker);
        em.persist(item);
        em.persist(booking);

        // When
        Collection<Booking> result = repository.findBookingsByItemIdAndEndBeforeOrderByStartDesc(item.getId(), shouldEndedBefore);

        // Then
        then(result).size().isEqualTo(1);
        then(result).containsExactlyElementsOf(List.of(booking));
    }

    @Test
    void test_findBookingsByItemIdAndStartAfterOrderByStartDesc() {
        // Given
        User booker = new User(null, "User name 1", "email@example.com");
        User owner = new User(null, "User name 2", "email2@example.com");
        Item item = new Item(null, "Item name", "Item description", true, owner, null);
        final LocalDateTime start = LocalDateTime.of(2022, Month.AUGUST, 1, 9, 50);
        final LocalDateTime end = start.plusDays(1);
        final LocalDateTime shouldStartAfter = start.minusSeconds(1);
        Booking booking = new Booking(null, start, end, item, booker, BookingStatus.WAITING);
        em.persist(owner);
        em.persist(booker);
        em.persist(item);
        em.persist(booking);

        // When
        Collection<Booking> result = repository.findBookingsByItemIdAndStartAfterOrderByStartDesc(item.getId(), shouldStartAfter);

        // Then
        then(result).size().isEqualTo(1);
        then(result).containsExactlyElementsOf(List.of(booking));
    }
}