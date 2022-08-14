package ru.practicum.shareitgateway.user.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserPatchValidator.class)
public @interface ValidUserPatch {

    String message() default "invalid object";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}