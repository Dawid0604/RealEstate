/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.command;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import pl.dawid0604.realestate.application.fixture.AnnotationAssertions;
import pl.dawid0604.realestate.application.validation.ValidArea;
import pl.dawid0604.realestate.application.validation.ValidBuildingType;
import pl.dawid0604.realestate.application.validation.ValidBuiltYear;
import pl.dawid0604.realestate.application.validation.ValidDescription;
import pl.dawid0604.realestate.application.validation.ValidEmail;
import pl.dawid0604.realestate.application.validation.ValidFloor;
import pl.dawid0604.realestate.application.validation.ValidFloors;
import pl.dawid0604.realestate.application.validation.ValidLocalityId;
import pl.dawid0604.realestate.application.validation.ValidNumberOfRooms;
import pl.dawid0604.realestate.application.validation.ValidPrice;
import pl.dawid0604.realestate.application.validation.ValidTitle;
import pl.dawid0604.realestate.application.validation.ValidTypeOfMarket;

import java.lang.annotation.Annotation;
import java.util.List;

class CreateCommercialAdvertisementCommandTest {

    @Test
    @DisplayName("Should implement Command interface")
    void shouldImplementsCommandInterface() {
        // Given
        // When
        // Then
        AnnotationAssertions.assertImplementsCommandInterface(
                CreateCommercialAdvertisementCommand.class);
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
    @DisplayName("Should have description field with required annotations")
    void shouldHaveDescriptionFieldWithRequiredAnnotations() {
        // Given
        // When
        // Then
        assertFieldAnnotations("description", List.of(ValidDescription.class));
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
    @DisplayName("Should have numberOfRooms field with required annotations")
    void shouldHaveNumberOfRoomsFieldWithRequiredAnnotations() {
        // Given
        // When
        // Then
        assertFieldAnnotations("numberOfRooms", List.of(ValidNumberOfRooms.class));
    }

    @Test
    @DisplayName("Should have floor field with required annotations")
    void shouldHaveFloorFieldWithRequiredAnnotations() {
        // Given
        // When
        // Then
        assertFieldAnnotations("floor", List.of(ValidFloor.class));
    }

    @Test
    @DisplayName("Should have floors field with required annotations")
    void shouldHaveFloorsFieldWithRequiredAnnotations() {
        // Given
        // When
        // Then
        assertFieldAnnotations("floors", List.of(ValidFloors.class));
    }

    @Test
    @DisplayName("Should have builtYear field with required annotations")
    void shouldHaveBuiltYearFieldWithRequiredAnnotations() {
        // Given
        // When
        // Then
        assertFieldAnnotations("builtYear", List.of(ValidBuiltYear.class));
    }

    @Test
    @DisplayName("Should have typeOfMarket field with required annotations")
    void shouldHaveTypeOfMarketFieldWithRequiredAnnotations() {
        // Given
        // When
        // Then
        assertFieldAnnotations("typeOfMarket", List.of(ValidTypeOfMarket.class));
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
                CreateCommercialAdvertisementCommand.class, fieldName, requiredAnnotations);
    }
}
