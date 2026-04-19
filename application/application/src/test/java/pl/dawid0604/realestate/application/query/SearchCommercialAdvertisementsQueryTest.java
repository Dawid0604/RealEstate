/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.query;

import java.lang.annotation.Annotation;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import pl.dawid0604.realestate.application.fixture.AnnotationAssertions;
import pl.dawid0604.realestate.application.validation.ValidArea;
import pl.dawid0604.realestate.application.validation.ValidBuiltYear;
import pl.dawid0604.realestate.application.validation.ValidFloor;
import pl.dawid0604.realestate.application.validation.ValidFloors;
import pl.dawid0604.realestate.application.validation.ValidLocalityId;
import pl.dawid0604.realestate.application.validation.ValidNumberOfRooms;
import pl.dawid0604.realestate.application.validation.ValidPageNumber;
import pl.dawid0604.realestate.application.validation.ValidPageSize;
import pl.dawid0604.realestate.application.validation.ValidPrice;
import pl.dawid0604.realestate.domain.shared.advertisement.SearchCommercialAdvertisementsCriteria;

class SearchCommercialAdvertisementsQueryTest {

    @Test
    @DisplayName("Should implement Query interface")
    void shouldImplementsQueryInterface() {
        // Given
        // When
        // Then
        AnnotationAssertions.assertImplementsQueryInterface(
                SearchCommercialAdvertisementsQuery.class);
    }

    @Test
    @DisplayName("Should have area field with required annotations")
    void shouldHaveAreaFieldWithRequiredAnnotations() {
        // Given
        // When
        // Then
        assertFieldAnnotations("areaFrom", List.of(ValidArea.class));
        assertFieldAnnotations("areaTo", List.of(ValidArea.class));
    }

    @Test
    @DisplayName("Should have price fields with required annotations")
    void shouldHavePriceFieldsWithRequiredAnnotations() {
        assertFieldAnnotations("priceFrom", List.of(ValidPrice.class));
        assertFieldAnnotations("priceTo", List.of(ValidPrice.class));
    }

    @Test
    @DisplayName("Should have pricePerSquareMeter fields with required annotations")
    void shouldHavePricePerSquareMeterFieldsWithRequiredAnnotations() {
        assertFieldAnnotations("pricePerSquareMeterFrom", List.of(ValidPrice.class));
        assertFieldAnnotations("pricePerSquareMeterTo", List.of(ValidPrice.class));
    }

    @Test
    @DisplayName("Should have pagination fields with required annotations")
    void shouldHavePaginationFieldsWithRequiredAnnotations() {
        assertFieldAnnotations("page", List.of(ValidPageNumber.class));
        assertFieldAnnotations("pageSize", List.of(ValidPageSize.class));
    }

    @Test
    @DisplayName("Should have floor fields with required annotations")
    void shouldHaveFloorFieldsWithRequiredAnnotations() {
        assertFieldAnnotations("floorFrom", List.of(ValidFloor.class));
        assertFieldAnnotations("floorTo", List.of(ValidFloor.class));
    }

    @Test
    @DisplayName("Should have floors fields with required annotations")
    void shouldHaveFloorsFieldsWithRequiredAnnotations() {
        assertFieldAnnotations("floorsFrom", List.of(ValidFloors.class));
        assertFieldAnnotations("floorsTo", List.of(ValidFloors.class));
    }

    @Test
    @DisplayName("Should have number of rooms fields with required annotations")
    void shouldHaveNumberOfRoomsFieldsWithRequiredAnnotations() {
        assertFieldAnnotations("numberOfRoomsFrom", List.of(ValidNumberOfRooms.class));
        assertFieldAnnotations("numberOfRoomsTo", List.of(ValidNumberOfRooms.class));
    }

    @Test
    @DisplayName("Should have built year fields with required annotations")
    void shouldHaveBuiltYearFieldsWithRequiredAnnotations() {
        assertFieldAnnotations("builtYearFrom", List.of(ValidBuiltYear.class));
        assertFieldAnnotations("builtYearTo", List.of(ValidBuiltYear.class));
    }

    @Test
    @DisplayName("Should have localityId field with required annotations")
    void shouldHaveLocalityIdFieldWithRequiredAnnotations() {
        assertFieldAnnotations("localityId", List.of(ValidLocalityId.class));
    }

    @Test
    @DisplayName("Should get criteria")
    void shouldGetCriteria() {
        // Given
        final BigDecimal areaFrom = BigDecimal.valueOf(25);
        final BigDecimal areaTo = BigDecimal.valueOf(35);
        final BigDecimal priceFrom = BigDecimal.valueOf(25_000);
        final BigDecimal priceTo = BigDecimal.valueOf(35_000);
        final BigDecimal pricePerSquareMeterFrom = BigDecimal.valueOf(3_000);
        final BigDecimal pricePerSquareMeterTo = BigDecimal.valueOf(5_000);
        final int page = 2;
        final int pageSize = 25;
        final Set<String> offerFrom = Set.of("a", "b");
        final Set<String> types = Set.of("c", "g");
        final Set<String> typeOfMarkets = Set.of("x", "d");
        final UUID localityId = UUID.randomUUID();
        final LocalDate dateFrom = LocalDate.of(2025, 1, 5);
        final LocalDate dateTo = LocalDate.of(2025, 3, 15);
        final int floorFrom = 3;
        final int floorTo = 5;
        final int floorsFrom = 1;
        final int floorsTo = 6;
        final int numberOfRoomsFrom = 10;
        final int numberOfRoomsTo = 16;
        final int builtYearFrom = 1900;
        final int builtYearTo = 1905;

        // When
        final var query =
                new SearchCommercialAdvertisementsQuery(
                        areaFrom,
                        areaTo,
                        priceFrom,
                        priceTo,
                        pricePerSquareMeterFrom,
                        pricePerSquareMeterTo,
                        page,
                        pageSize,
                        offerFrom,
                        types,
                        typeOfMarkets,
                        floorFrom,
                        floorTo,
                        floorsFrom,
                        floorsTo,
                        numberOfRoomsFrom,
                        numberOfRoomsTo,
                        builtYearFrom,
                        builtYearTo,
                        dateFrom,
                        dateTo,
                        localityId);

        // Then
        Assertions.assertThat(query.criteria())
                .asInstanceOf(
                        InstanceOfAssertFactories.type(
                                SearchCommercialAdvertisementsCriteria.class))
                .returns(areaFrom, SearchCommercialAdvertisementsCriteria::areaFrom)
                .returns(areaTo, SearchCommercialAdvertisementsCriteria::areaTo)
                .returns(priceFrom, SearchCommercialAdvertisementsCriteria::priceFrom)
                .returns(priceTo, SearchCommercialAdvertisementsCriteria::priceTo)
                .returns(
                        pricePerSquareMeterFrom,
                        SearchCommercialAdvertisementsCriteria::pricePerSquareMeterFrom)
                .returns(
                        pricePerSquareMeterTo,
                        SearchCommercialAdvertisementsCriteria::pricePerSquareMeterTo)
                .returns(page, SearchCommercialAdvertisementsCriteria::page)
                .returns(pageSize, SearchCommercialAdvertisementsCriteria::pageSize)
                .returns(floorFrom, SearchCommercialAdvertisementsCriteria::floorFrom)
                .returns(floorTo, SearchCommercialAdvertisementsCriteria::floorTo)
                .returns(floorsFrom, SearchCommercialAdvertisementsCriteria::floorsFrom)
                .returns(floorsTo, SearchCommercialAdvertisementsCriteria::floorsTo)
                .returns(
                        numberOfRoomsFrom,
                        SearchCommercialAdvertisementsCriteria::numberOfRoomsFrom)
                .returns(numberOfRoomsTo, SearchCommercialAdvertisementsCriteria::numberOfRoomsTo)
                .returns(builtYearFrom, SearchCommercialAdvertisementsCriteria::builtYearFrom)
                .returns(builtYearTo, SearchCommercialAdvertisementsCriteria::builtYearTo)
                .returns(localityId, SearchCommercialAdvertisementsCriteria::localityId)
                .returns(dateFrom, SearchCommercialAdvertisementsCriteria::dateFrom)
                .returns(dateTo, SearchCommercialAdvertisementsCriteria::dateTo)
                .satisfies(
                        c -> {
                            Assertions.assertThat(c.offerFrom())
                                    .containsExactlyElementsOf(offerFrom);

                            Assertions.assertThat(c.types()).containsExactlyElementsOf(types);

                            Assertions.assertThat(c.typeOfMarkets())
                                    .containsExactlyElementsOf(typeOfMarkets);
                        });
    }

    private static void assertFieldAnnotations(
            final String fieldName, final List<Class<? extends Annotation>> requiredAnnotations) {

        AnnotationAssertions.assertFieldAnnotations(
                SearchCommercialAdvertisementsQuery.class, fieldName, requiredAnnotations);
    }
}
