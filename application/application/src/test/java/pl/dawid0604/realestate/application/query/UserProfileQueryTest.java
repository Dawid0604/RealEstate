/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.query;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import pl.dawid0604.realestate.application.fixture.AnnotationAssertions;
import pl.dawid0604.realestate.application.validation.ValidEmail;

class UserProfileQueryTest {

    @Test
    @DisplayName("Should implement Query interface")
    void shouldImplementsQueryInterface() {
        // Given
        // When
        // Then
        AnnotationAssertions.assertImplementsQueryInterface(UserProfileQuery.class);
    }

    @Test
    @DisplayName("Should have email field with required annotations")
    void shouldHaveEmailFieldWithRequiredAnnotations() {
        // Given
        // When
        // Then
        AnnotationAssertions.assertFieldAnnotations(
                UserProfileQuery.class, "email", List.of(ValidEmail.class));
    }
}
