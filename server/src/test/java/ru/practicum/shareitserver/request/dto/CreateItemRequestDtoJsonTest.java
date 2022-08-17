package ru.practicum.shareitserver.request.dto;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

@JsonTest
class CreateItemRequestDtoJsonTest {

    @Autowired
    private JacksonTester<CreateItemRequestDto> json;

    @Test
    public void givenCorrectJson_whenParseJson_thenCorrectObjectShouldBeReturned()
            throws Exception {
        // Given
        String jsonContent = "{\"description\":\"Description\"}";

        // When
        CreateItemRequestDto result = this.json.parse(jsonContent).getObject();

        // Then
        then(result.getDescription()).isEqualTo("Description");
    }
}