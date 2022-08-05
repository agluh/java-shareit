package ru.practicum.shareit.request.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ItemRequestDto {

    @EqualsAndHashCode.Include
    private long id;

    private String description;

    private LocalDateTime created;

    private List<Item> items;

    @AllArgsConstructor
    @Getter
    @Setter
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    public static class Item {

        @EqualsAndHashCode.Include
        private long id;

        private String name;

        private String description;

        private boolean available;

        private long requestId;
    }
}
