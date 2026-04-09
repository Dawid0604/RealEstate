/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.command;

import java.lang.annotation.Annotation;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import pl.dawid0604.realestate.application.fixture.AnnotationAssertions;
import pl.dawid0604.realestate.application.validation.ValidEmail;
import pl.dawid0604.realestate.application.validation.ValidPassword;

class UpdateUserPasswordCommandTest {

    @Test
    @DisplayName("Should implement Command interface")
    void shouldImplementsCommandInterface() {
        // Given
        // When
        // Then
        AnnotationAssertions.assertImplementsCommandInterface(UpdateUserPasswordCommand.class);
    }

    @Test
    @DisplayName("Should have email field with required annotations")
    void shouldHaveEmailFieldWithRequiredAnnotations() {
        // Given
        // When
        // Then
        assertFieldAnnotations("email", List.of(ValidEmail.class));
    }

    @Test
    @DisplayName("Should have currentPassword field with required annotations")
    void shouldHaveCurrentPasswordFieldWithRequiredAnnotations() {
        // Given
        // When
        // Then
        assertFieldAnnotations("currentPassword", List.of(ValidPassword.class));
    }

    @Test
    @DisplayName("Should have newPassword field with required annotations")
    void shouldHaveNewPasswordFieldWithRequiredAnnotations() {
        // Given
        // When
        // Then
        assertFieldAnnotations("newPassword", List.of(ValidPassword.class));
    }

    private static void assertFieldAnnotations(
            final String fieldName, final List<Class<? extends Annotation>> requiredAnnotations) {

        AnnotationAssertions.assertFieldAnnotations(
                UpdateUserPasswordCommand.class, fieldName, requiredAnnotations);
    }
}
