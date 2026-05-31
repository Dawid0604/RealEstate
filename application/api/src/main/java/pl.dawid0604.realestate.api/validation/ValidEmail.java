/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.api.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.RECORD_COMPONENT;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Retention(RUNTIME)
@Constraint(validatedBy = {})
@Target({FIELD, RECORD_COMPONENT})
@Email(message = "Email must be valid")
@NotBlank(message = "Email cannot be blank")
public @interface ValidEmail {

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
