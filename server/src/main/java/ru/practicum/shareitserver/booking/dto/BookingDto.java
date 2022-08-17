package ru.practicum.shareitserver.booking.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareitserver.booking.model.BookingStatus;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class BookingDto {

    @EqualsAndHashCode.Include
    private Long id;

    private LocalDateTime start;

    private LocalDateTime end;

    private Item item;

    private Booker booker;

    private BookingStatus status;

    @AllArgsConstructor
    @Getter
    @Setter
    @EqualsAndHashCode
    public static class Booker {

        private long id;
    }

    @Getter
    @AllArgsConstructor
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    public static class Item {

        @EqualsAndHashCode.Include
        private long id;

        private String name;
    }
}
