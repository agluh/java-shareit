package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

public class BookingMapper {

    public static BookingDto toDto(Booking booking) {
        return new BookingDto(
            booking.getId(),
            booking.getStart(),
            booking.getEnd(),
            new BookingDto.Item(booking.getItem().getId(), booking.getItem().getName()),
            new BookingDto.Booker(booking.getBooker().getId()),
            booking.getStatus()
        );
    }
}
