package pl.dawid0604.realestate.application.command;

import pl.dawid0604.realestate.application.validation.ValidSlug;
import pl.dawid0604.realestate.application.validation.ValidUserId;

import java.util.UUID;

public record DisableFeaturedStateAdvertisementCommand(@ValidSlug String slug, @ValidUserId UUID userId) implements Command {}
