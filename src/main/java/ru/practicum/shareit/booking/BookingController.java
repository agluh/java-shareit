package ru.practicum.shareit.booking;

import java.util.Collection;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.service.BookingService;

/**
 * Controller of booking.
 */
@RestController
@AllArgsConstructor
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingDto bookEntity(@Valid @RequestBody CreateBookingDto dto) {
        Booking booking = bookingService.createBooking(dto);
        return BookingMapper.toDto(booking);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto reviewBooking(
        @PathVariable long bookingId,
        @RequestParam("approved") Boolean isApproved
    ) {
        Booking booking = bookingService.reviewBooking(bookingId, isApproved);
        return BookingMapper.toDto(booking);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBooking(@PathVariable long bookingId) {
        Booking booking = bookingService.getBooking(bookingId)
            .orElseThrow(BookingNotFoundException::new);
        return BookingMapper.toDto(booking);
    }

    @GetMapping
    public Collection<BookingDto> getBookingsOfCurrentUser(
        @RequestParam(name = "state", defaultValue = "ALL") BookingState state
    ) {
        return bookingService.getBookingsOfCurrentUser(state).stream()
            .map(BookingMapper::toDto)
            .collect(Collectors.toList());
    }

    @GetMapping("/owner")
    public Collection<BookingDto> getItemBookingsOfCurrentUser(
        @RequestParam(name = "state", defaultValue = "ALL") BookingState state
    ) {
        return bookingService.getItemBookingsOfCurrentUser(state).stream()
            .map(BookingMapper::toDto)
            .collect(Collectors.toList());
    }
}
