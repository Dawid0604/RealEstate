/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.command;

import java.lang.annotation.Annotation;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import pl.dawid0604.realestate.application.fixture.AnnotationAssertions;
import pl.dawid0604.realestate.application.validation.ValidEmail;
import pl.dawid0604.realestate.application.validation.ValidUserType;

class UpdateUserFullNameCommandTest {

    @Test
    @DisplayName("Should implement Command interface")
    void shouldImplementsCommandInterface() {
        // Given
        // When
        // Then
        AnnotationAssertions.assertImplementsCommandInterface(UpdateUserTypeCommand.class);
    }

    @Test
    @DisplayName("Should have username field with required annotations")
    void shouldHaveEmailFieldWithRequiredAnnotations() {
        // Given
        // When
        // Then
        assertFieldAnnotations("username", List.of(ValidEmail.class));
    }

    @Test
    @DisplayName("Should have type field with required annotations")
    void shouldHaveTypeFieldWithRequiredAnnotations() {
        // Given
        // When
        // Then
        assertFieldAnnotations("type", List.of(ValidUserType.class));
    }

    private static void assertFieldAnnotations(
            final String fieldName, final List<Class<? extends Annotation>> requiredAnnotations) {

        AnnotationAssertions.assertFieldAnnotations(
                UpdateUserTypeCommand.class, fieldName, requiredAnnotations);
    }
}
