package ru.practicum.shareit.booking;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.security.AuthService;
import ru.practicum.shareit.user.model.User;

/**
 * Controller of booking.
 */
@RestController
@AllArgsConstructor
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final ItemService itemService;
    private final AuthService authService;

    @PostMapping
    public BookingDto bookEntity(
        @RequestHeader Map<String, String> headers,
        @Valid @RequestBody CreateBookingDto dto
    ) {
        User user = authService.getCurrentUser(headers);
        dto.setUserId(user.getId());
        Booking booking = bookingService.createBooking(dto);
        return BookingMapper.toDto(booking);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto reviewBooking(
        @RequestHeader Map<String, String> headers,
        @PathVariable long bookingId,
        @RequestParam("approved") Boolean isApproved
    ) {
        User user = authService.getCurrentUser(headers);
        Booking booking = bookingService.reviewBooking(bookingId, user.getId(), isApproved);
        return BookingMapper.toDto(booking);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBooking(
        @RequestHeader Map<String, String> headers,
        @PathVariable long bookingId
    ) {
        User user = authService.getCurrentUser(headers);
        Booking booking = bookingService.getBooking(bookingId, user.getId())
            .orElseThrow(BookingNotFoundException::new);
        return BookingMapper.toDto(booking);
    }

    @GetMapping
    public Collection<BookingDto> getBookingsOfCurrentUser(
        @RequestHeader Map<String, String> headers,
        @RequestParam(name = "state", defaultValue = "ALL") BookingState state
    ) {
        User user = authService.getCurrentUser(headers);
        return bookingService.getBookingsOfUser(user.getId(), state).stream()
            .map(BookingMapper::toDto)
            .collect(Collectors.toList());
    }

    @GetMapping("/owner")
    public Collection<BookingDto> getItemBookingsOfCurrentUser(
        @RequestHeader Map<String, String> headers,
        @RequestParam(name = "state", defaultValue = "ALL") BookingState state
    ) {
        User user = authService.getCurrentUser(headers);
        return bookingService.getItemBookingsOfUser(user.getId(), state).stream()
            .map(BookingMapper::toDto)
            .collect(Collectors.toList());
    }
}
