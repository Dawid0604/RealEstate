/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.command;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import pl.dawid0604.realestate.application.fixture.AnnotationAssertions;
import pl.dawid0604.realestate.application.validation.ValidEmail;
import pl.dawid0604.realestate.application.validation.ValidUserAvatar;

import java.lang.annotation.Annotation;
import java.util.List;

class UpdateUserAvatarCommandTest {

    @Test
    @DisplayName("Should implement Command interface")
    void shouldImplementsCommandInterface() {
        // Given
        // When
        // Then
        AnnotationAssertions.assertImplementsCommandInterface(UpdateUserAvatarCommand.class);
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
    @DisplayName("Should have newAvatarUrl field with required annotations")
    void shouldHaveNewAvatarUrlFieldWithRequiredAnnotations() {
        // Given
        // When
        // Then
        assertFieldAnnotations("newAvatarUrl", List.of(ValidUserAvatar.class));
    }

    private static void assertFieldAnnotations(
            final String fieldName, final List<Class<? extends Annotation>> requiredAnnotations) {

        AnnotationAssertions.assertFieldAnnotations(
                UpdateUserAvatarCommand.class, fieldName, requiredAnnotations);
    }
}
