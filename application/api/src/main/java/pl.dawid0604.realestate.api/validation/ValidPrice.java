/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.api.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.RECORD_COMPONENT;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Constraint(validatedBy = {})
@Target({FIELD, RECORD_COMPONENT})
@DecimalMin(value = "0.01", message = "Price must be greater than 0.01")
@Digits(integer = 6, fraction = 2, message = "Price can have maximum 2 decimal places")
@NotNull(message = "Price cannot be null") public @interface ValidPrice {

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
