package ru.practicum.shareit.user.dto;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

@JsonTest
class CreateUserDtoJsonTest {

    @Autowired
    private JacksonTester<CreateUserDto> json;

    @Test
    public void givenCorrectJson_whenParseJson_thenCorrectObjectShouldBeReturned()
            throws Exception {
        // Given
        String jsonContent = "{\"name\":\"Name\",\"email\":\"email@example.com\"}";

        // When
        CreateUserDto result = this.json.parse(jsonContent).getObject();

        // Then
        then(result.getName()).isEqualTo("Name");
        then(result.getEmail()).isEqualTo("email@example.com");
    }
}