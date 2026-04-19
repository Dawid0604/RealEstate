/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.validation;

import java.time.LocalDate;
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

public class ValidBuiltYearTests {
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

    record BuiltYearWrapper(@ValidBuiltYear Integer builtYear) {}

    @ParameterizedTest
    @MethodSource("invalidBuiltYearDataProvider")
    @DisplayName("Should fail for invalid builtYear")
    void shouldFailForInvalidBuiltYear(final Integer value, final String expectedMessage) {
        // Given
        final BuiltYearWrapper builtYearWrapper = new BuiltYearWrapper(value);

        // When
        final var violations = validator.validate(builtYearWrapper);

        // Then
        Assertions.assertThat(violations)
                .anyMatch(
                        v ->
                                v.getPropertyPath().toString().equals("builtYear")
                                        && v.getMessage().equals(expectedMessage));
    }

    @ParameterizedTest
    @MethodSource("validBuiltYearDataProvider")
    @DisplayName("Should pass for valid builtYear")
    void shouldPassForValidBuiltYear(final Integer value) {
        // Given
        final BuiltYearWrapper builtYearWrapper = new BuiltYearWrapper(value);

        // When
        final var violations = validator.validate(builtYearWrapper);

        // Then
        Assertions.assertThat(violations).isEmpty();
    }

    private static Stream<Arguments> validBuiltYearDataProvider() {
        final int currentYear = LocalDate.now().getYear();

        return Stream.of(
                Arguments.of((Integer) null),
                Arguments.of(1800),
                Arguments.of(1900),
                Arguments.of(2011),
                Arguments.of(currentYear),
                Arguments.of(currentYear + 1),
                Arguments.of(currentYear + 2));
    }

    private static Stream<Arguments> invalidBuiltYearDataProvider() {
        final int currentYear = LocalDate.now().getYear();

        return Stream.of(
                Arguments.of(0, "BuiltYear cannot be before 1800"),
                Arguments.of(1, "BuiltYear cannot be before 1800"),
                Arguments.of(-1, "BuiltYear cannot be before 1800"),
                Arguments.of(-1800, "BuiltYear cannot be before 1800"),
                Arguments.of(1799, "BuiltYear cannot be before 1800"),
                Arguments.of(currentYear + 3, "BuiltYear cannot be after " + (currentYear + 2)),
                Arguments.of(currentYear + 5, "BuiltYear cannot be after " + (currentYear + 2)));
    }
}
