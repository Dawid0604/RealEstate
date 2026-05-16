/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.command;

import pl.dawid0604.realestate.domain.shared.AdvertisementType;

import java.util.UUID;

public record DeleteAdvertisementPhotoCommand(
        String slug, UUID photoId, AdvertisementType advertisementType, String userEmail)
        implements Command {}
