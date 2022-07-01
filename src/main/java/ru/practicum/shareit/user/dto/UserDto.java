package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import ru.practicum.shareit.user.validation.ValidUserPatch;

@AllArgsConstructor
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ValidUserPatch
public class UserDto {

    @EqualsAndHashCode.Include
    private final long id;

    private final String name;

    private final String email;
}
