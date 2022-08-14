package ru.practicum.shareitserver.booking.dto;

import java.time.LocalDateTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class CreateBookingDto {

    private LocalDateTime start;

    private LocalDateTime end;

    private long itemId;
}
