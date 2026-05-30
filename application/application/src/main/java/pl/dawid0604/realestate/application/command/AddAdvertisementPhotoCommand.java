/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.command;

import pl.dawid0604.realestate.domain.shared.AdvertisementType;

public record AddAdvertisementPhotoCommand(
        String slug,
        AdvertisementType advertisementType,
        String photoUrl,
        int position,
        String userEmail)
        implements Command {}
