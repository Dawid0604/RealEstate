/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.validation;

import java.math.BigDecimal;
import java.util.stream.Stream;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class ValidAreaTests {
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

    record AreaWrapper(@ValidArea BigDecimal area) {}

    @ParameterizedTest
    @MethodSource("validAreaDataProvider")
    @DisplayName("Should pass for valid area")
    void shouldPassForValidArea(final BigDecimal value) {
        // Given
        final AreaWrapper areaWrapper = new AreaWrapper(value);

        // When
        final var violations = validator.validate(areaWrapper);

        // Then
        Assertions.assertThat(violations).isEmpty();
    }

    @ParameterizedTest
    @MethodSource("invalidAreaDataProvider")
    @DisplayName("Should fail for invalid area")
    void shouldFailForInvalidArea(final BigDecimal value) {
        // Given
        final AreaWrapper areaWrapper = new AreaWrapper(value);

        // When
        final var violations = validator.validate(areaWrapper);

        // Then
        Assertions.assertThat(violations)
                .anyMatch(
                        v ->
                                v.getPropertyPath().toString().equals("area")
                                        && v.getMessage().equals("Area must be greater than 0.01"));
    }

    private static Stream<Arguments> invalidAreaDataProvider() {
        return Stream.of(
                Arguments.of(BigDecimal.ZERO),
                Arguments.of(BigDecimal.valueOf(0.001)),
                Arguments.of(BigDecimal.valueOf(-0.25)));
    }

    private static Stream<Arguments> validAreaDataProvider() {
        return Stream.of(
                Arguments.of(BigDecimal.ONE),
                Arguments.of(BigDecimal.valueOf(1_500)),
                Arguments.of(BigDecimal.valueOf(1_500_00.25)));
    }
}
