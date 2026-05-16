/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.query;

public sealed interface Query
        permits AdvertisementDetailsQuery,
        SearchAdvertisementsQuery,
                UserAdvertisementsQuery,
                UserProfileQuery {}
