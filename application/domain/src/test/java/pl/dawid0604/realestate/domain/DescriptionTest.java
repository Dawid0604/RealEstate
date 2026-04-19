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

class DescriptionTest {

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    @DisplayName("Should create instance successfully when value is blank")
    void shouldCreateInstanceSuccessfullyWhenValueIsBlank(final String description) {
        // Given
        // When
        final Description instance = new Description(description);

        // Then
        Assertions.assertThat(instance.value()).isNull();
    }

    @Test
    @DisplayName("Should create instance successfully and return same value")
    void shouldCreateInstanceSuccessfullyAndReturnSameValue() {
        // Given
        final String description = RandomStringUtils.secure().nextAlphanumeric(50);

        // When
        final Description instance = new Description(description);

        // Then
        Assertions.assertThat(instance.value()).isEqualTo(description);
    }

    @Test
    @DisplayName("Should create instance successfully and strip value")
    void shouldCreateInstanceSuccessfullyAndStripValue() {
        // Given
        final String description = RandomStringUtils.secure().nextAlphanumeric(50);

        // When
        final Description instance = new Description("  " + description + "   ");

        // Then
        Assertions.assertThat(instance.value()).isEqualTo(description);
    }

    @ParameterizedTest
    @DisplayName("Should throw exception when description is too long")
    @MethodSource("shouldThrowExceptionWhenDescriptionIsTooLongDataProvider")
    void shouldThrowExceptionWhenDescriptionIsTooLong(final String description) {
        // Given
        // When
        // Then
        Assertions.assertThatThrownBy(() -> new Description(description))
                .isExactlyInstanceOf(InvalidArgumentValueException.class)
                .hasMessageContaining("Description cannot be longer than ");
    }

    @ParameterizedTest
    @DisplayName("Should create instance successfully at boundary values")
    @MethodSource("shouldCreateInstanceSuccessfullyAtBoundaryValuesDataProvider")
    void shouldCreateInstanceSuccessfullyAtBoundaryValues(final String description) {
        // Given
        // When
        // Then
        Assertions.assertThatCode(() -> new Description(description)).doesNotThrowAnyException();
    }

    private static Stream<Arguments> shouldThrowExceptionWhenDescriptionIsTooLongDataProvider() {
        return Stream.of(
                Arguments.of(RandomStringUtils.secure().nextAlphanumeric(5001)),
                Arguments.of(RandomStringUtils.secure().nextAlphanumeric(7500)));
    }

    private static Stream<Arguments>
            shouldCreateInstanceSuccessfullyAtBoundaryValuesDataProvider() {

        return Stream.of(
                Arguments.of(RandomStringUtils.secure().nextAlphanumeric(5000)),
                Arguments.of(RandomStringUtils.secure().nextAlphanumeric(500)),
                Arguments.of(RandomStringUtils.secure().nextAlphanumeric(100)),
                Arguments.of(RandomStringUtils.secure().nextAlphanumeric(10)),
                Arguments.of(RandomStringUtils.secure().nextAlphanumeric(1)));
    }
}
