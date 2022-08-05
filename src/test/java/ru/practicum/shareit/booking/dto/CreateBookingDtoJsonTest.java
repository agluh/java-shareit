package ru.practicum.shareit.booking.dto;

import static org.assertj.core.api.BDDAssertions.then;

import java.time.LocalDateTime;
import java.time.Month;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

@JsonTest
class CreateBookingDtoJsonTest {

    @Autowired
    private JacksonTester<CreateBookingDto> json;

    @Test
    public void givenCorrectJson_whenParseJson_thenCorrectObjectShouldBeReturned()
            throws Exception {
        // Given
        String jsonContent
            = "{\"start\":\"2022-08-01T09:50:00\",\"end\":\"2022-08-02T09:50:00\",\"itemId\":1}";

        // When
        CreateBookingDto result = this.json.parse(jsonContent).getObject();

        // Then
        then(result.getStart()).isEqualTo(LocalDateTime.of(2022, Month.AUGUST, 1, 9, 50));
        then(result.getEnd()).isEqualTo(LocalDateTime.of(2022, Month.AUGUST, 2, 9, 50));
        then(result.getItemId()).isEqualTo(1);
    }
}