package ru.practicum.shareit.item.dto;

import static org.assertj.core.api.BDDAssertions.then;

import java.io.IOException;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

@JsonTest
class ItemDtoJsonTest {

    @Autowired
    private JacksonTester<ItemDto> json;

    @Test
    void givenItemDto_whenSerializeToJson_thenCorrectJsonShouldBeReturned() throws IOException {
        // Given
        final long itemId = 1;
        final String itemName = "Item";
        final String itemDescription = "Description";

        ItemDto dto = new ItemDto(itemId, itemName, itemDescription,
            true, null, Collections.emptyList());

        // When
        JsonContent<ItemDto> result = json.write(dto);

        // Then
        then(result).extractingJsonPathNumberValue("$.id").isEqualTo((int) itemId);
        then(result).extractingJsonPathStringValue("$.name").isEqualTo(itemName);
        then(result).extractingJsonPathStringValue("$.description").isEqualTo(itemDescription);
        then(result).extractingJsonPathBooleanValue("$.available").isTrue();
        then(result).extractingJsonPathNumberValue("$.requestId").isNull();
        then(result).hasJsonPathArrayValue("$.comments");
    }
}