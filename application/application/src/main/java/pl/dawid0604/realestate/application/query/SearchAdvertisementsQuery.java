/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.query;

import pl.dawid0604.realestate.domain.shared.advertisement.SearchAdvertisementsCriteria;

public sealed interface SearchAdvertisementsQuery extends Query
        permits SearchCommercialAdvertisementsQuery,
                SearchFlatAdvertisementsQuery,
                SearchHouseAdvertisementsQuery,
                SearchPlotAdvertisementsQuery {

    SearchAdvertisementsCriteria criteria();
}
