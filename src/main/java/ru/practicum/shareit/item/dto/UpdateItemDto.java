package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.validation.ValidItemPatch;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ValidItemPatch
public class UpdateItemDto {

    @Setter
    @JsonIgnore
    private long itemId;

    @Setter
    @JsonIgnore
    private long userId;

    private final String name;

    private final String description;

    private final Boolean available;
}
