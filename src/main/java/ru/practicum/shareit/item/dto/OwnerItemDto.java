package ru.practicum.shareit.item.dto;

import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.lang.Nullable;

@AllArgsConstructor
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class OwnerItemDto {

    @EqualsAndHashCode.Include
    private final long id;

    private final String name;

    private final String description;

    private final boolean available;

    private final Collection<CommentDto> comments;

    private final @Nullable Booking lastBooking;

    private final @Nullable Booking nextBooking;

    @AllArgsConstructor
    @Getter
    public static class Booking {

        private long id;

        private long bookerId;
    }
}
