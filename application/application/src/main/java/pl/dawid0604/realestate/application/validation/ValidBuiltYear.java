/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.RECORD_COMPONENT;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target({FIELD, RECORD_COMPONENT})
@Constraint(validatedBy = ValidBuiltYearValidatorImpl.class)
public @interface ValidBuiltYear {

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
