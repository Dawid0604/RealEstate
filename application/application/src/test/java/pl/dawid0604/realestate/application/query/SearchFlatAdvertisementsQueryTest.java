/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.query;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import pl.dawid0604.realestate.application.fixture.AnnotationAssertions;
import pl.dawid0604.realestate.domain.shared.advertisement.SearchFlatAdvertisementsCriteria;

import java.lang.annotation.Annotation;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

class SearchFlatAdvertisementsQueryTest {

    @Test
    @DisplayName("Should implement Query interface")
    void shouldImplementsQueryInterface() {
        // Given
        // When
        // Then
        AnnotationAssertions.assertImplementsQueryInterface(SearchFlatAdvertisementsQuery.class);
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
                new SearchFlatAdvertisementsQuery(
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
                        InstanceOfAssertFactories.type(SearchFlatAdvertisementsCriteria.class))
                .returns(areaFrom, SearchFlatAdvertisementsCriteria::areaFrom)
                .returns(areaTo, SearchFlatAdvertisementsCriteria::areaTo)
                .returns(priceFrom, SearchFlatAdvertisementsCriteria::priceFrom)
                .returns(priceTo, SearchFlatAdvertisementsCriteria::priceTo)
                .returns(
                        pricePerSquareMeterFrom,
                        SearchFlatAdvertisementsCriteria::pricePerSquareMeterFrom)
                .returns(
                        pricePerSquareMeterTo,
                        SearchFlatAdvertisementsCriteria::pricePerSquareMeterTo)
                .returns(page, SearchFlatAdvertisementsCriteria::page)
                .returns(pageSize, SearchFlatAdvertisementsCriteria::pageSize)
                .returns(floorFrom, SearchFlatAdvertisementsCriteria::floorFrom)
                .returns(floorTo, SearchFlatAdvertisementsCriteria::floorTo)
                .returns(floorsFrom, SearchFlatAdvertisementsCriteria::floorsFrom)
                .returns(floorsTo, SearchFlatAdvertisementsCriteria::floorsTo)
                .returns(numberOfRoomsFrom, SearchFlatAdvertisementsCriteria::numberOfRoomsFrom)
                .returns(numberOfRoomsTo, SearchFlatAdvertisementsCriteria::numberOfRoomsTo)
                .returns(builtYearFrom, SearchFlatAdvertisementsCriteria::builtYearFrom)
                .returns(builtYearTo, SearchFlatAdvertisementsCriteria::builtYearTo)
                .returns(localityId, SearchFlatAdvertisementsCriteria::localityId)
                .returns(dateFrom, SearchFlatAdvertisementsCriteria::dateFrom)
                .returns(dateTo, SearchFlatAdvertisementsCriteria::dateTo)
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
                SearchFlatAdvertisementsQuery.class, fieldName, requiredAnnotations);
    }
}
