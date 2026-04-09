/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.validation;

import java.util.UUID;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ValidLocalityIdTests {
    private static Validator validator;
    private static ValidatorFactory validatorFactory;

    @BeforeAll
    static void setUp() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    static void tearDown() {
        validatorFactory.close();
    }

    record LocalityIdWrapper(@ValidLocalityId UUID localityId) {}

    @Test
    @DisplayName("Should pass for valid localityId")
    void shouldPassForValidFloor() {
        // Given
        final LocalityIdWrapper localityIdWrapper = new LocalityIdWrapper(UUID.randomUUID());

        // When
        final var violations = validator.validate(localityIdWrapper);

        // Then
        Assertions.assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Should fail for invalid localityId")
    void shouldFailForInvalidLocalityId() {
        // Given
        final LocalityIdWrapper localityIdWrapper = new LocalityIdWrapper(null);

        // When
        final var violations = validator.validate(localityIdWrapper);

        // Then
        Assertions.assertThat(violations)
                .anyMatch(
                        v ->
                                v.getPropertyPath().toString().equals("localityId")
                                        && v.getMessage().equals("LocalityId cannot be null"));
    }
}
