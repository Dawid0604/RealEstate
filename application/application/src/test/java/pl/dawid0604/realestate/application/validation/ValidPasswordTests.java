/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.validation;

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

public class ValidPasswordTests {
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

    record PasswordWrapper(@ValidPassword String password) {}

    @ParameterizedTest
    @ValueSource(strings = {"abc", "c", "xyz"})
    @DisplayName("Should pass for valid password")
    void shouldPassForValidPassword(final String value) {
        // Given
        final PasswordWrapper passwordWrapper = new PasswordWrapper(value);

        // When
        final var violations = validator.validate(passwordWrapper);

        // Then
        Assertions.assertThat(violations).isEmpty();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"\t", "\n", "\r"})
    @DisplayName("Should fail for invalid password")
    void shouldFailForInvalidPassword(final String value) {
        // Given
        final PasswordWrapper passwordWrapper = new PasswordWrapper(value);

        // When
        final var violations = validator.validate(passwordWrapper);

        // Then
        Assertions.assertThat(violations)
                .anyMatch(
                        v ->
                                v.getPropertyPath().toString().equals("password")
                                        && v.getMessage().equals("Password cannot be blank"));
    }
}
