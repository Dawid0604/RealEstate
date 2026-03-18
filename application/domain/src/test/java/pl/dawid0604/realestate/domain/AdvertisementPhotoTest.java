/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import pl.dawid0604.realestate.domain.shared.exception.InvalidArgumentValueException;

class AdvertisementPhotoTest {

    @Test
    @DisplayName("Should throw exception when id is null")
    void shouldThrowExceptionWhenIdIsNull() {
        // Given
        // When
        // Then
        Assertions.assertThatThrownBy(() -> AdvertisementPhoto.of(null, null))
                .isExactlyInstanceOf(InvalidArgumentValueException.class)
                .hasMessage("Id cannot be null");
    }

    @Test
    @DisplayName("Should throw exception when url is null")
    void shouldThrowExceptionWhenUrlIsNull() {
        // Given
        // When
        // Then
        Assertions.assertThatThrownBy(() -> AdvertisementPhoto.of(Identifier.generate(), null))
                .isExactlyInstanceOf(InvalidArgumentValueException.class)
                .hasMessage("Url cannot be null");
    }

    @Test
    @DisplayName("Should reconstitute successfully and return same values")
    void shouldReconstituteInstanceSuccessfullyAndReturnSameValues() {
        // Given
        final Identifier identifier = Identifier.generate();
        final Url url = getValidUrl();

        // When
        final AdvertisementPhoto instance = AdvertisementPhoto.of(identifier, url);

        // Then
        Assertions.assertThat(instance.getId()).isEqualTo(identifier);
        Assertions.assertThat(instance.getUrl()).isEqualTo(url);
    }

    @Test
    @DisplayName("Should create successfully and return same values")
    void shouldCreateInstanceSuccessfullyAndReturnSameValues() {
        // Given
        final Url url = getValidUrl();

        // When
        final AdvertisementPhoto instance = AdvertisementPhoto.create(url);

        // Then
        Assertions.assertThat(instance.getId()).isNotNull();
        Assertions.assertThat(instance.getUrl()).isEqualTo(url);
    }

    private static Url getValidUrl() {
        return new Url("https://xyz");
    }
}
