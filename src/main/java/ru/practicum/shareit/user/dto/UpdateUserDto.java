package ru.practicum.shareit.user.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.user.validation.ValidUserPatch;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ValidUserPatch
public class UpdateUserDto {

    private String name;

    private String email;
}
