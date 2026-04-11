/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.query;

import static java.util.Collections.emptySet;

import java.util.Set;

public record SearchFlatAdvertisementsQuery(
        SearchAdvertisementCriteria criteria,
        Set<String> typeOfMarkets,
        Integer numberOfRoomsFrom,
        Integer numberOfRoomsTo,
        Integer floorFrom,
        Integer floorTo,
        Integer floorsFrom,
        Integer floorsTo,
        Integer builtYearFrom,
        Integer builtYearTo)
        implements SearchAdvertisementsQuery {

    public SearchFlatAdvertisementsQuery {
        typeOfMarkets = typeOfMarkets != null ? Set.copyOf(typeOfMarkets) : emptySet();
    }

    @Override
    public Set<String> typeOfMarkets() {
        return Set.copyOf(typeOfMarkets);
    }
}
