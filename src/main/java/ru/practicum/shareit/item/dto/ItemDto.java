package ru.practicum.shareit.item.dto;

import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ItemDto {

    @EqualsAndHashCode.Include
    private long id;

    private String name;

    private String description;

    private boolean available;

    private Long requestId;

    private Collection<CommentDto> comments;
}
