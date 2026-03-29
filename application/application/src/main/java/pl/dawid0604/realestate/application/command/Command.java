/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.command;

public sealed interface Command
        permits ActivateAdvertisementCommand, CreateAdvertisementCommand, DeactivateAdvertisementCommand, DeleteAdvertisementCommand, DisableFeaturedStateAdvertisementCommand, SetAsFeaturedAdvertisementCommand, SetAsSoldAdvertisementCommand {}
