/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import pl.dawid0604.realestate.domain.shared.exception.InvalidArgumentValueException;

import java.util.UUID;

class IdentifierTest {

    @Test
    @DisplayName("Should reconstitute identifier successfully")
    void shouldReconstituteIdentifier() {
        // Given
        final UUID uuid = UUID.randomUUID();

        // When
        final Identifier identifier = Identifier.of(uuid);

        // Then
        Assertions.assertThat(identifier.getValue()).isEqualTo(uuid);
    }

    @Test
    @DisplayName("Should generate identifier successfully")
    void shouldGenerateIdentifier() {
        // Given
        // When
        final Identifier identifier = Identifier.generate();

        // Then
        Assertions.assertThat(identifier.getValue()).isNotNull();
    }

    @Test
    @DisplayName("Should generate unique identifiers")
    void shouldGenerateUniqueIdentifiers() {
        // Given
        // When
        final Identifier identifier = Identifier.generate();
        final Identifier identifier2 = Identifier.generate();

        // Then
        Assertions.assertThat(identifier.getValue()).isNotEqualTo(identifier2.getValue());
    }

    @Test
    @DisplayName("Identifiers should be equal")
    void identifiersShouldBeEqual() {
        // Given
        final UUID value = UUID.randomUUID();

        // When
        final Identifier identifier = Identifier.of(value);
        final Identifier identifier2 = Identifier.of(value);

        // Then
        Assertions.assertThat(identifier).isEqualTo(identifier2);
    }

    @Test
    @DisplayName("Should throw exception while reconstituting when value is null")
    void shouldThrowExceptionWhileReconstitutingWhenValueIsNull() {
        // Given
        // When
        // Then
        Assertions.assertThatThrownBy(() -> Identifier.of(null))
                .isExactlyInstanceOf(InvalidArgumentValueException.class)
                .hasMessage("Value cannot be null");
    }
}
