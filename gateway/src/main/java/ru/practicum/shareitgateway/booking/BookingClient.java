package ru.practicum.shareitgateway.booking;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareitgateway.booking.dto.BookingState;
import ru.practicum.shareitgateway.booking.dto.CreateBookingDto;
import ru.practicum.shareitgateway.client.BaseClient;

@Service
public class BookingClient extends BaseClient {

    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(
        @Value("${shareit-server.url}") String serverUrl,
        @Value("${app.auth-header}") String authHeader,
        RestTemplateBuilder builder
    ) {
        super(
            builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .build(),
            authHeader
        );
    }

    public Object bookItem(long userId, CreateBookingDto requestDto) {
        return post("", userId, requestDto);
    }

    public Object reviewBooking(long userId, long bookingId, boolean isApproved) {
        return patch("/" + bookingId, userId, Map.of("approved", isApproved), null);
    }

    public Object getBooking(long userId, long bookingId) {
        return get("/" + bookingId, userId);
    }

    public Object getBookings(long userId, BookingState state, int from, int size) {
        Map<String, Object> parameters = Map.of(
            "state", state.name(),
            "from", from,
            "size", size
        );
        return get("", userId, parameters);
    }

    public Object getOwnerBookings(long userId, BookingState state, int from, int size) {
        Map<String, Object> parameters = Map.of(
            "state", state.name(),
            "from", from,
            "size", size
        );
        return get("/owner", userId, parameters);
    }
}
