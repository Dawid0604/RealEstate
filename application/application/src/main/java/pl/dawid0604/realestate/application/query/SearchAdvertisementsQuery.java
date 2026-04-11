/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.query;

public sealed interface SearchAdvertisementsQuery extends Query
        permits SearchCommercialAdvertisementsQuery,
                SearchFlatAdvertisementsQuery,
                SearchHouseAdvertisementsQuery,
                SearchPlotAdvertisementsQuery {

    SearchAdvertisementCriteria criteria();
}
