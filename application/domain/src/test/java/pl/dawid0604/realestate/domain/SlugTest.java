/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

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

import java.util.stream.Stream;

class SlugTest {

    @Nested
    final class CreateTests {

        @Test
        void shouldThrowExceptionWhenTitleIsNull() {
            // Given
            // When
            // Then
            Assertions.assertThatThrownBy(() -> Slug.create(null))
                    .isExactlyInstanceOf(InvalidArgumentValueException.class)
                    .hasMessage("Title cannot be null");
        }

        @Test
        @DisplayName("Should create successfully and append separator")
        void shouldCreateSuccessfullyAndAppendSeparator() {
            // Given
            final Title title = getValidTitle();

            // When
            final Slug instance = Slug.create(title);

            // Then
            Assertions.assertThat(instance.getValue())
                    .matches(v -> v.charAt(v.length() - 7) == '-');
        }

        @Test
        @DisplayName("Should create unique slugs")
        void shouldCreateCreateUniqueSlugs() {
            // Given
            final Title title = getValidTitle();

            // When
            final Slug instance = Slug.create(title);
            final Slug instance2 = Slug.create(title);

            // Then
            Assertions.assertThat(instance).isNotEqualTo(instance2);
            Assertions.assertThat(instance.getValue()).isNotEqualTo(instance2.getValue());
        }

        @Test
        @DisplayName("Should create successfully and append random suffix after separator")
        void shouldCreateSuccessfullyAndAppendRandomSuffixAfterSeparator() {
            // Given
            final Title title = getValidTitle();

            // When
            final Slug instance = Slug.create(title);

            // Then
            Assertions.assertThat(instance.getValue())
                    .matches(v -> v.substring(v.lastIndexOf('-') + 1).length() == 6);
        }

        @Test
        @DisplayName("Should create successfully and value is lowercase")
        void shouldCreateSuccessfullyAndValueIsLowercase() {
            // Given
            final Title title = new Title("Any tItLe abcd");

            // When
            final Slug instance = Slug.create(title);

            // Then
            Assertions.assertThat(instance.getValue()).matches(v -> v.matches("[a-z0-9-]+"));
        }

        @Test
        @DisplayName("Should create successfully with polish locale")
        void shouldCreateSuccessfullyWithPolishLocale() {
            // Given
            final Title title = new Title("ąśćęóżźćńę");
            final String expectedValue = "asceozzcne";

            // When
            final Slug instance = Slug.create(title);

            // Then
            Assertions.assertThat(instance.getValue()).startsWith(expectedValue);
        }
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    @DisplayName("Should throw exception when slug is blank")
    void shouldThrowExceptionWhenSlugIsBlank(final String slug) {
        // Given
        // When
        // Then
        Assertions.assertThatThrownBy(() -> Slug.of(slug))
                .isExactlyInstanceOf(InvalidArgumentValueException.class)
                .hasMessage("Slug cannot be blank");
    }

    @ParameterizedTest
    @ValueSource(strings = {"Abc-dd_123", "ABC-DD_123", "ABC-DD_", "_"})
    @DisplayName("Should throw exception when slug is invalid")
    void shouldThrowExceptionWhenSlugIsInvalid(final String slug) {
        // Given
        // When
        // Then
        Assertions.assertThatThrownBy(() -> Slug.of(slug))
                .isExactlyInstanceOf(InvalidArgumentValueException.class)
                .hasMessage("Given slug is invalid");
    }

    @ParameterizedTest
    @ValueSource(strings = {"abcd-1", "abc", "-", "-----", "123456789"})
    @DisplayName("Should throw exception when slug is too short")
    void shouldThrowExceptionWhenSlugIsTooShort(final String slug) {
        // Given
        // When
        // Then
        Assertions.assertThatThrownBy(() -> Slug.of(slug))
                .isExactlyInstanceOf(InvalidArgumentValueException.class)
                .hasMessageStartingWith("Slug cannot be shorter than ");
    }

    @ParameterizedTest
    @MethodSource("shouldThrowExceptionWhenSlugIsTooLongDataProvider")
    @DisplayName("Should throw exception when slug is too long")
    void shouldThrowExceptionWhenSlugIsTooLong(final String slug) {
        // Given
        // When
        // Then
        Assertions.assertThatThrownBy(() -> Slug.of(slug))
                .isExactlyInstanceOf(InvalidArgumentValueException.class)
                .hasMessageStartingWith("Slug cannot be longer than ");
    }

    private static Stream<Arguments> shouldThrowExceptionWhenSlugIsTooLongDataProvider() {
        return Stream.of(
                Arguments.of(RandomStringUtils.secure().nextNumeric(101)),
                Arguments.of(RandomStringUtils.secure().nextNumeric(125)),
                Arguments.of(RandomStringUtils.secure().nextNumeric(200)));
    }

    @Test
    @DisplayName("Should reconstitute slug successfully and return same value")
    void shouldReconstituteSlugSuccessfullyAndReturnSameValue() {
        // Given
        final String slug = "hgasqwe-ada-ad-adasdas-123";

        // When
        final Slug instance = Slug.of(slug);

        // Then
        Assertions.assertThat(instance.getValue()).isEqualTo(slug);
    }

    private static Title getValidTitle() {
        return new Title("Any super title");
    }
}
