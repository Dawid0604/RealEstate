package pl.dawid0604.realestate.application.command;

import pl.dawid0604.realestate.application.validation.ValidDescription;
import pl.dawid0604.realestate.application.validation.ValidSlug;
import pl.dawid0604.realestate.application.validation.ValidUserId;

import java.util.UUID;

public record UpdateAdvertisementDescriptionCommand(
        @ValidSlug String slug, @ValidDescription String newDescription, @ValidUserId UUID userId)
        implements Command {}
