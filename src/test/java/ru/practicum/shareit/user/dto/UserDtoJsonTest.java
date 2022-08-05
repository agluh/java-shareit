package ru.practicum.shareit.user.dto;

import static org.assertj.core.api.BDDAssertions.then;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

@JsonTest
class UserDtoJsonTest {

    @Autowired
    private JacksonTester<UserDto> json;

    @Test
    void givenUserDto_whenSerializeToJson_thenCorrectJsonShouldBeReturned() throws IOException {
        // Given
        final long id = 1;
        final String name = "Name";
        final String email = "email@example.com";

        UserDto dto = new UserDto(id, name, email);

        // When
        JsonContent<UserDto> result = json.write(dto);

        // Then
        then(result).extractingJsonPathNumberValue("$.id").isEqualTo((int) id);
        then(result).extractingJsonPathStringValue("$.name").isEqualTo(name);
        then(result).extractingJsonPathStringValue("$.email").isEqualTo(email);
    }
}