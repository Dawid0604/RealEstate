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
import pl.dawid0604.realestate.domain.shared.advertisement.SearchHouseAdvertisementsCriteria;

class SearchHouseAdvertisementsQueryTest {

    @Test
    @DisplayName("Should implement Query interface")
    void shouldImplementsQueryInterface() {
        // Given
        // When
        // Then
        AnnotationAssertions.assertImplementsQueryInterface(SearchHouseAdvertisementsQuery.class);
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
        final int floorsFrom = 1;
        final int floorsTo = 6;
        final int numberOfRoomsFrom = 10;
        final int numberOfRoomsTo = 16;
        final int builtYearFrom = 1900;
        final int builtYearTo = 1905;

        // When
        final var query =
                new SearchHouseAdvertisementsQuery(
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
                        InstanceOfAssertFactories.type(SearchHouseAdvertisementsCriteria.class))
                .returns(areaFrom, SearchHouseAdvertisementsCriteria::areaFrom)
                .returns(areaTo, SearchHouseAdvertisementsCriteria::areaTo)
                .returns(priceFrom, SearchHouseAdvertisementsCriteria::priceFrom)
                .returns(priceTo, SearchHouseAdvertisementsCriteria::priceTo)
                .returns(
                        pricePerSquareMeterFrom,
                        SearchHouseAdvertisementsCriteria::pricePerSquareMeterFrom)
                .returns(
                        pricePerSquareMeterTo,
                        SearchHouseAdvertisementsCriteria::pricePerSquareMeterTo)
                .returns(page, SearchHouseAdvertisementsCriteria::page)
                .returns(pageSize, SearchHouseAdvertisementsCriteria::pageSize)
                .returns(floorsFrom, SearchHouseAdvertisementsCriteria::floorsFrom)
                .returns(floorsTo, SearchHouseAdvertisementsCriteria::floorsTo)
                .returns(numberOfRoomsFrom, SearchHouseAdvertisementsCriteria::numberOfRoomsFrom)
                .returns(numberOfRoomsTo, SearchHouseAdvertisementsCriteria::numberOfRoomsTo)
                .returns(builtYearFrom, SearchHouseAdvertisementsCriteria::builtYearFrom)
                .returns(builtYearTo, SearchHouseAdvertisementsCriteria::builtYearTo)
                .returns(localityId, SearchHouseAdvertisementsCriteria::localityId)
                .returns(dateFrom, SearchHouseAdvertisementsCriteria::dateFrom)
                .returns(dateTo, SearchHouseAdvertisementsCriteria::dateTo)
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
                SearchHouseAdvertisementsQuery.class, fieldName, requiredAnnotations);
    }
}
