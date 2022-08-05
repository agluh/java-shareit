package ru.practicum.shareit.booking;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.BookingState;

@Service
public class StringToBookingStateConverter implements Converter<String, BookingState> {

    @Override
    public BookingState convert(String from) {
        try {
            return BookingState.valueOf(from);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(String.format("Unknown state: %s", from));
        }
    }
}
