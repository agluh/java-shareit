package ru.practicum.shareit.booking.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import ru.practicum.shareit.booking.model.BookingStatus;

@AllArgsConstructor
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class BookingDto {

    @EqualsAndHashCode.Include
    private Long id;

    private LocalDateTime start;

    private LocalDateTime end;

    private Item item;

    private Booker booker;

    private BookingStatus status;

    @Getter
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class Booker {

        private long id;
    }

    @Getter
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class Item {

        private long id;

        private String name;
    }
}
