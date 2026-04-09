/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.command;

import java.lang.annotation.Annotation;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import pl.dawid0604.realestate.application.fixture.AnnotationAssertions;
import pl.dawid0604.realestate.application.validation.ValidEmail;
import pl.dawid0604.realestate.application.validation.ValidFirstName;
import pl.dawid0604.realestate.application.validation.ValidLastName;

class UpdateUserFullNameCommandTest {

    @Test
    @DisplayName("Should implement Command interface")
    void shouldImplementsCommandInterface() {
        // Given
        // When
        // Then
        AnnotationAssertions.assertImplementsCommandInterface(UpdateUserFullNameCommand.class);
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
    @DisplayName("Should have newFirstName field with required annotations")
    void shouldHaveNewFirstNameFieldWithRequiredAnnotations() {
        // Given
        // When
        // Then
        assertFieldAnnotations("newFirstName", List.of(ValidFirstName.class));
    }

    @Test
    @DisplayName("Should have newLastName field with required annotations")
    void shouldHaveNewLastNameFieldWithRequiredAnnotations() {
        // Given
        // When
        // Then
        assertFieldAnnotations("newLastName", List.of(ValidLastName.class));
    }

    private static void assertFieldAnnotations(
            final String fieldName, final List<Class<? extends Annotation>> requiredAnnotations) {

        AnnotationAssertions.assertFieldAnnotations(
                UpdateUserFullNameCommand.class, fieldName, requiredAnnotations);
    }
}
