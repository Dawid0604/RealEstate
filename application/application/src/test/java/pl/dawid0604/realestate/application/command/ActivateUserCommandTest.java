/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.command;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class ActivateUserCommandTest {
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

    @ParameterizedTest
    @ValueSource(
            strings = {
                "jan@example.com",
                "jan.kowalski@example.com",
                "jan+tag@example.com",
                "jan@subdomain.example.com",
                "jan123@example.pl",
                "123@example.com",
                "jan@example.co.uk"
            })
    @DisplayName("Should pass for valid user email")
    void shouldPassForValidUserEmail(final String value) {
        // Given
        final ActivateUserCommand command = new ActivateUserCommand(value);

        // When
        final var violations = validator.validate(command);

        // Then
        Assertions.assertThat(violations).isEmpty();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("Should fail for blank user email")
    void shouldFailForBlankSlug(final String value) {
        // Given
        final ActivateUserCommand command = new ActivateUserCommand(value);

        // When
        final var violations = validator.validate(command);

        // Then
        Assertions.assertThat(violations)
                .anyMatch(
                        v ->
                                v.getPropertyPath().toString().equals("email")
                                        && v.getMessage().equals("Email cannot be blank"));
    }

    @ParameterizedTest
    @ValueSource(
            strings = {
                "notanemail",
                "@example.com",
                "jan@",
                "jan@.com",
                "jan@example.",
                "jan @example.com",
                "jan@example.c"
            })
    @DisplayName("Should fail for invalid user email")
    void shouldFailForInvalidSlug(final String value) {
        // Given
        final ActivateUserCommand command = new ActivateUserCommand(value);

        // When
        final var violations = validator.validate(command);

        // Then
        Assertions.assertThat(violations)
                .anyMatch(
                        v ->
                                v.getPropertyPath().toString().equals("email")
                                        && v.getMessage().equals("Email must be valid"));
    }
}
