/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain.shared.advertisement;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SearchPlotAdvertisementsCriteriaTest {

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
        final Set<String> types = Set.of("c", "g");
        final UUID localityId = UUID.randomUUID();
        final LocalDate dateFrom = LocalDate.of(2025, 1, 5);
        final LocalDate dateTo = LocalDate.of(2025, 3, 15);

        // When
        final var criteria =
                new SearchPlotAdvertisementsCriteria(
                        areaFrom,
                        areaTo,
                        priceFrom,
                        priceTo,
                        pricePerSquareMeterFrom,
                        pricePerSquareMeterTo,
                        page,
                        pageSize,
                        localityId,
                        dateFrom,
                        dateTo,
                        types);

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
                            Assertions.assertThat(c.types())
                                    .containsExactlyInAnyOrderElementsOf(types);

                            Assertions.assertThat(c.localityId()).isEqualTo(localityId);
                            Assertions.assertThat(c.dateFrom()).isEqualTo(dateFrom);
                            Assertions.assertThat(c.dateTo()).isEqualTo(dateTo);
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
        final Set<String> types = Set.of("c", "g");
        final UUID localityId = UUID.randomUUID();
        final LocalDate dateFrom = LocalDate.of(2025, 1, 5);
        final LocalDate dateTo = LocalDate.of(2025, 3, 15);

        final var criteria =
                new SearchPlotAdvertisementsCriteria(
                        areaFrom,
                        areaTo,
                        priceFrom,
                        priceTo,
                        pricePerSquareMeterFrom,
                        pricePerSquareMeterTo,
                        page,
                        pageSize,
                        localityId,
                        dateFrom,
                        dateTo,
                        types);

        // When
        // Then
        Assertions.assertThatThrownBy(() -> criteria.types().add("p"))
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

        // When
        final var criteria =
                new SearchPlotAdvertisementsCriteria(
                        areaFrom,
                        areaTo,
                        priceFrom,
                        priceTo,
                        pricePerSquareMeterFrom,
                        pricePerSquareMeterTo,
                        page,
                        pageSize,
                        localityId,
                        dateFrom,
                        dateTo,
                        null);

        // Then
        Assertions.assertThat(criteria).satisfies(c -> Assertions.assertThat(c.types()).isEmpty());
    }
}
