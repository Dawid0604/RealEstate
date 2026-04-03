package pl.dawid0604.realestate.application.command;

import pl.dawid0604.realestate.application.validation.ValidEmail;
import pl.dawid0604.realestate.application.validation.ValidSlug;

public record SetAsSoldAdvertisementCommand(@ValidSlug String slug, @ValidEmail String userEmail)
        implements Command {}
