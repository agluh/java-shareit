package ru.practicum.shareitserver.booking.dto;

import static org.assertj.core.api.BDDAssertions.then;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareitserver.booking.model.BookingStatus;

@JsonTest
class BookingDtoJsonTest {

    @Autowired
    private JacksonTester<BookingDto> json;

    @Test
    void givenBookingDto_whenSerializeToJson_thenCorrectJsonShouldBeReturned() throws IOException {
        // Given
        final long bookingId = 1;
        final long itemId = 1;
        final long bookerId = 1;
        final String itemName = "Item";
        final LocalDateTime start = LocalDateTime.of(2022, Month.AUGUST, 1, 9, 50);
        final LocalDateTime end = start.plusDays(1);

        BookingDto dto = new BookingDto(
            bookingId, start, end,
            new BookingDto.Item(itemId, itemName),
            new BookingDto.Booker(bookerId),
            BookingStatus.WAITING);

        // When
        JsonContent<BookingDto> result = json.write(dto);

        // Then
        then(result).extractingJsonPathNumberValue("$.id").isEqualTo((int) bookingId);
        then(result).extractingJsonPathStringValue("$.start").isEqualTo("2022-08-01T09:50:00");
        then(result).extractingJsonPathStringValue("$.end").isEqualTo("2022-08-02T09:50:00");
        then(result).hasJsonPathMapValue("$.item");
        then(result).extractingJsonPathNumberValue("$.item.id").isEqualTo((int) itemId);
        then(result).extractingJsonPathStringValue("$.item.name").isEqualTo(itemName);
        then(result).hasJsonPathMapValue("$.booker");
        then(result).extractingJsonPathNumberValue("$.booker.id").isEqualTo((int) bookerId);
        then(result).extractingJsonPathStringValue("$.status").isEqualTo("WAITING");
    }
}