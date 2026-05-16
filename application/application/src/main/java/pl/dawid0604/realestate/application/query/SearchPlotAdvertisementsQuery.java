/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.query;

import lombok.RequiredArgsConstructor;

import pl.dawid0604.realestate.domain.shared.advertisement.SearchAdvertisementsCriteria;
import pl.dawid0604.realestate.domain.shared.advertisement.SearchPlotAdvertisementsCriteria;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
public final class SearchPlotAdvertisementsQuery implements SearchAdvertisementsQuery {
    private final BigDecimal areaFrom;
    private final BigDecimal areaTo;
    private final BigDecimal priceFrom;
    private final BigDecimal priceTo;
    private final BigDecimal pricePerSquareMeterFrom;
    private final BigDecimal pricePerSquareMeterTo;
    private final int page;
    private final int pageSize;
    private final Set<String> types;
    private final LocalDate dateFrom;
    private final LocalDate dateTo;
    private final UUID localityId;

    @Override
    public SearchAdvertisementsCriteria criteria() {
        return new SearchPlotAdvertisementsCriteria(
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
    }
}
