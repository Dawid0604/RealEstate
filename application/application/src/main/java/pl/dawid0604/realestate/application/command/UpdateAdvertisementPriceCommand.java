package pl.dawid0604.realestate.application.command;

import pl.dawid0604.realestate.application.validation.ValidPrice;
import pl.dawid0604.realestate.application.validation.ValidSlug;
import pl.dawid0604.realestate.application.validation.ValidUserId;

import java.math.BigDecimal;
import java.util.UUID;

public record UpdateAdvertisementPriceCommand(
        @ValidSlug String slug, @ValidPrice BigDecimal newPrice, @ValidUserId UUID userId)
        implements Command {}
