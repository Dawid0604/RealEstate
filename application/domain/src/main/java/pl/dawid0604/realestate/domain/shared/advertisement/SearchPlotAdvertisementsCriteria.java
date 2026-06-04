/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain.shared.advertisement;

import static java.util.Collections.emptySet;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public record SearchPlotAdvertisementsCriteria(
        BigDecimal areaFrom,
        BigDecimal areaTo,
        BigDecimal priceFrom,
        BigDecimal priceTo,
        BigDecimal pricePerSquareMeterFrom,
        BigDecimal pricePerSquareMeterTo,
        int page,
        int pageSize,
        UUID localityId,
        LocalDate dateFrom,
        LocalDate dateTo,
        Set<String> types)
        implements SearchAdvertisementsCriteria {

    public SearchPlotAdvertisementsCriteria {
        types = types != null ? Set.copyOf(types) : emptySet();
    }

    @Override
    public Set<String> types() {
        return Set.copyOf(types);
    }
}
