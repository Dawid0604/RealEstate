/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.query;

public sealed interface Query
        permits AdvertisementDetailsQuery,
                IsUserBannedQuery,
                SearchAdvertisementsQuery,
                UserAdvertisementsQuery,
                UserLoginHistoryQuery,
                UserProfileQuery {}
