/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.query;

import lombok.RequiredArgsConstructor;

import pl.dawid0604.realestate.application.validation.ValidArea;
import pl.dawid0604.realestate.application.validation.ValidBuiltYear;
import pl.dawid0604.realestate.application.validation.ValidFloor;
import pl.dawid0604.realestate.application.validation.ValidFloors;
import pl.dawid0604.realestate.application.validation.ValidLocalityId;
import pl.dawid0604.realestate.application.validation.ValidNumberOfRooms;
import pl.dawid0604.realestate.application.validation.ValidPageNumber;
import pl.dawid0604.realestate.application.validation.ValidPageSize;
import pl.dawid0604.realestate.application.validation.ValidPrice;
import pl.dawid0604.realestate.domain.shared.advertisement.SearchAdvertisementsCriteria;
import pl.dawid0604.realestate.domain.shared.advertisement.SearchFlatAdvertisementsCriteria;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@SuppressWarnings("CPD-START")
public final class SearchFlatAdvertisementsQuery implements SearchAdvertisementsQuery {
    @ValidArea private final BigDecimal areaFrom;
    @ValidArea private final BigDecimal areaTo;
    @ValidPrice private final BigDecimal priceFrom;
    @ValidPrice private final BigDecimal priceTo;
    @ValidPrice private final BigDecimal pricePerSquareMeterFrom;
    @ValidPrice private final BigDecimal pricePerSquareMeterTo;
    @ValidPageNumber private final int page;
    @ValidPageSize private final int pageSize;
    private final Set<String> offerFrom;
    private final Set<String> types;
    private final Set<String> typeOfMarkets;
    @ValidFloor private final Integer floorFrom;
    @ValidFloor private final Integer floorTo;
    @ValidFloors private final Integer floorsFrom;
    @ValidFloors private final Integer floorsTo;
    @ValidNumberOfRooms private final Integer numberOfRoomsFrom;
    @ValidNumberOfRooms private final Integer numberOfRoomsTo;
    @ValidBuiltYear private final Integer builtYearFrom;
    @ValidBuiltYear private final Integer builtYearTo;
    private final LocalDate dateFrom;
    private final LocalDate dateTo;
    @ValidLocalityId private final UUID localityId;

    @Override
    public SearchAdvertisementsCriteria criteria() {
        return new SearchFlatAdvertisementsCriteria(
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
                floorFrom,
                floorTo,
                floorsFrom,
                floorsTo,
                numberOfRoomsFrom,
                numberOfRoomsTo,
                builtYearFrom,
                builtYearTo);
    }
}
