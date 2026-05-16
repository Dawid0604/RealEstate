/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.query;

import lombok.RequiredArgsConstructor;

import pl.dawid0604.realestate.domain.shared.advertisement.SearchAdvertisementsCriteria;
import pl.dawid0604.realestate.domain.shared.advertisement.SearchHouseAdvertisementsCriteria;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
public final class SearchHouseAdvertisementsQuery implements SearchAdvertisementsQuery {
    private final BigDecimal areaFrom;
    private final BigDecimal areaTo;
    private final BigDecimal priceFrom;
    private final BigDecimal priceTo;
    private final BigDecimal pricePerSquareMeterFrom;
    private final BigDecimal pricePerSquareMeterTo;
    private final int page;
    private final int pageSize;
    private final Set<String> types;
    private final Set<String> typeOfMarkets;
    private final Integer floorsFrom;
    private final Integer floorsTo;
    private final Integer numberOfRoomsFrom;
    private final Integer numberOfRoomsTo;
    private final Integer builtYearFrom;
    private final Integer builtYearTo;
    private final LocalDate dateFrom;
    private final LocalDate dateTo;
    private final UUID localityId;

    @Override
    public SearchAdvertisementsCriteria criteria() {
        return new SearchHouseAdvertisementsCriteria(
                areaFrom,
                areaTo,
                priceFrom,
                priceTo,
                pricePerSquareMeterFrom,
                pricePerSquareMeterTo,
                page,
                pageSize,
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
    }
}
