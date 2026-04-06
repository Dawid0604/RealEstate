/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.command;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import pl.dawid0604.realestate.application.fixture.AnnotationAssertions;
import pl.dawid0604.realestate.application.validation.ValidEmail;
import pl.dawid0604.realestate.application.validation.ValidFirstName;
import pl.dawid0604.realestate.application.validation.ValidLastName;
import pl.dawid0604.realestate.application.validation.ValidPassword;

import java.lang.annotation.Annotation;
import java.util.List;

class RegisterUserCommandTest {

    @Test
    @DisplayName("Should implement Command interface")
    void shouldImplementsCommandInterface() {
        // Given
        // When
        // Then
        AnnotationAssertions.assertImplementsCommandInterface(RegisterUserCommand.class);
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
    @DisplayName("Should have password field with required annotations")
    void shouldHavePasswordFieldWithRequiredAnnotations() {
        // Given
        // When
        // Then
        assertFieldAnnotations("password", List.of(ValidPassword.class));
    }

    @Test
    @DisplayName("Should have firstName field with required annotations")
    void shouldHaveFirstNameFieldWithRequiredAnnotations() {
        // Given
        // When
        // Then
        assertFieldAnnotations("firstName", List.of(ValidFirstName.class));
    }

    @Test
    @DisplayName("Should have lastName field with required annotations")
    void shouldHaveLastNameFieldWithRequiredAnnotations() {
        // Given
        // When
        // Then
        assertFieldAnnotations("lastName", List.of(ValidLastName.class));
    }

    private static void assertFieldAnnotations(
            final String fieldName, final List<Class<? extends Annotation>> requiredAnnotations) {

        AnnotationAssertions.assertFieldAnnotations(
                RegisterUserCommand.class, fieldName, requiredAnnotations);
    }
}
