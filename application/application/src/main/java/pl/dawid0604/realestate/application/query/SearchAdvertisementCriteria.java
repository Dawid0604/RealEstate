/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.query;

import static java.util.Collections.emptySet;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public record SearchAdvertisementCriteria(
        String slug,
        BigDecimal areaFrom,
        BigDecimal areaTo,
        BigDecimal priceFrom,
        BigDecimal priceTo,
        UUID localityId,
        BigDecimal pricePerSquareMeterFrom,
        BigDecimal pricePerSquareMeterTo,
        LocalDate dateFrom,
        LocalDate dateTo,
        Set<String> offerFrom,
        Set<String> types,
        int page,
        int pageSize) {

    public SearchAdvertisementCriteria {
        offerFrom = offerFrom != null ? Set.copyOf(offerFrom) : emptySet();
        types = types != null ? Set.copyOf(types) : emptySet();
    }
}
