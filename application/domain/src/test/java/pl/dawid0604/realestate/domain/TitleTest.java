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

class TitleTest {

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    @DisplayName("Should throw exception when title is invalid")
    void shouldThrowExceptionWhenTitleIsInvalid(final String title) {
        // Given
        // When
        // Then
        Assertions.assertThatThrownBy(() -> new Title(title))
                .isExactlyInstanceOf(InvalidArgumentValueException.class)
                .hasMessage("Title cannot be blank");
    }

    @ParameterizedTest
    @DisplayName("Should throw exception when title is too short")
    @MethodSource("shouldThrowExceptionWhenTitleIsTooShortDataProvider")
    void shouldThrowExceptionWhenTitleIsTooShort(final String title) {
        // Given
        // When
        // Then
        Assertions.assertThatThrownBy(() -> new Title(title))
                .isExactlyInstanceOf(InvalidArgumentValueException.class)
                .hasMessage("Title cannot be shorter than 10 characters");
    }

    @ParameterizedTest
    @DisplayName("Should throw exception when title is too long")
    @MethodSource("shouldThrowExceptionWhenTitleIsTooLongDataProvider")
    void shouldThrowExceptionWhenTitleIsTooLong(final String title) {
        // Given
        // When
        // Then
        Assertions.assertThatThrownBy(() -> new Title(title))
                .isExactlyInstanceOf(InvalidArgumentValueException.class)
                .hasMessageContaining("Title cannot be longer than ");
    }

    @ParameterizedTest
    @DisplayName("Should create instance successfully at boundary values")
    @MethodSource("shouldCreateInstanceSuccessfullyAtBoundaryValuesDataProvider")
    void shouldCreateInstanceSuccessfullyAtBoundaryValues(final String title) {
        // Given
        // When
        // Then
        Assertions.assertThatCode(() -> new Title(title)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Should create instance successfully and return same value")
    void shouldCreateInstanceSuccessfullyAndReturnSameValue() {
        // Given
        final String title = RandomStringUtils.secure().nextAlphanumeric(50);

        // When
        final Title instance = new Title(title);

        // Then
        Assertions.assertThat(instance.value()).isEqualTo(title);
    }

    @Test
    @DisplayName("Should create instance successfully and strip value")
    void shouldCreateInstanceSuccessfullyAndStripValue() {
        // Given
        final String title = RandomStringUtils.secure().nextAlphanumeric(50);

        // When
        final Title instance = new Title("   " + title + "   ");

        // Then
        Assertions.assertThat(instance.value()).isEqualTo(title);
    }

    private static Stream<Arguments> shouldThrowExceptionWhenTitleIsTooShortDataProvider() {
        return Stream.of(
                Arguments.of(RandomStringUtils.secure().nextAlphanumeric(1)),
                Arguments.of(RandomStringUtils.secure().nextAlphanumeric(5)),
                Arguments.of(RandomStringUtils.secure().nextAlphanumeric(9)));
    }

    private static Stream<Arguments> shouldThrowExceptionWhenTitleIsTooLongDataProvider() {
        return Stream.of(
                Arguments.of(RandomStringUtils.secure().nextAlphanumeric(101)),
                Arguments.of(RandomStringUtils.secure().nextAlphanumeric(125)));
    }

    private static Stream<Arguments>
            shouldCreateInstanceSuccessfullyAtBoundaryValuesDataProvider() {

        return Stream.of(
                Arguments.of(RandomStringUtils.secure().nextAlphanumeric(100)),
                Arguments.of(RandomStringUtils.secure().nextAlphanumeric(50)),
                Arguments.of(RandomStringUtils.secure().nextAlphanumeric(10)));
    }
}
