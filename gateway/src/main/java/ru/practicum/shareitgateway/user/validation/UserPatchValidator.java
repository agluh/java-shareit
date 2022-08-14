package ru.practicum.shareitgateway.user.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import ru.practicum.shareitgateway.user.dto.UpdateUserDto;

public class UserPatchValidator implements ConstraintValidator<ValidUserPatch, UpdateUserDto> {

    @Override
    public boolean isValid(UpdateUserDto patch, ConstraintValidatorContext context) {
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
