package ru.practicum.shareitserver.booking;

import org.springframework.stereotype.Service;
import ru.practicum.shareitserver.booking.dto.BookingDto;
import ru.practicum.shareitserver.booking.model.Booking;

@Service
public class BookingMapper {

    public BookingDto toDto(Booking booking) {
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
