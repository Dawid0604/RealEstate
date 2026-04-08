/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.validation;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class ValidPhotoIdTests {
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

    record PhotoIdWrapper(@ValidPhotoId UUID photoId) {}

    @Test
    @DisplayName("Should pass for valid photoId")
    void shouldPassForValidFloor() {
        // Given
        final PhotoIdWrapper photoIdWrapper = new PhotoIdWrapper(UUID.randomUUID());

        // When
        final var violations = validator.validate(photoIdWrapper);

        // Then
        Assertions.assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Should fail for invalid photoId")
    void shouldFailForInvalidPhotoId() {
        // Given
        final PhotoIdWrapper photoIdWrapper = new PhotoIdWrapper(null);

        // When
        final var violations = validator.validate(photoIdWrapper);

        // Then
        Assertions.assertThat(violations)
                .anyMatch(
                        v ->
                                v.getPropertyPath().toString().equals("photoId")
                                        && v.getMessage().equals("PhotoId cannot be null"));
    }
}
