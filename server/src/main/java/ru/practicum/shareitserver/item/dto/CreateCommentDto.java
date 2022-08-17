package ru.practicum.shareitserver.item.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class CreateCommentDto {

    @JsonIgnore
    @Setter
    private long itemId;

    private String text;
}
