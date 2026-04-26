/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import pl.dawid0604.realestate.domain.shared.exception.InvalidArgumentValueException;

class NumberOfRoomsTest {

    @Test
    @DisplayName("Should create instance successfully when value is null")
    void shouldCreateInstanceSuccessfullyWhenValueIsNull() {
        // Given
        // When
        final NumberOfRooms instance = new NumberOfRooms(null);

        // Then
        Assertions.assertThat(instance.value()).isNull();
    }

    @Test
    @DisplayName("Should create instance successfully when value is digit and return same value")
    void shouldCreateInstanceSuccessfullyWhenValueIsDigitAndReturnSameValue() {
        // Given
        final int value = 4;

        // When
        final NumberOfRooms instance = new NumberOfRooms(value);

        // Then
        Assertions.assertThat(instance.value()).isEqualTo(value);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 100})
    @DisplayName("Should create instance successfully with boundary values")
    void shouldCreateInstanceSuccessfullyWithBoundaryValues(final int value) {
        // Given
        // When
        // Then
        Assertions.assertThatCode(() -> new NumberOfRooms(value)).doesNotThrowAnyException();
    }

    @ParameterizedTest
    @ValueSource(ints = {-100, -1, 0, 101})
    @DisplayName("Should throw exception when value is invalid")
    void shouldThrowExceptionWhenValueIsInvalid(final int value) {
        // Given
        // When
        // Then
        Assertions.assertThatThrownBy(() -> new NumberOfRooms(value))
                .isExactlyInstanceOf(InvalidArgumentValueException.class)
                .hasMessageStartingWith("NumberOfRooms must be between");
    }
}
