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

class AdvertisementClaimTest {

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    @DisplayName("Should throw exception when key is invalid")
    void shouldThrowExceptionWhenKeyIsInvalid(final String key) {
        // Given
        // When
        // Then
        Assertions.assertThatThrownBy(() -> new AdvertisementClaim(key, "anyValue"))
                .isExactlyInstanceOf(InvalidArgumentValueException.class)
                .hasMessage("Key cannot be blank");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    @DisplayName("Should throw exception when value is invalid")
    void shouldThrowExceptionWhenValueIsInvalid(final String value) {
        // Given
        // When
        // Then
        Assertions.assertThatThrownBy(() -> new AdvertisementClaim("anyKey", value))
                .isExactlyInstanceOf(InvalidArgumentValueException.class)
                .hasMessage("Value cannot be blank");
    }

    @ParameterizedTest
    @DisplayName("Should throw exception when key is too long")
    @MethodSource("shouldThrowExceptionWhenKeyIsTooLongDataProvider")
    void shouldThrowExceptionWhenKeyIsTooLong(final String key) {
        // Given
        // When
        // Then
        Assertions.assertThatThrownBy(() -> new AdvertisementClaim(key, "anyValue"))
                .isExactlyInstanceOf(InvalidArgumentValueException.class)
                .hasMessageStartingWith("Key cannot be longer than ");
    }

    @ParameterizedTest
    @DisplayName("Should throw exception when value is too long")
    @MethodSource("shouldThrowExceptionWhenValueIsTooLongDataProvider")
    void shouldThrowExceptionWhenValueIsTooLong(final String value) {
        // Given
        // When
        // Then
        Assertions.assertThatThrownBy(() -> new AdvertisementClaim("anyKey", value))
                .isExactlyInstanceOf(InvalidArgumentValueException.class)
                .hasMessageStartingWith("Value cannot be longer than ");
    }

    @Test
    @DisplayName("Should create instance successfully and get same values")
    void shouldCreateInstanceSuccessfullyAndGetSameValues() {
        // Given
        final String key = "xyz";
        final String value = "abc";

        // When
        final AdvertisementClaim instance = new AdvertisementClaim(key, value);

        // Then
        Assertions.assertThat(instance.key()).isEqualTo(key);
        Assertions.assertThat(instance.value()).isEqualTo(value);
    }

    @Test
    @DisplayName("Should create instance successfully at boundary values")
    void shouldCreateInstanceSuccessfullyAtBoundaryValues() {
        // Given
        final String key = RandomStringUtils.secure().nextAlphanumeric(100);
        final String value = RandomStringUtils.secure().nextAlphanumeric(1024);

        // When
        // Then
        Assertions.assertThatCode(() -> new AdvertisementClaim(key, value))
                .doesNotThrowAnyException();
    }

    private static Stream<Arguments> shouldThrowExceptionWhenKeyIsTooLongDataProvider() {
        return Stream.of(
                Arguments.of(RandomStringUtils.secure().nextAlphanumeric(101)),
                Arguments.of(RandomStringUtils.secure().nextAlphanumeric(121)));
    }

    private static Stream<Arguments> shouldThrowExceptionWhenValueIsTooLongDataProvider() {
        return Stream.of(
                Arguments.of(RandomStringUtils.secure().nextAlphanumeric(1025)),
                Arguments.of(RandomStringUtils.secure().nextAlphanumeric(1225)));
    }
}
