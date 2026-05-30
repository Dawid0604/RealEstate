/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import pl.dawid0604.realestate.domain.shared.exception.InvalidArgumentValueException;

class LocalityTest {

    @Nested
    final class CreateTests {

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("Should throw exception when name is blank")
        void shouldThrowExceptionWhenNameIsBlank(final String name) {
            // Given
            // When
            // Then
            Assertions.assertThatThrownBy(() -> Locality.create(name))
                    .isExactlyInstanceOf(InvalidArgumentValueException.class)
                    .hasMessage("Name cannot be blank");
        }

        @Test
        @DisplayName("Should create instance successfully and return same value")
        void shouldCreateInstanceSuccessfullyAndReturnSameValue() {
            // Given
            final String name = "Warsaw";

            // When
            final Locality instance = Locality.create(name);

            // Then
            Assertions.assertThat(instance.getId()).isNotNull();
            Assertions.assertThat(instance.getName()).isEqualTo(name);
        }
    }

    @Nested
    final class ReconstituteTests {

        @Test
        @DisplayName("Should throw exception when id is null")
        void shouldThrowExceptionWhenIdIsNull() {
            // Given
            // When
            // Then
            Assertions.assertThatThrownBy(() -> Locality.reconstitute(null, "abc"))
                    .isExactlyInstanceOf(InvalidArgumentValueException.class)
                    .hasMessage("Id cannot be null");
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("Should throw exception when name is blank")
        void shouldThrowExceptionWhenNameIsBlank(final String name) {
            // Given
            // When
            // Then
            Assertions.assertThatThrownBy(() -> Locality.reconstitute(Identifier.generate(), name))
                    .isExactlyInstanceOf(InvalidArgumentValueException.class)
                    .hasMessage("Name cannot be blank");
        }

        @Test
        @DisplayName("Should reconstitute instance successfully and return same value")
        void shouldReconstituteInstanceSuccessfullyAndReturnSameValue() {
            // Given
            final Identifier id = Identifier.generate();
            final String name = "Warsaw";

            // When
            final Locality instance = Locality.reconstitute(id, name);

            // Then
            Assertions.assertThat(instance.getId()).isEqualTo(id);
            Assertions.assertThat(instance.getName()).isEqualTo(name);
        }
    }
}
