/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.validation;

import java.util.stream.Stream;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

public class ValidSlugTests {
    private static Validator validator;
    private static ValidatorFactory validatorFactory;

    @BeforeAll
    static void setUp() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    static void tearDown() {
        validatorFactory.close();
    }

    record SlugWrapper(@ValidSlug String slug) {}

    @ParameterizedTest
    @ValueSource(strings = {"qwertyuiop123123", "asdfghjkllkmn123123", "zxcvbnm123123"})
    @DisplayName("Should pass for valid slug")
    void shouldPassForValidSlug(final String value) {
        // Given
        final SlugWrapper slugWrapper = new SlugWrapper(value);

        // When
        final var violations = validator.validate(slugWrapper);

        // Then
        Assertions.assertThat(violations).isEmpty();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("Should fail for blank slug")
    void shouldFailForBlankSlug(final String value) {
        // Given
        final SlugWrapper slugWrapper = new SlugWrapper(value);

        // When
        final var violations = validator.validate(slugWrapper);

        // Then
        Assertions.assertThat(violations)
                .anyMatch(
                        v ->
                                v.getPropertyPath().toString().equals("slug")
                                        && v.getMessage().equals("Slug cannot be blank"));
    }

    @ParameterizedTest
    @MethodSource("boundarySlugDataProvider")
    @DisplayName("Should pass for boundary slug length")
    void shouldPassForBoundarySlugLength(final String value) {
        // Given
        final SlugWrapper slugWrapper = new SlugWrapper(value);

        // When
        final var violations = validator.validate(slugWrapper);

        // Then
        Assertions.assertThat(violations).isEmpty();
    }

    private static Stream<Arguments> boundarySlugDataProvider() {
        return Stream.of(
                Arguments.of(RandomStringUtils.secure().nextAlphanumeric(10)),
                Arguments.of(RandomStringUtils.secure().nextAlphanumeric(99)),
                Arguments.of(RandomStringUtils.secure().nextAlphanumeric(100)));
    }
}
