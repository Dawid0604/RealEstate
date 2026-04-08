/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.validation;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

public class ValidFloorsTests {
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

    record FloorsWrapper(@ValidFloors Integer floors) {}

    @NullSource
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3})
    @DisplayName("Should pass for valid floors")
    void shouldPassForValidFloors(final Integer value) {
        // Given
        final FloorsWrapper floorsWrapper = new FloorsWrapper(value);

        // When
        final var violations = validator.validate(floorsWrapper);

        // Then
        Assertions.assertThat(violations).isEmpty();
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -2, -3, -100})
    @DisplayName("Should fail for invalid floors")
    void shouldFailForInvalidFloors(final Integer value) {
        // Given
        final FloorsWrapper floorsWrapper = new FloorsWrapper(value);

        // When
        final var violations = validator.validate(floorsWrapper);

        // Then
        Assertions.assertThat(violations)
                .anyMatch(
                        v ->
                                v.getPropertyPath().toString().equals("floors")
                                        && v.getMessage().equals("Floors cannot be lower than 0"));
    }
}
