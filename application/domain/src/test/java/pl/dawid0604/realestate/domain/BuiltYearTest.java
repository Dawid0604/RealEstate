/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import pl.dawid0604.realestate.domain.shared.exception.InvalidArgumentValueException;

class BuiltYearTest {

    @Test
    @DisplayName("Should create instance successfully when value is null")
    void shouldCreateInstanceSuccessfullyWhenValueIsNull() {
        // Given
        // When
        final BuiltYear instance = new BuiltYear(null);

        // Then
        Assertions.assertThat(instance.value()).isNull();
    }

    @Test
    @DisplayName("Should create instance successfully and return same value")
    void shouldCreateInstanceSuccessfullyAndReturnSameValue() {
        // Given
        final int builtYear = 2010;

        // When
        final BuiltYear instance = new BuiltYear(builtYear);

        // Then
        Assertions.assertThat(instance.value()).isEqualTo(builtYear);
    }

    @Test
    @DisplayName("Should create instance successfully at boundary value")
    void shouldCreateInstanceSuccessfullyAtBoundaryValue() {
        // Given
        final int builtYear = 1900;

        // When
        final BuiltYear instance = new BuiltYear(builtYear);

        // Then
        Assertions.assertThat(instance.value()).isEqualTo(builtYear);
    }

    @ParameterizedTest
    @ValueSource(ints = {-100, 0, 1, 100, 1800, 1899})
    @DisplayName("Should throw exception when value is lower than minimum")
    void shouldThrowExceptionWhenValueIsLowerThanMinimum(final int builtYear) {
        // Given
        // When
        // Then
        Assertions.assertThatThrownBy(() -> new BuiltYear(builtYear))
                .isExactlyInstanceOf(InvalidArgumentValueException.class)
                .hasMessageContaining("Built year cannot be before ");
    }
}
