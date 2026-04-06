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

public class ValidBuildingTypeTests {
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

    record BuildingTypeWrapper(@ValidBuildingType String buildingType) {}

    @ParameterizedTest
    @ValueSource(strings = {"abc", "c", "xyz"})
    @DisplayName("Should pass for valid buildingType")
    void shouldPassForValidBuildingType(final String value) {
        // Given
        final BuildingTypeWrapper buildingTypeWrapper = new BuildingTypeWrapper(value);

        // When
        final var violations = validator.validate(buildingTypeWrapper);

        // Then
        Assertions.assertThat(violations).isEmpty();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"\t", "\n", "\r"})
    @DisplayName("Should fail for invalid area")
    void shouldFailForInvalidBuildingType(final String value) {
        // Given
        final BuildingTypeWrapper buildingTypeWrapper = new BuildingTypeWrapper(value);

        // When
        final var violations = validator.validate(buildingTypeWrapper);

        // Then
        Assertions.assertThat(violations)
                .anyMatch(
                        v ->
                                v.getPropertyPath().toString().equals("buildingType")
                                        && v.getMessage().equals("BuildingType cannot be blank"));
    }
}
