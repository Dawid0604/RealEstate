package pl.dawid0604.realestate.application.command;

import pl.dawid0604.realestate.application.validation.ValidLocalityId;
import pl.dawid0604.realestate.application.validation.ValidSlug;
import pl.dawid0604.realestate.application.validation.ValidUserId;

import java.util.UUID;

public record UpdateAdvertisementLocalityCommand(
        @ValidSlug String slug, @ValidLocalityId UUID newLocalityId, @ValidUserId UUID userId)
        implements Command {}
