package ru.practicum.shareit.item.dto;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

@JsonTest
class CreateCommentDtoJsonTest {

    @Autowired
    private JacksonTester<CreateCommentDto> json;

    @Test
    public void givenCorrectJson_whenParseJson_thenCorrectObjectShouldBeReturned()
            throws Exception {
        // Given
        String jsonContent = "{\"text\":\"Comment\"}";

        // When
        CreateCommentDto result = this.json.parse(jsonContent).getObject();

        // Then
        then(result.getText()).isEqualTo("Comment");
    }
}