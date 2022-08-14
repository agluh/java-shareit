package ru.practicum.shareitserver.request.dto;

import static org.assertj.core.api.BDDAssertions.then;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

@JsonTest
class ItemRequestDtoJsonTest {

    @Autowired
    private JacksonTester<ItemRequestDto> json;

    @Test
    void givenItemRequestDto_whenSerializeToJson_thenCorrectJsonShouldBeReturned()
            throws IOException {
        // Given
        final long requestId = 1;
        final long itemId = 1;
        final String itemName = "Name";
        final String itemDescription = "Description 1";
        final String requestDescription = "Description 2";
        final LocalDateTime created = LocalDateTime.of(2022, Month.AUGUST, 1, 9, 50);

        ItemRequestDto dto = new ItemRequestDto(requestId, requestDescription, created, List.of(
            new ItemRequestDto.Item(itemId, itemName, itemDescription, true, requestId)
        ));

        // When
        JsonContent<ItemRequestDto> result = json.write(dto);

        // Then
        then(result).extractingJsonPathNumberValue("$.id").isEqualTo((int) requestId);
        then(result).extractingJsonPathStringValue("$.description").isEqualTo(requestDescription);
        then(result).extractingJsonPathStringValue("$.created").isEqualTo("2022-08-01T09:50:00");
        then(result).hasJsonPathArrayValue("$.items");
        then(result).extractingJsonPathNumberValue("$.items[0].id").isEqualTo((int) itemId);
        then(result).extractingJsonPathStringValue("$.items[0].name")
            .isEqualTo(itemName);
        then(result).extractingJsonPathStringValue("$.items[0].description")
            .isEqualTo(itemDescription);
        then(result).extractingJsonPathBooleanValue("$.items[0].available").isTrue();
        then(result).extractingJsonPathNumberValue("$.items[0].requestId")
            .isEqualTo((int) requestId);
    }
}