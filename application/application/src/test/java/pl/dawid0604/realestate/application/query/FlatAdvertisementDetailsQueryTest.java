/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.query;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import pl.dawid0604.realestate.application.fixture.AnnotationAssertions;

class FlatAdvertisementDetailsQueryTest {

    @Test
    @DisplayName("Should implement Query interface")
    void shouldImplementsQueryInterface() {
        // Given
        // When
        // Then
        AnnotationAssertions.assertImplementsQueryInterface(FlatAdvertisementDetailsQuery.class);
    }
}
