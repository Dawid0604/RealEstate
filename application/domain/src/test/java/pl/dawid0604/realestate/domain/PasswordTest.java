/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

import java.util.Locale;
import java.util.stream.Stream;

import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import pl.dawid0604.realestate.domain.shared.exception.InvalidArgumentValueException;

class PasswordTest {

    @Nested
    final class PlainTests {

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"   ", "\t", "\n"})
        @DisplayName("Should throw exception when value is blank")
        void shouldThrowExceptionWhenValueIsBlank(final String value) {
            // Given
            // When
            // Then
            Assertions.assertThatThrownBy(() -> Password.ofPlain(value))
                    .isExactlyInstanceOf(InvalidArgumentValueException.class)
                    .hasMessage("Value cannot be blank");
        }

        @Test
        @DisplayName("Should throw exception when value is too sort")
        void shouldThrowExceptionWhenValueIsTooShort() {
            // Given
            final String value = RandomStringUtils.secure().nextAlphabetic(5);

            // When
            // Then
            Assertions.assertThatThrownBy(() -> Password.ofPlain(value))
                    .isExactlyInstanceOf(InvalidArgumentValueException.class)
                    .hasMessageStartingWith("Value cannot be less than ");
        }

        @Test
        @DisplayName("Should throw exception when value is too long")
        void shouldThrowExceptionWhenValueIsTooLong() {
            // Given
            final String value = RandomStringUtils.secure().nextAlphabetic(75);

            // When
            // Then
            Assertions.assertThatThrownBy(() -> Password.ofPlain(value))
                    .isExactlyInstanceOf(InvalidArgumentValueException.class)
                    .hasMessageStartingWith("Value cannot be longer than ");
        }

        @ParameterizedTest
        @ValueSource(
                strings = {
                    "secret123!",
                    "password1@",
                    "abcdef1!",
                    "SECRET123!",
                    "PASSWORD1@",
                    "ABCDEF1!",
                    "Secret!!!",
                    "Password@",
                    "HelloWorld!",
                    "Secret123",
                    "Password1",
                    "HelloWorld1",
                    "Secret123#",
                    "Secret123^",
                    "Secret123(",
                    "Secret123)",
                    "Secret123+",
                    "Secret123="
                })
        @DisplayName("Should throw exception when value strength is too weak")
        void shouldThrowExceptionWhenValueStrengthIsTooWeak(final String value) {
            // Given
            // When
            // Then
            Assertions.assertThatThrownBy(() -> Password.ofPlain(value))
                    .isExactlyInstanceOf(InvalidArgumentValueException.class)
                    .hasMessage(
                            "Password must contain uppercase, lowercase, digit and special character");
        }

        @ParameterizedTest
        @ValueSource(
                strings = {
                    "Secret123!",
                    "P@ssw0rd",
                    "MyP4$$word",
                    "Abcdef1@",
                    "A1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6q7r8s9t0u1v2w3x4y5z6A7B8C9D0E1F2G3H4!",
                    "Test$123",
                    "Hello@World1"
                })
        @DisplayName("Should create instance successfully and return same value")
        void shouldCreateInstanceSuccessfullyAndReturnSameValue(final String value) {
            // Given
            // When
            final Password instance = Password.ofPlain(value);

            // Then
            Assertions.assertThat(instance.getValue()).isEqualTo(value);
        }

        @ParameterizedTest
        @DisplayName("Should create instance successfully at boundary values")
        @MethodSource("shouldCreateInstanceSuccessfullyAtBoundaryValuesDataProvider")
        void shouldCreateInstanceSuccessfullyAtBoundaryValues(final String value) {
            // Given
            // When
            // Then
            Assertions.assertThatCode(() -> Password.ofPlain(value)).doesNotThrowAnyException();
        }

        private static Stream<Arguments>
                shouldCreateInstanceSuccessfullyAtBoundaryValuesDataProvider() {

            return Stream.of(
                    Arguments.of(
                            "R"
                                    + RandomStringUtils.secure()
                                            .nextAlphabetic(5)
                                            .toLowerCase(Locale.ENGLISH)
                                    + "1."),
                    Arguments.of(
                            "R"
                                    + RandomStringUtils.secure()
                                            .nextAlphabetic(69)
                                            .toLowerCase(Locale.ENGLISH)
                                    + "1@"));
        }

        @Test
        @DisplayName("Should create instance successfully and return false for hashed")
        void shouldCreateInstanceSuccessfullyAndReturnFalseForHashed() {
            // Given
            final String value = "Secret123!";

            // When
            final Password instance = Password.ofPlain(value);

            // Then
            Assertions.assertThat(instance.isHashed()).isFalse();
        }
    }

    @Nested
    final class HashedTests {

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"   ", "\t", "\n"})
        @DisplayName("Should throw exception when value is blank")
        void shouldThrowExceptionWhenValueIsBlank(final String value) {
            // Given
            // When
            // Then
            Assertions.assertThatThrownBy(() -> Password.ofHashed(value))
                    .isExactlyInstanceOf(InvalidArgumentValueException.class)
                    .hasMessage("Value cannot be blank");
        }

        @ParameterizedTest
        @ValueSource(
                strings = {
                    "a",
                    "abc",
                    "abcdefghijklmne",
                })
        @DisplayName("Should create instance successfully and return same value")
        void shouldCreateInstanceSuccessfullyAndReturnSameValue(final String value) {
            // Given
            // When
            final Password instance = Password.ofHashed(value);

            // Then
            Assertions.assertThat(instance.getValue()).isEqualTo(value);
        }

        @Test
        @DisplayName("Should create instance successfully and return true for hashed")
        void shouldCreateInstanceSuccessfullyAndReturnTrueForHashed() {
            // Given
            final String value = RandomStringUtils.secure().nextAlphabetic(15);

            // When
            final Password instance = Password.ofHashed(value);

            // Then
            Assertions.assertThat(instance.isHashed()).isTrue();
        }
    }
}
