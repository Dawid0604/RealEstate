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
import pl.dawid0604.realestate.application.validation.ValidLocalityId;
import pl.dawid0604.realestate.application.validation.ValidPageNumber;
import pl.dawid0604.realestate.application.validation.ValidPageSize;
import pl.dawid0604.realestate.application.validation.ValidPrice;
import pl.dawid0604.realestate.domain.shared.advertisement.SearchPlotAdvertisementsCriteria;

class SearchPlotAdvertisementsQueryTest {

    @Test
    @DisplayName("Should implement Query interface")
    void shouldImplementsQueryInterface() {
        // Given
        // When
        // Then
        AnnotationAssertions.assertImplementsQueryInterface(SearchPlotAdvertisementsQuery.class);
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
        final Set<String> types = Set.of("c", "g");
        final UUID localityId = UUID.randomUUID();
        final LocalDate dateFrom = LocalDate.of(2025, 1, 5);
        final LocalDate dateTo = LocalDate.of(2025, 3, 15);

        // When
        final var query =
                new SearchPlotAdvertisementsQuery(
                        areaFrom,
                        areaTo,
                        priceFrom,
                        priceTo,
                        pricePerSquareMeterFrom,
                        pricePerSquareMeterTo,
                        page,
                        pageSize,
                        types,
                        dateFrom,
                        dateTo,
                        localityId);

        // Then
        Assertions.assertThat(query.criteria())
                .asInstanceOf(
                        InstanceOfAssertFactories.type(SearchPlotAdvertisementsCriteria.class))
                .returns(areaFrom, SearchPlotAdvertisementsCriteria::areaFrom)
                .returns(areaTo, SearchPlotAdvertisementsCriteria::areaTo)
                .returns(priceFrom, SearchPlotAdvertisementsCriteria::priceFrom)
                .returns(priceTo, SearchPlotAdvertisementsCriteria::priceTo)
                .returns(
                        pricePerSquareMeterFrom,
                        SearchPlotAdvertisementsCriteria::pricePerSquareMeterFrom)
                .returns(
                        pricePerSquareMeterTo,
                        SearchPlotAdvertisementsCriteria::pricePerSquareMeterTo)
                .returns(page, SearchPlotAdvertisementsCriteria::page)
                .returns(pageSize, SearchPlotAdvertisementsCriteria::pageSize)
                .returns(localityId, SearchPlotAdvertisementsCriteria::localityId)
                .returns(dateFrom, SearchPlotAdvertisementsCriteria::dateFrom)
                .returns(dateTo, SearchPlotAdvertisementsCriteria::dateTo)
                .satisfies(c -> Assertions.assertThat(c.types()).containsExactlyElementsOf(types));
    }

    private static void assertFieldAnnotations(
            final String fieldName, final List<Class<? extends Annotation>> requiredAnnotations) {

        AnnotationAssertions.assertFieldAnnotations(
                SearchPlotAdvertisementsQuery.class, fieldName, requiredAnnotations);
    }
}
