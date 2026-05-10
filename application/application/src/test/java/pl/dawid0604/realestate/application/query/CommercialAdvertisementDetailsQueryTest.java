/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.query;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import pl.dawid0604.realestate.application.fixture.AnnotationAssertions;
import pl.dawid0604.realestate.application.validation.ValidSlug;

class CommercialAdvertisementDetailsQueryTest {

    @Test
    @DisplayName("Should implement Query interface")
    void shouldImplementsQueryInterface() {
        // Given
        // When
        // Then
        AnnotationAssertions.assertImplementsQueryInterface(
                CommercialAdvertisementDetailsQuery.class);
    }

    @Test
    @DisplayName("Should have slug field with required annotations")
    void shouldHaveSlugFieldWithRequiredAnnotations() {
        // Given
        // When
        // Then
        AnnotationAssertions.assertFieldAnnotations(
                CommercialAdvertisementDetailsQuery.class, "slug", List.of(ValidSlug.class));
    }
}
