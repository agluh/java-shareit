package ru.practicum.shareitgateway.item.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import ru.practicum.shareitgateway.item.dto.UpdateItemDto;

public class ItemPatchValidator implements ConstraintValidator<ValidItemPatch, UpdateItemDto> {

    @Override
    public boolean isValid(UpdateItemDto patch, ConstraintValidatorContext context) {
        if (patch.getName() == null
            && patch.getDescription() == null
            && patch.getAvailable() == null) {
            return false;
        }

        if (patch.getName() != null && patch.getName().isBlank()) {
            return false;
        }

        if (patch.getDescription() != null && patch.getDescription().isBlank()) {
            return false;
        }

        return true;
    }
}
