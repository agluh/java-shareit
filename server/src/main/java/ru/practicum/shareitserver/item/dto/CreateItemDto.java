package ru.practicum.shareitserver.item.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class CreateItemDto {

    private String name;

    private String description;

    private Boolean available;

    private Long requestId;
}
