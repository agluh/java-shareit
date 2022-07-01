package ru.practicum.shareit.user.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import ru.practicum.shareit.user.dto.UserDto;

public class UserPatchValidator implements ConstraintValidator<ValidUserPatch, UserDto> {

    @Override
    public boolean isValid(UserDto patch, ConstraintValidatorContext context) {
        if (patch.getEmail() == null && patch.getName() == null) {
            return false;
        }

        if (patch.getEmail() != null && patch.getEmail().isBlank()) {
            return false;
        }

        if (patch.getName() != null && patch.getName().isBlank()) {
            return false;
        }

        return true;
    }
}
