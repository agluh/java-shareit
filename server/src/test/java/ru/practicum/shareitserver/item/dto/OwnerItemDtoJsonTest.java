package ru.practicum.shareitserver.item.dto;

import static org.assertj.core.api.BDDAssertions.then;

import java.io.IOException;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

@JsonTest
class OwnerItemDtoJsonTest {

    @Autowired
    private JacksonTester<OwnerItemDto> json;

    @Test
    void givenOwnerItemDto_whenSerializeToJson_thenCorrectJsonShouldBeReturned()
            throws IOException {
        // Given
        final long itemId = 1;
        final long bookingId = 1;
        final long bookerId = 1;
        final String itemName = "Item";
        final String itemDescription = "Description";

        OwnerItemDto dto = new OwnerItemDto(itemId, itemName, itemDescription, true,
            null, Collections.emptyList(),
            new OwnerItemDto.Booking(bookingId, bookerId), null);

        // When
        JsonContent<OwnerItemDto> result = json.write(dto);

        // Then
        then(result).extractingJsonPathNumberValue("$.id").isEqualTo((int) itemId);
        then(result).extractingJsonPathStringValue("$.name").isEqualTo(itemName);
        then(result).extractingJsonPathStringValue("$.description").isEqualTo(itemDescription);
        then(result).extractingJsonPathBooleanValue("$.available").isTrue();
        then(result).extractingJsonPathNumberValue("$.requestId").isNull();
        then(result).hasJsonPathArrayValue("$.comments");
        then(result).hasJsonPathMapValue("$.lastBooking");
        then(result).extractingJsonPathNumberValue("$.lastBooking.id")
            .isEqualTo((int) bookingId);
        then(result).extractingJsonPathNumberValue("$.lastBooking.bookerId")
            .isEqualTo((int) bookerId);
        then(result).extractingJsonPathValue("$.nextBooking").isNull();
    }
}