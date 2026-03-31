package pl.dawid0604.realestate.application.command;

import pl.dawid0604.realestate.application.validation.ValidSlug;
import pl.dawid0604.realestate.application.validation.ValidTitle;
import pl.dawid0604.realestate.application.validation.ValidUserId;

import java.util.UUID;

public record UpdateAdvertisementTitleCommand(
        @ValidSlug String slug, @ValidTitle String newTitle, @ValidUserId UUID userId)
        implements Command {}
