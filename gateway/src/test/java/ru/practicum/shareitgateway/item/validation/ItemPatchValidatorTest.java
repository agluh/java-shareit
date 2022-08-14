package ru.practicum.shareitgateway.item.validation;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.Mockito.when;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareitgateway.item.dto.UpdateItemDto;

@SpringBootTest
class ItemPatchValidatorTest {

    @MockBean
    private ConstraintValidatorContext context;

    private ConstraintValidator<ValidItemPatch, UpdateItemDto> validator;

    @BeforeEach
    void setupValidator() {
        validator = new ItemPatchValidator();
        validator.initialize(Mockito.mock(ValidItemPatch.class));
    }

    @Test
    void givenNoData_whenValidate_thenNotValid() {
        // Given
        UpdateItemDto dto = Mockito.mock(UpdateItemDto.class);
        when(dto.getName()).thenReturn(null);
        when(dto.getDescription()).thenReturn(null);
        when(dto.getAvailable()).thenReturn(null);

        // When
        boolean result = validator.isValid(dto, context);

        // Then
        then(result).isFalse();
    }

    @Test
    void givenEmptyName_whenValidate_thenNotValid() {
        // Given
        UpdateItemDto dto = Mockito.mock(UpdateItemDto.class);
        when(dto.getAvailable()).thenReturn(null);
        when(dto.getDescription()).thenReturn(null);
        when(dto.getName()).thenReturn(" ");

        // When
        boolean result = validator.isValid(dto, context);

        // Then
        then(result).isFalse();
    }

    @Test
    void givenEmptyDescription_whenValidate_thenNotValid() {
        // Given
        UpdateItemDto dto = Mockito.mock(UpdateItemDto.class);
        when(dto.getAvailable()).thenReturn(null);
        when(dto.getName()).thenReturn(null);
        when(dto.getDescription()).thenReturn(" ");

        // When
        boolean result = validator.isValid(dto, context);

        // Then
        then(result).isFalse();
    }

    @Test
    void givenCorrectDescription_whenValidate_thenValid() {
        // Given
        UpdateItemDto dto = Mockito.mock(UpdateItemDto.class);
        when(dto.getDescription()).thenReturn("Description");

        // When
        boolean result = validator.isValid(dto, context);

        // Then
        then(result).isTrue();
    }

    @Test
    void givenCorrectName_whenValidate_thenValid() {
        // Given
        UpdateItemDto dto = Mockito.mock(UpdateItemDto.class);
        when(dto.getName()).thenReturn("Name");

        // When
        boolean result = validator.isValid(dto, context);

        // Then
        then(result).isTrue();
    }

    @Test
    void givenCorrectAvailable_whenValidate_thenValid() {
        // Given
        UpdateItemDto dto = Mockito.mock(UpdateItemDto.class);
        when(dto.getAvailable()).thenReturn(true);

        // When
        boolean result = validator.isValid(dto, context);

        // Then
        then(result).isTrue();
    }
}