package ru.practicum.shareitserver.item.dto;

import static org.assertj.core.api.BDDAssertions.then;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

@JsonTest
class CommentDtoJsonTest {

    @Autowired
    private JacksonTester<CommentDto> json;

    @Test
    void givenCommentDto_whenSerializeToJson_thenCorrectJsonShouldBeReturned() throws IOException {
        // Given
        final long commentId = 1;
        final String authorName = "Author";
        final String text = "Comment";
        final LocalDateTime created = LocalDateTime.of(2022, Month.AUGUST, 1, 9, 50);

        CommentDto dto = new CommentDto(commentId, authorName, text, created);

        // When
        JsonContent<CommentDto> result = json.write(dto);

        // Then
        then(result).extractingJsonPathNumberValue("$.id").isEqualTo((int) commentId);
        then(result).extractingJsonPathStringValue("$.authorName").isEqualTo(authorName);
        then(result).extractingJsonPathStringValue("$.text").isEqualTo(text);
        then(result).extractingJsonPathStringValue("$.created").isEqualTo("2022-08-01T09:50:00");
    }
}