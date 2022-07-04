package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import ru.practicum.shareit.item.validation.ValidItemPatch;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ValidItemPatch
public class UpdateItemDto {

    private final String name;

    private final String description;

    private final Boolean available;
}
