package ru.practicum.shareitserver.item.dto;

import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class OwnerItemDto {

    @EqualsAndHashCode.Include
    private long id;

    private String name;

    private String description;

    private boolean available;

    private Long requestId;

    private Collection<CommentDto> comments;

    private @Nullable Booking lastBooking;

    private @Nullable Booking nextBooking;

    @AllArgsConstructor
    @Getter
    @Setter
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    public static class Booking {

        @EqualsAndHashCode.Include
        private long id;

        private long bookerId;
    }
}
