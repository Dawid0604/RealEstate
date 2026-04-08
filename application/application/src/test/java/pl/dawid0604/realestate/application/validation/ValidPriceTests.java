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
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.stream.Stream;

public class ValidPriceTests {
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

    record PriceWrapper(@ValidPrice BigDecimal price) {}

    @ParameterizedTest
    @MethodSource("validPriceDataProvider")
    @DisplayName("Should pass for valid price")
    void shouldPassForValidPrice(final BigDecimal value) {
        // Given
        final PriceWrapper priceWrapper = new PriceWrapper(value);

        // When
        final var violations = validator.validate(priceWrapper);

        // Then
        Assertions.assertThat(violations).isEmpty();
    }

    @ParameterizedTest
    @MethodSource("invalidPriceDataProvider")
    @DisplayName("Should fail for invalid price")
    void shouldFailForInvalidPrice(final BigDecimal value) {
        // Given
        final PriceWrapper priceWrapper = new PriceWrapper(value);

        // When
        final var violations = validator.validate(priceWrapper);

        // Then
        Assertions.assertThat(violations)
                .anyMatch(
                        v ->
                                v.getPropertyPath().toString().equals("price")
                                        && v.getMessage()
                                                .equals("Price must be greater than 0.01"));
    }

    private static Stream<Arguments> invalidPriceDataProvider() {
        return Stream.of(
                Arguments.of(BigDecimal.ZERO),
                Arguments.of(BigDecimal.valueOf(0.001)),
                Arguments.of(BigDecimal.valueOf(-0.25)));
    }

    private static Stream<Arguments> validPriceDataProvider() {
        return Stream.of(
                Arguments.of(BigDecimal.ONE),
                Arguments.of(BigDecimal.valueOf(1_500)),
                Arguments.of(BigDecimal.valueOf(1_500_00.25)));
    }
}
