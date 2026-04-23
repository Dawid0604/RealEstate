/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.command;

import pl.dawid0604.realestate.application.validation.ValidAdvertisementType;
import pl.dawid0604.realestate.application.validation.ValidEmail;
import pl.dawid0604.realestate.application.validation.ValidLocalityId;
import pl.dawid0604.realestate.application.validation.ValidSlug;

import java.util.UUID;

public record UpdateAdvertisementLocalityCommand(
        @ValidSlug String slug,
        @ValidLocalityId UUID newLocalityId,
        @ValidAdvertisementType String advertisementType,
        @ValidEmail String userEmail)
        implements Command {}
