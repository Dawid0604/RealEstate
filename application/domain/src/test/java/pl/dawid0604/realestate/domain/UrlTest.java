/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

import java.util.stream.Stream;

import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import pl.dawid0604.realestate.domain.shared.exception.InvalidArgumentValueException;

class UrlTest {

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    @DisplayName("Should throw exception when value is blank")
    void shouldThrowExceptionWhenValueIsBlank(final String value) {
        // Given
        // When
        // Then
        Assertions.assertThatThrownBy(() -> new Url(value))
                .isExactlyInstanceOf(InvalidArgumentValueException.class)
                .hasMessage("Value cannot be blank");
    }

    @Test
    @DisplayName("Should throw exception when value is too long")
    void shouldThrowExceptionWhenValueIsToLong() {
        // Given
        final String value = RandomStringUtils.secure().nextAlphanumeric(1025);

        // When
        // Then
        Assertions.assertThatThrownBy(() -> new Url(value))
                .isExactlyInstanceOf(InvalidArgumentValueException.class)
                .hasMessageContaining("Value cannot be longer than ");
    }

    @ParameterizedTest
    @ValueSource(
            strings = {
                "xyz",
                "http",
                "http:",
                "http//",
                "https",
                "https:",
                "https//",
                "xyz:http",
                "xyz:https"
            })
    @DisplayName("Should throw exception when value is invalid")
    void shouldThrowExceptionWhenValueIsInvalid(final String value) {
        // Given
        // When
        // Then
        Assertions.assertThatThrownBy(() -> new Url(value))
                .isExactlyInstanceOf(InvalidArgumentValueException.class)
                .hasMessage("Value is invalid");
    }

    @ParameterizedTest
    @ValueSource(strings = {"https://xyz", "http://xyz"})
    @DisplayName("Should create instance successfully and return same value")
    void shouldCreateInstanceSuccessfullyAndReturnSameValue(final String value) {
        // Given
        // When
        final Url url = new Url(value);

        // Then
        Assertions.assertThat(url.value()).isEqualTo(value);
    }

    @ParameterizedTest
    @MethodSource("shouldCreateInstanceSuccessfullyAndRemoveSpacesDataProvider")
    @DisplayName("Should create instance successfully and return same value")
    void shouldCreateInstanceSuccessfullyAndRemoveSpaces(
            final String value, final String expectedValue) {

        // Given
        // When
        final Url url = new Url(value);

        // Then
        Assertions.assertThat(url.value()).isEqualTo(expectedValue);
    }

    private static Stream<Arguments> shouldCreateInstanceSuccessfullyAndRemoveSpacesDataProvider() {
        return Stream.of(
                Arguments.of("http:// abc.eu", "http://abc.eu"),
                Arguments.of("http://    abc.eu", "http://abc.eu"),
                Arguments.of(" http://abc . eu ", "http://abc.eu"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"http://", "https://"})
    @DisplayName("Should create instance successfully at boundary value")
    void shouldCreateInstanceSuccessfullyAtBoundaryValue(final String protocol) {
        // Given
        final String value =
                protocol + RandomStringUtils.secure().nextAlphanumeric(1024 - protocol.length());

        // When
        final Url url = new Url(value);

        // Then
        Assertions.assertThat(url.value()).isEqualTo(value);
    }
}
