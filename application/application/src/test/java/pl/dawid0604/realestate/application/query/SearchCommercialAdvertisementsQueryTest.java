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
