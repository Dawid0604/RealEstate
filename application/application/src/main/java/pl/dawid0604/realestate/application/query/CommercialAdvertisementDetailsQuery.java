/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.query;

import pl.dawid0604.realestate.application.validation.ValidSlug;

public record CommercialAdvertisementDetailsQuery(@ValidSlug String slug)
        implements AdvertisementDetailsQuery {}
