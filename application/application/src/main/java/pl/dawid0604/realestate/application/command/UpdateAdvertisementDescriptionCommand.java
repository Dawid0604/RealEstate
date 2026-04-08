/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.command;

import pl.dawid0604.realestate.application.validation.ValidEmail;
import pl.dawid0604.realestate.application.validation.ValidSlug;

public record UpdateAdvertisementDescriptionCommand(
        @ValidSlug String slug, String newDescription, @ValidEmail String userEmail)
        implements Command {}
