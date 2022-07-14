package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class CreateBookingDto {

    @JsonIgnore
    @Setter
    private long userId;

    @NotNull
    private LocalDateTime start;

    @NotNull
    private LocalDateTime end;

    private long itemId;
}
