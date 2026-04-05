/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.command;

import pl.dawid0604.realestate.application.validation.ValidEmail;
import pl.dawid0604.realestate.application.validation.ValidPrice;
import pl.dawid0604.realestate.application.validation.ValidSlug;

import java.math.BigDecimal;

public record UpdateAdvertisementPriceCommand(
        @ValidSlug String slug, @ValidPrice BigDecimal newPrice, @ValidEmail String userEmail)
        implements Command {}
