package ru.practicum.shareit.booking.dto;

import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class CreateBookingDto {

    @NotNull
    private LocalDateTime start;

    @NotNull
    private LocalDateTime end;

    private long itemId;
}
