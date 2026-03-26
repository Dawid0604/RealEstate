/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import pl.dawid0604.realestate.domain.shared.exception.InvalidArgumentValueException;

class LocalityTest {

    @Test
    @DisplayName("Should throw exception when id is null")
    void shouldThrowExceptionWhenIdIsNull() {
        // Given
        // When
        // Then
        Assertions.assertThatThrownBy(() -> new Locality(null))
                .isExactlyInstanceOf(InvalidArgumentValueException.class)
                .hasMessage("Id cannot be null");
    }

    @Test
    @DisplayName("Should create instance successfully and return same value")
    void shouldCreateInstanceSuccessfullyAndReturnSameValue() {
        // Given
        final Identifier id = Identifier.generate();

        // When
        final Locality instance = new Locality(id);

        // Then
        Assertions.assertThat(instance.id()).isEqualTo(id);
    }
}
