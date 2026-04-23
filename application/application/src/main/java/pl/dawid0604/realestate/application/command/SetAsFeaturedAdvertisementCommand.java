/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.command;

import pl.dawid0604.realestate.application.validation.ValidAdvertisementType;
import pl.dawid0604.realestate.application.validation.ValidEmail;
import pl.dawid0604.realestate.application.validation.ValidSlug;

public record SetAsFeaturedAdvertisementCommand(
        @ValidSlug String slug,
        @ValidAdvertisementType String advertisementType,
        @ValidEmail String userEmail)
        implements Command {}
