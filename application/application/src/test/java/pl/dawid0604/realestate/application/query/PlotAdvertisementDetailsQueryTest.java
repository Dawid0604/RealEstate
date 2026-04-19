/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.query;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import pl.dawid0604.realestate.application.fixture.AnnotationAssertions;
import pl.dawid0604.realestate.application.validation.ValidSlug;

class PlotAdvertisementDetailsQueryTest {

    @Test
    @DisplayName("Should implement Query interface")
    void shouldImplementsQueryInterface() {
        // Given
        // When
        // Then
        AnnotationAssertions.assertImplementsQueryInterface(PlotAdvertisementDetailsQuery.class);
    }

    @Test
    @DisplayName("Should have slug field with required annotations")
    void shouldHaveSlugFieldWithRequiredAnnotations() {
        // Given
        // When
        // Then
        AnnotationAssertions.assertFieldAnnotations(
                PlotAdvertisementDetailsQuery.class, "slug", List.of(ValidSlug.class));
    }
}
