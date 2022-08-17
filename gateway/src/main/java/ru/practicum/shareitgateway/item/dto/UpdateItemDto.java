package ru.practicum.shareitgateway.item.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareitgateway.item.validation.ValidItemPatch;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ValidItemPatch
public class UpdateItemDto {

    private String name;

    private String description;

    private Boolean available;
}
