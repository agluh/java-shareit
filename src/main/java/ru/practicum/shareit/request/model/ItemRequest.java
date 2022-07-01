package ru.practicum.shareit.request.model;

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
public class ItemRequest {

    @EqualsAndHashCode.Include
    @Setter(AccessLevel.NONE)
    private Long id;

    private String description;

    private long requesterId;

    private LocalDateTime createdAt;
}
