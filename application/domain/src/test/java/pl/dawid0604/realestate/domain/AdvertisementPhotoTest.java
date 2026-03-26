/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import pl.dawid0604.realestate.domain.shared.exception.InvalidArgumentValueException;

class AdvertisementPhotoTest {

    @Test
    @DisplayName("Should throw exception when id is null")
    void shouldThrowExceptionWhenIdIsNull() {
        // Given
        // When
        // Then
        Assertions.assertThatThrownBy(() -> AdvertisementPhoto.of(null, null, 0))
                .isExactlyInstanceOf(InvalidArgumentValueException.class)
                .hasMessage("Id cannot be null");
    }

    @Test
    @DisplayName("Should throw exception when url is null")
    void shouldThrowExceptionWhenUrlIsNull() {
        // Given
        // When
        // Then
        Assertions.assertThatThrownBy(() -> AdvertisementPhoto.of(Identifier.generate(), null, 0))
                .isExactlyInstanceOf(InvalidArgumentValueException.class)
                .hasMessage("Url cannot be null");
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -2, -3})
    @DisplayName("Should throw exception when position is negative")
    void shouldThrowExceptionWhenPositionIsNegative(final int position) {
        // Given
        // When
        // Then
        Assertions.assertThatThrownBy(
                        () -> AdvertisementPhoto.of(Identifier.generate(), getValidUrl(), position))
                .isExactlyInstanceOf(InvalidArgumentValueException.class)
                .hasMessage("Position cannot be negative");
    }

    @Test
    @DisplayName("Should reconstitute successfully and return same values")
    void shouldReconstituteInstanceSuccessfullyAndReturnSameValues() {
        // Given
        final Identifier identifier = Identifier.generate();
        final Url url = getValidUrl();
        final int position = 1;

        // When
        final AdvertisementPhoto instance = AdvertisementPhoto.of(identifier, url, position);

        // Then
        Assertions.assertThat(instance.getId()).isEqualTo(identifier);
        Assertions.assertThat(instance.getUrl()).isEqualTo(url);
        Assertions.assertThat(instance.getPosition()).isEqualTo(position);
    }

    @Test
    @DisplayName("Should create successfully and return same values")
    void shouldCreateInstanceSuccessfullyAndReturnSameValues() {
        // Given
        final Url url = getValidUrl();
        final int position = 1;

        // When
        final AdvertisementPhoto instance = AdvertisementPhoto.create(url, position);

        // Then
        Assertions.assertThat(instance.getId()).isNotNull();
        Assertions.assertThat(instance.getUrl()).isEqualTo(url);
        Assertions.assertThat(instance.getPosition()).isEqualTo(position);
    }

    @Test
    @DisplayName("Should create successfully at boundary values")
    void shouldCreateInstanceSuccessfullyAtBoundaryValues() {
        // Given
        final Url url = getValidUrl();
        final int position = 0;

        // When
        // Then
        Assertions.assertThatCode(() -> AdvertisementPhoto.create(url, position))
                .doesNotThrowAnyException();
    }

    private static Url getValidUrl() {
        return new Url("https://xyz");
    }
}
