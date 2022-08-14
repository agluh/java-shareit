package ru.practicum.shareitserver.item.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UpdateItemDto {

    @Setter
    @JsonIgnore
    @EqualsAndHashCode.Include
    private long itemId;

    private String name;

    private String description;

    private Boolean available;
}
