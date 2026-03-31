/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.command;

public sealed interface Command
        permits ActivateAdvertisementCommand, AddAdvertisementPhotoCommand, CreateAdvertisementCommand, DeactivateAdvertisementCommand, DeleteAdvertisementCommand, DisableFeaturedStateAdvertisementCommand, RemoveAdvertisementPhotoCommand, SetAsFeaturedAdvertisementCommand, SetAsSoldAdvertisementCommand, UpdateAdvertisementDescriptionCommand, UpdateAdvertisementLocalityCommand, UpdateAdvertisementPriceCommand, UpdateAdvertisementTitleCommand {}
