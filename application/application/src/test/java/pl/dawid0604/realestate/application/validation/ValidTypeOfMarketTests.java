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

public class ValidTypeOfMarketTests {
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

    record TypeOfMarketWrapper(@ValidTypeOfMarket String typeOfMarket) {}

    @ParameterizedTest
    @ValueSource(strings = {"abc", "c", "xyz"})
    @DisplayName("Should pass for valid typeOfMarket")
    void shouldPassForValidTypeOfMarket(final String value) {
        // Given
        final TypeOfMarketWrapper typeOfMarketWrapper = new TypeOfMarketWrapper(value);

        // When
        final var violations = validator.validate(typeOfMarketWrapper);

        // Then
        Assertions.assertThat(violations).isEmpty();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"\t", "\n", "\r"})
    @DisplayName("Should fail for invalid typeOfMarket")
    void shouldFailForInvalidTypeOfMarket(final String value) {
        // Given
        final TypeOfMarketWrapper typeOfMarketWrapper = new TypeOfMarketWrapper(value);

        // When
        final var violations = validator.validate(typeOfMarketWrapper);

        // Then
        Assertions.assertThat(violations)
                .anyMatch(
                        v ->
                                v.getPropertyPath().toString().equals("typeOfMarket")
                                        && v.getMessage().equals("TypeOfMarket cannot be blank"));
    }
}
