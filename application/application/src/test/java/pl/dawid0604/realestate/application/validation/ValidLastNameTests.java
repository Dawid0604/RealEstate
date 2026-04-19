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

public class ValidLastNameTests {
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

    record LastNameWrapper(@ValidLastName String lastName) {}

    @ParameterizedTest
    @ValueSource(strings = {"abc", "c", "xyz"})
    @DisplayName("Should pass for valid lastName")
    void shouldPassForValidLastName(final String value) {
        // Given
        final LastNameWrapper lastNameWrapper = new LastNameWrapper(value);

        // When
        final var violations = validator.validate(lastNameWrapper);

        // Then
        Assertions.assertThat(violations).isEmpty();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"\t", "\n", "\r"})
    @DisplayName("Should fail for invalid lastName")
    void shouldFailForInvalidLastName(final String value) {
        // Given
        final LastNameWrapper lastNameWrapper = new LastNameWrapper(value);

        // When
        final var violations = validator.validate(lastNameWrapper);

        // Then
        Assertions.assertThat(violations)
                .anyMatch(
                        v ->
                                v.getPropertyPath().toString().equals("lastName")
                                        && v.getMessage().equals("LastName cannot be blank"));
    }
}
