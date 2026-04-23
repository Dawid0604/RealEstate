/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.infrastructure.advertisement;

abstract sealed class AdvertisementEntity
        permits CommercialAdvertisementEntity,
                FlatAdvertisementEntity,
                HouseAdvertisementEntity,
                PlotAdvertisementEntity {}
