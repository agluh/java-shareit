package ru.practicum.shareit.item.dto;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

@JsonTest
class CreateItemDtoJsonTest {

    @Autowired
    private JacksonTester<CreateItemDto> json;

    @Test
    public void givenCorrectJsonWithoutOptionalField_whenParseJson_thenCorrectObjectShouldBeReturned()
            throws Exception {
        // Given
        String jsonContent =
            "{\"name\":\"Name\",\"description\":\"Description\",\"available\":true}";

        // When
        CreateItemDto result = this.json.parse(jsonContent).getObject();

        // Then
        then(result.getName()).isEqualTo("Name");
        then(result.getDescription()).isEqualTo("Description");
        then(result.getAvailable()).isTrue();
        then(result.getRequestId()).isNull();
    }

    @Test
    public void givenCorrectJsonWithOptionalField_whenParseJson_thenCorrectObjectShouldBeReturned()
            throws Exception {
        // Given
        String jsonContent = "{\"name\":\"Name\",\"description\":\"Description\","
                + "\"available\":true,\"requestId\":1}";

        // When
        CreateItemDto result = this.json.parse(jsonContent).getObject();

        // Then
        then(result.getName()).isEqualTo("Name");
        then(result.getDescription()).isEqualTo("Description");
        then(result.getAvailable()).isTrue();
        then(result.getRequestId()).isEqualTo(1);
    }
}