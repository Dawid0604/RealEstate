package pl.dawid0604.realestate.application.command;

import pl.dawid0604.realestate.application.validation.ValidEmail;
import pl.dawid0604.realestate.application.validation.ValidSlug;
import pl.dawid0604.realestate.application.validation.ValidTitle;

public record UpdateAdvertisementTitleCommand(
        @ValidSlug String slug, @ValidTitle String newTitle, @ValidEmail String userEmail)
        implements Command {}
