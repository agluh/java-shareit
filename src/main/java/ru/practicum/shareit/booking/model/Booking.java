package ru.practicum.shareit.booking.model;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Booking {

    @EqualsAndHashCode.Include
    @Setter(AccessLevel.NONE)
    private Long id;

    private LocalDateTime start;

    private LocalDateTime end;

    private long itemId;

    private long bookerId;

    private BookingStatus status;
}
