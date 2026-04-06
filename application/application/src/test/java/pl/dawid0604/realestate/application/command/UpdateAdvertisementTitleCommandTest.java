/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.command;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import pl.dawid0604.realestate.application.fixture.AnnotationAssertions;
import pl.dawid0604.realestate.application.validation.ValidEmail;
import pl.dawid0604.realestate.application.validation.ValidSlug;
import pl.dawid0604.realestate.application.validation.ValidTitle;

import java.lang.annotation.Annotation;
import java.util.List;

class UpdateAdvertisementTitleCommandTest {

    @Test
    @DisplayName("Should implement Command interface")
    void shouldImplementsCommandInterface() {
        // Given
        // When
        // Then
        AnnotationAssertions.assertImplementsCommandInterface(
                UpdateAdvertisementTitleCommand.class);
    }

    @Test
    @DisplayName("Should have slug field with required annotations")
    void shouldHaveSlugFieldWithRequiredAnnotations() {
        // Given
        // When
        // Then
        assertFieldAnnotations("slug", List.of(ValidSlug.class));
    }

    @Test
    @DisplayName("Should have newTitle field with required annotations")
    void shouldHaveNewTitleFieldWithRequiredAnnotations() {
        // Given
        // When
        // Then
        assertFieldAnnotations("newTitle", List.of(ValidTitle.class));
    }

    @Test
    @DisplayName("Should have userEmail field with required annotations")
    void shouldHaveUserEmailFieldWithRequiredAnnotations() {
        // Given
        // When
        // Then
        assertFieldAnnotations("userEmail", List.of(ValidEmail.class));
    }

    private static void assertFieldAnnotations(
            final String fieldName, final List<Class<? extends Annotation>> requiredAnnotations) {

        AnnotationAssertions.assertFieldAnnotations(
                UpdateAdvertisementTitleCommand.class, fieldName, requiredAnnotations);
    }
}
