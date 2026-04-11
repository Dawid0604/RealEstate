/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.command;

import java.lang.annotation.Annotation;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import pl.dawid0604.realestate.application.fixture.AnnotationAssertions;
import pl.dawid0604.realestate.application.validation.ValidArea;
import pl.dawid0604.realestate.application.validation.ValidBuildingType;
import pl.dawid0604.realestate.application.validation.ValidEmail;
import pl.dawid0604.realestate.application.validation.ValidLocalityId;
import pl.dawid0604.realestate.application.validation.ValidPrice;
import pl.dawid0604.realestate.application.validation.ValidTitle;

class CreatePlotAdvertisementCommandTest {

    @Test
    @DisplayName("Should implement Command interface")
    void shouldImplementsCommandInterface() {
        // Given
        // When
        // Then
        AnnotationAssertions.assertImplementsCommandInterface(CreatePlotAdvertisementCommand.class);
    }

    @Test
    @DisplayName("Should have title field with required annotations")
    void shouldHaveTitleFieldWithRequiredAnnotations() {
        // Given
        // When
        // Then
        assertFieldAnnotations("title", List.of(ValidTitle.class));
    }

    @Test
    @DisplayName("Should have price field with required annotations")
    void shouldHavePriceFieldWithRequiredAnnotations() {
        // Given
        // When
        // Then
        assertFieldAnnotations("price", List.of(ValidPrice.class));
    }

    @Test
    @DisplayName("Should have localityId field with required annotations")
    void shouldHaveLocalityIdFieldWithRequiredAnnotations() {
        // Given
        // When
        // Then
        assertFieldAnnotations("localityId", List.of(ValidLocalityId.class));
    }

    @Test
    @DisplayName("Should have userEmail field with required annotations")
    void shouldHaveUserEmailFieldWithRequiredAnnotations() {
        // Given
        // When
        // Then
        assertFieldAnnotations("userEmail", List.of(ValidEmail.class));
    }

    @Test
    @DisplayName("Should have buildingType field with required annotations")
    void shouldHaveBuildingTypeFieldWithRequiredAnnotations() {
        // Given
        // When
        // Then
        assertFieldAnnotations("buildingType", List.of(ValidBuildingType.class));
    }

    @Test
    @DisplayName("Should have area field with required annotations")
    void shouldHaveAreaFieldWithRequiredAnnotations() {
        // Given
        // When
        // Then
        assertFieldAnnotations("area", List.of(ValidArea.class));
    }

    private static void assertFieldAnnotations(
            final String fieldName, final List<Class<? extends Annotation>> requiredAnnotations) {

        AnnotationAssertions.assertFieldAnnotations(
                CreatePlotAdvertisementCommand.class, fieldName, requiredAnnotations);
    }
}
