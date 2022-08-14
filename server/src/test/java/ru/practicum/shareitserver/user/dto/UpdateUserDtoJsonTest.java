package ru.practicum.shareitserver.user.dto;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

@JsonTest
class UpdateUserDtoJsonTest {

    @Autowired
    private JacksonTester<UpdateUserDto> json;

    @Test
    public void givenCorrectJson_whenParseJson_thenCorrectObjectShouldBeReturned()
            throws Exception {
        // Given
        String jsonContent = "{\"name\":\"Name\",\"email\":\"email@example.com\"}";

        // When
        UpdateUserDto result = this.json.parse(jsonContent).getObject();

        // Then
        then(result.getName()).isEqualTo("Name");
        then(result.getEmail()).isEqualTo("email@example.com");
    }

    @Test
    public void givenCorrectJsonWithNameOnly_whenParseJson_thenCorrectObjectShouldBeReturned()
            throws Exception {
        // Given
        String jsonContent = "{\"name\":\"Name\"}";

        // When
        UpdateUserDto result = this.json.parse(jsonContent).getObject();

        // Then
        then(result.getName()).isEqualTo("Name");
        then(result.getEmail()).isNull();
    }

    @Test
    public void givenCorrectJsonWithEmailOnly_whenParseJson_thenCorrectObjectShouldBeReturned()
            throws Exception {
        // Given
        String jsonContent = "{\"email\":\"email@example.com\"}";

        // When
        UpdateUserDto result = this.json.parse(jsonContent).getObject();

        // Then
        then(result.getName()).isNull();
        then(result.getEmail()).isEqualTo("email@example.com");
    }
}