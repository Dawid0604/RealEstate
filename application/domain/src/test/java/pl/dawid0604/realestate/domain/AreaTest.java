/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.dawid0604.realestate.domain.shared.exception.InvalidArgumentValueException;

import java.math.BigDecimal;
import java.util.stream.Stream;

class AreaTest {

    @Test
    @DisplayName("Should create instance successfully when value is null")
    void shouldCreateInstanceSuccessfullyWhenValueIsNull() {
        // Given
        // When
        final Area instance = new Area(null);

        // Then
        Assertions.assertThat(instance.value()).isNull();
    }

    @Test
    @DisplayName("Should create instance successfully when value is digit and return same value")
    void shouldCreateInstanceSuccessfullyWhenValueIsDigitAndReturnSameValue() {
        // Given
        final BigDecimal value = new BigDecimal("50.25");

        // When
        final Area instance = new Area(value);

        // Then
        Assertions.assertThat(instance.value()).isEqualByComparingTo(value);
    }

    @ParameterizedTest
    @DisplayName("Should throw exception when value is lower than one")
    @MethodSource("shouldThrowExceptionWhenValueIsLowerThanOneDataProvider")
    void shouldThrowExceptionWhenValueIsLowerThanOne(final BigDecimal value) {
        // Given
        // When
        // Then
        Assertions.assertThatThrownBy(() -> new Area(value))
                .isExactlyInstanceOf(InvalidArgumentValueException.class)
                .hasMessage("Area must be greater than zero");
    }

    private static Stream<Arguments> shouldThrowExceptionWhenValueIsLowerThanOneDataProvider() {
        return Stream.of(
                Arguments.of(BigDecimal.ZERO),
                Arguments.of("0.0"),
                Arguments.of("-0.25"),
                Arguments.of(BigDecimal.valueOf(-1)));
    }
}
