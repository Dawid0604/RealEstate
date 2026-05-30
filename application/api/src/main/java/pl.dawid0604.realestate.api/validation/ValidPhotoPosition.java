/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.api.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.RECORD_COMPONENT;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Constraint(validatedBy = {})
@Target({FIELD, RECORD_COMPONENT})
@NotNull(message = "Position cannot be null") @PositiveOrZero(message = "Position cannot be lower than 0")
@Max(value = 20, message = "Position cannot be greater than 20")
public @interface ValidPhotoPosition {

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
