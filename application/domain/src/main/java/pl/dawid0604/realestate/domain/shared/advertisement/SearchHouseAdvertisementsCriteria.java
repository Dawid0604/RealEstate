/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain.shared.advertisement;

import static java.util.Collections.emptySet;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public record SearchHouseAdvertisementsCriteria(
        BigDecimal areaFrom,
        BigDecimal areaTo,
        BigDecimal priceFrom,
        BigDecimal priceTo,
        BigDecimal pricePerSquareMeterFrom,
        BigDecimal pricePerSquareMeterTo,
        int page,
        int pageSize,
        Set<String> types,
        UUID localityId,
        LocalDate dateFrom,
        LocalDate dateTo,
        Set<String> typeOfMarkets,
        Integer floorsFrom,
        Integer floorsTo,
        Integer numberOfRoomsFrom,
        Integer numberOfRoomsTo,
        Integer builtYearFrom,
        Integer builtYearTo)
        implements SearchAdvertisementsCriteria {

    @SuppressWarnings("CPD-START")
    public SearchHouseAdvertisementsCriteria {
        types = types != null ? Set.copyOf(types) : emptySet();
        typeOfMarkets = typeOfMarkets != null ? Set.copyOf(typeOfMarkets) : emptySet();
    }

    @Override
    public Set<String> typeOfMarkets() {
        return Set.copyOf(typeOfMarkets);
    }

    @Override
    public Set<String> types() {
        return Set.copyOf(types);
    }
}
