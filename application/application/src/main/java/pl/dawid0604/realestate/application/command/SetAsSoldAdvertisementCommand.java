/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.command;

import pl.dawid0604.realestate.domain.shared.AdvertisementType;

public record SetAsSoldAdvertisementCommand(
        String slug, AdvertisementType advertisementType, String userEmail) implements Command {}
