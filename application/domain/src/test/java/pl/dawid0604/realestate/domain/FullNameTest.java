/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

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

import java.util.stream.Stream;

class FullNameTest {

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    @DisplayName("Should throw exception when firstName is blank")
    void shouldThrowExceptionWhenFirstNameIsBlank(final String firstName) {
        // Given
        // When
        // Then
        Assertions.assertThatThrownBy(() -> new FullName(firstName, null))
                .isExactlyInstanceOf(InvalidArgumentValueException.class)
                .hasMessage("FirstName cannot be blank");
    }

    @Test
    @DisplayName("Should throw exception when firstName is too short")
    void shouldThrowExceptionWhenFirstNameIsTooShort() {
        // Given
        final String firstName = RandomStringUtils.secure().nextAlphabetic(2);

        // When
        // Then
        Assertions.assertThatThrownBy(() -> new FullName(firstName, null))
                .isExactlyInstanceOf(InvalidArgumentValueException.class)
                .hasMessageStartingWith("FirstName cannot be less than ");
    }

    @Test
    @DisplayName("Should throw exception when lastName is too short")
    void shouldThrowExceptionWhenLastNameIsTooShort() {
        // Given
        final String firstName = RandomStringUtils.secure().nextAlphabetic(12);
        final String lastName = RandomStringUtils.secure().nextAlphabetic(2);

        // When
        // Then
        Assertions.assertThatThrownBy(() -> new FullName(firstName, lastName))
                .isExactlyInstanceOf(InvalidArgumentValueException.class)
                .hasMessageStartingWith("LastName cannot be less than ");
    }

    @Test
    @DisplayName("Should throw exception when firstName is too long")
    void shouldThrowExceptionWhenFirstNameIsTooLong() {
        // Given
        final String firstName = RandomStringUtils.secure().nextAlphabetic(129);

        // When
        // Then
        Assertions.assertThatThrownBy(() -> new FullName(firstName, null))
                .isExactlyInstanceOf(InvalidArgumentValueException.class)
                .hasMessageStartingWith("FirstName cannot be longer than ");
    }

    @Test
    @DisplayName("Should throw exception when lastName is too long")
    void shouldThrowExceptionWhenLastNameIsTooLong() {
        // Given
        final String firstName = RandomStringUtils.secure().nextAlphabetic(12);
        final String lastName = RandomStringUtils.secure().nextAlphabetic(129);

        // When
        // Then
        Assertions.assertThatThrownBy(() -> new FullName(firstName, lastName))
                .isExactlyInstanceOf(InvalidArgumentValueException.class)
                .hasMessageStartingWith("LastName cannot be longer than ");
    }

    @ParameterizedTest
    @DisplayName("Should throw exception when firstName is invalid")
    @MethodSource("shouldThrowExceptionWhenNameIsInvalidDataProvider")
    void shouldThrowExceptionWhenFirstNameIsInvalid(final String firstName) {
        // Given
        // When
        // Then
        Assertions.assertThatThrownBy(() -> new FullName(firstName, null))
                .isExactlyInstanceOf(InvalidArgumentValueException.class)
                .hasMessage("FirstName is invalid");
    }

    @ParameterizedTest
    @DisplayName("Should throw exception when lastName is invalid")
    @MethodSource("shouldThrowExceptionWhenNameIsInvalidDataProvider")
    void shouldThrowExceptionWhenLastNameIsInvalid(final String lastName) {
        // Given
        final String firstName = RandomStringUtils.secure().nextAlphabetic(12);

        // When
        // Then
        Assertions.assertThatThrownBy(() -> new FullName(firstName, lastName))
                .isExactlyInstanceOf(InvalidArgumentValueException.class)
                .hasMessage("LastName is invalid");
    }

    private static Stream<Arguments> shouldThrowExceptionWhenNameIsInvalidDataProvider() {
        return Stream.of(
                Arguments.of("Jan123"),
                Arguments.of("123"),
                Arguments.of("J4n"),
                Arguments.of("Jan@Kowalski"),
                Arguments.of("Jan!"),
                Arguments.of("Jan#"),
                Arguments.of("Jan$"),
                Arguments.of("Jan.Nowak"),
                Arguments.of("Jan_Nowak"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    @DisplayName("Should throw exception when lastName is blank")
    void shouldThrowExceptionWhenLastNameIsBlank(final String lastName) {
        // Given
        final String firstName = RandomStringUtils.secure().nextAlphabetic(10);

        // When
        // Then
        Assertions.assertThatThrownBy(() -> new FullName(firstName, lastName))
                .isExactlyInstanceOf(InvalidArgumentValueException.class)
                .hasMessage("LastName cannot be blank");
    }

    @ParameterizedTest
    @DisplayName("Should create instance successfully and return same values")
    @MethodSource("shouldCreateInstanceSuccessfullyAndReturnSameValuesDataProvider")
    void shouldCreateInstanceSuccessfullyAndReturnSameValues(
            final String firstName, final String lastName) {

        // Given
        // When
        final FullName instance = new FullName(firstName, lastName);

        // Then
        Assertions.assertThat(instance.firstName()).isEqualTo(firstName);
        Assertions.assertThat(instance.lastName()).isEqualTo(lastName);
    }

    @Test
    @DisplayName("Should strip values")
    void shouldStripValues() {
        // Given
        final String firstName = RandomStringUtils.secure().nextAlphabetic(52);
        final String lastName = RandomStringUtils.secure().nextAlphabetic(52);

        // When
        final FullName instance = new FullName("  " + firstName + "  ", "  " + lastName + " ");

        // Then
        Assertions.assertThat(instance.firstName()).isEqualTo(firstName);
        Assertions.assertThat(instance.lastName()).isEqualTo(lastName);
    }

    @ParameterizedTest
    @DisplayName("Should create instance successfully at boundary values length")
    @MethodSource("shouldCreateInstanceSuccessfullyAtBoundaryValuesLengthDataProvider")
    void shouldCreateInstanceSuccessfullyAtBoundaryValuesLength(
            final String firstName, final String lastName) {

        // Given
        // When
        // Then
        Assertions.assertThatCode(() -> new FullName(firstName, lastName))
                .doesNotThrowAnyException();
    }

    private static Stream<Arguments>
            shouldCreateInstanceSuccessfullyAndReturnSameValuesDataProvider() {

        return Stream.of(
                Arguments.of("Józef", "Wójcik"),
                Arguments.of("Łukasz", "Źródłowski"),
                Arguments.of("Marie-Claire", "Nowak"),
                Arguments.of("O'Brien", "Kowalski"),
                Arguments.of("Jan Maria", "Nowak"),
                Arguments.of("Jan", "Jan"),
                Arguments.of("Ana", "Nowak"));
    }

    private static Stream<Arguments>
            shouldCreateInstanceSuccessfullyAtBoundaryValuesLengthDataProvider() {

        return Stream.of(
                Arguments.of("Jan", "Jan"),
                Arguments.of(
                        RandomStringUtils.secure().nextAlphabetic(128),
                        RandomStringUtils.secure().nextAlphabetic(128)));
    }
}
