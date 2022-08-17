package ru.practicum.shareitgateway.user.validation;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.Mockito.when;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareitgateway.user.dto.UpdateUserDto;

@SpringBootTest
class UserPatchValidatorTest {

    @MockBean
    private ConstraintValidatorContext context;

    private ConstraintValidator<ValidUserPatch, UpdateUserDto> validator;

    @BeforeEach
    void setupValidator() {
        validator = new UserPatchValidator();
        validator.initialize(Mockito.mock(ValidUserPatch.class));
    }

    @Test
    void givenNoData_whenValidate_thenNotValid() {
        // Given
        UpdateUserDto dto = Mockito.mock(UpdateUserDto.class);
        when(dto.getName()).thenReturn(null);
        when(dto.getEmail()).thenReturn(null);

        // When
        boolean result = validator.isValid(dto, context);

        // Then
        then(result).isFalse();
    }

    @Test
    void givenEmptyEmail_whenValidate_thenNotValid() {
        // Given
        UpdateUserDto dto = Mockito.mock(UpdateUserDto.class);
        when(dto.getName()).thenReturn(null);
        when(dto.getEmail()).thenReturn(" ");

        // When
        boolean result = validator.isValid(dto, context);

        // Then
        then(result).isFalse();
    }

    @Test
    void givenEmptyName_whenValidate_thenNotValid() {
        // Given
        UpdateUserDto dto = Mockito.mock(UpdateUserDto.class);
        when(dto.getName()).thenReturn(" ");
        when(dto.getEmail()).thenReturn(null);

        // When
        boolean result = validator.isValid(dto, context);

        // Then
        then(result).isFalse();
    }

    @Test
    void givenCorrectEmail_whenValidate_thenValid() {
        // Given
        UpdateUserDto dto = Mockito.mock(UpdateUserDto.class);
        when(dto.getEmail()).thenReturn("email@example.com");

        // When
        boolean result = validator.isValid(dto, context);

        // Then
        then(result).isTrue();
    }

    @Test
    void givenCorrectName_whenValidate_thenValid() {
        // Given
        UpdateUserDto dto = Mockito.mock(UpdateUserDto.class);
        when(dto.getName()).thenReturn("Name");

        // When
        boolean result = validator.isValid(dto, context);

        // Then
        then(result).isTrue();
    }
}