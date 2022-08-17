package ru.practicum.shareitgateway.booking;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareitgateway.booking.dto.BookingState;
import ru.practicum.shareitgateway.booking.dto.CreateBookingDto;
import ru.practicum.shareitgateway.security.UserId;

/**
 * Controller of booking.
 */
@RestController
@AllArgsConstructor
@RequestMapping("/bookings")
@Validated
public class BookingController {

    private BookingClient client;

    @PostMapping
    public Object bookEntity(
        @UserId long userId,
        @Valid @RequestBody CreateBookingDto dto
    ) {
        return client.bookItem(userId, dto);
    }

    @PatchMapping("/{bookingId}")
    public Object reviewBooking(
        @UserId long userId,
        @PathVariable long bookingId,
        @RequestParam("approved") Boolean isApproved
    ) {
        return client.reviewBooking(userId, bookingId, isApproved);
    }

    @GetMapping("/{bookingId}")
    public Object getBooking(
        @UserId long userId,
        @PathVariable long bookingId
    ) {
        return client.getBooking(userId, bookingId);
    }

    @GetMapping
    public Object getBookingsOfUser(
        @UserId long userId,
        @RequestParam(name = "state", defaultValue = "ALL") BookingState state,
        @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero int from,
        @RequestParam(name = "size", defaultValue = "10") @Positive int size
    ) {
        return client.getBookings(userId, state, from, size);
    }

    @GetMapping("/owner")
    public Object getItemBookingsOfUser(
        @UserId long userId,
        @RequestParam(name = "state", defaultValue = "ALL") BookingState state,
        @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero int from,
        @RequestParam(name = "size", defaultValue = "10") @Positive int size
    ) {
        return client.getOwnerBookings(userId, state, from, size);
    }
}
