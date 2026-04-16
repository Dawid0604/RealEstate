/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain.shared.advertisement;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

class SearchHouseAdvertisementsCriteriaTest {

    @Test
    @DisplayName("Should create instance")
    void shouldCreateInstance() {
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
        final int floorsFrom = 1;
        final int floorsTo = 6;
        final int numberOfRoomsFrom = 10;
        final int numberOfRoomsTo = 16;
        final int builtYearFrom = 1900;
        final int builtYearTo = 1905;

        // When
        final var criteria =
                new SearchHouseAdvertisementsCriteria(
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
                        localityId,
                        dateFrom,
                        dateTo,
                        typeOfMarkets,
                        floorsFrom,
                        floorsTo,
                        numberOfRoomsFrom,
                        numberOfRoomsTo,
                        builtYearFrom,
                        builtYearTo);

        // Then
        Assertions.assertThat(criteria)
                .satisfies(
                        c -> {
                            Assertions.assertThat(c.areaFrom()).isEqualTo(areaFrom);
                            Assertions.assertThat(c.areaTo()).isEqualTo(areaTo);
                            Assertions.assertThat(c.priceFrom()).isEqualTo(priceFrom);
                            Assertions.assertThat(c.priceTo()).isEqualTo(priceTo);
                            Assertions.assertThat(c.pricePerSquareMeterFrom())
                                    .isEqualTo(pricePerSquareMeterFrom);

                            Assertions.assertThat(c.pricePerSquareMeterTo())
                                    .isEqualTo(pricePerSquareMeterTo);

                            Assertions.assertThat(c.page()).isEqualTo(page);
                            Assertions.assertThat(c.pageSize()).isEqualTo(pageSize);
                            Assertions.assertThat(c.offerFrom())
                                    .containsExactlyInAnyOrderElementsOf(offerFrom);

                            Assertions.assertThat(c.types())
                                    .containsExactlyInAnyOrderElementsOf(types);

                            Assertions.assertThat(c.localityId()).isEqualTo(localityId);
                            Assertions.assertThat(c.dateFrom()).isEqualTo(dateFrom);
                            Assertions.assertThat(c.dateTo()).isEqualTo(dateTo);
                            Assertions.assertThat(c.typeOfMarkets())
                                    .containsExactlyInAnyOrderElementsOf(typeOfMarkets);

                            Assertions.assertThat(c.floorsFrom()).isEqualTo(floorsFrom);
                            Assertions.assertThat(c.floorsTo()).isEqualTo(floorsTo);
                            Assertions.assertThat(c.numberOfRoomsFrom())
                                    .isEqualTo(numberOfRoomsFrom);

                            Assertions.assertThat(c.numberOfRoomsTo()).isEqualTo(numberOfRoomsTo);
                            Assertions.assertThat(c.builtYearFrom()).isEqualTo(builtYearFrom);
                            Assertions.assertThat(c.builtYearTo()).isEqualTo(builtYearTo);
                        });
    }

    @Test
    @DisplayName("Should copy collections")
    void shouldCopyCollections() {
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
        final int floorsFrom = 1;
        final int floorsTo = 6;
        final int numberOfRoomsFrom = 10;
        final int numberOfRoomsTo = 16;
        final int builtYearFrom = 1900;
        final int builtYearTo = 1905;

        final var criteria =
                new SearchHouseAdvertisementsCriteria(
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
                        localityId,
                        dateFrom,
                        dateTo,
                        typeOfMarkets,
                        floorsFrom,
                        floorsTo,
                        numberOfRoomsFrom,
                        numberOfRoomsTo,
                        builtYearFrom,
                        builtYearTo);

        // When
        // Then
        Assertions.assertThatThrownBy(() -> criteria.offerFrom().add("p"))
                .isExactlyInstanceOf(UnsupportedOperationException.class);

        Assertions.assertThatThrownBy(() -> criteria.types().add("p"))
                .isExactlyInstanceOf(UnsupportedOperationException.class);

        Assertions.assertThatThrownBy(() -> criteria.typeOfMarkets().add("p"))
                .isExactlyInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    @DisplayName("Should set empty when collections are null")
    void shouldSetEmptyWhenCollectionsAreNull() {
        // Given
        final BigDecimal areaFrom = BigDecimal.valueOf(25);
        final BigDecimal areaTo = BigDecimal.valueOf(35);
        final BigDecimal priceFrom = BigDecimal.valueOf(25_000);
        final BigDecimal priceTo = BigDecimal.valueOf(35_000);
        final BigDecimal pricePerSquareMeterFrom = BigDecimal.valueOf(3_000);
        final BigDecimal pricePerSquareMeterTo = BigDecimal.valueOf(5_000);
        final int page = 2;
        final int pageSize = 25;
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
        final var criteria =
                new SearchHouseAdvertisementsCriteria(
                        areaFrom,
                        areaTo,
                        priceFrom,
                        priceTo,
                        pricePerSquareMeterFrom,
                        pricePerSquareMeterTo,
                        page,
                        pageSize,
                        null,
                        null,
                        localityId,
                        dateFrom,
                        dateTo,
                        null,
                        floorsFrom,
                        floorsTo,
                        numberOfRoomsFrom,
                        numberOfRoomsTo,
                        builtYearFrom,
                        builtYearTo);

        // Then
        Assertions.assertThat(criteria)
                .satisfies(
                        c -> {
                            Assertions.assertThat(c.types()).isEmpty();
                            Assertions.assertThat(c.typeOfMarkets()).isEmpty();
                            Assertions.assertThat(c.offerFrom()).isEmpty();
                        });
    }
}
