package ru.practicum.shareitserver.booking;

import java.util.Collection;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareitserver.booking.dto.BookingDto;
import ru.practicum.shareitserver.booking.dto.CreateBookingDto;
import ru.practicum.shareitserver.booking.exception.BookingNotFoundException;
import ru.practicum.shareitserver.booking.model.Booking;
import ru.practicum.shareitserver.booking.model.BookingState;
import ru.practicum.shareitserver.booking.service.BookingService;

/**
 * Controller of booking.
 */
@RestController
@AllArgsConstructor
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final BookingMapper mapper;

    @PostMapping
    public BookingDto bookEntity(@RequestBody CreateBookingDto dto) {
        Booking booking = bookingService.createBooking(dto);
        return mapper.toDto(booking);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto reviewBooking(
        @PathVariable long bookingId,
        @RequestParam("approved") Boolean isApproved
    ) {
        Booking booking = bookingService.reviewBooking(bookingId, isApproved);
        return mapper.toDto(booking);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBooking(@PathVariable long bookingId) {
        Booking booking = bookingService.getBooking(bookingId)
            .orElseThrow(BookingNotFoundException::new);
        return mapper.toDto(booking);
    }

    @GetMapping
    public Collection<BookingDto> getBookingsOfCurrentUser(
        @RequestParam(name = "state", defaultValue = "ALL") BookingState state,
        @RequestParam(name = "from", defaultValue = "0") int from,
        @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return bookingService.getBookingsOfCurrentUser(state, from, size).stream()
            .map(mapper::toDto)
            .collect(Collectors.toList());
    }

    @GetMapping("/owner")
    public Collection<BookingDto> getItemBookingsOfCurrentUser(
        @RequestParam(name = "state", defaultValue = "ALL") BookingState state,
        @RequestParam(name = "from", defaultValue = "0") int from,
        @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return bookingService.getItemBookingsOfCurrentUser(state, from, size).stream()
            .map(mapper::toDto)
            .collect(Collectors.toList());
    }
}
