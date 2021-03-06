package ru.practicum.shareit.item.dto;

import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ItemDto {

    @EqualsAndHashCode.Include
    private final long id;

    private final String name;

    private final String description;

    private final boolean available;

    private final Collection<CommentDto> comments;
}
