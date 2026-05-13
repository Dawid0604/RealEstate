/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.query;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import pl.dawid0604.realestate.application.fixture.AnnotationAssertions;
import pl.dawid0604.realestate.application.validation.ValidEmail;

import java.util.List;

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
    @DisplayName("Should have username field with required annotations")
    void shouldHaveEmailFieldWithRequiredAnnotations() {
        // Given
        // When
        // Then
        AnnotationAssertions.assertFieldAnnotations(
                UserProfileQuery.class, "username", List.of(ValidEmail.class));
    }
}
