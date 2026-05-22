/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.api.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.RECORD_COMPONENT;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.DecimalMin;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Constraint(validatedBy = {})
@Target({FIELD, RECORD_COMPONENT})
@DecimalMin(value = "0.01", message = "AreaFrom must be greater than 0.01")
public @interface ValidAreaFrom {

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
