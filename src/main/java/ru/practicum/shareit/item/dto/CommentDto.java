package ru.practicum.shareit.item.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CommentDto {

    @EqualsAndHashCode.Include
    private long id;

    private String authorName;

    private String text;

    private LocalDateTime created;
}
