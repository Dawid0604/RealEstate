/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.query;

public sealed interface AdvertisementDetailsQuery extends Query
        permits CommercialAdvertisementDetailsQuery,
                FlatAdvertisementDetailsQuery,
                HouseAdvertisementDetailsQuery,
                PlotAdvertisementDetailsQuery {

    String slug();
}
