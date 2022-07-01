package ru.practicum.shareit.item.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ItemDto {

    @EqualsAndHashCode.Include
    private final long id;

    @NotNull
    @NotBlank
    private final String name;

    @NotNull
    @NotBlank
    private final String description;

    private final boolean available;
}
